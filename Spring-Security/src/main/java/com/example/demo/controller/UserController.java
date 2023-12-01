package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.AuthRequest;
import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.JwtService;

@RestController
public class UserController {
	
	@Autowired
	private CustomUserDetails details;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
		
	@GetMapping("/api/welcome")
	public String home()
	{
		return ("<h1>Welcome To EndPoint</h1>");
	}
	
	@PostMapping("/api/user/new")
	public String saveUser(@RequestBody User user)
	{
		return details.saveUser(user);
	}
	
	@GetMapping("/api/user/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<User> getAll()
	{
		return details.getAll();
	}
	
	
	@GetMapping("/api/user/{id}")
	@PreAuthorize("hasAuthority('ROLE_EMP')")
	public ResponseEntity<User> getUserById(@PathVariable  int id)
	{
		return new ResponseEntity<User>(details.getUserById(id),HttpStatus.OK);
	}
	
	@PostMapping("/api/user/authenticate")
	public String AuthenticateAndGetToken(@RequestBody AuthRequest request)
	{
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		if(authenticate.isAuthenticated())
		{
		return jwtService.generateToken(request.getUsername());
		}
		else
		{
			throw new UsernameNotFoundException("Inavlid user");
		}
	}
}
