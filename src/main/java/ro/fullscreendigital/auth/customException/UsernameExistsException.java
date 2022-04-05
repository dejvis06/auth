package ro.fullscreendigital.auth.customException;

public class UsernameExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8842295621009939444L;

	public UsernameExistsException(String message) {
		super(message);
	}
}
