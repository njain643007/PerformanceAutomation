/**
 * 
 */
package common.util;

import java.io.File;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;


/**
 * @author rajesh
 *
 */

public class AWSHelper {

	private static PropertiesReader prop = new PropertiesReader(System.getProperty("user.dir")+"/config.properties");
	private static AmazonS3 s3client;
	private String keyFullPath;

	private static AWSHelper instance;

	public static AWSHelper getInstance() {
		if (instance == null) {
			instance = new AWSHelper();
		}

		return instance;
	}

	public String getKeyFullPath() {
		return keyFullPath;
	}

	public void setKeyFullPath(String keyFullPath) {
		this.keyFullPath = keyFullPath;
	}

	private AWSHelper() {
		AWSCredentials credentials = new BasicAWSCredentials(prop.getPropValue("AWSaccesskey"),
				prop.getPropValue("AWSsecretkey"));
		s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.AP_SOUTH_1).build();
	}

	public String getS3Path(String screenShotPath) {
		String path = null;
		path = "https://" + prop.getPropValue("bucketName") + ".s3."
				+ Regions.AP_SOUTH_1.toString().toLowerCase().replace("_", "-") + ".amazonaws.com" + "/"
				+ System.getProperty("jobName") + "/" + System.getProperty("buildNumber") + screenShotPath;
		return path;
	}

	public void uploadScreenshotToS3(String key, String filePath) {
		s3client.putObject(prop.getPropValue("bucketName"), key, new File(filePath));
	}

	public String uploadFileToS3Bucket(String file) {
		String filePath = System.getProperty("user.dir") + file;
		String key = System.getProperty("jobName") + "/" + System.getProperty("buildNumber") + file;
		s3client.putObject(prop.getPropValue("bucketName"), key, new File(filePath));
		return getKeyFullPath(key);
	}

	public String uploadFolderToS3Bucket(String folderPath) throws AmazonClientException, InterruptedException {
		TransferManager tm = null;
		String s3Folder = null;
		try {
			System.setProperty("jobName","ins-perfomance-testing");
			System.setProperty("buildNumber","3");

	        s3Folder =System.getProperty("jobName") + "/" + System.getProperty("buildNumber");

	        tm = TransferManagerBuilder.standard()
                    .withS3Client(s3client)
                    .build();

	        File localDirectory = new File(folderPath);
            if (!localDirectory.isDirectory()) {
                System.err.println("The provided path is not a directory.");
                return null;
            }
            
            uploadDirectory(tm, prop.getPropValue("bucketName"), s3Folder, localDirectory);

		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		} 
		finally {
            tm.shutdownNow();
		}
        return getKeyFullPath(s3Folder+"/index.html");
	}
	private static void uploadDirectory(TransferManager transferManager, String bucketName, String s3Folder, File localDirectory) throws AmazonServiceException, AmazonClientException, InterruptedException {
	
		for (File localFile : localDirectory.listFiles()) {
            if (localFile.isFile()) {
                String s3Key = s3Folder + "/" + localFile.getName();
                Upload upload = transferManager.upload(bucketName, s3Key, localFile);
                upload.waitForCompletion();
                System.out.println("Uploaded: " + localFile.getName());
            } else if (localFile.isDirectory()) {
                String newS3Folder = s3Folder + "/" + localFile.getName();
                uploadDirectory(transferManager, bucketName, newS3Folder, localFile);
            }
        }
    }

	public static String getKeyFullPath(String key) {
		String path = null;
		path = prop.getPropValue("bucketName") + ".s3." + Regions.AP_SOUTH_1.toString().toLowerCase().replace("_", "-")
				+ ".amazonaws.com" + "/" + key;
		return path;
	}

	public static void main(String... strings) throws AmazonClientException, InterruptedException {
		String string = 
		new AWSHelper().uploadFolderToS3Bucket("/home/nikhil/Documents/Software/apache-jmeter-5.5/programming/Dashboard");
		System.out.println(string);

	}
}
