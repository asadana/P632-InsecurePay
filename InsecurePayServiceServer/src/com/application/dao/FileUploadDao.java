package com.application.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;

import com.application.common.Constants;
import com.application.service.Logging;

public class FileUploadDao extends BaseDao {

	String uploadedFileLocation;

	public FileUploadDao(Connection conn) {
		super(conn);
		uploadedFileLocation = null;
	}

	public boolean saveUploadedFile(InputStream uploadedInputStream, String fileName) {
		File folderFileObj = new File(Constants.fileUploadDir);

		Logging.logger.debug("saveUploadedFile: Folder path: " + Constants.fileUploadDir);
		Logging.logger.debug("saveUploadedFile: Folder exists?: " + folderFileObj.exists());
		Logging.logger.debug("saveUploadedFile: Folder is a directory?: " + folderFileObj.isDirectory());
		
		if (!(folderFileObj.exists() && folderFileObj.isDirectory())) {
			Logging.logger.debug("saveUploadedFile: folder not found");
			if (folderFileObj.mkdir()) {
				Logging.logger.debug("saveUploadedFile: Creating folder");
				if (!(folderFileObj.exists() && folderFileObj.isDirectory())) {
					Logging.logger.debug("saveUploadedFile: Unable to create a folder");
					return false;
				}
			} else {
				Logging.logger.debug("writeToFile: Unable to create a folder");
				return false;
			}
		}
		
		uploadedFileLocation = Constants.fileUploadDir + "/" + fileName;
		
		boolean booleanObj = writeToFile(uploadedInputStream, uploadedFileLocation); 
		if (booleanObj) {
			Logging.logger.debug("storeUploadedFile: writeToFile returned true");
			return booleanObj;
		} else {
			Logging.logger.debug("storeUploadedFile: writeToFile returned false");
			return booleanObj;
		}
	}

	// save uploaded file to new location
	private boolean writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

		try {
			
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			Logging.logger.debug("writeToFile: File created.");
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			Logging.logger.debug("writeToFile: File writing started.");
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			Logging.logger.debug("writeToFile: File writing completed.");
			return true;
		} catch (IOException e) {
			Logging.logger.debug("writeToFile: Unable to write to a file");
			Logging.logger.error(e);
			return false;
		}

	}

}
