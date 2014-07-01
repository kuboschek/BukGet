package is.hw.get.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {
	public static String getMD5(File f) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte hash[] = md.digest(Files.readAllBytes(f.toPath()));
		StringBuilder sb = new StringBuilder(2 * hash.length);
		//
		for(byte b : hash) {
			sb.append(String.format("%02x", b & 0xff));
		}
		//
		return sb.toString();
	}
	
	public static void writeFile(File f, String data) throws IOException {
		FileWriter fw = new FileWriter(f, false);
		fw.write(data);
		fw.flush();
		fw.close();
	}
	
	public static String readFile(File f) throws IOException {
		return new String(Files.readAllBytes(f.toPath()));
	}
	
	public static boolean removeRecursive(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(files != null){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                	removeRecursive(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
	}
}
