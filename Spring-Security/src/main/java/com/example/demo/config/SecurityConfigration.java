package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtFilter;
import com.example.demo.service.JwtService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigration {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private JwtFilter filter;
	
	@Bean
	public UserDetailsService detailsService()
	{
//		UserDetails admin=User.withUsername("Ramesh")
//				.password(encoder.encode("java"))
//				.roles("ADMIN")
//				.build();
//		UserDetails emp=User.withUsername("vaiju")
//				.password(encoder.encode("pwd1"))
//				.roles("EMP")
//				.build();
//		return new InMemoryUserDetailsManager(admin,emp);
		
		return new UserInfoUserDetailsService();
	}
	
	
	@Bean
    @SuppressWarnings("removal")
	public SecurityFilterChain chain(HttpSecurity http) throws Exception
	{
	     	return  http.csrf(csrf -> csrf.disable()).authorizeHttpRequests().requestMatchers("api/welcome","api/user/new","api/user/authenticate").permitAll()
                     .and().authorizeHttpRequests().requestMatchers("/api/user/***").authenticated().and().sessionManagement()
                     .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                     .and()
                     .authenticationProvider(authenticationProvider())
                     .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                     .build();
	}
	@Bean
	public PasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(detailsService());
		provider.setPasswordEncoder(encoder());
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
	{
		return authenticationConfiguration.getAuthenticationManager();
	}
}
