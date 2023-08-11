
package com.smartcontact.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Myconfig {

	@Bean
	public UserDetailsService getUserDetailsService() {

		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;
	}

	
	  
	 @Bean 
	 public SecurityFilterChain securityFilterChain(HttpSecurity http)
	  throws Exception {
		  http.csrf().disable() .authorizeHttpRequests().
	           requestMatchers("/user/**").hasRole("USER")
	           .requestMatchers("/admin/**").hasRole("ADMIN")
	               .requestMatchers("/**").permitAll() .anyRequest().authenticated() .and()
	                .formLogin()
	                .loginPage("/login")
	                .loginProcessingUrl("/dologin")
	                .defaultSuccessUrl("/user/index");
		      http.authenticationProvider(daoAuthProvider());
	  
	  return http.build(); }
	 

}
