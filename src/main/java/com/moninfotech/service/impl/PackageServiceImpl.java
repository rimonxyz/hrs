package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.Package;
import com.moninfotech.exceptions.NotFoundException;
import com.moninfotech.repository.PackageRepository;
import com.moninfotech.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepo;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepo) {
        this.packageRepo = packageRepo;
    }

    @Override
    public List<Package> findAll(Integer page, Integer size) {
        if (page == null || size == null)
            return this.packageRepo.findAll();
        return this.packageRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID)).getContent();
    }

    @Override
    public Package save(Package pckg) {
        return this.packageRepo.save(pckg);
    }

    @Override
    public Package findOne(Long id) {
        return this.packageRepo.findOne(id);
    }

    @Override
    public void delete(Long id) {
        this.packageRepo.delete(id);
    }

    @Override
    public Package getLatestPackage() throws NotFoundException {
        Package pckg = this.packageRepo.findFirstByOrderByIdDesc();
        if (pckg==null) throw new NotFoundException("Could not find any package!");
        return pckg;
    }
}
