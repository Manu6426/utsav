package com.utsav.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A vendor category, e.g. "decorators", "makeup".
 * The id is the same slug the frontend uses (cat:"decorators"), so the
 * existing frontend can call this API without any mapping layer.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    private String id;

    private String name;

    /** Category card image, relative to the frontend (e.g. "images/cat-decorator.jpg"). */
    private String imageUrl;

    private String blurb;

    public Category() {
    }

    public Category(String id, String name, String imageUrl, String blurb) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.blurb = blurb;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }
}
