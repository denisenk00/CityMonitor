package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.entities.Polygon;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.repositories.hibernate.LayoutRepository;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;

import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.*;


@Service
public class LayoutEntityToDTOConverter implements EntityDTOConverter<Layout, LayoutDTO> {

    @Autowired
    private LayoutRepository layoutRepository;

    @Override
    public Layout convertDTOToEntity(LayoutDTO layoutDTO) throws ConversionFailedException {
        if(layoutDTO == null) throw new IllegalArgumentException();

        Layout layout;
        if(layoutDTO.getId() != null){
            Optional<Layout> layoutOptional = layoutRepository.findById(layoutDTO.getId());
            layout = layoutOptional.orElse(new Layout());
        }else {
            layout = new Layout();
        }

        layout.setId(layoutDTO.getId());
        layout.setName(layoutDTO.getName());

        if(layoutDTO.getStatus() != null) {
            try {
                layout.setStatus(LayoutStatus.getByTitle(layoutDTO.getStatus()));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        String geoJson = layoutDTO.getPolygonsGeoJson();

        GeoJSONReader geoJSONReader = new GeoJSONReader();
        FeatureCollection featureCollection = (FeatureCollection) GeoJSONFactory.create(geoJson);
        List<Polygon> oldPolygonEntities = layout.getPolygons();
        List<Polygon> newPolygonEntities = new LinkedList<>();

        for(Feature feature : featureCollection.getFeatures()){
            org.wololo.geojson.Geometry geometry = feature.getGeometry();
            org.locationtech.jts.geom.Polygon polygon = (org.locationtech.jts.geom.Polygon) geoJSONReader.read(geometry);

            String title = (String) feature.getProperties().get("title");
            Long id = (Long) feature.getProperties().get("id");

            Polygon polygonEntity = new Polygon(id, title, polygon, layout);
            newPolygonEntities.add(polygonEntity);
        }
        oldPolygonEntities.removeAll(newPolygonEntities);
        oldPolygonEntities.addAll(newPolygonEntities);

        return layout;
    }

    @Override
    public LayoutDTO convertEntityToDTO(Layout layout) throws ConversionFailedException {
        if(layout == null) throw new IllegalArgumentException();

        LayoutDTO.LayoutDTOBuilder layoutDTOBuilder = LayoutDTO.builder();
        layoutDTOBuilder.id(layout.getId())
                        .name(layout.getName());

        Optional.ofNullable(layout.getStatus()).ifPresent(
                s -> layoutDTOBuilder.status(s.getTitle()));

        String polygonsGeoJson;
        GeoJSONWriter geoJSONWriter = new GeoJSONWriter();
        ArrayList<Feature> featureList = new ArrayList<>();
        for (Polygon polygonEntity : layout.getPolygons()){
            org.locationtech.jts.geom.Polygon polygon = polygonEntity.getPolygon();
            Geometry geometry = geoJSONWriter.write(polygon);

            Map<String, Object> properties = new HashMap<>();
            properties.put("title", polygonEntity.getName());
            Optional.ofNullable(polygonEntity.getId()).ifPresent(
                    id -> properties.put("id", id.toString()));

            Feature feature = new Feature(geometry, properties);
            featureList.add(feature);
        }
        FeatureCollection featureCollection = new FeatureCollection(featureList.toArray(new Feature[featureList.size()]));
        polygonsGeoJson = featureCollection.toString();

        layoutDTOBuilder.polygonsGeoJson(polygonsGeoJson);

        return layoutDTOBuilder.build();
    }
}