package com.moninfotech.repository;

import com.moninfotech.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sayemkcn on 4/2/17.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
}
