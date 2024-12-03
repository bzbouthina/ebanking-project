package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Localtransfer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Localtransfer entity.
 */
@Repository
public interface LocaltransferRepository extends JpaRepository<Localtransfer, Long> {
    default Optional<Localtransfer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Localtransfer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Localtransfer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select localtransfer from Localtransfer localtransfer left join fetch localtransfer.bankAccount",
        countQuery = "select count(localtransfer) from Localtransfer localtransfer"
    )
    Page<Localtransfer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select localtransfer from Localtransfer localtransfer left join fetch localtransfer.bankAccount")
    List<Localtransfer> findAllWithToOneRelationships();

    @Query("select localtransfer from Localtransfer localtransfer left join fetch localtransfer.bankAccount where localtransfer.id =:id")
    Optional<Localtransfer> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select lt from Localtransfer lt where lt.bankAccount.login = :login")
    Page<Localtransfer>findByBankAccountLogin(@Param("login") String login, Pageable pageable);


}
