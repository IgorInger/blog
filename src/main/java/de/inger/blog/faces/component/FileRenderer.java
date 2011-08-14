package de.inger.blog.faces.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import javax.faces.render.FacesRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;

import de.inger.blog.servlet.MultipartHttpServletRequest;

/**
 * File renderer.
 * 
 * @author Igor Inger
 * 
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "javax.faces.File")
public class FileRenderer extends HtmlBasicInputRenderer {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(FileRenderer.class);

	@Override
	protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue)
			throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("input", component);
		writer.writeAttribute("type", "file", null);
		writer.writeAttribute("name", component.getClientId(context), "clientId");
		writer.endElement("input");

	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		rendererParamsNotNull(context, component);
		if (!shouldDecode(component)) {
			return;
		}
		String clientId = decodeBehaviors(context, component);
		if (clientId == null) {
			clientId = component.getClientId(context);
		}

		try {
			MultipartHttpServletRequest request = (MultipartHttpServletRequest) context.getExternalContext()
					.getRequest();
			setSubmittedValue(component, request.getFileUploads().get(clientId));
		} catch (ClassCastException e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {
		return submittedValue;
	}

}
