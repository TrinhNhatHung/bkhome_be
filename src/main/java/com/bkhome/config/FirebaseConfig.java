package com.bkhome.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean(name = "firebaseApp")
    public FirebaseApp getFirebaseApp() throws IOException {
        InputStream serviceAccount = this.getClass().getClassLoader().getResourceAsStream("./bkhome-11321-firebase-adminsdk-brc2r-d53f3bb4d5.json");
        assert serviceAccount != null;
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://bkhome-11321.firebaseapp.com/")
                .setStorageBucket("bkhome-11321.appspot.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean(name = "bucket")
    public Bucket getStorage() throws IOException {
        return StorageClient.getInstance(getFirebaseApp()).bucket();
    }
}
