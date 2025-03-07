package com.gisele.RoadandutilityInsepectionmanagementsystem.dao;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ProjectRepository extends PagingAndSortingRepository<Project, Integer> {

    //searching using a keyword
    @Query("SELECT p FROM Project p WHERE "
            + "CONCAT(p.pname, p.location, p.start_date, p.theCurrent_date, p.projectType, p.safety, p.weatherConditions, p.status,p.aesthetics, p.reportName, p.ImageName)" + "LIKE %?1%")
    public Page<Project> findAll(String keyword, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.user.id = ?1 AND "
            + "CONCAT(p.pname, p.location, p.start_date, p.theCurrent_date, p.projectType, p.safety, p.weatherConditions, p.status,p.aesthetics, p.reportName, p.ImageName)" + "LIKE %?2%")
    public Page<Project> findAllByInspectorId(Long userId, String keyword, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.user.id = ?1")
    public Page<Project> findAllByInspectorId(Long inspectorId, Pageable pageable);

}
