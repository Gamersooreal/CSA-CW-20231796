package com.mycompany.cw_20231796.filter;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger MyLogger = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MyLogger.info("=== Incoming Request ===");
        MyLogger.info("Method: " + requestContext.getMethod());
        MyLogger.info("URI: " + requestContext.getUriInfo().getAbsolutePath().toString());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MyLogger.info("=== Outgoing Response ===");
        MyLogger.info("Status: " + responseContext.getStatus());
    }
}
