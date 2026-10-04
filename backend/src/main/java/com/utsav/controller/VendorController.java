package com.utsav.controller;

import com.utsav.dto.CreateVendorRequest;
import com.utsav.model.Vendor;
import com.utsav.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Vendor catalog endpoints. Every filter is optional, so
 * GET /api/vendors with no params returns the whole catalog.
 */
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public List<Vendor> search(@RequestParam(required = false) String category,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) Integer maxBudget,
                               @RequestParam(required = false) Boolean newcomer,
                               @RequestParam(required = false) Boolean verified) {
        return vendorService.search(category, city, maxBudget, newcomer, verified);
    }

    @GetMapping("/{id}")
    public Vendor get(@PathVariable String id) {
        return vendorService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vendor onboard(@Valid @RequestBody CreateVendorRequest request) {
        return vendorService.onboard(request);
    }
}
