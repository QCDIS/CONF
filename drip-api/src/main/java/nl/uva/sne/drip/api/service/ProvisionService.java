/*
 * Copyright 2017 S. Koulouzis, Wang Junchao, Huan Zhou, Yang Hu 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.uva.sne.drip.api.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.uva.sne.drip.api.exception.BadRequestException;
import nl.uva.sne.drip.api.exception.CloudCredentialsNotFoundException;
import nl.uva.sne.drip.api.exception.ExceptionHandler;
import nl.uva.sne.drip.api.exception.NotFoundException;
import nl.uva.sne.drip.api.exception.PlanNotFoundException;
import nl.uva.sne.drip.api.rpc.DRIPCaller;
import nl.uva.sne.drip.api.v1.rest.ProvisionController;
import nl.uva.sne.drip.commons.utils.Converter;
import nl.uva.sne.drip.drip.commons.data.v1.external.CloudCredentials;
import nl.uva.sne.drip.drip.commons.data.internal.Message;
import nl.uva.sne.drip.drip.commons.data.internal.MessageParameter;
import nl.uva.sne.drip.drip.commons.data.v1.external.PlanResponse;
import nl.uva.sne.drip.drip.commons.data.v1.external.ProvisionRequest;
import nl.uva.sne.drip.drip.commons.data.v1.external.ProvisionResponse;
import nl.uva.sne.drip.drip.commons.data.v1.external.User;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import nl.uva.sne.drip.api.dao.ProvisionResponseDao;
import nl.uva.sne.drip.api.rpc.ProvisionerCaller1;
import nl.uva.sne.drip.commons.utils.DRIPLogHandler;
import nl.uva.sne.drip.commons.utils.TOSCAUtils;
import static nl.uva.sne.drip.commons.utils.TOSCAUtils.getVMsFromTopology;
import static nl.uva.sne.drip.commons.utils.TOSCAUtils.getVMsNodeNamesFromTopology;
import nl.uva.sne.drip.drip.commons.data.v1.external.ScaleRequest;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author S. Koulouzis
 */
@Service
@PreAuthorize("isAuthenticated()")
public class ProvisionService {

    @Autowired
    private ProvisionResponseDao provisionDao;

    @Autowired
    private CloudCredentialsService cloudCredentialsService;

    @Autowired
    private SimplePlannerService simplePlanService;

    @Autowired
    private ScriptService userScriptService;

    @Autowired
    private KeyPairService keyPairService;

    @Value("${message.broker.host}")
    private String messageBrokerHost;
    private final Logger logger;

    @Autowired
    public ProvisionService(@Value("${message.broker.host}") String messageBrokerHost) throws IOException, TimeoutException {
        logger = Logger.getLogger(ProvisionService.class.getName());
        logger.addHandler(new DRIPLogHandler(messageBrokerHost));
    }

    public ProvisionResponse save(ProvisionResponse ownedObject) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String owner = user.getUsername();
        ownedObject.setOwner(owner);

        return provisionDao.save(ownedObject);
    }

    @PostAuthorize("(returnObject.owner == authentication.name) or (hasRole('ROLE_ADMIN'))")
    public ProvisionResponse findOne(String id) {
        ProvisionResponse provisionInfo = provisionDao.findOne(id);
        if (provisionInfo == null) {
            throw new NotFoundException();
        }
        return provisionInfo;
    }

    @PostAuthorize("(returnObject.owner == authentication.name) or (hasRole('ROLE_ADMIN'))")
    public ProvisionResponse delete(String id) {
        ProvisionResponse provisionInfo = provisionDao.findOne(id);
        if (provisionInfo != null) {
            provisionDao.delete(provisionInfo);
            return provisionInfo;
        } else {
            throw new NotFoundException();
        }
    }

//    @PreAuthorize(" (hasRole('ROLE_ADMIN')) or (hasRole('ROLE_USER'))")
    @PostFilter("(filterObject.owner == authentication.name) or (hasRole('ROLE_ADMIN'))")
//    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'admin')")
    public List<ProvisionResponse> findAll() {
        return provisionDao.findAll();
    }

    public ProvisionResponse provisionResources(ProvisionRequest provisionRequest, int provisionerVersion) throws IOException, TimeoutException, JSONException, InterruptedException, Exception {
        return callProvisioner1(provisionRequest);
    }

    private List<MessageParameter> buildTopologyParams(PlanResponse plan) throws JSONException, FileNotFoundException {
        if (plan == null) {
            throw new PlanNotFoundException();
        }
        List<MessageParameter> parameters = new ArrayList();
        MessageParameter topology = new MessageParameter();
        topology.setName("topology");
        String val = Converter.map2YmlString(plan.getKeyValue());
        val = val.replaceAll("\\uff0E", ".");
        String encodedValue = new String(Base64.encodeBase64(val.getBytes()));
        topology.setValue(encodedValue);
        Map<String, String> attributes = new HashMap<>();
        topology.setAttributes(attributes);
        parameters.add(topology);
        return parameters;
    }

    private List<MessageParameter> buildProvisionedTopologyParams(ProvisionResponse provisionInfo) throws JSONException {
        List<MessageParameter> parameters = new ArrayList();

        Map<String, Object> map = provisionInfo.getKeyValue();
        for (String topoName : map.keySet()) {
            Map<String, Object> topo = (Map<String, Object>) map.get(topoName);
            MessageParameter topology = new MessageParameter();
            topology.setName("topology");
            topology.setValue(Converter.map2YmlString(topo));
            HashMap<String, String> attributes = new HashMap<>();
            if (topoName.equals("topology_main")) {
                attributes.put("level", String.valueOf(0));
            } else {
                attributes.put("level", String.valueOf(1));
            }
            attributes.put("filename", topoName);
            topology.setAttributes(attributes);
            parameters.add(topology);
        }
        return parameters;
    }

    private List<MessageParameter> buildClusterKeyParams(ProvisionResponse provisionInfo) {
        List<MessageParameter> parameters = new ArrayList();
//        List<String> ids = provisionInfo.getDeployerKeyPairIDs();
//        for (String id : ids) {
//            KeyPair pair = keyPairService.findOne(id);
//
//            MessageParameter param = new MessageParameter();
//            param.setName("private_deployer_key");
//            param.setValue(pair.getPrivateKey().getKey());
//            HashMap<String, String> attributes = new HashMap<>();
//            if (pair.getPrivateKey() != null && pair.getPrivateKey().getAttributes() != null) {
//                attributes.putAll(pair.getPrivateKey().getAttributes());
//            }
//            attributes.put("name", pair.getPrivateKey().getName());
//            param.setAttributes(attributes);
//            parameters.add(param);
//
//            param = new MessageParameter();
//            param.setName("public_deployer_key");
//            param.setValue(pair.getPublicKey().getKey());
//            attributes = new HashMap<>();
//            if (pair.getPublicKey() != null && pair.getPublicKey().getAttributes() != null) {
//                attributes.putAll(pair.getPublicKey().getAttributes());
//            }
//            param.setAttributes(attributes);
//            attributes.put("name", pair.getPublicKey().getName());
//            parameters.add(param);
//        }

        return parameters;
    }

    private List<MessageParameter> buildUserKeyParams(ProvisionResponse provisionInfo) {
        List<MessageParameter> parameters = new ArrayList();
//        List<String> ids = provisionInfo.getUserKeyPairIDs();
//        for (String id : ids) {
//            KeyPair pair = keyPairService.findOne(id);
//
//            MessageParameter param = new MessageParameter();
//            param.setName("private_user_key");
//            param.setValue(pair.getPrivateKey().getKey());
//            HashMap<String, String> attributes = new HashMap<>();
//            if (pair.getPrivateKey().getAttributes() != null) {
//                attributes.putAll(pair.getPrivateKey().getAttributes());
//            }
//            attributes.put("name", pair.getPrivateKey().getName());
//            param.setAttributes(attributes);
//            parameters.add(param);
//
//            param = new MessageParameter();
//            param.setName("public_user_key");
//            param.setValue(pair.getPublicKey().getKey());
//            if (pair.getPublicKey().getAttributes() != null) {
//                attributes.putAll(pair.getPublicKey().getAttributes());
//            }
//            attributes.put("name", pair.getPublicKey().getName());
//            param.setAttributes(attributes);
//            parameters.add(param);
//        }
        return parameters;
    }

    @PostFilter("(hasRole('ROLE_ADMIN'))")
    public void deleteAll() {
        provisionDao.deleteAll();
    }

    private ProvisionResponse callProvisioner1(ProvisionRequest provisionRequest) throws IOException, TimeoutException, JSONException, InterruptedException, Exception {
        try (DRIPCaller provisioner = new ProvisionerCaller1(messageBrokerHost);) {
            Message provisionerInvokationMessage = buildProvisioner1Message(provisionRequest);
            provisionerInvokationMessage.setOwner(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            logger.info("Calling provisioner");
            Message response = (provisioner.call(provisionerInvokationMessage));
            logger.info("Got provisioner response");
            return parseCreateResourcesResponse(response.getParameters(), provisionRequest, null, true, true);
        }

    }

    public void deleteProvisionedResources(ProvisionResponse provisionInfo) throws IOException, TimeoutException, InterruptedException, JSONException {
        try (DRIPCaller provisioner = new ProvisionerCaller1(messageBrokerHost);) {
            Message deleteInvokationMessage = buildTopoplogyModificationMessage(provisionInfo, "kill_topology", null);
            deleteInvokationMessage.setOwner(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            logger.info("Calling provisioner");
            Message response = (provisioner.call(deleteInvokationMessage));
            logger.info("Got provisioner response");
            parseDeleteResourcesResponse(response.getParameters(), provisionInfo);
        }
    }

    public ProvisionResponse scale(ScaleRequest scaleRequest) throws IOException, TimeoutException, JSONException, InterruptedException, Exception {
        String provisionID = scaleRequest.getScaleTargetID();
        String scaleName = scaleRequest.getScaleTargetName();
        if (scaleName == null || scaleName.length() < 1) {
            throw new BadRequestException("Must specify wich topology to scale. \'scaleName\' was empty ");
        }
        ProvisionResponse provisionInfo = findOne(provisionID);
        boolean scaleNameExists = false;
        int currentNumOfInstances = 1;
        Map<String, Object> plan = provisionInfo.getKeyValue();
        String cloudProvider = null;
        String domain = null;
        for (String key : plan.keySet()) {
            Map<String, Object> subMap = (Map<String, Object>) plan.get(key);
            if (subMap.containsKey("topologies")) {
                List< Map<String, Object>> topologies = (List< Map<String, Object>>) subMap.get("topologies");
                for (Map<String, Object> topology : topologies) {
                    if (topology.get("tag").equals("scaling") && topology.get("topology").equals(scaleName) && !scaleNameExists) {
                        cloudProvider = (String) topology.get("cloudProvider");
                        domain = (String) topology.get("domain");
                        scaleNameExists = true;
                    } else if (topology.get("tag").equals("scaled")
                            && topology.get("status").equals("running")
                            && topology.get("copyOf").equals(scaleName)) {
                        currentNumOfInstances++;
                    }
                }
            }
        }

        if (!scaleNameExists) {
            throw new BadRequestException("Name of topology dosen't exist");
        }
        int numOfInstances = Math.abs(currentNumOfInstances - scaleRequest.getNumOfInstances());

        if (currentNumOfInstances > scaleRequest.getNumOfInstances() && currentNumOfInstances != 0) {
            try (DRIPCaller provisioner = new ProvisionerCaller1(messageBrokerHost);) {
                Map<String, String> extra = new HashMap<>();
                extra.put("scale_topology_name", scaleName);
                extra.put("number_of_instances", String.valueOf(numOfInstances));
                extra.put("cloud_provider", cloudProvider);
                extra.put("domain", domain);

                Message scaleMessage = buildTopoplogyModificationMessage(provisionInfo, "scale_topology_down", extra);
                scaleMessage.setOwner(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
                logger.info("Calling provisioner");
                Message response = provisioner.call(scaleMessage);
                logger.info("Got response from provisioner");
                parseCreateResourcesResponse(response.getParameters(), null, provisionInfo, false, false);
            }
        } else if (currentNumOfInstances < scaleRequest.getNumOfInstances() || currentNumOfInstances == 0) {
            try (DRIPCaller provisioner = new ProvisionerCaller1(messageBrokerHost);) {
                Map<String, String> extra = new HashMap<>();
                extra.put("scale_topology_name", scaleName);
                extra.put("number_of_instances", String.valueOf(numOfInstances));
                extra.put("cloud_provider", cloudProvider);
                extra.put("domain", domain);

                Message scaleMessage = buildTopoplogyModificationMessage(provisionInfo, "scale_topology_up", extra);
                scaleMessage.setOwner(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
                logger.info("Calling provisioner");
                Message response = provisioner.call(scaleMessage);
                logger.info("Got response from provisioner");
                parseCreateResourcesResponse(response.getParameters(), null, provisionInfo, false, false);
            }
        }
        return provisionInfo;
    }

    private Message buildProvisioner1Message(ProvisionRequest provisionRequest) throws JSONException, FileNotFoundException, IOException {
        Message invokationMessage = new Message();
        List<MessageParameter> parameters = new ArrayList();

        MessageParameter action = new MessageParameter();
        action.setName("action");
        action.setValue("start_topology");
        parameters.add(action);

        PlanResponse plan = addCloudCredentialsOnTOSCAPlan(provisionRequest);
        List<MessageParameter> topologies = buildTopologyParams(plan);
        parameters.addAll(topologies);
        invokationMessage.setParameters(parameters);
        invokationMessage.setCreationDate((System.currentTimeMillis()));
        return invokationMessage;
    }

    private Message buildTopoplogyModificationMessage(ProvisionResponse provisionInfo, String actionName, Map<String, String> extraAttributes) throws JSONException, IOException {
//        Message invokationMessage = new Message();
//        List<MessageParameter> parameters = new ArrayList();
//
//        MessageParameter action = new MessageParameter();
//        action.setName("action");
//        action.setValue(actionName);
//        parameters.add(action);
//
//        List<MessageParameter> topologies = buildProvisionedTopologyParams(provisionInfo);
//        parameters.addAll(topologies);
//
//        List<MessageParameter> clusterKeys = buildClusterKeyParams(provisionInfo);
//        parameters.addAll(clusterKeys);
//
//        List<MessageParameter> userKeys = buildUserKeyParams(provisionInfo);
//        parameters.addAll(userKeys);
//
//        List<MessageParameter> cloudKeys = buildCloudKeyParams(provisionInfo);
//        parameters.addAll(cloudKeys);
//
//        
//        for (String id : provisionInfo.getCloudCredentialsIDs()) {
//            CloudCredentials cred = cloudCredentialsService.findOne(id);
//            if (cred == null) {
//                throw new CloudCredentialsNotFoundException();
//            }
////            List<MessageParameter> cloudCredentialParams = buildCloudCredentialParam(cred, 1);
////            parameters.addAll(cloudCredentialParams);
//        }
//        if (extraAttributes != null && extraAttributes.containsKey("scale_topology_name")) {
//            MessageParameter scale = new MessageParameter();
//            scale.setName("scale_topology_name");
//            scale.setValue(extraAttributes.get("scale_topology_name"));
//            Map<String, String> attributes = new HashMap<>();
//            attributes.put("number_of_instances", extraAttributes.get("number_of_instances"));
//            attributes.put("cloud_provider", extraAttributes.get("cloud_provider"));
//            attributes.put("domain", extraAttributes.get("domain"));
//            scale.setAttributes(attributes);
//            parameters.add(scale);
//        }
//        invokationMessage.setParameters(parameters);
//        invokationMessage.setCreationDate(System.currentTimeMillis());
        return null;
    }

    private ProvisionResponse parseCreateResourcesResponse(List<MessageParameter> parameters,
            ProvisionRequest provisionRequest, ProvisionResponse provisionResponse, boolean saveUserKeys, boolean saveDeployerKeyI) throws Exception {
        if (provisionResponse == null) {
            provisionResponse = new ProvisionResponse();
        }

        PlanResponse plan = addCloudCredentialsOnTOSCAPlan(provisionRequest);
        Map<String, Object> toscaPlan = plan.getKeyValue();
        Map<String, Object> topologyTemplate = (Map<String, Object>) ((Map<String, Object>) toscaPlan.get("topology_template"));
        List<Map<String, Object>> vmList = getVMsFromTopology(toscaPlan);
        List<String> nodeNames = getVMsNodeNamesFromTopology(toscaPlan);

        Map<String, Object> outputs = new HashMap<>();

        for (MessageParameter p : parameters) {
            String name = p.getName();
            if (name.toLowerCase().contains("exception")) {
                RuntimeException ex = ExceptionHandler.generateException(name, p.getValue());
                Logger.getLogger(ProvisionController.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            if (name.equals("deploy_parameters")) {
                String value = p.getValue();
                String[] lines = value.split("\n");
                if (value.length() < 2) {
                    throw new Exception("Provision failed");
//                        this.deleteProvisionedResources(provisionResponse);
                }

                for (int i = 0; i < lines.length; i++) {
                    String[] parts = lines[i].split(" ");
                    String deployIP = parts[0];
                    String deployUser = parts[1];
                    String deployRole = parts[2];

                    String nodeName = nodeNames.get(i);

                    Map<String, Object> properties = (Map<String, Object>) vmList.get(i).get("properties");
                    properties.put("user_name", deployUser);

                    outputs = TOSCAUtils.buildTOSCAOutput(outputs, nodeName, deployIP, "ip", false);
                    outputs = TOSCAUtils.buildTOSCAOutput(outputs, nodeName, deployUser, "user_name", false);
                    outputs = TOSCAUtils.buildTOSCAOutput(outputs, nodeName, deployRole, "role", false);
                }
            }
            if (name.contains("_key")) {
                for (String nodeName : nodeNames) {
                    outputs = TOSCAUtils.buildTOSCAOutput(outputs, nodeName, p.getValue(), name, true);
                }
            }
        }

        topologyTemplate.put("outputs", outputs);
        provisionResponse.setKvMap(toscaPlan);
        provisionResponse = save(provisionResponse);
        return provisionResponse;
    }

    private void parseDeleteResourcesResponse(List<MessageParameter> parameters, ProvisionResponse provisionInfo) {
    }

    private PlanResponse addCloudCredentialsOnTOSCAPlan(ProvisionRequest provisionRequest) {
        List<Map<String, Object>> credentials = new ArrayList<>();

        for (String id : provisionRequest.getCloudCredentialsIDs()) {
            CloudCredentials cred = cloudCredentialsService.findOne(id);
            if (cred == null) {
                throw new CloudCredentialsNotFoundException();
            }
            Map<String, Object> toscaCredential = new HashMap<>();
            Map<String, Object> toscaCredentialProperties = new HashMap<>();
            toscaCredentialProperties.put("token_type", "secretKey");
            toscaCredentialProperties.put("token", cred.getSecretKey());
            toscaCredentialProperties.put("accessKeyId", cred.getAccessKeyId());
            toscaCredentialProperties.put("cloud_provider_name", cred.getCloudProviderName());
            toscaCredentialProperties.put("attributes", cred.getAttributes());
            toscaCredential.put("properties", toscaCredentialProperties);
            credentials.add(toscaCredential);
        }
        PlanResponse plan = simplePlanService.getDao().findOne(provisionRequest.getPlanID());
        Map<String, Object> toscaPlan = plan.getKeyValue();
        Map<String, Object> nodeTemplates = (Map<String, Object>) ((Map<String, Object>) toscaPlan.get("topology_template")).get("node_templates");
        Iterator it = nodeTemplates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry node = (Map.Entry) it.next();
            Map<String, Object> value = (Map<String, Object>) node.getValue();
            String type = (String) value.get("type");
            if (type.equals("tosca.nodes.ARTICONF.VM.topology")) {
                Map<String, Object> properties = (Map<String, Object>) value.get("properties");
                properties.put("credentials", credentials);
            }
        }
        return plan;
    }

    public String get(String id, String format) throws JSONException {
        ProvisionResponse pro = findOne(id);
        Map<String, Object> map = pro.getKeyValue();

        if (format != null && format.equals("yml")) {
            String ymlStr = Converter.map2YmlString(map);
            ymlStr = ymlStr.replaceAll("\\uff0E", ".");
            return ymlStr;
        }
        if (format != null && format.equals("json")) {
            String jsonStr = Converter.map2JsonString(map);
            jsonStr = jsonStr.replaceAll("\\uff0E", ".");
            return jsonStr;
        }
        String ymlStr = Converter.map2YmlString(map);
        ymlStr = ymlStr.replaceAll("\\uff0E", ".");
        return ymlStr;

    }
}
