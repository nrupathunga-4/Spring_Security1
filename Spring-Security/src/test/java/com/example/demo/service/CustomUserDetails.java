package com.example.demo.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class CustomUserDetails {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public String saveUser(User user)
	{
		user.setPassword(encoder.encode(user.getPassword()));
		repository.save(user);
		return "User Added to Database";
	}
	
	public List<User> getAll()
	{
		return repository.findAll();
	}
	
	public User getUserById(int id)
	{
		return repository.findById(id).orElse(null);
	}
}
