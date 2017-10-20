package com.moninfotech.controllers.packages.admin;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.domain.Package;
import com.moninfotech.service.PackageService;
import com.moninfotech.utils.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/packages")
public class PackageAdminController {

    private final PackageService packageService;

    @Autowired
    public PackageAdminController(PackageService packageService) {
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
//        model.addAttribute("template", "fragments/package/admin/packages");
        return "adminlte/fragments/package/admin/packages";
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
        if (pckg.getDate() == null) pckg.setDate(new Date());
        model.addAttribute("package", pckg);
        model.addAttribute("pckgDate", DateUtils.getParsableDateFormat().format(pckg.getDate()));
        model.addAttribute("packageList", this.packageService.findAll(null, null));
//        model.addAttribute("template", "fragments/package/admin/packages");
        return "adminlte/fragments/package/admin/packages";
    }

    @PostMapping("/edit/{id}")
    private String edit(@ModelAttribute Package pckg, BindingResult bindingResult,
                        @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        Package existing = this.packageService.findOne(pckg.getId());
        if (existing == null) return "redirect:/admin/packages?message=Couldn't find packages.";
        pckg.setCreated(existing.getCreated());
        if (ImageValidator.isImageValid(multipartFile))
            pckg.setImage(multipartFile.getBytes());
        else pckg.setImage(existing.getImage());
        pckg = this.packageService.save(pckg);
        return "redirect:/admin/packages?messageinfo=Package Saved!";
    }

    @PostMapping("/delete/{id}")
    private String delete(@PathVariable("id") Long id) {
        this.packageService.delete(id);
        return "redirect:/admin/packages?messageinfo=Package deleted!";
    }


}
