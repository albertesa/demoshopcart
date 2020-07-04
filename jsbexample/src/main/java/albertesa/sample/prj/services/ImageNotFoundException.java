package albertesa.sample.prj.services;

import albertesa.sample.prj.repositories.FileSystemException;

public class ImageNotFoundException extends FileSystemException {

	private static final long serialVersionUID = 2344085406146032431L;

	public ImageNotFoundException(String message) {
		super(message);
	}

	public ImageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
