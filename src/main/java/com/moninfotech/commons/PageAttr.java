package com.moninfotech.commons;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageAttr{

    public static PageRequest buildPageRequest(int page,int size){
        return  new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID);
    }
    public static PageRequest buildPageRequest(int page,int size,String sortBy){
        return  new PageRequest(page, size, Sort.Direction.DESC, sortBy);
    }
}
