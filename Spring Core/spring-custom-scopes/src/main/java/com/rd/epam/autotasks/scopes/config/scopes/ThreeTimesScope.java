package com.rd.epam.autotasks.scopes.config.scopes;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

public class ThreeTimesScope implements Scope {

    private final Map<String, Object> scopedObjects = new HashMap<>();
    private int callCounter = 0;

    @Override
    public Object get(String s, ObjectFactory<?> objectFactory) {
        if (callCounter++ % 3 == 0) {
            scopedObjects.put(s, objectFactory.getObject());
        }
        return scopedObjects.get(s);
    }

    @Override
    public Object remove(String s) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    @Override
    public Object resolveContextualObject(String s) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
