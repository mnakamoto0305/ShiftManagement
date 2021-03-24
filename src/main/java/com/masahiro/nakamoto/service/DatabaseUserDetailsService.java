package com.masahiro.nakamoto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.masahiro.nakamoto.mybatis.UserMapper;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		if (StringUtils.isEmpty(username)) {
		    throw new UsernameNotFoundException("username is empty");
		  }

		UserDetails user = userMapper.identifyUser(username);

		if (user == null) {
		    throw new UsernameNotFoundException("Not found username : " + username);
		  }

		return userMapper.identifyUser(username);
	}

}
