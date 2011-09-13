package de.inger.blog.faces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 * Paginator component.
 * 
 * @author Igor Inger
 * 
 */
@FacesComponent(value = "HtmlOutputPaginator")
public class HtmlOutputPaginator extends UIOutput {

	/**
	 * Constructor.
	 */
	public HtmlOutputPaginator() {
		super();
		setRendererType("javax.faces.Paginator");
	}

}
