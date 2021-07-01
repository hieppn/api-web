package com.pycogroup.superblog.service;

import com.pycogroup.superblog.model.Article;
import com.pycogroup.superblog.model.Category;
import com.pycogroup.superblog.model.Comment;

import java.util.List;

public interface ArticleService {
	List<Article> getAllArticles();
	Article createArticle(Article article, List<String> categoryNames);
	void deleteArticleById(String articleId, String authorId);
	Article getArticleById(String articleId);
	String createComment(String articleId,Comment comment);
	void deleteComment(String articleId, String commentId, String userEmail);
	void updateComment(String articleId, String commentId, String authorId, String comment);
	void updateArticle(String articleId, String title, String content);
}
