package com.fasterxml.jackson.module.hibernate;

import java.util.*;

import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;

import org.codehaus.jackson.map.*;
import org.codehaus.jackson.type.JavaType;

import com.fasterxml.jackson.module.hibernate.HibernateModule.Feature;

public class HibernateSerializers implements Serializers
{
    protected final int _moduleFeatures;
    
    public HibernateSerializers(int features)
    {
        _moduleFeatures = features;
    }

    public JsonSerializer<?> findArraySerializer(SerializationConfig serializationConfig,
                                                 ArrayType arrayType,
                                                 BeanDescription beanDescription,
                                                 BeanProperty beanProperty,
                                                 TypeSerializer typeSerializer,
                                                 JsonSerializer<Object> objectJsonSerializer) {
        return null;
    }

    public JsonSerializer<?> findCollectionSerializer(SerializationConfig serializationConfig,
                                                      CollectionType collectionType,
                                                      BeanDescription beanDescription,
                                                      BeanProperty beanProperty,
                                                      TypeSerializer typeSerializer,
                                                      JsonSerializer<Object> objectJsonSerializer) {
        return null;
    }

    public JsonSerializer<?> findCollectionLikeSerializer(SerializationConfig serializationConfig, CollectionLikeType collectionLikeType, BeanDescription beanDescription, BeanProperty beanProperty, TypeSerializer typeSerializer, JsonSerializer<Object> objectJsonSerializer) {
        return null;
    }

    public JsonSerializer<?> findMapSerializer(SerializationConfig serializationConfig, MapType mapType, BeanDescription beanDescription, BeanProperty beanProperty, JsonSerializer<Object> objectJsonSerializer, TypeSerializer typeSerializer, JsonSerializer<Object> objectJsonSerializer1) {
        return null;
    }

    public JsonSerializer<?> findMapLikeSerializer(SerializationConfig serializationConfig, MapLikeType mapLikeType, BeanDescription beanDescription, BeanProperty beanProperty, JsonSerializer<Object> objectJsonSerializer, TypeSerializer typeSerializer, JsonSerializer<Object> objectJsonSerializer1) {
        return null;
    }

    public JsonSerializer<?> findSerializer(
            SerializationConfig config, JavaType type,
            BeanDescription beanDesc, BeanProperty beanProperty )
    {
        Class<?> raw = type.getRawClass();
        /* All Hibernate collection types (including maps!) implement this interface; but
         * it is not much more than a tag interface. So we will have at least 3 kinds
         * of subtypes to consider... Maps, Collections (List, Set); other, where last
         * type can still be serialized using iterator.
         */
        
        if (PersistentCollection.class.isAssignableFrom(raw)) {
            if (Map.class.isAssignableFrom(raw)) {
                // Let's just cast back to Map<K,V>, using whatever parametrization we have (if any)
                return new PersistentCollectionSerializer(type.widenBy(Map.class),
                        isEnabled(Feature.FORCE_LAZY_LOADING));
            }
            if (Collection.class.isAssignableFrom(raw)) {
                // Lists are slightly more efficient to serialize, so:
                if (List.class.isAssignableFrom(raw)) {
                    type = type.widenBy(List.class);
                } else {
                    type = type.widenBy(Collection.class);
                }
                return new PersistentCollectionSerializer(type, isEnabled(Feature.FORCE_LAZY_LOADING));
            }
            /* Other types could be supported in future, but for now this'll have
             * to do.
             */
        }

        if (HibernateProxy.class.isAssignableFrom(raw)) {
//            return new HibernateProxySerializer(type, config);
//            return new PersistentCollectionSerializer(type, isEnabled(Feature.FORCE_LAZY_LOADING));
        }
        return null;
    }

    public final boolean isEnabled(HibernateModule.Feature f) {
        return (_moduleFeatures & f.getMask()) != 0;
    }
}
