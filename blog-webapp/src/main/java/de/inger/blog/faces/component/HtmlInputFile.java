package de.inger.blog.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;

/**
 * Input file element.
 * 
 * @author Igor Inger
 * 
 */
@FacesComponent(value = "HtmlInputFile")
public class HtmlInputFile extends HtmlInputText {

	@Override
	public String getRendererType() {
		return "javax.faces.File";
	}

}
