package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.InternatonalTransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InternatonalTransfer entity.
 */
@Repository
public interface InternatonalTransferRepository extends JpaRepository<InternatonalTransfer, Long> {
    default Optional<InternatonalTransfer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InternatonalTransfer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InternatonalTransfer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select internatonalTransfer from InternatonalTransfer internatonalTransfer left join fetch internatonalTransfer.bankAccount",
        countQuery = "select count(internatonalTransfer) from InternatonalTransfer internatonalTransfer"
    )
    Page<InternatonalTransfer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select internatonalTransfer from InternatonalTransfer internatonalTransfer left join fetch internatonalTransfer.bankAccount")
    List<InternatonalTransfer> findAllWithToOneRelationships();

    @Query(
        "select internatonalTransfer from InternatonalTransfer internatonalTransfer left join fetch internatonalTransfer.bankAccount where internatonalTransfer.id =:id"
    )
    Optional<InternatonalTransfer> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select c from InternatonalTransfer c where c.bankAccount.login = ?1")
    Page<InternatonalTransfer>findByBankAccountLogin(String login, Pageable pageable);



}
