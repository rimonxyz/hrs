package com.moninfotech.controllers.offer;


import com.moninfotech.service.OfferService;
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
@RequestMapping("/offers")
public class OfferController {
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService= offerService;
    }


    @GetMapping("/{id}")
    private String getPackage(@PathVariable("id") Long id, Model model){
        model.addAttribute("offer",this.offerService.findOne(id));
        model.addAttribute("offerList",this.offerService.findAll(null,null));

        model.addAttribute("template", "fragments/offer/details");
        return "adminlte/index";
    }

    // get Image by id
    @GetMapping("/image/{id}")
    @ResponseBody
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(this.offerService.findOne(id).getImage());

    }
}
