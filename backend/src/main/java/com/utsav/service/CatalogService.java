package com.utsav.service;

import com.utsav.model.Category;
import com.utsav.model.Occasion;
import com.utsav.repository.CategoryRepository;
import com.utsav.repository.OccasionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Read-only catalog data: categories and occasions.
 */
@Service
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final OccasionRepository occasionRepository;

    public CatalogService(CategoryRepository categoryRepository, OccasionRepository occasionRepository) {
        this.categoryRepository = categoryRepository;
        this.occasionRepository = occasionRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Occasion> occasions() {
        return occasionRepository.findAll();
    }
}
