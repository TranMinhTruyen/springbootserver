package com.ggapp.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Tran Minh Truyen
 */
@Data
@AllArgsConstructor
public class CustomUserDetail implements UserDetails {
	AccountDetail accountDetail;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		GrantedAuthority authorities = new SimpleGrantedAuthority(accountDetail.getRole());
		return Collections.singleton(authorities);
	}

	@Override
	public String getPassword() {
		return accountDetail.getPassword();
	}

	@Override
	public String getUsername() {
		return accountDetail.getAccount();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return accountDetail.isActive();
	}
}
