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

	public String saveUploadedFile(InputStream uploadedInputStream, String fileName) {
		uploadedFileLocation = Constants.fileUploadDir + fileName;
		
		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);

		String output = "File uploaded to : " + uploadedFileLocation;
		return output;
	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			Logging.logger.error(e);
		}

	}

}
