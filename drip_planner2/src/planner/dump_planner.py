
from toscaparser import *
from toscaparser.tosca_template import ToscaTemplate
import toscaparser.utils.yamlparser

class DumpPlanner:
    
    def __init__(self, tosca_file_path):
        self.yaml_dict_tpl = toscaparser.utils.yamlparser.load_yaml(tosca_file_path)
        self.errors = []
        self.warnings = []
        self.tt = None
        try:
            self.tt = ToscaTemplate(path=None, yaml_dict_tpl=self.yaml_dict_tpl)
        except:
            self.warnings.append("Not a valid tosca file")
            
        self.DOCKER_TYPE = 'Switch.nodes.Application.Container.Docker'
        self.COMPUTE_TYPE = 'Switch.nodes.Compute'
        self.HW_HOST_TYPE = 'Switch.datatypes.hw.host'
        self.HOSTED_NODE_TYPE = [self.DOCKER_TYPE, self.COMPUTE_TYPE]
        
        
    def get_docker_types(self):
        docker_types = set([])
        node_types = self.get_node_types()
        for node_type_key in node_types:
            if node_types[node_type_key] and 'derived_from' in node_types[node_type_key].keys():
                if node_types[node_type_key]['derived_from'] == self.DOCKER_TYPE:
                    docker_types.add(node_type_key)
        return  docker_types
    

    def get_node_types(self):
        return self.yaml_dict_tpl['node_types']
    
    
    def get_node_templates(self):
        return self.yaml_dict_tpl['topology_template']['node_templates']
    
    def get_artifacts(self, node):
        if 'artifacts' in node:
            return node['artifacts']
         
 
    
    def get_hosted_nodes(self, node_templates):
        docker_types = self.get_docker_types()
        self.HOSTED_NODE_TYPE  = self.HOSTED_NODE_TYPE + list(docker_types)
        hosted_nodes = []
        for node_key in node_templates:
            for hosted_type in self.HOSTED_NODE_TYPE:
                if node_templates[node_key]['type'] == hosted_type:
                    hosted_node = node_templates[node_key]
                    hosted_node['id'] = node_key
                    hosted_nodes.append(hosted_node)
                    break
        return hosted_nodes
    
    def get_properties(self, node):
        if 'properties' in node:
            return node['properties']
        
    def plan(self):
        node_templates = self.get_node_templates() 
        hosted_nodes = self.get_hosted_nodes(node_templates)
        vms = []
        for node in hosted_nodes:
            vm = {}
            vm['name'] = node['id']
            vm['type'] = self.COMPUTE_TYPE
            
            if 'requirements' in node:
                for req in node['requirements']:
                    if 'host' in req and 'node_filter' in req['host']:
                        vm['host'] = req['host']['node_filter']['capabilities']['host']
                        vm['os'] = req['host']['node_filter']['capabilities']['os']
            if 'host' not in vm:
                host = {}
                host['cpu_frequency'] = '2GHz'
                host['mem_size'] = '4GB'
                host['num_cpus'] = '1'
                host['disk_size'] = '10GB'
                vm['host'] = host
            if 'os' not in vm:
                os = {}
                os['os_version'] = 16.04
                os['distribution'] = 'ubuntu'
                os['type'] = 'linux'
                os['architecture'] = 'x86_64'
                vm['os'] = os
            
            properties = self.get_properties(node)
            if properties and 'scaling_mode' in properties:
                vm['scaling_mode'] = properties['scaling_mode']
            else:
                vm['scaling_mode'] = 'single'
            vms.append(vm)
        return vms
