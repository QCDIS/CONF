/*
 * tosca-sure
 * TOSCA Simple qUeRy sErvice (SURE). 
 *
 * OpenAPI spec version: 1.0.0
 * Contact: S.Koulouzis@uva.nl
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package nl.uva.sne.drip.sure_tosca.client;

import nl.uva.sne.drip.sure.tosca.client.ApiException;
import java.io.File;
import org.junit.Test;

import java.util.List;
import static nl.uva.sne.drip.commons.utils.ToscaHelperTest.isServiceUp;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;

/**
 * API tests for DefaultApi
 */
@Ignore
public class DefaultApiTest {

    private String serviceBasePath;
    private final Boolean serviceUp;

    public DefaultApiTest() {
        serviceUp = isServiceUp(serviceBasePath);
    }

//    private final DefaultApi api = new DefaultApi();
    /**
     *
     *
     * Recursively get all requirements all the way to the ROOT including the
     * input node&#39;s
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getAllAncestorPropertiesTest() throws ApiException {
        String id = "1";
        String nodeName = null;
        assertTrue(true);
//        List<Map<String, Object>> response = api.getAllAncestorProperties(id, nodeName);

        // TODO: test validations
    }

//    /**
//     *
//     *
//     * Recursively get all requirements all the way to the ROOT including the
//     * input node&#39;s
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getAllAncestorTypesTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        List<String> response = api.getAllAncestorTypes(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * Recursively get all requirements all the way to the ROOT including the
//     * input node&#39;s
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getAncestorsRequirementsTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        Map<String, Object> response = api.getAncestorsRequirements(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * returns the interface types
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getDslDefinitionsTest() throws ApiException {
//        String id = null;
//        List<String> anchors = null;
//        String derivedFrom = null;
////        List<Map<String, Object>> response = api.getDslDefinitions(id, anchors, derivedFrom);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * returns the interface types
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getImportsTest() throws ApiException {
//        String id = null;
////        List<Map<String, Object>> response = api.getImports(id);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * s
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getNodeOutputsTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        List<Map<String, Object>> response = api.getNodeOutputs(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * s
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getNodePropertiesTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        Map<String, Object> response = api.getNodeProperties(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * Returns the requirements for an input node as described in the template
//     * not in the node&#39;s definition
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getNodeRequirementsTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        Map<String, Object> response = api.getNodeRequirements(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * returns nodes templates in topology
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getNodeTemplatesTest() throws ApiException {
//        String id = null;
//        String typeName = null;
//        String nodeName = null;
//        Boolean hasInterfaces = null;
//        Boolean hasProperties = null;
//        Boolean hasAttributes = null;
//        Boolean hasRequirements = null;
//        Boolean hasCapabilities = null;
//        Boolean hasArtifacts = null;
////        List<NodeTemplate> response = api.getNodeTemplates(id, typeName, nodeName, hasInterfaces, hasProperties, hasAttributes, hasRequirements, hasCapabilities, hasArtifacts);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     *
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getNodeTypeNameTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        String response = api.getNodeTypeName(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     *
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getParentTypeNameTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        String response = api.getParentTypeName(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * s
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getRelatedNodesTest() throws ApiException {
//        String id = null;
//        String nodeName = null;
////        List<NodeTemplate> response = api.getRelatedNodes(id, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * returns the interface types
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getRelationshipTemplatesTest() throws ApiException {
//        String id = null;
//        String typeName = null;
//        String derivedFrom = null;
////        List<Map<String, Object>> response = api.getRelationshipTemplates(id, typeName, derivedFrom);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * r
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getTopologyTemplateTest() throws ApiException {
//        String id = null;
////        TopologyTemplate response = api.getTopologyTemplate(id);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     *
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getToscaTemplateTest() throws ApiException {
//        String id = null;
////        ToscaTemplate response = api.getToscaTemplate(id);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * returns the interface types
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void getTypesTest() throws ApiException {
//        String id = null;
//        String kindOfType = null;
//        Boolean hasInterfaces = null;
//        String typeName = null;
//        Boolean hasProperties = null;
//        Boolean hasAttributes = null;
//        Boolean hasRequirements = null;
//        Boolean hasCapabilities = null;
//        Boolean hasArtifacts = null;
//        String derivedFrom = null;
////        List<Map<String, Object>> response = api.getTypes(id, kindOfType, hasInterfaces, typeName, hasProperties, hasAttributes, hasRequirements, hasCapabilities, hasArtifacts, derivedFrom);
//
//        // TODO: test validations
//    }
//
//    /**
//     *
//     *
//     * s
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void setNodePropertiesTest() throws ApiException {
//        String id = null;
//        Object properties = null;
//        String nodeName = null;
////        String response = api.setNodeProperties(id, properties, nodeName);
//
//        // TODO: test validations
//    }
//
//    /**
//     * upload a tosca template description file
//     *
//     * upload and validate a tosca template description file
//     *
//     * @throws ApiException if the Api call fails
//     */
//    @Test
//    public void uploadToscaTemplateTest() throws ApiException {
//        File file = null;
////        String response = api.uploadToscaTemplate(file);
//
//        // TODO: test validations
//    }
}
