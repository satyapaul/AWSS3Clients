package com.aws.s3.clients;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


public class S3ProxyFileUpload {

	
	public static String MY_TEST_BUCKET = "b2bi-s3-demo";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    AmazonS3 s3Client = AmazonS3ClientBuilder
	            .standard()
	            .withCredentials(
	              new AWSStaticCredentialsProvider(
	                new BasicAWSCredentials("AKIAIY4UJO57573QUWTA", 
	                		"JF/HqT2PpZR9l6ctOSIBp/kUzG8uxwjKtxWqoFhv")))
	            .withEndpointConfiguration(
	              new EndpointConfiguration("http://127.0.0.1:8080", 
	                                        Regions.US_EAST_1.getName()))
	            .build();
	    //s3Client.createBucket("test_bucket");
	    
	    //exercise your code that you want to test, using the above client
	    //myS3Uploader.uploadFile(s3Client, someFile);	
	    
        String testInput = "content";
        s3Client.putObject(MY_TEST_BUCKET, "s3proxy-file.txt", testInput);

	}

}
