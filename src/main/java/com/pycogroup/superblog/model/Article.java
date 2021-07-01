package com.pycogroup.superblog.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "articles")
@Builder
@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Article {
	@Id
	@Getter
	private ObjectId id;

	@Getter
	@Setter
	private String title;


	@Getter
	@Setter
	private String content;

	@Getter
	@Setter
	private String authorId;

	@Getter
	@Setter
	private String authorName;

	@Getter
	@Setter
	private List<Comment> commentList;

	@Getter
	@Setter
	private List<String> categoryList;

}
