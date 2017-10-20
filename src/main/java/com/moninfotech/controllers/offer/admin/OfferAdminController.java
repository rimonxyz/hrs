package com.moninfotech.controllers.offer.admin;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.domain.Offer;
import com.moninfotech.domain.Package;
import com.moninfotech.service.OfferService;
import com.moninfotech.service.PackageService;
import com.moninfotech.utils.ImageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/offers")
public class OfferAdminController {

    private final OfferService offerService;

    @Autowired
    public OfferAdminController(OfferService packageService) {
        this.offerService = packageService;
    }

//    @GetMapping("")
//    private String allPackages(){
//
//    }

    @GetMapping("")
    private String offers(@RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "size", required = false) Integer size, Model model) {
        List<Offer> offerList = this.offerService.findAll(page, size);
        model.addAttribute(offerList);
//        model.addAttribute("template", "fragments/offer/admin/offers");
        return "adminlte/fragments/offer/admin/offers";
    }

    @PostMapping("/create")
    private String create(@ModelAttribute Offer offer, BindingResult bindingResult,
                          @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        if (!ImageValidator.isImageValid(multipartFile) || offer.getTitle() == null || offer.getTitle().isEmpty())
            return "redirect:/admin/offers?message=Image is not valid!";
        offer.setImage(multipartFile.getBytes());
        offer = this.offerService.save(offer);
        return "redirect:/admin/offers?messageinfo=Offer Saved!";
    }

    @GetMapping("/edit/{id}")
    private String editPage(@PathVariable("id") Long id, Model model) {
        Offer offer = this.offerService.findOne(id);
        if (offer == null) return "redirect:/admin/offers?message=Offer not found!";
        if (offer.getDate() == null) offer.setDate(new Date());

        model.addAttribute("offer", offer);
        model.addAttribute("offerDate", DateUtils.getParsableDateFormat().format(offer.getDate()));
        model.addAttribute("offerList", this.offerService.findAll(null, null));
//        model.addAttribute("template", "fragments/offer/admin/offers");
        return "adminlte/fragments/offer/admin/offers";
    }

    @PostMapping("/edit/{id}")
    private String edit(@ModelAttribute Offer offer, BindingResult bindingResult,
                        @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) System.out.println(bindingResult.toString());
        Offer existing = this.offerService.findOne(offer.getId());
        if (existing == null) return "redirect:/admin/offers?message=Could not find offers!";
        offer.setCreated(existing.getCreated());

        if (ImageValidator.isImageValid(multipartFile))
            offer.setImage(multipartFile.getBytes());
        else
            offer.setImage(existing.getImage());
        offer = this.offerService.save(offer);
        return "redirect:/admin/offers?messageinfo=Offer Saved!";
    }

    @PostMapping("/delete/{id}")
    private String delete(@PathVariable("id") Long id) {
        this.offerService.delete(id);
        return "redirect:/admin/offers?messageinfo=Offer deleted!";
    }


}
