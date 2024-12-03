package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CardPayment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CardPayment entity.
 */
@Repository
public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {
    default Optional<CardPayment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CardPayment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CardPayment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cardPayment from CardPayment cardPayment left join fetch cardPayment.payment",
        countQuery = "select count(cardPayment) from CardPayment cardPayment"
    )
    Page<CardPayment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cardPayment from CardPayment cardPayment left join fetch cardPayment.payment")
    List<CardPayment> findAllWithToOneRelationships();

    @Query("select cardPayment from CardPayment cardPayment left join fetch cardPayment.payment where cardPayment.id =:id")
    Optional<CardPayment> findOneWithToOneRelationships(@Param("id") Long id);
}
