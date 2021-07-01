package com.pycogroup.superblog.service;

import com.pycogroup.superblog.model.QUser;
import com.pycogroup.superblog.model.User;
import com.pycogroup.superblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User createUser(User user) {
		if(findUserByEmail(user.getEmail())!=null)
		throwExceptions(HttpStatus.BAD_REQUEST,"Email already existed!");
		return userRepository.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> findUsersByPrefixName(String prefix) {
		QUser userQuery = QUser.user;
		return (List<User>) userRepository.findAll(userQuery.name.startsWith(prefix));

	}
	private void throwExceptions(HttpStatus status, String s) {
		throw new ResponseStatusException(
				status, s
		);
	}
}
