package com.moninfotech.controllers.packages;

import com.moninfotech.domain.Package;
import com.moninfotech.service.PackageService;
import com.moninfotech.utils.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/packages")
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

//    @GetMapping("")
//    private String allPackages(){
//
//    }

    @GetMapping("")
    private String packages(@RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "size", required = false) Integer size, Model model) {
        List<Package> packageList = this.packageService.findAll(page, size);
        model.addAttribute(packageList);
        model.addAttribute("template", "fragments/package/admin/packages");
        return "adminlte/index";
    }

    @PostMapping("/create")
    private String create(@ModelAttribute Package pckg, BindingResult bindingResult,
                          @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        if (!ImageValidator.isImageValid(multipartFile) || pckg.getTitle() == null || pckg.getTitle().isEmpty())
            return "redirect:/admin/packages?message=Image is not valid!";
        pckg.setImage(multipartFile.getBytes());
        pckg = this.packageService.save(pckg);
        return "redirect:/admin/packages?messageinfo=Package Saved!";
    }

    @GetMapping("/edit/{id}")
    private String editPage(@PathVariable("id") Long id, Model model) {
        Package pckg = this.packageService.findOne(id);
        if (pckg == null) return "redirect:/admin/packages?message=Package not found!";
        model.addAttribute("package", pckg);
        model.addAttribute("packageList",this.packageService.findAll(null,null));
        model.addAttribute("template", "fragments/package/admin/packages");
        return "adminlte/index";
    }

    @PostMapping("/edit/{id}")
    private String edit(@ModelAttribute Package pckg,BindingResult bindingResult){
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        pckg = this.packageService.save(pckg);
        return "redirect:/admin/packages?messageinfo=Package Saved!";
    }

    @PostMapping("/delete/{id}")
    private String delete(@PathVariable("id") Long id) {
        this.packageService.delete(id);
        return "redirect:/admin/packages?messageinfo=Package deleted!";
    }

}
