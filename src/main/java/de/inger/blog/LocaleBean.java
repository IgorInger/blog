package de.inger.blog;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.faces.context.FacesContext;

/**
 * l18n controller.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@NoneScoped
public class LocaleBean {

	/**
	 * Switch current locale to german.
	 */
	public void switchToGerman() {
		switchTo(Locale.GERMAN);
	}

	/**
	 * Switch current locale to english.
	 */
	public void switchToEnglish() {
		switchTo(Locale.ENGLISH);
	}

	/**
	 * Switch current locale to russian.
	 */
	public void switchToRussian() {
		switchTo(new Locale("ru"));
	}

	/**
	 * Switch current locale.
	 * 
	 * @param locale
	 *            - new locale.
	 */
	private void switchTo(Locale locale) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getViewRoot().setLocale(locale);
	}

}
