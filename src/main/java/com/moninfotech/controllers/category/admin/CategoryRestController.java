package com.moninfotech.controllers.category.admin;

import com.moninfotech.domain.Category;
import com.moninfotech.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sayem on 13-Jul-17.
 */
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        if (id == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Category>(this.categoryService.findOne(id), HttpStatus.OK);
    }
}
