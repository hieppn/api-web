package com.pycogroup.superblog.service;

import com.mongodb.BasicDBObject;
import com.pycogroup.superblog.model.Article;
import com.pycogroup.superblog.model.Category;
import com.pycogroup.superblog.model.Comment;
import com.pycogroup.superblog.model.User;
import com.pycogroup.superblog.repository.ArticleRepository;
import com.pycogroup.superblog.repository.CategoryRepository;
import com.pycogroup.superblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article createArticle(Article article, List<String> categoryList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(article.getTitle()));
        query.fields().include("title");
        Article existArticle = mongoOperations.findOne(query, Article.class);
        if (existArticle != null) {
            throwExceptions(HttpStatus.BAD_REQUEST, article.getTitle() + " already existed");
        }
        if (categoryList == null) {
            throwExceptions(HttpStatus.BAD_REQUEST, " Please add category for your article.");
        }
        for (int i = 0; i < categoryList.size(); i++) {
            if (findByCategoryName(categoryList.get(i)) == false) {
                throwExceptions(HttpStatus.BAD_REQUEST, "Category with the name: " + categoryList.get(i) + " not found. ");
            }
        }
        if(findDuplicateCategoryName(categoryList)==true){
            throwExceptions(HttpStatus.BAD_REQUEST, "Duplicate category name!!!");
        }
        article.setCategoryList(categoryList);
        return articleRepository.save(article);
    }
    private boolean findDuplicateCategoryName(List<String> categoryNameList){
        for (int i= 0;i<categoryNameList.size()-1;i++)
            for (int j= i+1;j<categoryNameList.size();j++)
                if(categoryNameList.get(i).equals(categoryNameList.get(j)))
                    return true;
        return false;
    }
    private boolean findByCategoryName(String name) {
        int count = 0;
        List<Category> category = categoryRepository.findAll();
        for (int i = 0; i < category.size(); i++) {
            String getName = category.get(i).getName();
            if (getName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteArticleById(String articleId, String authorId) {
        if (articleRepository.findById(articleId) == null) {
            throwExceptions(HttpStatus.BAD_REQUEST, articleId + " not found");
        } else {
            if (userRepository.findUserById(authorId) == null || articleRepository.findByIdAndAuthorId(articleId, authorId) == null) {
                throwExceptions(HttpStatus.BAD_REQUEST, "User does not have permission.");
            } else {
                articleRepository.deleteArticleById(articleId, authorId);
            }
        }
    }

    @Override
    public Article getArticleById(String articleId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(articleId));
        query.fields().include("content");
        query.fields().include("title");
        query.fields().include("commentList");
        query.fields().include("authorName");
        query.fields().include("authorId");
        Article article = mongoOperations.findOne(query, Article.class);
        return article;
    }

    @Override
    public String createComment(String articleId, Comment comment) {
        if (comment.getUserEmail() == null || comment.getUserName() == null || comment.getComment() == null) {
            throwExceptions(HttpStatus.BAD_REQUEST, "User email, name, comment not null");
        }
        User persistentUser = userRepository.findByEmail(comment.getUserEmail());
        if (persistentUser == null) {
            throwExceptions(HttpStatus.NOT_FOUND, "userEmail " + comment.getUserEmail() + " does not found");
        }
        if (!persistentUser.getName().equals(comment.getUserName())) {
            throwExceptions(HttpStatus.CONFLICT, "userName " + comment.getUserEmail() + " does not " +
                    "\n have the name " + comment.getUserName());
        }

        Article article = articleRepository.getArticleById(articleId);
        String commentId = UUID.randomUUID().toString();
        comment.setCommentId(commentId);
        comment.setArticleId(articleId);
        if (article == null) {
            throwExceptions(HttpStatus.NOT_FOUND, comment.getArticleId() + " not found");
        }

        if (article.getCommentList() == null) {
            article.setCommentList(Arrays.asList(comment));
        } else {
            article.getCommentList().add(comment);
        }
        mongoOperations.save(article);
        return comment.getCommentId();
    }

    @Override
    public void deleteComment(String articleId, String commentId, String userEmail) {
        Query findCommentId = Query.query(Criteria.where("commentList._id").is(commentId));
        Article existCommentId = mongoOperations.findOne(new Query(Criteria.where("commentList._id").is(commentId)), Article.class);
        //System.out.println(existCommentId);
        if (findCommentId == null || existCommentId == null) {
            throwExceptions(HttpStatus.NOT_FOUND, commentId + " does not found");
        } else {
            String existedArticle = articleRepository.findById(articleId);
            if (existedArticle == null) {
                throwExceptions(HttpStatus.NOT_FOUND, " Article not found");
            } else {
                Query findUserEmail = Query.query(Criteria.where("commentList.$.userEmail").is(userEmail));
                if (findUserEmail == null || userRepository.findByEmail(userEmail) == null) {
                    throwExceptions(HttpStatus.BAD_REQUEST, "User does not have permission.");
                } else {
                    Update update = new Update().pull("commentList", new BasicDBObject("_id", commentId));
                    mongoOperations.updateFirst(findCommentId, update, Article.class);
                    throwExceptions(HttpStatus.CREATED, "Delete successfully.");
                }
            }
        }
    }

    @Override
    public void updateComment(String articleId, String commentId, String authorId, String comment) {
        Query findCommentId = Query.query(Criteria.where("commentList._id").is(commentId));
        Article existCommentId = mongoOperations.findOne(new Query(Criteria.where("commentList._id").is(commentId)), Article.class);
        if (findCommentId == null || existCommentId == null) {
            throwExceptions(HttpStatus.NOT_FOUND, "Comment does not found");
        } else {
            String existedArticle = articleRepository.findById(articleId);
            if (existedArticle == null) {
                throwExceptions(HttpStatus.NOT_FOUND, " Article not found");
            } else {
                Article article = articleRepository.findByIdAndAuthorId(articleId, authorId);
                if (article == null) {
                    throwExceptions(HttpStatus.BAD_REQUEST, "User does not have permission.");
                } else {
                    mongoTemplate.updateMulti(
                            new Query(Criteria.where("commentList._id").is(commentId)),
                            new Update().set("commentList.$.comment", comment),
                            Article.class
                    );
                }
            }
        }
    }

    @Override
    public void updateArticle(String articleId, String title, String content) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(articleId));
        if (query == null) {
            throwExceptions(HttpStatus.NOT_FOUND, articleId + " does not found");
        } else {
            Article article = articleRepository.getArticleById(articleId);
            if (content == null) {
                content = article.getContent();
            }
            if (title == null) {
                title = article.getTitle();
            }
            Update update = new Update();
            update.set("content", content);
            update.set("title", title);
            mongoOperations.findAndModify(
                    query, update,
                    new FindAndModifyOptions().returnNew(true), Article.class);
        }
    }

    private void throwExceptions(HttpStatus status, String s) {
        throw new ResponseStatusException(
                status, s
        );
    }
}
