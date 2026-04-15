package com.mycompany.cw_20231796.resource;

import com.mycompany.cw_20231796.dao.MockDatabase;
import com.mycompany.cw_20231796.exception.DataNotFoundException;
import com.mycompany.cw_20231796.exception.SensorUnavailableException;
import com.mycompany.cw_20231796.model.Sensor;
import com.mycompany.cw_20231796.model.SensorReading;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class SensorReadingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadings(@PathParam("sensorId") String sensorId) {
        boolean sensorExists = false;
        for (Sensor sensor : MockDatabase.SENSORS) {
            if (sensor.getId().equals(sensorId)) {
                sensorExists = true;
                break;
            }
        }

        if (!sensorExists) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        List<SensorReading> readings = MockDatabase.READINGS.get(sensorId);
        if (readings == null) {
            return new ArrayList<>();
        }
        return readings;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(@PathParam("sensorId") String sensorId, SensorReading reading, @Context UriInfo uriInfo) {
        Sensor matchedSensor = null;

        for (Sensor sensor : MockDatabase.SENSORS) {
            if (sensor.getId().equals(sensorId)) {
                matchedSensor = sensor;
                break;
            }
        }

        if (matchedSensor == null) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        if ("MAINTENANCE".equalsIgnoreCase(matchedSensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is in maintenance mode.");
        }

        MockDatabase.READINGS.computeIfAbsent(sensorId, key -> new ArrayList<>()).add(reading);

        matchedSensor.setCurrentValue(reading.getValue());

        URI location = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(location).entity(reading).build();
    }
}
