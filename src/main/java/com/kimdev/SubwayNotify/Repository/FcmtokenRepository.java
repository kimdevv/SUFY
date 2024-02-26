package com.kimdev.SubwayNotify.Repository;

import com.kimdev.SubwayNotify.model.Account;
import com.kimdev.SubwayNotify.model.Fcmtoken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmtokenRepository extends JpaRepository<Fcmtoken, Integer> {
    Fcmtoken findByUser(Account user);
}
