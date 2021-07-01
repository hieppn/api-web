package com.pycogroup.superblog.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Builder
@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    @Id
    @Setter
    @Getter
    private ObjectId id;

    @Getter
    @Setter
    private String name;

}
