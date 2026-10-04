package com.utsav.service;

import com.utsav.dto.CreateVendorRequest;
import com.utsav.exception.ResourceNotFoundException;
import com.utsav.model.Category;
import com.utsav.model.Vendor;
import com.utsav.repository.CategoryRepository;
import com.utsav.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Vendor catalog: search, lookup, onboarding.
 */
@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public VendorService(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Vendor> search(String categoryId, String city, Integer maxBudget,
                               Boolean newcomer, Boolean verified) {
        return vendorRepository.search(categoryId, city, maxBudget, newcomer, verified);
    }

    @Transactional(readOnly = true)
    public Vendor getById(String id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + id));
    }

    /**
     * Onboards a vendor. New vendors start unverified with no reviews —
     * verification is a manual ops step (Phase 2), not an API concern yet.
     */
    @Transactional
    public Vendor onboard(CreateVendorRequest request) {
        if (vendorRepository.existsById(request.getId())) {
            throw new IllegalArgumentException("Vendor id already taken: " + request.getId());
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));

        Vendor vendor = new Vendor();
        vendor.setId(request.getId());
        vendor.setName(request.getName());
        vendor.setCategory(category);
        vendor.setCity(request.getCity());
        vendor.setCountry(request.getCountry());
        vendor.setCurrency(request.getCurrency());
        vendor.setStartingPrice(request.getStartingPrice());
        vendor.setTagline(request.getTagline());
        vendor.setBio(request.getBio());
        vendor.setInstagram(request.getInstagram());
        vendor.setVerified(false);
        vendor.setNewcomer(true);
        vendor.setRating(0.0);
        vendor.setReviewCount(0);
        return vendorRepository.save(vendor);
    }
}
