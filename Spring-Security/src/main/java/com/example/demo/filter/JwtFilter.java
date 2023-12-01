package com.example.demo.filter;

import java.io.IOException;
import java.security.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.config.UserInfoUserDetailsService;
import com.example.demo.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserInfoUserDetailsService detailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authheader=request.getHeader("Authorization");
		String token=null;
		String username=null;
		if(authheader!=null && authheader.startsWith("Bearer"))
		{
			token.substring(7);
			username = jwtService.extractUsername(username);
		}
		if(username!=null  && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails loadUserByUsername = detailsService.loadUserByUsername(username);
			if(jwtService.validateToken(token, loadUserByUsername))
			{
				UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loadUserByUsername,loadUserByUsername.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
			filterChain.doFilter(request, response);
	}

	
}
