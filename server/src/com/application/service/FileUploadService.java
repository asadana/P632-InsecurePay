package com.application.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.application.common.DaoFactory;
import com.application.dao.FileUploadDao;

@Path("/chatFileUpload")
public class FileUploadService extends BaseService {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		String resultObj = null;
		try {
			resultObj = DaoFactory.getInstance(FileUploadDao.class, 
					this.getConnection()).saveUploadedFile(uploadedInputStream,
							fileDetail.getFileName());
		} catch (ClassNotFoundException | InstantiationException 
				| IllegalAccessException | NoSuchMethodException 
				| SecurityException | IllegalArgumentException 
				| InvocationTargetException | SQLException e) {
			logger.error(e);
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return Response.status(Response.Status.ACCEPTED).entity(resultObj).build();
	}

	
}