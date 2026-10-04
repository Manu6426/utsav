package com.utsav.service;

import com.utsav.dto.CreateBookingRequest;
import com.utsav.exception.ResourceNotFoundException;
import com.utsav.model.Booking;
import com.utsav.model.BookingStatus;
import com.utsav.model.ServiceOffering;
import com.utsav.model.Vendor;
import com.utsav.repository.BookingRepository;
import com.utsav.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

/**
 * Slot bookings. The price is always resolved from the vendor's own service
 * list — the request carries the service name, never a price.
 */
@Service
public class BookingService {

    private static final String REF_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();

    private final BookingRepository bookingRepository;
    private final VendorRepository vendorRepository;

    public BookingService(BookingRepository bookingRepository, VendorRepository vendorRepository) {
        this.bookingRepository = bookingRepository;
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public Booking create(CreateBookingRequest request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + request.getVendorId()));

        ServiceOffering service = vendor.getServices().stream()
                .filter(s -> s.getName().equalsIgnoreCase(request.getServiceName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vendor does not offer service: " + request.getServiceName()));

        Booking booking = new Booking();
        booking.setReference(newReference());
        booking.setVendor(vendor);
        booking.setServiceName(service.getName());
        booking.setPrice(service.getPrice());
        booking.setCurrency(vendor.getCurrency());
        booking.setDate(request.getDate());
        booking.setTimeSlot(request.getTimeSlot());
        booking.setCustomerName(request.getCustomerName());
        booking.setPhone(request.getPhone());
        booking.setCity(request.getCity());
        booking.setNotes(request.getNotes());
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<Booking> list(String vendorId) {
        if (vendorId != null) {
            return bookingRepository.findByVendorIdOrderByCreatedAtDesc(vendorId);
        }
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Booking getByReference(String reference) {
        return bookingRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + reference));
    }

    @Transactional
    public Booking cancel(String reference) {
        Booking booking = getByReference(reference);
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    /**
     * Customer-facing confirmation code, e.g. "UTS-8K2QXA".
     * Retried on the (astronomically unlikely) collision.
     */
    private String newReference() {
        String ref;
        do {
            StringBuilder sb = new StringBuilder("UTS-");
            for (int i = 0; i < 6; i++) {
                sb.append(REF_ALPHABET.charAt(random.nextInt(REF_ALPHABET.length())));
            }
            ref = sb.toString();
        } while (bookingRepository.findByReference(ref).isPresent());
        return ref;
    }
}
