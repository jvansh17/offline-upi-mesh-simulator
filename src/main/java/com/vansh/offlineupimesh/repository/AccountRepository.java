package com.vansh.offlineupimesh.repository;

import com.vansh.offlineupimesh.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}