package com.pycogroup.superblog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {
	@GetMapping(value = "/")
	public String hello() {
		return "hello";
	}
}
