package de.inger.blog.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 * Latex element.
 * 
 * @author Igor Inger
 * 
 */
@FacesComponent(value = "HtmlOutputLatex")
public class HtmlOutputLatex extends UIOutput {

	@Override
	public String getRendererType() {
		return "javax.faces.Latex";
	}

}
