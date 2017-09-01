package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.Activity;
import com.moninfotech.domain.User;
import com.moninfotech.repository.ActivityRepository;
import com.moninfotech.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepo;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepo) {
        this.activityRepo = activityRepo;
    }

    public Activity save(Activity activity) {
        if (activity.getId() == null) { // new activity (user logged in)
            Activity firstActivity = this.findFirst();
            if (firstActivity != null) {
                long total = firstActivity.getTotalVisitors();
                activity.setTotalVisitors(++total);
                firstActivity.setTotalVisitors(total);
                this.activityRepo.save(firstActivity);
            }
        }
        return this.activityRepo.save(activity);
    }

    @Override
    public Activity findFirst() {
        return this.activityRepo.findFirstBy();
    }

    @Override
    public Activity findLast(User user) {
        return activityRepo.findFirstByUserOrderByIdDesc(user);
    }

    @Override
    public Page<Activity> findByUser(User user, int page, int size) {
        return this.activityRepo.findByUser(user, new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID));
    }

    @Override
    public Activity findOne(long id) {
        return this.activityRepo.findOne(id);
    }

}
