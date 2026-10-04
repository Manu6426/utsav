package com.utsav.repository;

import com.utsav.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Vendor search. All filters are optional — a null parameter means
 * "don't filter on this". This keeps one query instead of a combinatorial
 * explosion of derived finder methods.
 */
public interface VendorRepository extends JpaRepository<Vendor, String> {

    @Query("""
            SELECT v FROM Vendor v
            WHERE (:categoryId IS NULL OR v.category.id = :categoryId)
              AND (:city IS NULL OR LOWER(v.city) = :city)
              AND (:maxBudget IS NULL OR v.startingPrice <= :maxBudget)
              AND (:newcomer IS NULL OR v.newcomer = :newcomer)
              AND (:verified IS NULL OR v.verified = :verified)
            ORDER BY v.rating DESC, v.reviewCount DESC
            """)
    List<Vendor> search(@Param("categoryId") String categoryId,
                        @Param("city") String city,
                        @Param("maxBudget") Integer maxBudget,
                        @Param("newcomer") Boolean newcomer,
                        @Param("verified") Boolean verified);

    List<Vendor> findByCategoryId(String categoryId);
}
