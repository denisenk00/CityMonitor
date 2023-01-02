package com.denysenko.citymonitorweb.models.domain.paging;

import com.denysenko.citymonitorweb.enums.PageItemType;
import lombok.*;

/**
 *  @author https://github.com/martinwojtus/tutorials/tree/master/thymeleaf/thymeleaf-bootstrap-pagination
 */

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
