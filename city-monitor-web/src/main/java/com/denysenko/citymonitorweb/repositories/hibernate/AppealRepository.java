package com.denysenko.citymonitorweb.repositories.hibernate;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.denysenko.citymonitorweb.models.dto.AppealDTO;
import com.denysenko.citymonitorweb.models.entities.Appeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Set;

public interface AppealRepository extends JpaRepository<Appeal, Long> {

    long countByStatusEquals(AppealStatus status);

    @Query(value = "select distinct a from Appeal a" +
                    " inner join fetch a.local l " +
                    " left join fetch a.files f " +
                    " where a.status in ?1",
        countQuery = "select count (a) from Appeal a where a.status in ?1")
    @QueryHints(value = {
            @QueryHint(name = org.hibernate.annotations.QueryHints.PASS_DISTINCT_THROUGH, value = "false")
    })
    Page<AppealDTO> findAllByStatusIn(Set<AppealStatus> statuses, Pageable pageable);

}
