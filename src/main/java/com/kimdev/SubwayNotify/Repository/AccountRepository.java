package com.kimdev.SubwayNotify.Repository;


import com.kimdev.SubwayNotify.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);
}
