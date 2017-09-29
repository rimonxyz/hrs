package com.moninfotech.repository;

import com.moninfotech.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sayemkcn on 4/2/17.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    List<User> findByRolesIn(String role, Pageable pageable);
}
