package de.inger.blog.faces.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/**
 * Paginator renderer.
 * 
 * @author Igor Inger
 * 
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "javax.faces.Paginator")
public class PaginatorRenderer extends Renderer {

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", component);
		long pages = getAsInteger(component.getAttributes().get("pages"));
		long page = getAsInteger(component.getAttributes().get("page"));
		for (int i = 1; i <= pages; i++) {
			if (page == i) {
				responseWriter.write(String.format("%d ", i));
			} else {
				if (i == 1) {
					HtmlOutcomeTargetLink link = new HtmlOutcomeTargetLink();
					link.setOutcome("pretty:index");
					link.setValue(1);
					link.encodeAll(context);
				} else {
					UIParameter parameter = new UIParameter();
					parameter.setName("page");
					parameter.setValue(i);

					HtmlOutcomeTargetLink link = new HtmlOutcomeTargetLink();
					link.setOutcome("pretty:page");
					link.setValue(i);
					link.getChildren().add(parameter);
					link.encodeAll(context);
				}
				responseWriter.write(" ");
			}
		}
		responseWriter.endElement("div");
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		super.encodeEnd(context, component);
	}

	/**
	 * Gets parameter as long.
	 * 
	 * @param obj
	 *            value.
	 * @return long value.
	 */
	public long getAsLong(Object obj) {
		if (obj instanceof String) {
			return Long.parseLong((String) obj);
		}
		return (Long) obj;
	}

	/**
	 * Gets parameter as interger.
	 * 
	 * @param obj
	 *            value.
	 * @return integer value.
	 */
	public int getAsInteger(Object obj) {
		if (obj instanceof String) {
			return Integer.parseInt((String) obj);
		}
		return (Integer) obj;
	}

}
