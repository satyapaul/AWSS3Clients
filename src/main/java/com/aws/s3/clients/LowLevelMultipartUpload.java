package com.aws.s3.clients;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;

// Refer to following AWS documentation for details
//https://docs.aws.amazon.com/AmazonS3/latest/dev/llJavaUploadFile.html

public class LowLevelMultipartUpload {

	private String local_path = "";
	private String s3_path = "";
	private String s3_region = "";
	
	private String bucketName = "";
	
	private String keyName = "";
	
	private int chunkSize = 5; 

	
	public void captureUserInputs(String args[]) {
		local_path = args.length > 0 ? args[0] : null;

		s3_path = args.length > 1 ? args[1] : null;

		s3_region = args.length > 2 ? args[2] : null;
		
		String _chunkSize = args.length > 3 ? args[3] : "5";
		
		try {
			chunkSize = Integer.parseInt(_chunkSize);
		} catch(NumberFormatException nfe) {
			System.out.println("Please provide 'chunkSize' in integer. " + nfe.getMessage());
		}
		

		if (local_path == null || s3_path == null || s3_region == null) {
			System.out.println(
					"Usage: com.aws.s3.clients.LowLevelMultipartUpload file-to-be-uploaded-at-source.txt s3_bucket_name/uploaded-file-at-dest.txt us-east-1 chunk_size (in MB)");
		}

		// only the first '/' character is of interest to get the bucket name.
		// Subsequent ones are part of the key name.
		String s3_path_split[] = s3_path.split("/", 2);

		bucketName = s3_path_split[0];
		

		keyName = s3_path_split.length > 1 ? s3_path_split[1] : null;

		
		if (s3_region == null || "".equals(s3_region.trim())) {
			s3_region = "us-east-1"; // US East (N. Virginia)
		}
		

		if (local_path == null || "".equals(local_path.trim())) {
			local_path = "demo-upload-file.txt";
		}
	}

	public static void main(String[] args) throws IOException {

		LowLevelMultipartUpload llmultipartUploadAgent = new LowLevelMultipartUpload();
		
		try {
			llmultipartUploadAgent.captureUserInputs(args);
		} catch(Exception e) {
			System.out.println("Please ensure you pass the correct parameters!!!!!!");
			System.out.println("Usage: com.aws.s3.clients.LowLevelMultipartUpload file-to-be-uploaded-at-source.txt s3_bucket_name/uploaded-file-at-dest.txt us-east-1");
			
		}
		
		llmultipartUploadAgent.uploadFile();

	}
	
	public void uploadFile() {
		File file = new File(local_path);
		
		if( keyName == null || "".equals(keyName)) {
			keyName = file.getName();
		}
		
		long contentLength = file.length();
		long partSize = chunkSize * 1024 * 1024; // Set part size to 5 MB.

		try {
			long t0 = System.currentTimeMillis();
			System.out.println("5 s3_region = "+ s3_region);
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(s3_region)
					.withCredentials(new ProfileCredentialsProvider()).build();

			// Create a list of ETag objects. You retrieve ETags for each object part
			// uploaded,
			// then, after each individual part has been uploaded, pass the list of ETags to
			// the request to complete the upload.
			List<PartETag> partETags = new ArrayList<PartETag>();

			// Initiate the multipart upload.
			InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
			InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

			// Upload the file parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
				// Because the last part could be less than 5 MB, adjust the part size as
				// needed.
				partSize = Math.min(partSize, (contentLength - filePosition));

				// Create the request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucketName).withKey(keyName)
						.withUploadId(initResponse.getUploadId()).withPartNumber(i).withFileOffset(filePosition)
						.withFile(file).withPartSize(partSize);

				// Upload the part and add the response's ETag to our list.
				UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
				partETags.add(uploadResult.getPartETag());

				filePosition += partSize;
			}

			// Complete the multipart upload.
			CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName,
					initResponse.getUploadId(), partETags);
			s3Client.completeMultipartUpload(compRequest);
			long t1 = System.currentTimeMillis();
			System.out.println("LowLevelMultipartUpload: time taken = " + (t1 - t0));
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}

	}
	
}
