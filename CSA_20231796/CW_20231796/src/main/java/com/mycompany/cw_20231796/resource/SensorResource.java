package com.mycompany.cw_20231796.resource;

import com.mycompany.cw_20231796.dao.MockDatabase;
import com.mycompany.cw_20231796.exception.LinkedResourceNotFoundException;
import com.mycompany.cw_20231796.model.Room;
import com.mycompany.cw_20231796.model.Sensor;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type == null || type.isEmpty()) {
            return MockDatabase.SENSORS;
        }

        List<Sensor> filtered = new ArrayList<>();
        for (Sensor sensor : MockDatabase.SENSORS) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSensor(Sensor sensor) {
        Room matchedRoom = null;

        for (Room room : MockDatabase.ROOMS) {
            if (room.getId().equals(sensor.getRoomId())) {
                matchedRoom = room;
                break;
            }
        }

        if (matchedRoom == null) {
            throw new LinkedResourceNotFoundException("Room with ID " + sensor.getRoomId() + " not found.");
        }

        MockDatabase.SENSORS.add(sensor);
        matchedRoom.getSensorIds().add(sensor.getId());
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource() {
        return new SensorReadingResource();
    }
}
