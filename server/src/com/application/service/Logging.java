package com.application.service;

import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;

import org.apache.logging.log4j.LogManager;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

@ApplicationPath("/rest")
public class Logging extends ResourceConfig {
	protected final org.apache.logging.log4j.Logger logger = LogManager
			.getLogger(this.getClass().getName());

	public Logging() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		packages("com.application.service", "com.fasterxml.jackson.jaxrs.json",
				"com.application.service.BO");
		java.util.logging.Logger javaLogger = Logger.getLogger(BaseService.class
				.getName());
		registerInstances(new LoggingFilter(javaLogger, true));
	}

}
