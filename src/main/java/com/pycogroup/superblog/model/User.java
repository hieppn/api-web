package com.pycogroup.superblog.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Document(collection = "users")
@Builder
@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@Getter
	private ObjectId id;

	@Getter
	@Setter
	@NotNull
	@NotBlank(message="Please enter your name")
	private String name;

	@Getter
	@Setter
	@Indexed(unique=true)
	@NotNull
	@NotBlank(message="Please enter your email")
	private String email;
	@Getter
	@Setter
	@NotNull
	@NotBlank(message="Please enter your address")
	private String address;
	@Getter
	@Setter
	private String image;
	@Getter
	@Setter
	@NotNull
	@NotBlank(message="Please enter your phone")
	@Pattern(regexp="(^$|[0-9]{10})")
	private String phone;
	@Getter
	@Setter
	private Boolean enabled;
}
