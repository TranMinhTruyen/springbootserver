package com.ggapp.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
@AllArgsConstructor
public class CustomUserDetail implements UserDetails {
	AccountDetail accountDetail;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if (StringUtils.hasText(accountDetail.getRole())) {
			authorities.add(new SimpleGrantedAuthority(accountDetail.getRole()));
		}
		if (StringUtils.hasText(accountDetail.getDepartmentName())) {
			authorities.add(new SimpleGrantedAuthority(accountDetail.getDepartmentName()));
		}
		if (StringUtils.hasText(accountDetail.getPosition())) {
			authorities.add(new SimpleGrantedAuthority(accountDetail.getPosition()));
		}
		return authorities;
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
