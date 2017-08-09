package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.domain.User;
import com.moninfotech.repository.UserRepository;
import com.moninfotech.service.MailService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sayemkcn on 4/2/17.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, MailService mailService) {
        this.userRepo = userRepo;
        this.mailService = mailService;
    }

    @Override
    public User save(User user) {
        this.mailService.sendEmail(user.getEmail(), "HotelsWave Registration", "Please confirm your email by clicking this link https://blog.rimon.xyz");
        return this.userRepo.save(user);
    }

    @Override
    public User findOne(Long id) {
        return this.userRepo.findOne(id);
    }

    @Override
    public List<User> findAll(int page, int size) {
        return this.userRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, Constants.FIELD_ID)).getContent();
    }

    @Override
    public void delete(Long id) {
        this.userRepo.delete(id);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepo.findByEmail(email);
    }

    @Override
    public List<User> findByName(String name) {
        return this.userRepo.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<User> findByEmailOrPhoneNumber(String email, String phoneNumber) {
        return this.userRepo.findByEmailOrPhoneNumber(email, phoneNumber);
    }
}
