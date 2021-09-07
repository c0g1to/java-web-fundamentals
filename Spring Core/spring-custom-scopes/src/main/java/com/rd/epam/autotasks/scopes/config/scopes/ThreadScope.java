package com.rd.epam.autotasks.scopes.config.scopes;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.*;

public class ThreadScope implements Scope {

    private final Map<String, Object> scopedObjects = Collections.synchronizedMap(new HashMap<>());
    private final List<Long> threads = new ArrayList<>();

    @Override
    public Object get(String s, ObjectFactory<?> objectFactory) {
        if (!threads.contains(Thread.currentThread().getId())) {
            scopedObjects.put(s, objectFactory.getObject());
            threads.add(Thread.currentThread().getId());
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

