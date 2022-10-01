package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.models.entities.Layout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
}
