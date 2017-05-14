package com.moninfotech.controllers.category.admin;

import com.moninfotech.commons.FileIO;
import com.moninfotech.domain.Category;
import com.moninfotech.domain.Facilities;
import com.moninfotech.domain.Room;
import com.moninfotech.logger.Log;
import com.moninfotech.service.CategoryService;
import com.moninfotech.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by sayemkcn on 4/8/17.
 */
@Controller
@RequestMapping(value = "/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final RoomService roomService;

    @Autowired
    public CategoryController(CategoryService categoryService, RoomService roomService) {
        this.categoryService = categoryService;
        this.roomService = roomService;
    }


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
    private String create(@ModelAttribute Category category, BindingResult bindingResult,
                          @RequestParam("images") MultipartFile[] multipartFiles) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        // if all images aren't valid
        if (files.size() != multipartFiles.length)
            return "redirect:/admin/categories/create?message=One or more images are invalid!";
        category.setImages(files);
        // fascilities object will be null if no checkbox is selected, so initialise new object
        if (category.getFacilities() == null) category.setFacilities(new Facilities());
        category = this.categoryService.save(category);
        Log.print(category.toString());
        return "redirect:/admin/categories";
    }

    // CREATE

    // EDIT

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    private String editPage(@PathVariable("id") Long id,
                            Model model) {
        Category category = this.categoryService.findOne(id);
        if (category == null) return "redirect:/admin/categories?message=Category not found!";
        model.addAttribute("category", category);
        return "category/admin/edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    private String edit(@ModelAttribute Category category, BindingResult bindingResult,
                        @PathVariable("id") Long id,
                        @RequestParam("images") MultipartFile[] multipartFiles) {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        List<byte[]> files = FileIO.convertMultipartFiles(multipartFiles);
        boolean isValid = files.size() == multipartFiles.length;
        if (!isValid)
            category.setImages(this.categoryService.findOne(id).getImages());
        else category.setImages(files);
        // fascilities object will be null if no checkbox is selected, so initialise new object
        if (category.getFacilities() == null) category.setFacilities(new Facilities());
        category.setId(id);
        category = this.categoryService.save(category);
        Log.print(category.toString());
        return "redirect:/admin/categories";
    }

    // DELETE

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    private String delete(@RequestParam("delCatId") Long delCatId,
                          @RequestParam("newCatId") Long newCatId) {
        Category delCategory = this.categoryService.findOne(delCatId);
        Category newCategory = this.categoryService.findOne(newCatId);
        if (delCategory == null || newCategory == null)
            return "redirect:/admin/categories?message=Could not find category!";
        // transfer rooms to new category
        List<Room> processedRoomList = this.roomService.organiseRoomListCategory(delCategory.getRoomList(), newCategory);
        this.roomService.saveAll(processedRoomList);
        // delete old category
        try {
            this.categoryService.delete(delCategory.getId());
        } catch (Exception e) {
            return "redirect:/admin/categories?message=Can not delete category, you should select another category to transfer this category rooms.";
        }
        return "redirect:/admin/categories?message=Successfully deleted!";
    }

}

