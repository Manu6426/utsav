package com.utsav.model;

import jakarta.persistence.Embeddable;

/**
 * One portfolio photo on a vendor profile.
 * budgetTag is Utsav's differentiator — "This mandap: $1,800 · 120 guests" —
 * and stays nullable until vendors tag their work (Phase 2).
 */
@Embeddable
public class PortfolioItem {

    private String imageUrl;

    /** e.g. "This mandap: $1,800 · 120 guests". Null when the vendor hasn't tagged it yet. */
    private String budgetTag;

    public PortfolioItem() {
    }

    public PortfolioItem(String imageUrl) {
        this(imageUrl, null);
    }

    public PortfolioItem(String imageUrl, String budgetTag) {
        this.imageUrl = imageUrl;
        this.budgetTag = budgetTag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBudgetTag() {
        return budgetTag;
    }

    public void setBudgetTag(String budgetTag) {
        this.budgetTag = budgetTag;
    }
}
