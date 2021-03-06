package com.moninfotech.service;

import com.moninfotech.domain.Category;

import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
public interface CategoryService {
    Category save(Category category);

    List<Category> findAll();

    Category findByName(String name);

    List<Category> findAll(int page, int size);

    Category findOne(Long id);

    void delete(Long id);
}
