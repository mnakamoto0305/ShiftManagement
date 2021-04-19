package com.masahiro.nakamoto;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser>{

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // ユーザ名・パスワードで認証するためのトークンを発行
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customUser.username(), customUser.password());
        // ログイン処理
        AuthenticationManager authenticationManager = new AuthenticationManager() {

			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				// TODO 自動生成されたメソッド・スタブ
				return null;
			}
		};

		Authentication auth = authenticationManager.authenticate(authToken);

        context.setAuthentication(auth);

        return context;
    }

}