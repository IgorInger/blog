package de.inger.blog.controller;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.inger.blog.servlet.UploadedFile;

/**
 * File controller.
 * 
 * @author Igor Inger
 * 
 */
@ManagedBean
@SessionScoped
public class FileBean {

	/**
	 * uploaded file.
	 */
	private UploadedFile file;

	/**
	 * uploaded file description.
	 */
	private String description;

	/**
	 * Returns the file value.
	 * 
	 * @return the file value
	 */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * Sets the file value.
	 * 
	 * @param file
	 *            the file value to set
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}

	/**
	 * Uploads file.
	 * 
	 * @throws IOException
	 *             if io errors occurs.
	 */
	public void upload() throws IOException {
		System.out.println(description);
	}

	/**
	 * Returns the description value.
	 * 
	 * @return the description value
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description value.
	 * 
	 * @param description
	 *            the description value to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
