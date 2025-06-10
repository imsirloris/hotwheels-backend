package com.loris.hw.infra.config;

import org.springframework.beans.factory.annotation.Value;

public class FirestoreConfig {

    @Value("${spring.cloud.gcp.firestore.project-id}")
    private String projectId;



}
