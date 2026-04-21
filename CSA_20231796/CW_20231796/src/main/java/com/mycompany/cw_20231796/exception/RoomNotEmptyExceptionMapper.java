package com.mycompany.cw_20231796.exception;

import com.mycompany.cw_20231796.model.ErrorMessage;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(), 409, "Room cannot be deleted while sensors are still linked.");

        // gets server status as errorMessage, builds and returns it as response
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorMessage)
                .build();
    }
}
