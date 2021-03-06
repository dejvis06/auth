package ro.fullscreendigital.auth.role;

import static ro.fullscreendigital.auth.role.Authority.*;

public enum Role {

	ROLE_USER(USER_AUTHORITIES), ROLE_HR(HR_AUTHORITIES), ROLE_MANAGER(MANAGER_AUTHORITIES),
	ROLE_ADMIN(ADMIN_AUTHORITIES), ROLE_SUPER_USER(SUPER_USER_AUTHORITIES);

	private String[] authorities;

	private Role(String... authorities) {
		this.authorities = authorities;
	}

	public String[] getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}

}
