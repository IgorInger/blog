package de.inger.blog.faces.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * Converter from mardown text to html.
 * 
 * @author Igor Inger
 * 
 */
@FacesConverter(value = "markdown")
public class MarkdownConverter implements Converter {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(MarkdownConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		log.trace("MarkdownConverter.getAsObject()");
		log.debug(value);
		return null;
	}

	@Override
	public String getAsString(FacesContext content, UIComponent component, Object value) {
		return new MarkdownProcessor().markdown((String) value);
	}

}
