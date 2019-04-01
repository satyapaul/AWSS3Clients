package com.aws.s3.clients;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class JavaFileCopy {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		String sourceFileName = args.length > 0 ? args[0] : null;
		
		String destFileName = args.length > 1 ? args[1] : null;

		String io_vs_nio = args.length > 2 ? args[2] : null;
		
		if( sourceFileName == null || destFileName == null || io_vs_nio ==  null) {
			System.out.println("Usage (First parameter is mandatory) : com.aws.s3.clients.JavaFileCopy file-to-be-uploaded-at-source.txt uploaded-file-at-dest.txt nio");
		}
		

		File source = new File(sourceFileName);

		
		if( destFileName == null || "".equals(destFileName)) {
			destFileName = sourceFileName;
		}
		File dest = new File(destFileName);
		
		long t0 = System.currentTimeMillis();

		if( "nio".equalsIgnoreCase(io_vs_nio)) {
			copyFileUsingChannel(source,  dest);
		} else {
			copyFileUsingStream(source,  dest);
		}
		
		long t1 = System.currentTimeMillis();
		System.out.println(io_vs_nio + " : JavaFile Copy : time taken = " + (t1 - t0));

	}
	
	
	// file copy using IO file input/output streams
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}	
	
	// NIO channel based file copy
	private static void copyFileUsingChannel(File source, File dest) throws IOException {
	    FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	   }
	}

}
