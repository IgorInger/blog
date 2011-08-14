package de.inger.blog.faces.component;

import java.io.IOException;

import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Latex renderer.
 * 
 * @author Igor Inger
 * 
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "javax.faces.Latex")
public class LatexRenderer extends Renderer {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(LatexRenderer.class);

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		log.trace("LatexRenderer.encodeBegin()");
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("img", component);
		encodeChildren(context, component);
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		log.trace("LatexRenderer.encodeChildren()");
		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		log.trace("LatexRenderer.encodeEnd()");
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("img");
	}

}
