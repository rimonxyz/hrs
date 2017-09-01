package com.moninfotech.crontasks;

import com.moninfotech.commons.DateUtils;
import com.moninfotech.domain.Activity;
import com.moninfotech.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {

    private final ActivityService activityService;

    @Autowired
    public ScheduledTasks(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Scheduled(cron = "0 1 15 * * ?")
    void deleteActivitiesOlderThanAMonth() {
        Activity firstActivity = this.activityService.findFirst();
        List<Activity> activityList = this.activityService.findAll();
        for (Activity activity : activityList) {
            if (!activity.getId().equals(firstActivity.getId())) { // exclude first activity
                if (DateUtils.getDateDiff(activity.getCreated(), new Date(), TimeUnit.DAYS) >= 31) {
                    this.activityService.delete(activity.getId());
                    System.out.println("Deleted activity! " + activity.getId());
                }
            }
        }
        System.out.println("Deleted old user activities!");
    }

}
