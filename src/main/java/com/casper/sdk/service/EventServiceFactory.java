package com.casper.sdk.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.*;

/**
 * The factory class for creating the {@link EventService}
 *
 * @author ian@meywood.com
 */
final class EventServiceFactory {

    /**
     * The name of the class that implements the {@link com.casper.sdk.service.EventService}
     */
    private static final String IMPLEMENTATION_CLASS = "com.casper.sdk.service.impl.event.EventServiceImpl";
    /**
     * The map of service method names to log
     */
    private static final Map<Method, List<String>> methodParamMap = new HashMap<>();
    /**
     * The logger that logs all event service calls at debug level
     */
    private static final Logger logger = LoggerFactory.getLogger(EventServiceFactory.class);

    /**
     * Creates a new EventService for the specified host
     *
     * @param uri to URO of  the host to connect to
     * @return a newly created event service
     */
    static EventService create(final URI uri) {
        return proxy(createEventService(uri));
    }

    private static EventService proxy(final EventService eventService) {

        return (EventService) Proxy.newProxyInstance(
                EventServiceFactory.class.getClassLoader(),
                new Class[]{EventService.class},
                (proxy, method, args) -> {
                    if (logger.isDebugEnabled() && isServiceMethod(method)) {
                        logger.debug("{}({})", method.getName(), argsToString(method, args));
                    }
                    return method.invoke(eventService, args);
                }
        );
    }

    private static boolean isServiceMethod(final Method method) {
        return Arrays.asList(EventService.class.getDeclaredMethods()).contains(method);
    }

    private static EventService createEventService(final Object param) {

        try {
            //noinspection JavaReflectionMemberAccess
            final Constructor<?> constructor = Class.forName(IMPLEMENTATION_CLASS)
                    .getDeclaredConstructor(param.getClass());
            constructor.setAccessible(true);
            return (EventService) constructor.newInstance(param);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String argsToString(final Method method, final Object[] args) {

        final Iterator<String> parameterNames = getParameterNames(method).iterator();

        final StringBuilder builder = new StringBuilder();

        if (args != null) {
            for (Object arg : args) {

                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(parameterNames.next());
                builder.append('=');
                builder.append(arg);

            }
        }
        return builder.toString();
    }

    private static List<String> getParameterNames(final Method method) {

        return methodParamMap.computeIfAbsent(method, method1 -> {
            final Parameter[] parameters = method1.getParameters();
            final List<String> parameterNames = new ArrayList<>();
            for (Parameter parameter : parameters) {
                parameterNames.add(parameter.getName());
            }
            return parameterNames;
        });
    }
}
