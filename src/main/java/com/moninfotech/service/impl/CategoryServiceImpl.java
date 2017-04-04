package com.moninfotech.service.impl;

import com.moninfotech.domain.Category;
import com.moninfotech.repository.CategoryRepository;
import com.moninfotech.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sayemkcn on 4/4/17.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public Category save(Category category) {
        return this.categoryRepo.save(category);
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepo.findAll();
    }

    @Override
    public Category findOne(Long id) {
        return this.categoryRepo.findOne(id);
    }
}
