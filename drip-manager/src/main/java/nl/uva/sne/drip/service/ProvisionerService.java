/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.drip.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.uva.sne.drip.commons.utils.TOSCAUtils;
import nl.uva.sne.drip.model.Message;
import nl.uva.sne.drip.model.NodeTemplate;
import nl.uva.sne.drip.model.ToscaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author S. Koulouzis
 */
@Service
public class ProvisionerService {

    @Value("${message.broker.queue.provisioner}")
    private String queueName;

    @Value("${tosca.types.interface}")
    private String toscaTypesInterface;

    @Autowired
    private ToscaTemplateService toscaTemplateService;

    public String provision(String id) throws IOException {

        String ymlToscaTemplate = toscaTemplateService.findByID(id);
        ToscaTemplate toscaTemplate = toscaTemplateService.getYaml2ToscaTemplate(ymlToscaTemplate);

        toscaTemplate = addProvisionInterface(toscaTemplate);
        

        return null;
    }

    private List<Map<String, NodeTemplate>> getVmTopologies(ToscaTemplate toscaTemplate) {
        return TOSCAUtils.getNodesByType(toscaTemplate, "tosca.nodes.ARTICONF.VM.topology");
    }

    protected ToscaTemplate addProvisionInterface(ToscaTemplate toscaTemplate) {
        List<Map<String, NodeTemplate>> vmTopologies = getVmTopologies(toscaTemplate);
        for (Map<String, NodeTemplate> vmTopologyMap : vmTopologies) {
            NodeTemplate vmTopology = vmTopologyMap.get(vmTopologyMap.keySet().iterator().next());
            Map<String, Object> interfaces = vmTopology.getInterfaces();
        }

        return toscaTemplate;
    }

}