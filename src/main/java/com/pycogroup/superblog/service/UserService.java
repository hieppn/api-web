package com.pycogroup.superblog.service;

import com.pycogroup.superblog.model.User;

import java.util.List;

public interface UserService {
	List<User> getAllUsers();
	User createUser(User user);
	User findUserByEmail(String email);
	List<User> findUsersByPrefixName(String prefix);
}
