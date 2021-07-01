package com.pycogroup.superblog.service;

import com.pycogroup.superblog.model.Article;
import com.pycogroup.superblog.model.Category;
import com.pycogroup.superblog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Override
    public Category createCategory(Category category, String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        Category existCategory = mongoOperations.findOne(query, Category.class);        System.out.println(query);
       if(existCategory!=null){
           throwExceptions(HttpStatus.BAD_REQUEST,"Category with "+name+ "is existed");
       }
        return categoryRepository.save(category);
    }

    private void throwExceptions(HttpStatus status, String s) {
        throw new ResponseStatusException(
                status, s
        );
    }
}
