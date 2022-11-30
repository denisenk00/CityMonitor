package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.Layout;

import java.util.List;

public interface LocalService {
    List<Long> getChatIdsLocatedWithinLayout(Layout layout);
}
