package com.moninfotech.service;

import com.moninfotech.domain.User;

import java.util.List;

/**
 * Created by sayemkcn on 4/2/17.
 */
public interface UserService {

    User save(User user);

    User findOne(Long id);

    List<User> findAll(int page,int size);

    void delete(Long id);

}
