package com.utsav.service;

import com.utsav.dto.CreateReviewRequest;
import com.utsav.exception.ResourceNotFoundException;
import com.utsav.model.Review;
import com.utsav.model.Vendor;
import com.utsav.repository.ReviewRepository;
import com.utsav.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Reviews. After saving, the vendor's aggregate rating and review count
 * are recomputed so the catalog always shows fresh numbers.
 */
@Service
public class ReviewService {

    private static final DateTimeFormatter DATE_LABEL = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);

    private final ReviewRepository reviewRepository;
    private final VendorRepository vendorRepository;

    public ReviewService(ReviewRepository reviewRepository, VendorRepository vendorRepository) {
        this.reviewRepository = reviewRepository;
        this.vendorRepository = vendorRepository;
    }

    @Transactional(readOnly = true)
    public List<Review> forVendor(String vendorId) {
        vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorId));
        return reviewRepository.findByVendorIdOrderByCreatedAtDesc(vendorId);
    }

    @Transactional
    public Review add(String vendorId, CreateReviewRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorId));

        Review review = new Review();
        review.setVendor(vendor);
        review.setAuthor(request.getAuthor());
        review.setRating(request.getRating());
        review.setText(request.getText());
        review.setDateLabel(LocalDate.now().format(DATE_LABEL));
        Review saved = reviewRepository.save(review);

        recomputeAggregate(vendor);
        return saved;
    }

    private void recomputeAggregate(Vendor vendor) {
        List<Review> reviews = reviewRepository.findByVendorIdOrderByCreatedAtDesc(vendor.getId());
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        vendor.setRating(Math.round(avg * 10.0) / 10.0);
        vendor.setReviewCount(reviews.size());
        vendorRepository.save(vendor);
    }
}
