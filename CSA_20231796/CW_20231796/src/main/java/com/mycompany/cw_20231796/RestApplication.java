package com.mycompany.cw_20231796;

import com.mycompany.cw_20231796.exception.*;
import com.mycompany.cw_20231796.filter.LoggingFilter;
import com.mycompany.cw_20231796.resource.*;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Resources
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);

        // Exception Mappers
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(DataNotFoundExceptionMapper.class);
        classes.add(GenericExceptionMapper.class);

        // Filters
        classes.add(LoggingFilter.class);

        return classes;
    }
}