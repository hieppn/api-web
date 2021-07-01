package com.pycogroup.superblog.controller;

import com.pycogroup.superblog.api.ArticlesApi;
import com.pycogroup.superblog.api.model.*;
import com.pycogroup.superblog.model.Article;
import com.pycogroup.superblog.model.Comment;
import com.pycogroup.superblog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class ArticleController implements ArticlesApi {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ArticleListResponse> getArticleList() {
        List<Article> articleList = articleService.getAllArticles();
        return buildArticleListResponse(articleList);
    }

    @Override
    public ResponseEntity<ObjectUpdateSuccessResponse> updateArticle(String articleId, @Valid UpdateArticleRequest updateArticleRequest) {
        //System.out.println(updateArticleRequest.getTitle());
        String title = updateArticleRequest.getTitle();
        String content = updateArticleRequest.getContent();
        articleService.updateArticle(articleId, title, content);
        ObjectUpdateSuccessResponse result = new ObjectUpdateSuccessResponse();
        result.setResponseCode(HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ObjectUpdateSuccessResponse> updateComment(String articleId, String commentId, @Valid UpdateCommentRequest updateCommentRequest) {
        articleService.updateComment(articleId, commentId, updateCommentRequest.getAuthorId(),updateCommentRequest.getComment());
        ObjectUpdateSuccessResponse result = new ObjectUpdateSuccessResponse();
        result.setResponseCode(HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ObjectCreationSuccessResponse> createArticle(@Valid CreateArticleRequest createArticleRequest) {
        Article article = modelMapper.map(createArticleRequest, Article.class);
        Article persistArticle = articleService.createArticle(article,createArticleRequest.getCategoryNames() );
        ObjectCreationSuccessResponse result = new ObjectCreationSuccessResponse();
        result.setId(persistArticle.getId().toString());
        result.setResponseCode(HttpStatus.CREATED.value());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ObjectCreationSuccessResponse> createComment(String articleId,@Valid CreateCommentRequest createCommentRequest) {
        Comment comment = modelMapper.map(createCommentRequest, Comment.class);
        String persistComment = articleService.createComment(articleId,comment);
        ObjectCreationSuccessResponse result = new ObjectCreationSuccessResponse();
        result.setId(persistComment);
        result.setResponseCode(HttpStatus.CREATED.value());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ArticleResponseModel> getArticleById(String articleId) {
        Article article = articleService.getArticleById(articleId);
        System.out.println(article);
        return buildArticleResponse(article);
    }

    private ResponseEntity<ArticleListResponse> buildArticleListResponse(List<Article> articleList) {
        ArticleListResponse articleListResponse = new ArticleListResponse();

        if (articleList != null) {
            articleList.forEach(item -> articleListResponse.addArticlesItem(modelMapper.map(item, com.pycogroup.superblog.api.model.ArticleResponseModel.class)));
        }
        return new ResponseEntity(articleListResponse, HttpStatus.OK);
    }
    private ResponseEntity<ArticleResponseModel> buildArticleResponse(Article article) {
        ArticleResponseModel articleResponse = new ArticleResponseModel();
        if (article != null) {
            modelMapper.map(article, com.pycogroup.superblog.api.model.ArticleResponseModel.class);
        }
        return new ResponseEntity(articleResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ObjectDeleteSuccessResponse> deleteArticleById(String articleId, @Valid DeleteArticleRequest deleteArticleRequest) {
        articleService.deleteArticleById(articleId, deleteArticleRequest.getAuthorId());
        ObjectDeleteSuccessResponse result = new ObjectDeleteSuccessResponse();
        result.setResponseCode(HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ObjectDeleteSuccessResponse> deleteComment(String articleId, String commentId, @Valid DeleteCommentRequest deleteCommentRequest) {
        articleService.deleteComment(articleId, commentId, deleteCommentRequest.getUserEmail());
        ObjectDeleteSuccessResponse result = new ObjectDeleteSuccessResponse();
        result.setResponseCode(HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
