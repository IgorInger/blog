package de.inger.blog.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Plugin annotation.
 * 
 * @author Igor Inger
 * 
 */
@Target(value = ElementType.TYPE)
@interface Plugin {

	/**
	 * Gets name.
	 * 
	 * @return
	 */
	String name();

}
