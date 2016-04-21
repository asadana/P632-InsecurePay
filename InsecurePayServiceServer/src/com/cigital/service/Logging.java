package com.cigital.service;

import org.apache.logging.log4j.LogManager;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.ApplicationPath;

import java.util.logging.Logger;

@ApplicationPath("/rest")
public class Logging extends ResourceConfig {
	public static final org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(BaseService.class.getName());

	public Logging() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		packages("com.cigital.service", "com.fasterxml.jackson.jaxrs.json",
				"com.cigital.service.BO");
		java.util.logging.Logger javaLogger = Logger
				.getLogger(BaseService.class.getName());
		register(MultiPartFeature.class);
		registerInstances(new LoggingFilter(javaLogger, true));
	}

}
