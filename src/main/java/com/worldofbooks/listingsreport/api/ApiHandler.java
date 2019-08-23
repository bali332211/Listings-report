package com.worldofbooks.listingsreport.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class ApiHandler {

    private RestTemplate restTemplate;

    @Autowired
    public ApiHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> List<T> getEntitiesFromAPI(String url, Class<T> tClass) {
        ResponseEntity<List<T>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<T>>() {
                public Type getType() {
                    return new MyParameterizedTypeImpl((ParameterizedType) super.getType(), new Type[]{tClass});
                }
            });
        List<T> entities = response.getBody();
        return entities;
    }

    public static final class MyParameterizedTypeImpl implements ParameterizedType {
        private ParameterizedType delegate;
        private Type[] actualTypeArguments;

        MyParameterizedTypeImpl(ParameterizedType delegate, Type[] actualTypeArguments) {
            this.delegate = delegate;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getRawType() {
            return delegate.getRawType();
        }

        @Override
        public Type getOwnerType() {
            return delegate.getOwnerType();
        }

    }

}
