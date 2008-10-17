package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.ParameterBuilder;


public class ListPageGenerator extends AbstractGenerator
{
	private static final String TEMPLATE = "list_page.ftl";
	

	public void generate(Environment env, ParameterBuilder pb)
	{
		Template template = env.getTemplate(TEMPLATE);	  
		env.writeWebSource(pb.getParameters().get("model_name")+"-list", template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, ParameterBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getName() {
		return "list";
	}

	public String getDisplayName() {
		return "JSF list page";
	}
	
	public String getDescription() {
		return "Produces a facelets template for list page.";
	}
}
