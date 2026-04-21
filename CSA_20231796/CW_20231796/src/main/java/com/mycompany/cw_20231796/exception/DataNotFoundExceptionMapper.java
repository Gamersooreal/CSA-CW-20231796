package com.mycompany.cw_20231796.exception;

import com.mycompany.cw_20231796.model.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

    @Override
    public Response toResponse(DataNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(), 404, "The requested resource could not be found.");

        // gets server status as errorMessage, builds and returns it as response
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}
