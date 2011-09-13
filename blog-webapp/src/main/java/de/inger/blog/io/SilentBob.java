package de.inger.blog.io;

public abstract class SilentBob {

	private static Exception lastExpection;

	public static Exception getLastException() {
		Exception ret = lastExpection;
		lastExpection = null;
		return ret;
	}

	protected static void setLastException(Exception exception) {
		lastExpection = exception;
	}

}
