package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.models.entities.Layout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    List<Layout> findByStatusNot(LayoutStatus layoutStatus);
}
