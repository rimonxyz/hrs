package com.moninfotech.controllers.category.admin;

import com.moninfotech.domain.Category;
import com.moninfotech.domain.Facilities;
import com.moninfotech.logger.Log;
import com.moninfotech.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by sayemkcn on 4/8/17.
 */
@Controller
@RequestMapping(value = "/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String allCategories(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null || page < 0) page = 0;
        model.addAttribute("categoryList", this.categoryService.findAll());
        return "category/admin/all";
    }


    // CREATE

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    private String createPage() {
        return "category/admin/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    private String create(@ModelAttribute Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        // fascilities object will be null if no checkbox is selected, so initialise new object
        if (category.getFacilities() == null) category.setFacilities(new Facilities());
        category = this.categoryService.save(category);
        Log.print(category.toString());
        return "redirect:/admin/categories";
    }

}

