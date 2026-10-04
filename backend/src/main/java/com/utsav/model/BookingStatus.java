package com.utsav.model;

/**
 * Lifecycle of a booking. Phase 1 keeps it simple: a booking is confirmed
 * when created and can be cancelled. Payment states arrive with the billing
 * phase (see docs/production-plan.md).
 */
public enum BookingStatus {
    CONFIRMED,
    CANCELLED
}
