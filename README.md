# AWSS3Clients
AWS S3 clients covering different options/APIs to upload a file to S3 bucket. 

Execute:
=======
mvn clean package

This will create target/AWSS3Clients-0.0.1-SNAPSHOT.jar

Option#1:
========
Usage: com.aws.s3.clients.SimpleUploadObject file-to-be-uploaded-at-source.txt s3_bucket_name/uploaded-file-at-dest.txt us-east-1

Example:
========

java -classpath target/AWSS3Clients-0.0.1-SNAPSHOT.jar com.aws.s3.clients.SimpleUploadObject resources/demo-upload-file.txt  b2bi-s3-demo/demo-upload-file_1.txt us-east-1

SimpleUploadObject (File): time taken = 663

Option#2:
========

Usage: com.aws.s3.clients.LowLevelMultipartUpload file-to-be-uploaded-at-source.txt s3_bucket_name/uploaded-file-at-dest.txt us-east-1 chunk_size (in MB)

Example:
========

java -classpath target/AWSS3Clients-0.0.1-SNAPSHOT.jar com.aws.s3.clients.LowLevelMultipartUpload resources/demo-upload-file.txt  b2bi-s3-demo/demo-upload-file_1.txt us-east-1

5 s3_region = us-east-1
LowLevelMultipartUpload: time taken = 4794

Option#3:
========

Usage:
    java -classpath target/AWSS3Clients-0.0.1-SNAPSHOT.jar com.aws.s3.clients.XferMgrUpload [--recursive] [--pause] <s3_path> <local_paths>

Where:
    --recursive - Only applied if local_path is a directory.
                  Copies the contents of the directory recursively.

    --pause     - Attempt to pause+resume the upload. This may not work for
                  small files.

    s3_path     - The S3 destination (bucket/path) to upload the file(s) to.

    local_paths - One or more local paths to upload to S3. These can be files
                  or directories. Globs are permitted (*.xml, etc.)

Examples:
    XferMgrUpload public_photos/cat_happy.png my_photos/funny_cat.png
    XferMgrUpload public_photos my_photos/cat_sad.png
    XferMgrUpload public_photos my_photos/cat*.png
    XferMgrUpload public_photos my_photos
    
Example:
========
java -classpath target/AWSS3Clients-0.0.1-SNAPSHOT.jar com.aws.s3.clients.XferMgrUpload b2bi-s3-demo resources/demo-upload-file.txt 
No dir to copy
file: resources/demo-upload-file.txt
Uploading to b2bi-s3-demo/resources/demo-upload-file.txt
  [########################################]: Completed
XferMgrUpload: time taken to copy single file 2803




