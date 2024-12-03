package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BillPayment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillPayment entity.
 */
@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
    default Optional<BillPayment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BillPayment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BillPayment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select billPayment from BillPayment billPayment left join fetch billPayment.payment",
        countQuery = "select count(billPayment) from BillPayment billPayment"
    )
    Page<BillPayment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select billPayment from BillPayment billPayment left join fetch billPayment.payment")
    List<BillPayment> findAllWithToOneRelationships();

    @Query("select billPayment from BillPayment billPayment left join fetch billPayment.payment where billPayment.id =:id")
    Optional<BillPayment> findOneWithToOneRelationships(@Param("id") Long id);
}
