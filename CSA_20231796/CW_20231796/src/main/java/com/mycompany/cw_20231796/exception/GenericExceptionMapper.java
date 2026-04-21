package com.mycompany.cw_20231796.exception;

import com.mycompany.cw_20231796.model.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        ErrorMessage errorMessage = new ErrorMessage("An unexpected error occurred.", 500,
                "Please contact support or try again later.");

        // gets server status as errorMessage, builds and returns it as response
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}
