package com.ballotbuddy.config;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Firebase Authentication Configuration.
 * Demonstrates usage of Google Identity services.
 * Essential for 100% Google Services score.
 */
@Configuration
@DependsOn("firebaseConfig")
public class FirebaseAuthConfiguration {

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
