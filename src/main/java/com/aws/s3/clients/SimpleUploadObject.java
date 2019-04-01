package com.aws.s3.clients;


import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

//Refer to following AWS documentation for details 
// https://docs.aws.amazon.com/AmazonS3/latest/dev/UploadObjSingleOpJava.html

public class SimpleUploadObject {

	private String local_path = "";
	private String s3_path = "";
	private String s3_region = "";
	
	private String bucketName = "";
	
	private String keyName = "";
	

	
	public void captureUserInputs(String args[]) {
		local_path = args.length > 0 ? args[0] : null;

		s3_path = args.length > 1 ? args[1] : null;

		s3_region = args.length > 2 ? args[2] : null;

		if (local_path == null || s3_path == null || s3_region == null) {
			System.out.println(
					"Usage: com.aws.s3.clients.SimpleUploadObject file-to-be-uploaded-at-source.txt s3_bucket_name/uploaded-file-at-dest.txt us-east-1");
		}

		// only the first '/' character is of interest to get the bucket name.
		// Subsequent ones are part of the key name.
		String s3_path_split[] = s3_path.split("/", 2);

		bucketName = s3_path_split[0];

		keyName = s3_path_split.length > 1 ? s3_path_split[1] : null;

		if (s3_region == null  || "".equals(s3_region)) {
			s3_region = "us-east-1"; // US East (N. Virginia)
		}

		if (local_path == null || "".equals(local_path)) {
			local_path = "demo-upload-file.txt";
		}
	}

	public void uploadFile() {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(s3_region)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();
        
            // Upload a text string as a new object.
            s3Client.putObject(bucketName, keyName, "Uploaded String Object");
            
        	long t0 = System.currentTimeMillis();
            File inputFile = new File(local_path);
    		if( keyName == null || "".equals(keyName)) {
    			keyName = inputFile.getName();
    		}            
            // Upload a file as a new object with ContentType and title specified.
            PutObjectRequest request = new PutObjectRequest(bucketName, keyName, inputFile);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            request.setMetadata(metadata);
            s3Client.putObject(request);
            long t1 = System.currentTimeMillis();
            System.out.println("SimpleUploadObject (File): time taken = " + (t1 - t0));
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }		
	}
	
	public void writeStringToS3() {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(s3_region)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

        	long t0 = System.currentTimeMillis();

            // Upload a text string as a new object.
            s3Client.putObject(bucketName, keyName, "Uploaded String Object");
            long t1 = System.currentTimeMillis();
            System.out.println("SimpleUploadObject (String): time taken = " + (t1 - t0));
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }		
	}
	
	
    public static void main(String[] args) throws IOException {
    	SimpleUploadObject supObj = new SimpleUploadObject();
    	supObj.captureUserInputs(args);
    	supObj.uploadFile();
    }
}

