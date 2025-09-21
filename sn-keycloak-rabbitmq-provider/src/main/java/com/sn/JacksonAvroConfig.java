package com.sn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sn.events.User;
import com.sn.events.UserEvent;
import org.apache.avro.Schema;

public class JacksonAvroConfig {
    @JsonIgnoreProperties({"SCHEMA$", "schema"})
    abstract static class AvroMixin {
        @JsonIgnore
        public abstract Schema getSchema();
    }

    public static ObjectMapper configuredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(User.class, AvroMixin.class);
        mapper.addMixIn(UserEvent.class, AvroMixin.class);
        return mapper;
    }
}
