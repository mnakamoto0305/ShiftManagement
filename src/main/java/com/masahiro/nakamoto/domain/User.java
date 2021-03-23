package com.masahiro.nakamoto.domain;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class User implements UserDetails {

	private String id;
	private String password;
	private Date passUpdateDate;
	private int loginMissTimes;
	private boolean unlock;
	private boolean enabled;
	private Date userDueDate;
	private Collection<? extends GrantedAuthority> authority;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO 自動生成されたメソッド・スタブ
		return authority;
	}

	@Override
	public String getPassword() {
		// TODO 自動生成されたメソッド・スタブ
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO 自動生成されたメソッド・スタブ
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		if(this.userDueDate.after(new Date())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO 自動生成されたメソッド・スタブ
		return this.unlock;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		if(this.passUpdateDate.after(new Date())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isEnabled() {
		// TODO 自動生成されたメソッド・スタブ
		return this.enabled;
	}

}
