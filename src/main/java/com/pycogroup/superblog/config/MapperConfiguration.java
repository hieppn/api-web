package com.pycogroup.superblog.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
