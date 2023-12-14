package com.ecommerce.myapp.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3;

    public S3Service(S3Client s3) {
        this.s3 = s3;
    }

    public void putObject(String bucketName, String key, byte[] file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3.putObject(objectRequest, RequestBody.fromBytes(file));
    }

    /**
     * Get one ImageFile
     *
     * @param bucketName name of bucket on aws cloud
     * @param key        key of image
     * @return object request
     */
    public byte[] getObject(String bucketName, String key) {
        System.out.println(key);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> res = s3.getObject(getObjectRequest);
        try {
            return res.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get ImageFiles
     *
     * @param bucketName name of bucket on aws cloud
     * @param keys       keys of image
     * @return Map of each image with key
     */
    public List<String> getObjects(String bucketName, List<String> keys) {
        return keys.stream().map(key -> String.format("https://%s.s3.amazonaws.com/%s", bucketName, key)).toList();
    }

    public String getObjectUrl(String bucketName, String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }

    /**
     * Deletes an object from S3
     *
     * @param bucketName The name of the S3 bucket
     * @param key        The key of the object to delete
     */
    public void deleteObject(String bucketName, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3.deleteObject(deleteObjectRequest);
    }

    public void moveObjects(List<String> sourceKeys, String sourceBucket, String destinationBucket) {
        for (String sourceKey : sourceKeys) {
            // Get the object from the source bucket
            byte[] objectData = getObject(sourceBucket, sourceKey);

            // Put the object in the destination bucket
            putObject(destinationBucket, sourceKey, objectData);

            // Delete the original object from the source bucket
            deleteObject(sourceBucket, sourceKey);
        }
    }
}
