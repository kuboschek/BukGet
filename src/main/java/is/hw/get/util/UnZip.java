package is.hw.get.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/*****************************************************************************************************************
This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.

Simon Tümmler, Leonhard Kuboschek 2013
******************************************************************************************************************/

public class UnZip {
	public static String unzipFileToString(File zipfile, String entryName) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(zipfile);
		//
		ZipEntry entry = zipFile.getEntry(entryName);
		if (entry == null) {
			zipFile.close();
			throw new FileNotFoundException("The requested file " + entryName + " could not be found in " + zipfile.getName());
		}
		//
		InputStream is = zipFile.getInputStream(entry);
		byte[] entryBytes = new byte[is.available()];
		is.read(entryBytes);
		String content = new String(entryBytes);
		//
		is.close();
		zipFile.close();
		//
		return content;
	}
	
	public static void unzipFile(File zipfileraw, String entryName, File tofile) throws IOException {
		ZipFile zipFile = new ZipFile(zipfileraw);
		ZipEntry entry = zipFile.getEntry(entryName);
		if (entry == null) {
			zipFile.close();
			throw new FileNotFoundException("The requested file " + entryName + " could not be found in " + zipfileraw.getName());
		}
		//
		InputStream is = zipFile.getInputStream(entry);
		OutputStream fileout = new FileOutputStream(tofile);
		//
		copyStream(is, fileout);
		//
		fileout.flush();
		fileout.close();
		is.close();
		zipFile.close();
	}
	
	public static void unzip(File zipfileraw, File todir) throws IOException {
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfileraw));
		ZipEntry entry = zis.getNextEntry();
		//
		while (entry != null) {
			String entryName = entry.getName();
			File outputFile = new File(todir, entryName);
			new File(outputFile.getParent()).mkdirs();
			//
			FileOutputStream fos = new FileOutputStream(outputFile);
			//
			copyStream(zis, fos);
			//
			fos.close();
			entry = zis.getNextEntry();
		}
		//
		zis.closeEntry();
		zis.close();
	}
	
	public static void unzipJars(File zipfileraw, File todir, FileUnzippedAction act) throws IOException {
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfileraw));
		ZipEntry entry = zis.getNextEntry();
		//
		while (entry != null) {
			String entryName = entry.getName();
			if (entryName.endsWith(".jar")) {
				File outputFile = new File(todir, entryName);
				new File(outputFile.getParent()).mkdirs();
				//
				FileOutputStream fos = new FileOutputStream(outputFile);
				//
				copyStream(zis, fos);
				//
				fos.close();
				//
				act.unzipped(outputFile);
			}
			entry = zis.getNextEntry();
		}
		//
		zis.closeEntry();
		zis.close();
	}
	
	public static boolean fileExists(File zipfile, String entry) {
		try {
			ZipFile zipFile = new ZipFile(zipfile);
			if (zipFile.getEntry(entry) == null) {
				zipFile.close();
				return false;
			} else {
				zipFile.close();
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static void copyStream(InputStream is, OutputStream os) throws IOException {
		int read = 0;
		byte[] buffer = new byte[1024];
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
	}
	
	public static interface FileUnzippedAction {
		public void unzipped(File f);
	}
}
