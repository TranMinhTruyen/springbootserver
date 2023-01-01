package com.ggapp.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
@AllArgsConstructor
public class JwtResponse {
	private String tokenType = "Bearer";
	private String accessToken;
	private String role;
	private List<String> authorities;

	public JwtResponse(String jwt) {
		this.accessToken = jwt;
	}
}
