package com.moninfotech.controllers.packages;


import com.moninfotech.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/packages")
public class PackageController {
    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }


    @GetMapping("/{id}")
    private String getPackage(@PathVariable("id") Long id, Model model){
        model.addAttribute("pckg",this.packageService.findOne(id));
        model.addAttribute("pckgList",this.packageService.findAll(null,null));

        model.addAttribute("template", "fragments/package/details");
        return "adminlte/index";
    }

    // get Image by id
    @GetMapping("/image/{id}")
    @ResponseBody
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(this.packageService.findOne(id).getImage());

    }
}
