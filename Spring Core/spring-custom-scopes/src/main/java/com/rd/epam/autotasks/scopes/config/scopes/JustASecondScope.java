package com.rd.epam.autotasks.scopes.config.scopes;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.*;

public class JustASecondScope implements Scope {

    private final Map<String, Object> scopedObjects = new HashMap<>();
    private final Map<String, Thread> threads = new HashMap<>();

    @Override
    public Object get(String s, ObjectFactory<?> objectFactory) {
        if (threads.get(s) == null || !threads.get(s).isAlive()) {
            scopedObjects.put(s, objectFactory.getObject());
            threads.put(s, new Thread(new OneSecondRunnable(s)));
            threads.get(s).start();
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

    private class OneSecondRunnable implements Runnable {

        public String name;

        public OneSecondRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}




