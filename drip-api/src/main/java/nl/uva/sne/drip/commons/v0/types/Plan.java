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
package nl.uva.sne.drip.commons.v0.types;

import com.webcohesion.enunciate.metadata.DocumentationExample;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * This class represents a plan to be provided to the planner.
 *
 * @author S. Koulouzis
 */
@XmlRootElement
public class Plan implements Serializable {

    /**
     * Not used. It's only there for backwords compatibility
     */
    @DocumentationExample("user")
    public String user;

    /**
     * Not used. It's only there for backwords compatibility
     */
    @DocumentationExample("123")
    public String pwd;

    /**
     * The contents of the TOSCA description
     */
    @DocumentationExample("tosca_definitions_version: tosca_simple_yaml_1_0\\n\\n\\ndescription: example file for infrastructure planner\\n\\n\\nrepositories:\\n    MOG_docker_hub: \\n      description: MOG project’s code repository in GitHub\\n      url: https://github.com/switch-project/mog\\n      credential:\\n        protocol: xauth\\n        token_type: X-Auth-Token\\n        # token encoded in Base64\\n        token: 604bbe45ac7143a79e14f3158df67091\\n\\n\\nartifact_types:\\n  tosca.artifacts.Deployment.Image.Container.Docker:\\n    derived_from: tosca.artifacts.Deployment.Image\\n\\n\\ndata_types:\\n  Switch.datatypes.QoS.AppComponent:\\n    derived_from: tosca.datatypes.Root\\n    properties:\\n      response_time:\\n        type: string\\n\\n  Switch.datatypes.Application.Connection.EndPoint:\\n    derived_from: tosca.datatypes.Root\\n    properties:\\n      address:\\n        type: string\\n      component_name:\\n        type: string\\n      netmask:\\n        type: string\\n      port_name:\\n        type: string\\n\\n  Switch.datatypes.Application.Connection.Multicast:\\n    derived_from: tosca.datatypes.Root\\n    properties:\\n      multicastAddrIP:\\n        type: string\\n      multicastAddrPort:\\n        type: integer\\n\\n  Switch.datatypes.Network.EndPoint:\\n    derived_from: tosca.datatypes.Root\\n    properties:\\n      address:\\n        type: string\\n      host_name:\\n        type: string\\n      netmask:\\n        type: string\\n      port_name:\\n        type: string\\n\\n  Switch.datatypes.Network.Multicast:\\n    derived_from: tosca.datatypes.Root\\n    properties:\\n      multicastAddrIP:\\n        type: string\\n      multicastAddrPort:\\n        type: integer\\n\\n\\nnode_types:\\n\\n  Switch.nodes.Application.Container.Docker:\\n    derived_from: tosca.nodes.Container.Application\\n    properties:\\n      QoS:\\n        type: Switch.datatypes.QoS.AppComponent\\n    artifacts:\\n      docker_image:\\n        type: tosca.artifacts.Deployment.Image.Container.Docker\\n    interfaces:\\n      Standard:\\n        create:\\n          inputs:\\n            command:\\n              type: string\\n            exported_ports:\\n              type: list\\n              entry_schema:\\n                type: string\\n            port_bindings:\\n              type: list\\n              entry_schema:\\n                type: string\\n\\n  Switch.nodes.Application.Container.Docker.MOG.InputDistributor:\\n    derived_from: Switch.nodes.Application.Container.Docker\\n    artifacts:\\n      docker_image:\\n        type: tosca.artifacts.Deployment.Image.Container.Docker\\n        file: \"mogswitch/InputDistributor:1.0\"\\n        repository: MOG_docker_hub\\n    properties:\\n      inPort: \\n        type: integer\\n      waitingTime:\\n        type: integer\\n      multicastAddrIP:\\n        type: string\\n      multicastAddrPort:\\n        type: integer\\n      videoWidth:\\n        type: integer\\n      videoHeight:\\n        type: integer\\n\\n  Switch.nodes.Application.Container.Docker.MOG.ProxyTranscoder:\\n    derived_from: Switch.nodes.Application.Container.Docker \\n    artifacts:\\n      docker_image:\\n        type: tosca.artifacts.Deployment.Image.Container.Docker\\n        file: \"mogswitch/ProxyTranscoder:1.0\"\\n        repository: MOG_docker_hub\\n    properties:\\n      multicastAddrIP: \\n        type: string\\n      multicastAddrPort:\\n        type: integer\\n      videoWidth:\\n        type: integer\\n      videoHeight:\\n        type: integer\\n\\n  Switch.nodes.Application.Connection:\\n    derived_from: tosca.nodes.Root \\n    properties:\\n      source:\\n        type: Switch.datatypes.Application.Connection.EndPoint\\n      target:\\n        type: Switch.datatypes.Application.Connection.EndPoint\\n      bandwidth:\\n        type: string\\n      latency: \\n        type: string\\n      jitter:  \\n        type: string\\n      multicast:\\n        type: Switch.datatypes.Application.Connection.Multicast\\n\\n  Switch.nodes.Compute:\\n    derived_from: tosca.nodes.Compute\\n    properties:\\n      OStype:\\n        type: string\\n      nodetype:\\n        type: string\\n      domain:\\n        type: string\\n      public_address:\\n        type: string\\n      \n"
            + "        ethernet_port:\\n        type: list\\n        entry_schema:\\n          type: tosca.datatypes.network.NetworkInfo\\n      script:\\n        type: string\\n      installation:\\n        type: string\\n      ssh_credential:\\n        type: tosca.datatypes.Credential\\n\\n  Switch.nodes.Network:\\n    derived_from: tosca.nodes.network.Network\\n    properties:\\n      bandwidth:\\n        type: string\\n      latency:\\n        type: string\\n      jitter:\\n        type: string\\n      source:\\n        type: Switch.datatypes.Network.EndPoint\\n      target:\\n        type: Switch.datatypes.Network.EndPoint\\n      multicast:\\n        type: Switch.datatypes.Network.Multicast\\n\\n\\ntopology_template:\\n  \\n  node_templates:\\n    2d13d708e3a9441ab8336ce874e08dd1:\\n      type: Switch.nodes.Application.Container.Docker.MOG.InputDistributor\\n      artifacts:\\n        docker_image:\\n          file: \"mogswitch/InputDistributor:1.0\"\\n          type: tosca.artifacts.Deployment.Image.Container.Docker\\n          repository: MOG_docker_hub\\n      properties:\\n        QoS:\\n          response_time: 30ms\\n        inPort: 2000\\n        waitingTime: 5\\n        multicastAddrIP: 255.2.2.0\\n        multicastAddrPort: 3000\\n        videoWidth: 176\\n        videoHeight: 100\\n      interfaces:\\n        Standard:\\n          create:\\n            implementation: docker_image\\n            inputs:\\n              command: InputDistributor\\n              exported_ports:\\n                - 2000\\n              port_bindings:\\n                - \"2000:2000\"\\n                - \"3000:3000\"\\n\\n    8fcc1788d9ee462c826572c79fdb2a6a:\\n      type: Switch.nodes.Application.Container.Docker.MOG.ProxyTranscoder\\n      artifacts:\\n        docker_image:\\n          file: \"mogswitch/ProxyTranscoder:1.0\"\\n          type: tosca.artifacts.Deployment.Image.Container.Docker\\n          repository: MOG_docker_hub\\n      properties:\\n        QoS:\\n          response_time: 30ms\\n        multicastAddrIP: 255.2.2.0\\n        multicastAddrPort: 3000\\n        videoWidth: 176\\n        videoHeight: 100\\n      interfaces:\\n        Standard:\\n          create:\\n            implementation: docker_image\\n            inputs:\\n              command: ProxyTranscoder\\n              exported_ports:\\n                - 80\\n              port_bindings:\\n                - \"8080:80\"\\n\\n    5e0add703c8a43938a39301f572e46c0:\\n      type: Switch.nodes.Application.Connection\\n      properties:\\n        source:\\n          address: 192.168.21.11\\n          component_name: 2d13d708e3a9441ab8336ce874e08dd1\\n          netmask: 255.255.255.0\\n          port_name: \"inputDistributor_out\"\\n        target:\\n          address: 192.168.21.12\\n          component_name: 8fcc1788d9ee462c826572c79fdb2a6a\\n          netmask: 255.255.255.0\\n          port_name: \"proxyTranscoder_in\"\\n        latency: 30ms\\n        bandwidth: 130MB/s\\n        jitter: 500ms\\n        multicast:\\n          multicastAddrIP: 255.2.2.0\\n          multicastAddrPort: 3000\\n")
    public String file;

}