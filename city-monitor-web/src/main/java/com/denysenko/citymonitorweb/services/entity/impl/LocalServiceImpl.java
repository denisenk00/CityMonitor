package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Local;
import com.denysenko.citymonitorweb.models.entities.Polygon;
import com.denysenko.citymonitorweb.repositories.hibernate.LocalRepository;
import com.denysenko.citymonitorweb.services.entity.LocalService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LocalServiceImpl implements LocalService {

    private final LocalRepository localRepository;

    @Override
    public List<Long> getChatIdsLocatedWithinLayout(Layout layout) {
        List<Local> allLocals = localRepository.findAll();
        List<Long> filteredChatIds = new LinkedList<>();
        allLocals.forEach(local -> {
            Point locationPoint = local.getLocation();
            if(layoutContainsPoint(layout, locationPoint)){
                filteredChatIds.add(local.getChatId());
            }
        });
        return filteredChatIds;
    }

    private boolean layoutContainsPoint(Layout layout, Point point){
        for(Polygon polygon : layout.getPolygons()){
            org.locationtech.jts.geom.Polygon jtsPolygon = polygon.getPolygon();
            if(jtsPolygon.contains(point)) return true;
        }
        return false;
    }
}
