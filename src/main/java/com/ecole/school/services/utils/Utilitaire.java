package com.ecole.school.services.utils;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Component
public class Utilitaire {

    public MappingJacksonValue getFilter(Object o, String filterName, String propertyName) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(propertyName);

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter(filterName, filter);

        MappingJacksonValue filtres = new MappingJacksonValue(o);

        filtres.setFilters(listDeNosFiltres);

        return filtres;
    }

    public MappingJacksonValue getFilterList(Object o, String filterName1, String propertyName1, String filterName2, String propertyName2) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(propertyName1);
        SimpleBeanPropertyFilter filter2 = SimpleBeanPropertyFilter.serializeAllExcept(propertyName2);

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter(filterName1, filter).addFilter(filterName2, filter2);

        MappingJacksonValue filtres = new MappingJacksonValue(o);

        filtres.setFilters(listDeNosFiltres);

        return filtres;
    }

}
