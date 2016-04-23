package com.cigital.insecurepay.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.cigital.insecurepay.common.DaoFactory;
import com.cigital.insecurepay.dao.FileUploadDao;

/**
 * ChatService extends {@link BaseService}. 
 * This class is a service that receives a subject message from the 
 * user and sends it back.
 */
@Path("/chatService")
public class ChatService extends BaseService {

	/**
	 * chatMessage is a function that receives the subject message and sends it
	 * back.
	 * 
	 * @param subject
	 *            Contains the subject message as String variable.
	 * 
	 * @return Response Return a {@link Response}.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response chatMessage(String subject) {

		Logging.logger.debug("Sending the message back");

		return Response.status(Response.Status.OK).entity(subject).build();
	}

	/**
	 * uploadFile is a function that receives InputStream from the user and
	 * saves it as a local file with the fileDetail from the user.
	 * 
	 * @param uploadedInputStream		Contains the file content in the form of
	 * 									{@link InputStream}.
	 * @param fileDetail				Contains the file details in for of 
	 * 									{@link FormDataContentDisposition}.
	 * 
	 * @return Response					Return a {@link Response}.
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		Boolean booleanResultObj = false;

		try {
			booleanResultObj = DaoFactory.getInstance(FileUploadDao.class, this.getConnection())
					.saveUploadedFile(uploadedInputStream, fileDetail.getFileName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException | IllegalArgumentException
				| InvocationTargetException | SQLException e) {
			logger.error(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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
