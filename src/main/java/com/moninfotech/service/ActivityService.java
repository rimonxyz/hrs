package com.moninfotech.service;

import com.moninfotech.domain.Activity;
import com.moninfotech.domain.User;
import org.springframework.data.domain.Page;

public interface ActivityService {
    Activity save(Activity activity);
    Activity findFirst();
    Activity findLast(User user);
    Page<Activity> findByUser(User user, int page, int size);
    Activity findOne(long id);
}
