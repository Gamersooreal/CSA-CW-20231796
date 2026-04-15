package com.mycompany.cw_20231796.exception;

import com.mycompany.cw_20231796.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(), 403, "The sensor cannot accept new readings in its current state."
        );

        return Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
    }
}
