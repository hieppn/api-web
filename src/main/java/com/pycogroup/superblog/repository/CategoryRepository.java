package com.pycogroup.superblog.repository;

import com.pycogroup.superblog.model.Article;
import com.pycogroup.superblog.model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, ObjectId>, QueryByExampleExecutor<Category> {
}
