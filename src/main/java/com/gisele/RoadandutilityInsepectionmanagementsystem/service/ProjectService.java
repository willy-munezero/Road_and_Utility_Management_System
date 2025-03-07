package com.gisele.RoadandutilityInsepectionmanagementsystem.service;



import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Project;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {

	List<Project> findAll();
	//@Cacheable("projects")
	Page<Project> findAll(int pageNumber, String keyword);

	Page<Project> findAllByInspectorId(Long userId, int pageNumber, String keyword);
	//@Cacheable("projects")
	Project findById(int theId);
	//@CachePut("projects")
	void save(Project project, Long userId);
	//@CacheEvict("projects")
	void deleteById(int theId);

}
