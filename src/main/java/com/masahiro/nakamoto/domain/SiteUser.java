package com.masahiro.nakamoto.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

/**
 * ログインユーザーの情報を表現するオブジェクト
 */
@Data
public class SiteUser implements UserDetails {


	/**
	 * 社員ID
	 */
	private String id;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * パスワード更新日付
	 */
	private String passUpdateDate;

	/**
	 * 権限
	 */
	private String role;

	/**
	 * 権限のCollection
	 */
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
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	/**
	 * 引数なしコンストラクタ
	 */
	public SiteUser() {
		super();
	}

	/**
	 * コンストラクタ
	 *
	 * @param id
	 * @param password
	 * @param passUpdateDate
	 * @param role
	 */
	public SiteUser(String id, String password, String passUpdateDate, String role) {
		super();
		this.id = id;
		this.password = password;
		this.passUpdateDate = passUpdateDate;
		this.role = role;
	}

}
