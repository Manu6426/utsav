package com.utsav.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * An occasion collection, e.g. "wedding", "sangeet", "halloween".
 * Ids match the frontend's OCCASIONS slugs so both sides stay in sync.
 */
@Entity
@Table(name = "occasions")
public class Occasion {

    @Id
    private String id;

    private String name;

    private String imageUrl;

    private String description;

    public Occasion() {
    }

    public Occasion(String id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
