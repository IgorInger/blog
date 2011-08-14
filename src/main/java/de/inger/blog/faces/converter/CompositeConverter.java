package de.inger.blog.faces.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Composite converter.
 * 
 * @author Igor Inger
 * 
 */
@FacesConverter("composite")
public class CompositeConverter implements Converter {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(CompositeConverter.class);

	/**
	 * Converters.
	 */
	private List<Converter> converters;

	/**
	 * Sets converters.
	 * 
	 * @param converters
	 *            converters.
	 */
	public void setConverters(List<Converter> converters) {
		this.converters = converters;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		log.trace("CompositeConverter.getAsObject()");
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String result = (String) value;
		for (Converter converter : converters) {
			result = converter.getAsString(context, component, result);
		}
		return result;
	}

}
