package com.moninfotech.service.impl;

import com.moninfotech.commons.Constants;
import com.moninfotech.commons.SessionIdentifierGenerator;
import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.AcValidationToken;
import com.moninfotech.domain.User;
import com.moninfotech.exceptions.AlreadyExistsException;
import com.moninfotech.repository.UserRepository;
import com.moninfotech.service.AcValidationTokenService;
import com.moninfotech.service.MailService;
import com.moninfotech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sayemkcn on 4/2/17.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final MailService mailService;
    private final AcValidationTokenService acValidationTokenService;

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, MailService mailService, AcValidationTokenService acValidationTokenService) {
        this.userRepo = userRepo;
        this.mailService = mailService;
        this.acValidationTokenService = acValidationTokenService;
    }

    @Override
    public User save(User user) {
        boolean isNewUser = user.getId() == null;
        // save user
        if (isNewUser && findByEmail(user.getEmail()) != null)
            throw new AlreadyExistsException("User already exists with email " + user.getEmail());
        user = this.userRepo.save(user);
        // for new user registration
        // send email for validation
        if (isNewUser)
            this.requireAccountValidationByEmail(user, "/user/validation");
        return user;
    }

    public void requireAccountValidationByEmail(User user, String validationUrl) {
        SessionIdentifierGenerator sessionIdentifierGenerator = new SessionIdentifierGenerator();
        AcValidationToken acValidationToken = new AcValidationToken();
        acValidationToken.setToken(sessionIdentifierGenerator.nextSessionId());
        acValidationToken.setTokenValid(true);
        acValidationToken.setUser(user);
        // save acvalidationtoken
        acValidationToken = this.acValidationTokenService.save(acValidationToken);
        // build confirmation link
        String confirmationLink = baseUrl.trim() + validationUrl + "?token=" + acValidationToken.getToken() + "&enabled=true";
        // send link by email
        this.mailService.sendEmail(user.getEmail(), "HotelsWave Registration", "Please confirm your email by clicking this link " + confirmationLink);
    }

    @Override
    @Transactional
    public void save(User user, AcValidationToken acToken) {
        this.userRepo.save(user);
        this.acValidationTokenService.save(acToken);
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

    @Override
    public List<User> findByRole(String role, int page) {
        return this.userRepo.findByRolesIn(role, new PageRequest(page, Constants.PAGE_SIZE, Sort.Direction.DESC, SortAttributes.FIELD_ID));
    }
}
