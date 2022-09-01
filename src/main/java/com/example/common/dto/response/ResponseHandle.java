package com.example.common.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@JsonPropertyOrder(value = {
        "timestamp",
        "status",
        "error",
        "message",
        "payload"
})
public class ResponseHandle implements Serializable {
    public static ResponseEntity<Object> generateResponse(HttpStatus status,
                                                          String message,
                                                          Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timestamp", new Date());
        map.put("status", status.value());
        map.put("message", message);
        map.put("payload", responseObj);
        return new ResponseEntity<Object>(map,status);
    }
}
