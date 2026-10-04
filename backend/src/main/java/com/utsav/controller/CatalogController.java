package com.utsav.controller;

import com.utsav.model.Category;
import com.utsav.model.Occasion;
import com.utsav.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only catalog metadata: the 8 vendor categories and 14 occasions.
 */
@RestController
@RequestMapping("/api")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    public List<Category> categories() {
        return catalogService.categories();
    }

    @GetMapping("/occasions")
    public List<Occasion> occasions() {
        return catalogService.occasions();
    }
}
