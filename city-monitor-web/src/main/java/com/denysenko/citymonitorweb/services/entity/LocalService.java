package com.denysenko.citymonitorweb.services.entity;

import com.denysenko.citymonitorweb.models.entities.Layout;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface LocalService {
    List<Long> getChatIdsLocatedWithinLayout(@NotNull Layout layout);
}
