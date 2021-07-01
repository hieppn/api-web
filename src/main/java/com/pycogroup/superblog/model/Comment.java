package com.pycogroup.superblog.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "comments")
@Builder
@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @Getter
    @Setter
    private String commentId;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private boolean hidden;

    @Getter
    @Setter
    private String articleId;

    @Setter
    @Getter
    private String userName;

    @Setter
    @Getter
    private String userEmail;

}
