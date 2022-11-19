package com.ggapp.common.jwt;
import com.ggapp.common.utils.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author Tran Minh Truyen
 */

@Component
@Slf4j
public class JWTTokenProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTTokenProvider.class);

	public String generateToken(CustomUserDetail userDetail, boolean isRemember) {
		long now = (new Date()).getTime();
		Date expiryDate;
		if (!isRemember)
			expiryDate = new Date(now + Constant.EXPIRATIONTIME);
		else expiryDate = new Date(now + Constant.EXPIRATIONTIME_FOR_REMEMBER);
		Claims claims = Jwts.claims().setSubject(Integer.toString(userDetail.getUser().getId()));
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, Constant.SECRET)
				.compact();
	}

	public int getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(Constant.SECRET)
				.parseClaimsJws(token)
				.getBody();
		return Integer.parseInt(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(Constant.SECRET).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty.");
		}
		return false;
	}
}
