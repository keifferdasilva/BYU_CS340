package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class S3DAO implements ImageDAO{
    @Override
    public String uploadImage(String image, String alias) {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-west-1").build();
        String bucketName = "keifferdasilvacs340bucket";
        byte[] byteArray = Base64.getDecoder().decode(image);
        ObjectMetadata data = new ObjectMetadata();
        data.setContentLength(byteArray.length);
        data.setContentType("image/jpeg");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, alias, new ByteArrayInputStream(byteArray), data)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(putObjectRequest);
        return s3.getUrl(bucketName, alias).toString();
    }
}
