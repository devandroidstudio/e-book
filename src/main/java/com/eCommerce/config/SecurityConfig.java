package com.eCommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.eCommerce.service.impl.UserSecurityService;
import com.eCommerce.utility.SecurityUtility;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserSecurityService();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	private BCryptPasswordEncoder passwordEncoder() {
		return SecurityUtility.passwordEncoder();
	}

	private static final String[] PUBLIC_MATCHERS = {
			"/css/**",
			"/js/**",
			"/image/**",
			"/newUser",
			"/forgetPassword",
			"/login",
			"/fonts/**",
			"/bookshelf",
			"/bookDetail",
			"/faq",
			"/searchByCategory",
			"/searchBook"

	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http
//			.authorizeRequests().
//		/*	antMatchers("/**").*/
//			antMatchers(PUBLIC_MATCHERS).
//			permitAll().anyRequest().authenticated();
//
//		http
//			.csrf().disable().cors().disable()
//			.formLogin().failureUrl("/login?error")
//			/*.defaultSuccessUrl("/")*/
//			.loginPage("/login").permitAll()
//			.and()
//			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//			.logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
//			.and()
//			.rememberMe();


		http.authorizeRequests()
				.antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated()
				.antMatchers("/").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
				.antMatchers("/admins/**").hasAnyAuthority("ROLE_ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin().failureUrl("/login?error")
				.loginPage("/login").permitAll()
				.and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
				.and()
				.rememberMe()
				.and()
				.exceptionHandling().accessDeniedPage("/403")
		;

	}



}
