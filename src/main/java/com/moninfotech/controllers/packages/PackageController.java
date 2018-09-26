package com.moninfotech.controllers.packages;


import com.moninfotech.commons.utils.FileIO;
import com.moninfotech.domain.Package;
import com.moninfotech.exceptions.NotFoundException;
import com.moninfotech.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

//        model.addAttribute("template", "fragments/package/details");
        return "adminlte/fragments/package/details";
    }

    // get Image by id
    @GetMapping("/image/{id}")
    @ResponseBody
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id,
                                            @RequestParam(value = "height", required = false) Integer height,
                                            @RequestParam(value = "width", required = false) Integer width) throws IOException {
        Package pckg = this.packageService.findOne(id);
        if (pckg == null) return ResponseEntity.noContent().build();
        byte[] imageBytes = pckg.getImage();
        if (height != null && width != null)
            imageBytes = FileIO.getScaledImage(imageBytes, width, height);
        return ResponseEntity.ok()
                .body(imageBytes);

    }

    @GetMapping("/latest")
    private String getLatestPackage() throws NotFoundException {
        Package pckg = this.packageService.getLatestPackage();
        return "redirect:/packages/" + pckg.getId();
    }
}
