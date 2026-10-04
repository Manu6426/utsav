package com.utsav.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * A vendor on the marketplace, e.g. "Meera's Marigold Events".
 * The id is the frontend's vendor slug ("meera"), so the existing
 * frontend can switch to this API without remapping identifiers.
 */
@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    /** Currency symbol as shown in the frontend: "$", "₹", "AED". */
    @Column(nullable = false, length = 8)
    private String currency;

    private double rating;

    private int reviewCount;

    private boolean verified;

    private boolean newcomer;

    /** "Starting at" price shown on cards; the budget filter compares against this. */
    @Column(nullable = false)
    private int startingPrice;

    @Column(length = 500)
    private String tagline;

    @Column(length = 2000)
    private String bio;

    @ElementCollection
    @CollectionTable(name = "vendor_languages", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "language")
    private List<String> languages = new ArrayList<>();

    /** Occasion slugs this vendor serves ("wedding", "sangeet", ...). */
    @ElementCollection
    @CollectionTable(name = "vendor_occasions", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "occasion_id")
    private List<String> occasionIds = new ArrayList<>();

    /** Vendor-to-vendor collaboration graph: ids of vendors this vendor recommends. */
    @ElementCollection
    @CollectionTable(name = "vendor_collaborators", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "collaborator_id")
    private List<String> collaboratorIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "vendor_services", joinColumns = @JoinColumn(name = "vendor_id"))
    @OrderColumn(name = "service_order")
    private List<ServiceOffering> services = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "vendor_portfolio", joinColumns = @JoinColumn(name = "vendor_id"))
    @OrderColumn(name = "photo_order")
    private List<PortfolioItem> portfolio = new ArrayList<>();

    /** Self-reported credentials / kit: certifications, courses, equipment, products. Optional. */
    @ElementCollection
    @CollectionTable(name = "vendor_credentials", joinColumns = @JoinColumn(name = "vendor_id"))
    @OrderColumn(name = "credential_order")
    private List<Credential> credentials = new ArrayList<>();

    public Vendor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isNewcomer() {
        return newcomer;
    }

    public void setNewcomer(boolean newcomer) {
        this.newcomer = newcomer;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getOccasionIds() {
        return occasionIds;
    }

    public void setOccasionIds(List<String> occasionIds) {
        this.occasionIds = occasionIds;
    }

    public List<String> getCollaboratorIds() {
        return collaboratorIds;
    }

    public void setCollaboratorIds(List<String> collaboratorIds) {
        this.collaboratorIds = collaboratorIds;
    }

    public List<ServiceOffering> getServices() {
        return services;
    }

    public void setServices(List<ServiceOffering> services) {
        this.services = services;
    }

    public List<PortfolioItem> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<PortfolioItem> portfolio) {
        this.portfolio = portfolio;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }
}
