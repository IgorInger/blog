package de.inger.blog.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * File upload filter.
 * 
 * @author Igor Inger
 *
 */
/**
 * @author Igor Inger
 * 
 */
public class FileUploadFilter implements Filter {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(FileUploadFilter.class);

	/**
	 * Item factory.
	 */
	private DiskFileItemFactory factory;

	/**
	 * Default max file size.
	 */
	private static final long DEFAULT_MAX_FILE_SIZE = 0xA00000;

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		boolean isHttpServletRequest = request instanceof HttpServletRequest;
		if (!isHttpServletRequest) {
			chain.doFilter(request, response);
			return;
		}

		boolean isMultipartContent = ServletFileUpload.isMultipartContent((HttpServletRequest) request);
		if (!isMultipartContent) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		if (!ServletFileUpload.isMultipartContent(servletRequest)) {
			chain.doFilter(request, response);
			return;
		}

		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
		servletFileUpload.setFileSizeMax(DEFAULT_MAX_FILE_SIZE);
		try {
			List<DiskFileItem> diskFileItems = servletFileUpload.parseRequest(servletRequest);
			MultipartHttpServletRequest multipartHttpServletRequest = new MultipartHttpServletRequest(servletRequest,
					diskFileItems);
			chain.doFilter(multipartHttpServletRequest, response);
			return;
		} catch (FileUploadException e) {
			log.warn(e.getMessage());
			chain.doFilter(request, response);
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		factory = new DiskFileItemFactory();
		ServletContext context = config.getServletContext();
		FileCleaningTracker tracker = FileCleanerCleanup.getFileCleaningTracker(context);
		factory.setFileCleaningTracker(tracker);
	}

}
