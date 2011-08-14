package de.inger.blog.servlet;

import java.io.InputStream;

/**
 * Uploaded files.
 * 
 * @author Igor Inger
 * 
 */
public class UploadedFile {

	/**
	 * Name.
	 */
	private String name;

	/**
	 * Input stream.
	 */
	private InputStream inputStream;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            name
	 * @param inputStream
	 *            input stream.
	 */
	public UploadedFile(String name, InputStream inputStream) {
		this.name = name;
		this.inputStream = inputStream;
	}

	/**
	 * Returns the name value.
	 * 
	 * @return the name value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name value.
	 * 
	 * @param name
	 *            the name value to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the inputStream value.
	 * 
	 * @return the inputStream value
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Sets the inputStream value.
	 * 
	 * @param inputStream
	 *            the inputStream value to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
