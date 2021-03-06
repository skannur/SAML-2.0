/*
 * Copyright 2008 University Corporation for Advanced Internet Development, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.util;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.jcip.annotations.ThreadSafe;

/**
 * This class is used to store instances of objects that may be created independently but are, in face, the same object.
 * For example, {@link org.opensaml.xml.signature.KeyInfo}s contain keys, certs, and CRLs. Multiple unique instances of
 * a KeyInfo may contain, and separately construct, the exact same cert. KeyInfo could, therefore, create a class-level
 * instance of this object store and put certs within it. In this manner the cert is only sitting in memory once and
 * each KeyInfo simply stores a reference (index) to stored object.
 * 
 * This store uses basic reference counting to keep track of how many of the respective objects are pointing to an
 * entry. Adding an object that already exists, as determined by the objects <code>hashCode()</code> method, simply
 * increments the reference counter. Removing an object decrements the counter. Only when the counter reaches zero is
 * the object actually freed for garbage collection.
 * 
 * <strong>Note</strong> the instance of an object returned by {@link #get(String)} need not be the same object as 
 * stored via {@link #put(Object)}.  However, their hash codes will be equal.  Therefore this store should never be 
 * used to store objects that produce identical hash codes but are not functionally identical objects.
 * 
 * @param <T> type of object being stored
 */
@ThreadSafe
public class IndexingObjectStore<T> {

    /** Read/Write lock used to control synchronization over the backing data store. */
    private ReadWriteLock rwLock;

    /** Backing object data store. */
    private Map<String, StoredObjectWrapper> objectStore;

    /** Constructor. */
    public IndexingObjectStore() {
        rwLock = new ReentrantReadWriteLock();
        objectStore = new LazyMap<String, StoredObjectWrapper>();
    }

    /** Clears the object store. */
    public void clear() {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            objectStore.clear();
        } finally {
            writeLock.unlock();
        }

    }

    /**
     * Checks whether the store contains an object registered under the given index.
     * 
     * @param index the index to check
     * 
     * @return true if an object is associated with the given index, false if not
     */
    public boolean contains(String index) {
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            return objectStore.containsKey(index);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Checks if the store is empty.
     * 
     * @return true if the store is empty, false if not
     */
    public boolean isEmpty() {
        return objectStore.isEmpty();
    }

    /**
     * Adds the given object to the store. Technically this method only adds the object if it does not already exist in
     * the store. If it does this method simply increments the reference count of the object.
     * 
     * @param object the object to add to the store, may be null
     * 
     * @return the index that may be used to later retrieve the object or null if the object was null
     */
    public String put(T object) {
        if (object == null) {
            return null;
        }

        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            String index = Integer.toString(object.hashCode());

            StoredObjectWrapper objectWrapper = objectStore.get(index);
            if (objectWrapper == null) {
                objectWrapper = new StoredObjectWrapper(object);
                objectStore.put(index, objectWrapper);
            }
            objectWrapper.incremementReferenceCount();

            return index;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Gets a registered object by its index.
     * 
     * @param index the index of an object previously registered, may be null
     * 
     * @return the registered object or null if no object is registered for that index
     */
    public T get(String index) {
        if (index == null) {
            return null;
        }

        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            StoredObjectWrapper objectWrapper = objectStore.get(index);
            if (objectWrapper != null) {
                return objectWrapper.getObject();
            }

            return null;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Removes the object associated with the given index. Technically this method decrements the reference counter to
     * the object. If, after the decrement, the reference counter is zero then, and only then, is the object actually
     * freed for garbage collection.
     * 
     * @param index the index of the object, may be null
     */
    public void remove(String index) {
        if (index == null) {
            return;
        }

        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            StoredObjectWrapper objectWrapper = objectStore.get(index);
            if (objectWrapper != null) {
                objectWrapper.decremementReferenceCount();
                if (objectWrapper.getReferenceCount() == 0) {
                    objectStore.remove(index);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Gets the total number of unique items in the store. This number is unaffected by the reference count of the
     * individual stored objects.
     * 
     * @return number of items in the store
     */
    public int size() {
        return objectStore.size();
    }

    /** Wrapper class that keeps track of the reference count for a stored object. */
    private class StoredObjectWrapper {

        /** The stored object. */
        private T object;

        /** The object reference count. */
        private int referenceCount;

        /**
         * Constructor.
         * 
         * @param wrappedObject the object being wrapped
         */
        public StoredObjectWrapper(T wrappedObject) {
            object = wrappedObject;
            referenceCount = 0;
        }

        /**
         * Gets the wrapped object.
         * 
         * @return the wrapped object
         */
        public T getObject() {
            return object;
        }

        /**
         * Gets the current reference count.
         * 
         * @return current reference count
         */
        public int getReferenceCount() {
            return referenceCount;
        }

        /** Increments the current reference count by one. */
        public void incremementReferenceCount() {
            referenceCount += 1;
        }

        /** Decrements the current reference count by one. */
        public void decremementReferenceCount() {
            referenceCount -= 1;
        }
    }
}