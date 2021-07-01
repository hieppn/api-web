package com.pycogroup.superblog.controller;

import com.pycogroup.superblog.api.CategoriesApi;
import com.pycogroup.superblog.api.model.CreateCategoryRequest;
import com.pycogroup.superblog.api.model.ObjectCreationSuccessResponse;
import com.pycogroup.superblog.model.Category;
import com.pycogroup.superblog.model.User;
import com.pycogroup.superblog.service.CategoryService;
import com.pycogroup.superblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RestController
@Slf4j
public class CategoryController implements CategoriesApi {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<ObjectCreationSuccessResponse> createCategory(@Valid CreateCategoryRequest createCategoryRequest) {
        Category category = modelMapper.map(createCategoryRequest, Category.class);
        Category persistCategory= categoryService.createCategory(category, createCategoryRequest.getName());
        ObjectCreationSuccessResponse result = new ObjectCreationSuccessResponse();
        result.setId(persistCategory.getId().toString());
        result.setResponseCode(HttpStatus.CREATED.value());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
