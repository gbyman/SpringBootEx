package org.zero.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zero.security.CustomLoginSuccessHandler;
import org.zero.security.CustomUserDetailsService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Configuration
@EnableWebSecurity
@Log4j
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Setter(onMethod_ = {@Autowired})
	private DataSource dataSource;
	
/*	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		log.info("configure...........");
		auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
		
		// $2a$10$PTY/PdIVpI5Jtef.jUyBkeMRnK9M2N.C/TayRarmbvJcmkLmfI4ca
		auth.inMemoryAuthentication().withUser("member")
			.password("$2a$10$PTY/PdIVpI5Jtef.jUyBkeMRnK9M2N.C/TayRarmbvJcmkLmfI4ca")
			.roles("MEMBER");
	}*/
	
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService customUserService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		return repo;
	}
	
	/*@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		log.info("configure...........");
		String queryUser = "select userid, userpw, enabled from tbl_member where userid = ?";
		String queryDetails = "select userid, auth from tbl_member_auth where userid = ?";
		
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder())
			.usersByUsernameQuery(queryUser).authoritiesByUsernameQuery(queryDetails);
	}*/
	
	//in custom userdetails
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/sample/all").permitAll()
			.antMatchers("/sample/admin").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/sample/member").access("hasRole('ROLE_MEMBER')");
		
		// xml을 이용하는 경우와 전체적으로 거의 유사함
		// 가장 큰 차이는 xml의 경우 기본으로 POST 방식을 처리하는 경로가 '/login'으로 지정되지만
		// Java 설정을 이용하는 경우 loginPage()에 해당하는 경로를 기본으로 사용한다는 점
		// xml과 동일하게 동작하는 것을 목표로 하기 위해 loginProcessingUrl()을 이용해서 '/login'을 지정
		http.formLogin().loginPage("/customLogin").loginProcessingUrl("/login").successHandler(loginSuccessHandler());
		
		http.logout().logoutUrl("/customLogout").invalidateHttpSession(true).deleteCookies("remember-me", "JSESSION_ID");
		
		http.rememberMe().key("zero").tokenRepository(persistentTokenRepository()).tokenValiditySeconds(604800);
	}
	
}
