package com.ggapp.common.dto.response;

import lombok.Data;

/**
 * @author Tran Minh Truyen
 */
@Data
public class JwtResponse {
	private String tokenType = "Bearer";
	private String accessToken;

	public JwtResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}
