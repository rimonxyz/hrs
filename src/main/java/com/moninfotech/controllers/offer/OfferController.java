package com.moninfotech.controllers.offer;


import com.moninfotech.commons.utils.FileIO;
import com.moninfotech.domain.Offer;
import com.moninfotech.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

//        model.addAttribute("template", "fragments/offer/details");
        return "adminlte/fragments/offer/details";
    }

    // get Image by id
    @GetMapping("/image/{id}")
    @ResponseBody
    private ResponseEntity<byte[]> getImage(@PathVariable("id") Long id,
                                            @RequestParam(value = "height", required = false) Integer height,
                                            @RequestParam(value = "width", required = false) Integer width) throws IOException {
        Offer offer = this.offerService.findOne(id);
        if (offer == null) return ResponseEntity.noContent().build();
        byte[] imageBytes = offer.getImage();
        if (height != null && width != null)
            imageBytes = FileIO.getScaledImage(imageBytes, width, height);
        return ResponseEntity.ok()
                .body(imageBytes);

    }
}
