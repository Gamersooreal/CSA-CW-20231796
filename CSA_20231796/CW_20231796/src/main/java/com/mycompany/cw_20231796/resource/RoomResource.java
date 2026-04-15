package com.mycompany.cw_20231796.resource;

import com.mycompany.cw_20231796.dao.MockDatabase;
import com.mycompany.cw_20231796.exception.RoomNotEmptyException;
import com.mycompany.cw_20231796.model.Room;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return MockDatabase.ROOMS;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addRoom(Room room) {
        MockDatabase.ROOMS.add(room);
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Room getRoomById(@PathParam("roomId") String roomId) {
        for (Room room : MockDatabase.ROOMS) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    @DELETE
    @Path("/{roomId}")
    public void deleteRoom(@PathParam("roomId") String roomId) {
        for (Room room : MockDatabase.ROOMS) {
            if (room.getId().equals(roomId) && !room.getSensorIds().isEmpty()) {
                throw new RoomNotEmptyException("Room " + roomId + " still has sensors assigned.");
            }
        }
        MockDatabase.ROOMS.removeIf(room -> room.getId().equals(roomId));
    }
}
