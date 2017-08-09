package com.moninfotech.service.impl;

import com.moninfotech.commons.SortAttributes;
import com.moninfotech.domain.Feedback;
import com.moninfotech.repository.FeedbackRepository;
import com.moninfotech.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepo;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public List<Feedback> findAll(int page, int size) {
        return this.feedbackRepo.findAll(new PageRequest(page, size, Sort.Direction.DESC, SortAttributes.FIELD_ID)).getContent();
    }

    @Override
    public Feedback getOne(Long id) {
        return this.feedbackRepo.findOne(id);
    }

    @Override
    public Feedback save(Feedback feedback) {
        return this.feedbackRepo.save(feedback);
    }

    @Override
    public void delete(Long id) {
        this.feedbackRepo.delete(id);
    }
}
