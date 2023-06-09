package com.dataportal.dataportal.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException, URISyntaxException {
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/dataportal-fb2e8-firebase-adminsdk-vz4wc-05a230809d.json");
        URL resource = getClass().getClassLoader().getResource("config/dataportal-fb2e8-firebase-adminsdk-vz4wc-05a230809d.json");
        File file = new File(resource.toURI());
        FileInputStream serviceAccount =
                new FileInputStream(file);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://dataportal-fb2e8-default-rtdb.europe-west1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);
        return FirebaseAuth.getInstance();
    }
}
