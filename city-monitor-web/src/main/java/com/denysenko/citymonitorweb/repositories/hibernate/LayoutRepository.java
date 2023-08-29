package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.dto.LayoutPreviewDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LayoutRepository extends JpaRepository<Layout, Long> {

    Page<LayoutPreviewDTO> findAllPreviewsBy(Pageable pageable);
    List<LayoutPreviewDTO> findPreviewsByStatusNot(LayoutStatus layoutStatus);

    @Query("select l.status from Layout l where l.id = ?1")
    LayoutStatus getLayoutStatusById(Long id);

    @Query("select l from Layout l left join fetch l.polygons where l.id = ?1")
    Layout getLayoutWithPolygonsBy(Long id);

}
