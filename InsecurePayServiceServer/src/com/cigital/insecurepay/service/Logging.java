package com.cigital.insecurepay.service;

import org.apache.logging.log4j.LogManager;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.ApplicationPath;

import java.util.logging.Logger;

/**
 * Logging extends {@link ResourceConfig}. This class is used to provide a
 * logger for all the other classes.
 */
@ApplicationPath("/rest")
public class Logging extends ResourceConfig {
	public static final org.apache.logging.log4j.Logger logger = 
			LogManager.getLogger(BaseService.class.getName());

	/**
	 * Logging is the default unparameterized constructor that is used to
	 * initializ the logging functionality.
	 */
	public Logging() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		packages("com.cigital.insecurepay.service", 
				"com.fasterxml.jackson.jaxrs.json",
				"com.cigital.insecurepay.service.BO");
		
		java.util.logging.Logger javaLogger = Logger
				.getLogger(BaseService.class.getName());
		
		register(MultiPartFeature.class);
		registerInstances(new LoggingFilter(javaLogger, true));
	}

}
