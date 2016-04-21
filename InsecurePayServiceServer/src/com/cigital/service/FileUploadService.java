package com.cigital.service;

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

import com.cigital.common.DaoFactory;
import com.cigital.dao.FileUploadDao;

@Path("/chatFileUpload")
public class FileUploadService extends BaseService {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		Boolean booleanResultObj = false;
		
		try {
			booleanResultObj = DaoFactory.getInstance(FileUploadDao.class, 
					this.getConnection()).saveUploadedFile(uploadedInputStream,
							fileDetail.getFileName());
		} catch (ClassNotFoundException | InstantiationException 
				| IllegalAccessException | NoSuchMethodException 
				| SecurityException | IllegalArgumentException 
				| InvocationTargetException | SQLException e) {
			logger.error(e);
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			try {
				close();
			} catch (SQLException e) {
				logger.error(e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		if (booleanResultObj) {
			return Response.status(Response.Status.ACCEPTED).entity(booleanResultObj).build();	
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	
}