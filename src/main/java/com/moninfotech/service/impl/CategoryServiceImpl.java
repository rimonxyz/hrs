package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.domain.Category;
import com.moninfotech.repository.CategoryRepository;
import com.moninfotech.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return categoryRepo.findAll();
    }

    @Override
    public Category findByName(String name) {
        return this.categoryRepo.findByName(name);
    }

    @Override
    public List<Category> findAll(int page,int size) {
        return this.categoryRepo.findAll(new PageRequest(page,size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public Category findOne(Long id) {
        return this.categoryRepo.findOne(id);
    }

    @Override
    public void delete(Long id) {
        this.categoryRepo.delete(id);
    }
}
