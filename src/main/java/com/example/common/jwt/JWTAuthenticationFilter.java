package com.example.common.jwt;

import com.example.services.implement.UserServicesImplement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * @author Tran Minh Truyen
 */

@Slf4j
public class JWTAuthenticationFilter extends GenericFilter {

	@Autowired
	private JWTTokenProvider jwtTokenProvider;

	@Autowired
	private UserServicesImplement userServicesImplement;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		try {
			String jwt = getJwtFromRequest((HttpServletRequest) servletRequest);
			if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
				int userId = jwtTokenProvider.getUserIdFromJWT(jwt);
				UserDetails userDetails = userServicesImplement.loadUserById(userId);
				if(userDetails != null && userDetails.isEnabled()) {
					UsernamePasswordAuthenticationToken
							authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) servletRequest));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception exception) {
			SecurityContextHolder.clearContext();
			throw new AccessDeniedException(exception.getMessage());
		}
		filterChain.doFilter((HttpServletRequest) servletRequest, servletResponse);
	}

	public String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
