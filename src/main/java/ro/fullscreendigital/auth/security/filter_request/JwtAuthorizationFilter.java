package ro.fullscreendigital.auth.security.filter_request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ro.fullscreendigital.auth.security.util.JwtTokenUtil;
import ro.fullscreendigital.auth.security.util.SecurityConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
			response.setStatus(HttpStatus.OK.value());
		} else {

			String authorizationheader = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (authorizationheader == null || !authorizationheader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = authorizationheader.substring(SecurityConstant.TOKEN_PREFIX.length());
			String username = jwtTokenUtil.getUsername(token);

			if (jwtTokenUtil.isTokenValid(token, username)
					&& SecurityContextHolder.getContext().getAuthentication() == null) {

				List<GrantedAuthority> authorities = jwtTokenUtil.getAuthorities(token);

				Authentication authentication = jwtTokenUtil.getAuthentication(username, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}

}
