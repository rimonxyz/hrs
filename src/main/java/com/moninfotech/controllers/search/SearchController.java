package com.moninfotech.controllers.search;

import com.moninfotech.commons.pojo.ParamFacilities;
import com.moninfotech.domain.Hotel;
import com.moninfotech.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by sayemkcn on 4/21/17.
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController {

    private final HotelService hotelService;

    @Autowired
    public SearchController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String search(@RequestParam(value = "query",required = false,defaultValue = "") String query,
                          @RequestParam(value = "isDesc", required = false) boolean isDesc,
                          @RequestParam(value = "page", required = false,defaultValue = "0") Integer page,
                          @RequestParam(value = "sortBy", required = false) String soryBy,
                          @RequestParam(value = "filterType", required = false) String filterType,
                          @RequestParam(value = "filterValue", required = false) String filterValue,
                          Model model) {
        if (page < 0) page = 0;

        List<Hotel> hotels = this.hotelService.searchHotel(query,page);
        // filter
        if (filterType != null && !filterType.isEmpty() && filterValue != null && !filterValue.isEmpty())
            hotels = this.hotelService.filterHotels(hotels, filterType, filterValue);

        model.addAttribute("hotelList", hotels);
        if (filterValue != null)
            model.addAttribute("filterValue", filterValue);
        model.addAttribute("areaList", this.hotelService.getAddressAreaAndUpazilaList());
        model.addAttribute("isDesc", !isDesc);
        model.addAttribute("query", query);
        model.addAttribute("hotelType","Hotel");
        model.addAttribute("page",page);
        model.addAttribute("f", new ParamFacilities());
        model.addAttribute("sidebarCollapse", true);
//        model.addAttribute("template","fragments/hotel/all");
        return "adminlte/fragments/hotel/all";
    }

}
