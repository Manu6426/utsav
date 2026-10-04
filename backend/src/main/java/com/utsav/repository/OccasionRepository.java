package com.utsav.repository;

import com.utsav.model.Occasion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccasionRepository extends JpaRepository<Occasion, String> {
}
