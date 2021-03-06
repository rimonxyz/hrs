package com.moninfotech.service;

import com.moninfotech.domain.Package;
import com.moninfotech.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PackageService {

    List<Package> findAll(Integer page, Integer size);

    Package save(Package pckg);

    Package findOne(Long id);

    void delete(Long id);

    Package getLatestPackage() throws NotFoundException;

}
