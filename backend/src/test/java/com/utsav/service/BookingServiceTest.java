package com.utsav.service;

import com.utsav.dto.CreateBookingRequest;
import com.utsav.exception.ResourceNotFoundException;
import com.utsav.model.Booking;
import com.utsav.model.BookingStatus;
import com.utsav.model.ServiceOffering;
import com.utsav.model.Vendor;
import com.utsav.repository.BookingRepository;
import com.utsav.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the booking service: pricing is resolved server-side,
 * the confirmation reference is generated, and missing vendors fail fast.
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private BookingService bookingService;

    private Vendor vendorWithService() {
        Vendor vendor = new Vendor();
        vendor.setId("meera");
        vendor.setCurrency("$");
        vendor.setServices(List.of(new ServiceOffering("Full mandap decor", 1200, "event")));
        return vendor;
    }

    private CreateBookingRequest request() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setVendorId("meera");
        request.setServiceName("Full mandap decor");
        request.setDate(LocalDate.now().plusDays(7));
        request.setTimeSlot("10:00 AM");
        request.setCustomerName("Test Customer");
        request.setPhone("555-0100");
        return request;
    }

    @Test
    void createPricesFromVendorServiceList() {
        when(vendorRepository.findById("meera")).thenReturn(Optional.of(vendorWithService()));
        when(bookingRepository.findByReference(any())).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking booking = bookingService.create(request());

        assertThat(booking.getPrice()).isEqualTo(1200);
        assertThat(booking.getCurrency()).isEqualTo("$");
        assertThat(booking.getReference()).matches("UTS-[A-Z2-9]{6}");
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void createRejectsUnknownService() {
        when(vendorRepository.findById("meera")).thenReturn(Optional.of(vendorWithService()));

        CreateBookingRequest bad = request();
        bad.setServiceName("Moon landing");

        assertThatThrownBy(() -> bookingService.create(bad))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Moon landing");
    }

    @Test
    void createRejectsUnknownVendor() {
        when(vendorRepository.findById("ghost")).thenReturn(Optional.empty());

        CreateBookingRequest bad = request();
        bad.setVendorId("ghost");

        assertThatThrownBy(() -> bookingService.create(bad))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ghost");
    }

    @Test
    void cancelMarksBookingCancelled() {
        Booking booking = new Booking();
        booking.setReference("UTS-ABC123");
        booking.setStatus(BookingStatus.CONFIRMED);
        when(bookingRepository.findByReference("UTS-ABC123")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking cancelled = bookingService.cancel("UTS-ABC123");

        assertThat(cancelled.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }
}
