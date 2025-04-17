package com.webapp.ecom.Service.category;

import com.webapp.ecom.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategory();
    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategory(Long id);

}
