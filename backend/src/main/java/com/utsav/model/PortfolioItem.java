package com.utsav.model;

import jakarta.persistence.Embeddable;

/**
 * One portfolio photo on a vendor profile.
 * title/caption describe the work ("Mandap canopy detail"); budgetTag is
 * Utsav's differentiator — "This mandap: $1,800 · 120 guests" — and stays
 * nullable until vendors tag their work (Phase 2).
 */
@Embeddable
public class PortfolioItem {

    private String imageUrl;

    /** Short title, e.g. "Grand floral mandap". */
    private String title;

    /** One-line description of the work shown. */
    private String caption;

    /** e.g. "This mandap: $1,800 · 120 guests". Null when the vendor hasn't tagged it yet. */
    private String budgetTag;

    public PortfolioItem() {
    }

    public PortfolioItem(String imageUrl) {
        this(imageUrl, null, null, null);
    }

    public PortfolioItem(String imageUrl, String title, String caption) {
        this(imageUrl, title, caption, null);
    }

    public PortfolioItem(String imageUrl, String title, String caption, String budgetTag) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.caption = caption;
        this.budgetTag = budgetTag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getBudgetTag() {
        return budgetTag;
    }

    public void setBudgetTag(String budgetTag) {
        this.budgetTag = budgetTag;
    }
}
