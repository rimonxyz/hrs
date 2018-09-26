package com.moninfotech.repository;

import com.moninfotech.domain.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    Package findFirstByOrderByIdDesc();
}
