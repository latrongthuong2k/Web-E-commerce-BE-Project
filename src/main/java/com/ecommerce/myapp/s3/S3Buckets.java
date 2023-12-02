package com.ecommerce.myapp.s3;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "aws.s3.buckets")
public class S3Buckets {

    private String product;
    private String termBucket;
    public void setProduct(String product) {
        this.product = product;
    }
    public void setTermBucket(String termBucket) {
        this.termBucket = termBucket;
    }
}