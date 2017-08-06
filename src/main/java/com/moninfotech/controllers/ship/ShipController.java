package com.moninfotech.controllers.ship;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.controllers.hotel.HotelController;
import com.moninfotech.domain.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Sayem Hossain
 * @since Aug, 6 2017
 */

@Controller
@RequestMapping("/ships")
public class ShipController {

    @GetMapping("")
    private String all(@RequestParam(value = "query", required = false,defaultValue = "") String query,
                       @RequestParam(value = "page", required = false,defaultValue = "0") Integer page,
                       Model model) {
        return "redirect:/hotels?type=" + Hotel.Type.SHIP + "&query=" + query + "&page=" + page;
    }

}
