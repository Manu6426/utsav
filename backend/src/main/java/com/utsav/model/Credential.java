package com.utsav.model;

import jakarta.persistence.Embeddable;

/**
 * One vendor credential or piece of kit, e.g. {label: "Certification",
 * detail: "Lakmé Makeup Artistry Course, 2023"} or {label: "Equipment",
 * detail: "Sony A7IV + 35mm f/1.4 GM"}.
 *
 * Optional and self-reported — never a verification claim.
 */
@Embeddable
public class Credential {

    /** e.g. "Certification", "Course", "Equipment", "Products used". */
    private String label;

    /** Free-text detail, e.g. "Sony A7IV + 35mm f/1.4 GM". */
    private String detail;

    public Credential() {
    }

    public Credential(String label, String detail) {
        this.label = label;
        this.detail = detail;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
