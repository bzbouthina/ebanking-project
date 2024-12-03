package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("select c from BankAccount c where c.login = ?1")
    Page<BankAccount>findByLogin(String login, Pageable pageable);


}
