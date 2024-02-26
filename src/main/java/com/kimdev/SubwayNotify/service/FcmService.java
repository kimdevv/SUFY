package com.kimdev.SubwayNotify.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.kimdev.SubwayNotify.DTO.FcmTokenDTO;
import com.kimdev.SubwayNotify.DTO.SendPushDTO;
import com.kimdev.SubwayNotify.model.Account;
import com.kimdev.SubwayNotify.model.Fcmtoken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kimdev.SubwayNotify.Repository.AccountRepository;
import com.kimdev.SubwayNotify.Repository.FcmtokenRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FcmService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FcmtokenRepository fcmtokenRepository;

    @PostConstruct
    public void fcmInitialize() throws IOException {
        ClassPathResource key = new ClassPathResource("static/firebase-key.json");
        try (InputStream serviceAccount = key.getInputStream()) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://sufy-4fdf2-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }

    @Transactional
    public void saveFcmtoken(FcmTokenDTO fcmTokenDto) {
        int accountId = fcmTokenDto.getAccountId();
        String fcmToken = fcmTokenDto.getFcmToken();

        Account user = accountRepository.findById(accountId).get();

        Fcmtoken fcmtoken = fcmtokenRepository.findByUser(user);
        if (fcmtoken == null) {
            fcmtoken = new Fcmtoken();
            fcmtoken.setUser(user);
            fcmtoken.setToken(fcmToken);
            fcmtokenRepository.save(fcmtoken);
        } else {
            fcmtoken.setToken(fcmToken);
        }
    }

    @Transactional
    public void sendPush(SendPushDTO sendPushDto) throws FirebaseMessagingException {
        Account user = sendPushDto.getUser();
        if (user == null) { // 비정상적인 유저
            return;
        }

        String token = fcmtokenRepository.findByUser(user).getToken();
        String title = sendPushDto.getTitle();
        String content = sendPushDto.getContent();

        // 메시지 객체 생성
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(content)
                .setImage("/image/SUFY2.png")
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(token)
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
