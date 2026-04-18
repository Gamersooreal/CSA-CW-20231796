package com.mycompany.cw_20231796.dao;

import com.mycompany.cw_20231796.model.Room;
import com.mycompany.cw_20231796.model.Sensor;
import com.mycompany.cw_20231796.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockDatabase {
    public static final List<Room> ROOMS = new ArrayList<>();// room list
    public static final List<Sensor> SENSORS = new ArrayList<>();// sensor list
    public static final Map<String, List<SensorReading>> READINGS = new HashMap<>();// reading list
}
