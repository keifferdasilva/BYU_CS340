package org.example;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-1")
                .build();

        String fileName = args[0];
        File file = new File(fileName);
        String bucketName = args[1];
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, file.getName(), file);
        s3.putObject(putObjectRequest);
    }
}