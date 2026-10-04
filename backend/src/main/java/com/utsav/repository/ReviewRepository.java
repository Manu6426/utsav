package com.utsav.repository;

import com.utsav.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByVendorIdOrderByCreatedAtDesc(String vendorId);
}
