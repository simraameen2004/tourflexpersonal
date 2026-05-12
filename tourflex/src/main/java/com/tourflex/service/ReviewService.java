package com.tourflex.service;

import com.tourflex.model.Review;
import com.tourflex.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getLatestReviews() {
        return reviewRepository.findAllByOrderByReviewDateDesc();
    }

    public List<Review> getOldestReviews() {
        return reviewRepository.findAllByOrderByReviewDateAsc();
    }

    public List<Review> getReviewsByPackage(String packageName, String sort) {
        // Only consider the first 4 characters for matching as per requirement
        String searchPrefix = packageName;
        if (packageName != null && packageName.length() > 4) {
            searchPrefix = packageName.substring(0, 4);
        }

        if ("oldest".equals(sort)) {
            return reviewRepository.findByPackageNameStartingWithIgnoreCaseOrderByReviewDateAsc(searchPrefix);
        }
        return reviewRepository.findByPackageNameStartingWithIgnoreCaseOrderByReviewDateDesc(searchPrefix);
    }

    public void deleteReview(int id) {
        reviewRepository.deleteById(id);
    }

    public Review getReviewById(int id) {
        return reviewRepository.findById(id).orElse(null);
    }
}