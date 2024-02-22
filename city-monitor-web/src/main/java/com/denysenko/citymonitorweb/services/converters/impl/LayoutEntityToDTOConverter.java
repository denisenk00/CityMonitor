package com.denysenko.citymonitorweb.services.converters.impl;

import com.denysenko.citymonitorweb.models.entities.Polygon;
import com.denysenko.citymonitorweb.models.dto.LayoutDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.*;


@RequiredArgsConstructor
@Service
public class LayoutEntityToDTOConverter implements EntityDTOConverter<Layout, LayoutDTO> {

    @Override
    public Layout convertDTOToEntity(LayoutDTO layoutDTO) throws ConversionFailedException {
        try {
            Layout layout = new Layout();

            layout.setId(layoutDTO.getId());
            layout.setName(layoutDTO.getName());

            if (layoutDTO.getStatus() != null) {
                layout.setStatus(layoutDTO.getStatus());
            }

            String geoJson = layoutDTO.getPolygonsGeoJson();

            GeoJSONReader geoJSONReader = new GeoJSONReader();
            FeatureCollection featureCollection = (FeatureCollection) GeoJSONFactory.create(geoJson);
            List<Polygon> newPolygonEntities = new LinkedList<>();

            for (Feature feature : featureCollection.getFeatures()) {
                org.wololo.geojson.Geometry geometry = feature.getGeometry();
                org.locationtech.jts.geom.Polygon polygon = (org.locationtech.jts.geom.Polygon) geoJSONReader.read(geometry);

                String title = (String) feature.getProperties().get("title");
                Long id = (Long) feature.getProperties().get("id");

                Polygon polygonEntity = new Polygon(id, title, polygon, layout);
                newPolygonEntities.add(polygonEntity);
            }

            newPolygonEntities.forEach(polygon -> layout.addPolygon(polygon));

            return layout;
        } catch (Exception e) {
            throw new ConversionFailedException(TypeDescriptor.forObject(layoutDTO), TypeDescriptor.valueOf(Layout.class), null, e);
        }
    }

    @Override
    public LayoutDTO convertEntityToDTO(Layout layout) throws ConversionFailedException {
        try {
            LayoutDTO layoutDTO = new LayoutDTO();
            layoutDTO.setId(layout.getId());
            layoutDTO.setName(layout.getName());
            layoutDTO.setStatus(layout.getStatus());

            String polygonsGeoJson;
            GeoJSONWriter geoJSONWriter = new GeoJSONWriter();
            ArrayList<Feature> featureList = new ArrayList<>();
            for (Polygon polygonEntity : layout.getPolygons()) {
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
            layoutDTO.setPolygonsGeoJson(polygonsGeoJson);

            return layoutDTO;
        } catch (Exception e) {
            throw new ConversionFailedException(TypeDescriptor.forObject(layout), TypeDescriptor.valueOf(Layout.class), null, e);
        }
    }
}
