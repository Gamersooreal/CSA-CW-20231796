package com.mycompany.cw_20231796.resource;

import com.mycompany.cw_20231796.dao.MockDatabase;
import com.mycompany.cw_20231796.exception.SensorUnavailableException;
import com.mycompany.cw_20231796.model.Sensor;
import com.mycompany.cw_20231796.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class SensorReadingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadings(@PathParam("sensorId") String sensorId) {
        List<SensorReading> readings = MockDatabase.READINGS.get(sensorId);
        if (readings == null) {
            return new ArrayList<>();
        }
        return readings;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addReading(@PathParam("sensorId") String sensorId, SensorReading reading) {
        Sensor matchedSensor = null;

        for (Sensor sensor : MockDatabase.SENSORS) {
            if (sensor.getId().equals(sensorId)) {
                matchedSensor = sensor;
                break;
            }
        }

        if (matchedSensor != null && "MAINTENANCE".equalsIgnoreCase(matchedSensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is in maintenance mode.");
        }

        MockDatabase.READINGS.computeIfAbsent(sensorId, key -> new ArrayList<>()).add(reading);

        if (matchedSensor != null) {
            matchedSensor.setCurrentValue(reading.getValue());
        }
    }
}
