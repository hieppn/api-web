package com.pycogroup.superblog.service;

import com.pycogroup.superblog.model.Category;

public interface CategoryService {
    Category createCategory(Category category, String name);
}
