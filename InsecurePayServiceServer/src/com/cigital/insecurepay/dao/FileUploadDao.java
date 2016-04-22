package com.cigital.insecurepay.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;

import com.cigital.insecurepay.common.Constants;
import com.cigital.insecurepay.service.Logging;

/**
 * FileUploadDao extends {@link BaseDao}.
 * This class takes the InputStream from the user
 * and stores it into a file with a given fileName.
 */
public class FileUploadDao extends BaseDao {

	// String stores the complete path of the file being stored.
	String uploadedFileLocation;

	/**
	 * FileUploadDao is a parameterized constructor to initialize the 
	 * super class.
	 */
	public FileUploadDao(Connection conn) {
		super(conn);
		uploadedFileLocation = null;
	}

	/**
	 * saveUploadFile is a function that takes the InputStream and fileName 
	 * and performs write access checks, then passes the InputStream and fileName to
	 * writeToFile function.
	 * 
	 * @param	uploadedInputStream		Contains the InputStream coming from the user.
	 * @param	fileName				Contains the file name received from the user.
	 * 
	 * @return	Boolean		Return a boolean object depending on if the file was 
	 * 						successfully saved.
	 */
	public boolean saveUploadedFile(InputStream uploadedInputStream, String fileName) {
		
		// Folder location from the Constants class
		File folderFileObj = new File(Constants.fileUploadDir);

		Logging.logger.debug("Folder path: " + Constants.fileUploadDir);
		Logging.logger.debug("Folder exists?: " + folderFileObj.exists());
		Logging.logger.debug("Folder is a directory?: " + folderFileObj.isDirectory());
		
		// If condition checks if the the folder path doesn't exists or is not a directory
		if (!(folderFileObj.exists() && folderFileObj.isDirectory())) {
			Logging.logger.debug("Folder not found");
			
			// If condition attempts to make a directory
			if (folderFileObj.mkdir()) {
				Logging.logger.debug("Creating folder");
				
				// Check again if the folder path exists or is not a directory
				if (!(folderFileObj.exists() && folderFileObj.isDirectory())) {
					Logging.logger.debug("Unable to create a folder");
					return false;
				}
			} else {
				Logging.logger.debug("Unable to create a folder");
				return false;
			}
		}
		
		// Setting up the file path in the fileUploadDir folder
		uploadedFileLocation = Constants.fileUploadDir + "/" + fileName;
		
		// boolean object to store if the file write was successful or not
		boolean booleanObj = writeToFile(uploadedInputStream, uploadedFileLocation); 
		
		Logging.logger.debug("writeToFile returned " + booleanObj);
		return booleanObj;
	}

	/**
	 * writeToFile is a function that takes the InputStream and file path
	 * and saves it as file to with the given path.
	 * 
	 * @param	uploadedInputStream		Contains the InputStream with the file content.
	 * @param	uploadedFileLocation	Contains the file path to be used to save the file.
	 *
	 * @return	Boolean		Return a boolean object depending on if the file was 
	 * 						successfully saved.
	 */
	private boolean writeToFile(InputStream uploadedInputStream, 
								String uploadedFileLocation) {

		try {
			OutputStream outputStream = new FileOutputStream(new File(uploadedFileLocation));
			Logging.logger.debug("File created.");
			
			int readBytes = 0;
			byte[] bytesArray = new byte[1024];

			outputStream = new FileOutputStream(new File(uploadedFileLocation));
			
			Logging.logger.debug("File writing started.");
			
			// While loop reads the bytes from the uploadedInputStream
			while ((readBytes = uploadedInputStream.read(bytesArray)) != -1) {
				outputStream.write(bytesArray, 0, readBytes);
			}
			
			outputStream.flush();
			outputStream.close();
			Logging.logger.debug("File writing completed.");
			
			return true;
		} catch (IOException e) {
			Logging.logger.debug("Unable to write to a file");
			Logging.logger.error(e);
			return false;
		}
	}
}
