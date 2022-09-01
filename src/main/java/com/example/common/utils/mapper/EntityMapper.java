package com.example.common.utils.mapper;

import java.util.List;

public interface EntityMapper <Response, Request, Entity>{
    Entity requestToEntity(Request request);
    Response entityToResponse(Entity entity);
    List<Entity> requestToEntity(List<Request> requests);
    List<Response> entityToResponse(List<Entity> entities);
}
