package com.denysenko.citymonitorweb.models.domain.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 *  @author https://github.com/martinwojtus/tutorials/tree/master/thymeleaf/thymeleaf-bootstrap-pagination
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paged<T> {

    private Page<T> page;
    private Paging paging;
}
