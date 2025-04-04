package com.microservice.administrative.shared.domain.bus.query;

import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;
import java.lang.reflect.ParameterizedType;

public final class QueryHandlerInformation {
    HashMap<Class<? extends Query>, Class<? extends QueryHandler>> indexedQueryHandlers;
    
    public QueryHandlerInformation() {
        Reflections reflections = new Reflections("microservice.administrative");
        Set<Class<? extends QueryHandler>> classes = reflections.getSubTypesOf(QueryHandler.class);
        indexedQueryHandlers = formatHandlers(classes);
    }

    public Class<? extends QueryHandler> search(Class<? extends Query> queryClass)
            throws QueryNotRegisteredException {

        Class<? extends QueryHandler> queryHandlerClass = indexedQueryHandlers
                .get(queryClass);

        if (null == queryHandlerClass) {
            throw new QueryNotRegisteredException(queryClass);
        }

        return queryHandlerClass;
    }
    private HashMap<Class<? extends Query>, Class<? extends QueryHandler>> formatHandlers(
        Set<Class<? extends QueryHandler>> queryHandlers) {
    HashMap<Class<? extends Query>, Class<? extends QueryHandler>> handlers = new HashMap<>();

    for (Class<? extends QueryHandler> handler : queryHandlers) {
        ParameterizedType paramType = (ParameterizedType) handler.getGenericInterfaces()[0];
        Class<? extends Query> queryClass = (Class<? extends Query>) paramType.getActualTypeArguments()[0];

        handlers.put(queryClass, handler);
    }

    return handlers;
}
}
