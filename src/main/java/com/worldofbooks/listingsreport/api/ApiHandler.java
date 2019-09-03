package com.worldofbooks.listingsreport.api;

import com.worldofbooks.listingsreport.database.ReferenceDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class ApiHandler {

    private RestTemplate restTemplate;

    @Value(value = "${worldofbooks.api.key}")
    private String apiKey;

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
        return response.getBody();
    }

    public ListingDataSet getListingDataSetFromApi() {
        ReferenceDataSet referenceDataSet = getReferencesFromApi();
        List<Listing> listings = getEntitiesFromAPI(constructApiMockarooUri("/listing"), Listing.class);

        return new ListingDataSet(listings, referenceDataSet);
    }

    private ReferenceDataSet getReferencesFromApi() {
        List<Status> statuses = getEntitiesFromAPI(constructApiMockarooUri("/listingStatus"), Status.class);
        List<Location> locations = getEntitiesFromAPI(constructApiMockarooUri("/location"), Location.class);
        List<Marketplace> marketplaces = getEntitiesFromAPI(constructApiMockarooUri("/marketplace"), Marketplace.class);

        return new ReferenceDataSet(statuses, locations, marketplaces);
    }

    private String constructApiMockarooUri(String path) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme("https").host("my.api.mockaroo.com").path(path).query("key={keyword}").buildAndExpand(apiKey);

        return uriComponents.toUriString();
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
