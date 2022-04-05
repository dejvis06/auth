package ro.fullscreendigital.auth.role;

public class Authority {

	// read -> user info
	public static final String[] USER_AUTHORITIES = { "user:read" };
	public static final String[] HR_AUTHORITIES = { "user:read", "user:update" };
	public static final String[] MANAGER_AUTHORITIES = { "user:read", "user:update" };
	public static final String[] ADMIN_AUTHORITIES = { "user:read", "user:create", "user:update" };
	public static final String[] SUPER_USER_AUTHORITIES = { "user:read", "user:create", "user:update", "user:delete" };

}
