package com.pycogroup.superblog.repository;
import com.pycogroup.superblog.model.Article;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface ArticleRepository extends MongoRepository<Article, ObjectId>, QueryByExampleExecutor<Article> {
    void deleteArticleById(String articleId,String authorId);
    Article getArticleById(String articleId);
    String findById(String id);
    Article findByIdAndAuthorId(String id, String authorId);
}
