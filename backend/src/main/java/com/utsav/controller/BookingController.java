package com.utsav.controller;

import com.utsav.dto.CreateBookingRequest;
import com.utsav.model.Booking;
import com.utsav.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * Slot bookings. Bookings are looked up by their customer-facing
 * reference (e.g. "UTS-8K2QXA"), not the internal numeric id.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking create(@Valid @RequestBody CreateBookingRequest request) {
        return bookingService.create(request);
    }

    @GetMapping
    public List<Booking> list(@RequestParam(required = false) String vendorId) {
        return bookingService.list(vendorId);
    }

    @GetMapping("/{reference}")
    public Booking get(@PathVariable String reference) {
        return bookingService.getByReference(reference);
    }

    @DeleteMapping("/{reference}")
    public Booking cancel(@PathVariable String reference) {
        return bookingService.cancel(reference);
    }
}
