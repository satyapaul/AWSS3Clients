# AWSS3Clients
AWS S3 clients covering different options/APIs to upload a file to S3 bucket. 

execute 
mvn clean package

This will create target/AWSS3Clients-0.0.1-SNAPSHOT.jar

Option#1:
java -classpath target/AWSS3Clients-0.0.1-SNAPSHOT.jar com.aws.s3.clients.SimpleUploadObject resources/demo-upload-file.txt  b2bi-s3-demo/demo-upload-file_1.txt us-east-1
SimpleUploadObject (File): time taken = 663

Option#2:
java -classpath target/AWSS3Clients-0.0.1-SNAPSHOT.jar com.aws.s3.clients.LowLevelMultipartUpload

Usage: com.aws.s3.clients.LowLevelMultipartUpload file-to-be-uploaded-at-source.txt s3_bucket_name/uploaded-file-at-dest.txt us-east-1 chunk_size (in MB)
Please ensure you pass the correct parameters!!!!!!
Usage: com.aws.s3.clients.LowLevelMultipartUpload file-to-be-uploaded-at-source.txt s3_bucket_name/uploaded-file-at-dest.txt us-east-1

Option#3:
java -classpath target/AWSS3Clients-0.0.1-SNAPSHOT.jar com.aws.s3.clients.XferMgrUpload



