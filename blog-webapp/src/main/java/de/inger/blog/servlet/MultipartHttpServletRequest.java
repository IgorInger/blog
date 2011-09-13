package de.inger.blog.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Multipart http servlet request.
 * 
 * @author Igor Inger
 * 
 */
public class MultipartHttpServletRequest extends HttpServletRequestWrapper {

	/**
	 * Parameters.
	 */
	private Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * File uploads.
	 */
	private Map<String, UploadedFile> fileUploads = new HashMap<String, UploadedFile>();

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(MultipartHttpServletRequest.class);

	/**
	 * Constructor.
	 * 
	 * @param request
	 *            request.
	 * @param diskFileItems
	 *            disk file items.
	 */
	public MultipartHttpServletRequest(HttpServletRequest request, List<DiskFileItem> diskFileItems) {
		super(request);
		updateStates(diskFileItems);
	}

	/**
	 * Update states.
	 * 
	 * @param diskFileItems
	 *            disk files items.
	 */
	private void updateStates(List<DiskFileItem> diskFileItems) {
		for (DiskFileItem diskFileItem : diskFileItems) {
			String key = diskFileItem.getFieldName();
			if (diskFileItem.isFormField()) {
				String value = new String(diskFileItem.get());
				parameters.put(key, value);
			} else {
				try {
					String name = diskFileItem.getName();
					InputStream inputStream = diskFileItem.getInputStream();
					UploadedFile fileUpload = new UploadedFile(name, inputStream);
					fileUploads.put(key, fileUpload);
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	@Override
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Gets file upload.
	 * 
	 * @return file uploads.
	 */
	public Map<String, UploadedFile> getFileUploads() {
		return fileUploads;
	}

}
