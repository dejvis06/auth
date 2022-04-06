package ro.fullscreendigital.auth.custom_exception;

public class EmailNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8107035310904072885L;

	public EmailNotFoundException(String message) {
		super(message);
	}
}
