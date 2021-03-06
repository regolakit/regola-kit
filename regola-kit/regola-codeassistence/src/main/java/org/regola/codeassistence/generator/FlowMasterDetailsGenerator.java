package org.regola.codeassistence.generator;

import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;
import org.regola.codeassistence.Utils;


public class FlowMasterDetailsGenerator extends AbstractGenerator {
	
	public void generate(Environment env, VariablesBuilder pb)
	{
		String flowName = pb.getStringValue("flow_name");
        
		 
        env.addFlowsResource(flowName, "list.xml", env.getTemplate("flow-list_xml.ftl"),pb.getParameters(), null);
        env.addFlowsResource(flowName, "list.xhtml", env.getTemplate("flow-list_xhtml.ftl"),pb.getParameters(), null);
        
        env.addFlowsResource(flowName, "form.xml", env.getTemplate("flow-form_xml.ftl"),pb.getParameters(), null);
        env.addFlowsResource(flowName, "form.xhtml", env.getTemplate("flow-form_xhtml.ftl"),pb.getParameters(), null);
        
        env.addFlowsResource(flowName, "beans.xml", env.getTemplate("flow-beans_xml.ftl"),pb.getParameters(), null);
    	
        env.addFlowDefinition("/WEB-INF/flows/" + flowName + "/list.xml", env.getTemplate("flow-webflow-config_xml.ftl"), pb.getParameters());
        
        //bisogna aggiungere il flusso alla lista dei flussi
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public  FlowMasterDetailsGenerator() {
		name =  "flowMasterDetails";
	}

	public String getDisplayName() {
		return "Webflow Master/Details";
	}
	
	public String getDescription() {
		return "Produces a flow to manage master/details pages.";

	}
}
