package com.denysenko.citymonitorweb.models.domain.paging;

import com.denysenko.citymonitorweb.enums.PageItemType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageItem {
    private PageItemType pageItemType;
    private int index;
    private boolean active;
}
