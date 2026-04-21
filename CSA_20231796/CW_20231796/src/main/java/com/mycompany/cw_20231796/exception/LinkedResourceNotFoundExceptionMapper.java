package com.mycompany.cw_20231796.exception;

import com.mycompany.cw_20231796.model.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(), 422, "A linked resource referenced by the request could not be found.");

        // gets server status as errorMessage, builds and returns it as response
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}
