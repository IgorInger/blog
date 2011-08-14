package de.inger.blog.faces.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.view.facelets.ConverterConfig;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;

import javax.faces.view.facelets.TagAttribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Composite converter handler.
 * 
 * @author Igor Inger
 * 
 */
public class CompositeConverterHandler extends ConverterHandler {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(CompositeConverterHandler.class);

	/**
	 * Converters.
	 */
	private TagAttribute converters;

	/**
	 * Constructor.
	 * @param config converter config.
	 */
	public CompositeConverterHandler(ConverterConfig config) {
		super(config);
		log.trace("ConvertCompositeHandler.ConvertCompositeHandler()");
		this.converters = getAttribute("converters");
	}

	/**
	 * Creates converter.
	 * @param ctx facelet context.
	 * @param id id.
	 * @return coverter.
	 */
	private Converter createConverter(FaceletContext ctx, String id) {
		return ctx.getFacesContext().getApplication().createConverter(id);
	}

	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
		log.trace("ConvertCompositeHandler.setAttributes()");
		CompositeConverter compositeConverter = (CompositeConverter) instance;
		String unparsedConverters = converters.getValue(ctx);
		String[] unparsedConverterArray = unparsedConverters.split(",");
		List<String> unparsedConverterList = Arrays.asList(unparsedConverterArray);
		List<Converter> converters = new ArrayList<Converter>();
		for (String unparsedConverter : unparsedConverterList) {
			String parsedConverter = unparsedConverter.trim();
			Converter converter = null;
			try {
				converter = createConverter(ctx, parsedConverter);
				converters.add(converter);
			} catch (RuntimeException e) {
				log.warn(e);
			}
		}
		compositeConverter.setConverters(converters);
		super.setAttributes(ctx, instance);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MetaRuleset createMetaRuleset(Class type) {
		return super.createMetaRuleset(type).ignoreAll();
	}

}
