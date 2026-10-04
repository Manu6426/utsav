package com.utsav.service;

import com.utsav.dto.CreateVendorRequest;
import com.utsav.exception.ResourceNotFoundException;
import com.utsav.model.Category;
import com.utsav.model.Vendor;
import com.utsav.repository.CategoryRepository;
import com.utsav.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the vendor catalog service.
 * Repositories are mocked — these tests verify service logic only,
 * not the database.
 */
@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private VendorService vendorService;

    @Test
    void searchPassesFiltersThroughToRepository() {
        Vendor vendor = new Vendor();
        vendor.setId("meera");
        when(vendorRepository.search("decorators", "Houston", 1500, null, true))
                .thenReturn(List.of(vendor));

        List<Vendor> result = vendorService.search("decorators", "Houston", 1500, null, true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("meera");
        verify(vendorRepository).search("decorators", "Houston", 1500, null, true);
    }

    @Test
    void getByIdThrowsWhenVendorMissing() {
        when(vendorRepository.findById("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendorService.getById("ghost"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ghost");
    }

    @Test
    void onboardCreatesUnverifiedNewcomer() {
        Category category = new Category("decorators", "Decorators", "images/cat-decorator.jpg", "blurb");
        when(vendorRepository.existsById("newbie")).thenReturn(false);
        when(categoryRepository.findById("decorators")).thenReturn(Optional.of(category));
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(i -> i.getArgument(0));

        CreateVendorRequest request = new CreateVendorRequest();
        request.setId("newbie");
        request.setName("Newbie Decor");
        request.setCategoryId("decorators");
        request.setCity("Dallas");
        request.setCountry("USA");
        request.setCurrency("$");
        request.setStartingPrice(200);

        Vendor saved = vendorService.onboard(request);

        assertThat(saved.isVerified()).isFalse();
        assertThat(saved.isNewcomer()).isTrue();
        assertThat(saved.getRating()).isZero();
        assertThat(saved.getReviewCount()).isZero();
    }

    @Test
    void onboardRejectsUnknownCategory() {
        when(vendorRepository.existsById("newbie")).thenReturn(false);
        when(categoryRepository.findById("nope")).thenReturn(Optional.empty());

        CreateVendorRequest request = new CreateVendorRequest();
        request.setId("newbie");
        request.setName("Newbie Decor");
        request.setCategoryId("nope");
        request.setCity("Dallas");
        request.setCountry("USA");
        request.setCurrency("$");

        assertThatThrownBy(() -> vendorService.onboard(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("nope");
    }
}
