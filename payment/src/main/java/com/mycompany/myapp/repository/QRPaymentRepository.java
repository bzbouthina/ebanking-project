package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.QRPayment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QRPayment entity.
 */
@Repository
public interface QRPaymentRepository extends JpaRepository<QRPayment, Long> {
    default Optional<QRPayment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<QRPayment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<QRPayment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select qRPayment from QRPayment qRPayment left join fetch qRPayment.payment",
        countQuery = "select count(qRPayment) from QRPayment qRPayment"
    )
    Page<QRPayment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select qRPayment from QRPayment qRPayment left join fetch qRPayment.payment")
    List<QRPayment> findAllWithToOneRelationships();

    @Query("select qRPayment from QRPayment qRPayment left join fetch qRPayment.payment where qRPayment.id =:id")
    Optional<QRPayment> findOneWithToOneRelationships(@Param("id") Long id);
}
