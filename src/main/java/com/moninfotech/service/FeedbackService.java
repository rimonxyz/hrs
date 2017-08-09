package com.moninfotech.service;

import com.moninfotech.domain.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> findAll(int page, int size);
    Feedback getOne(Long id);
    Feedback save(Feedback feedback);
    void delete(Long id);
}
