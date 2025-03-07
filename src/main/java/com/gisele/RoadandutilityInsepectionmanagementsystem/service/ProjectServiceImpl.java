package com.gisele.RoadandutilityInsepectionmanagementsystem.service;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Project;
import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.User;
import com.gisele.RoadandutilityInsepectionmanagementsystem.dao.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectRepository projectRepository;
	private UserService userService;

	private EntityManager entityManager;

	@Autowired
	public ProjectServiceImpl(ProjectRepository theprojectRepository, UserService theuserService, EntityManager theentityManager) {
		projectRepository = theprojectRepository;
		this.userService = theuserService;
		this.entityManager = theentityManager;
	}

	///Normal FindALL
	    @Override
    public List<Project> findAll() {
        // create query
		TypedQuery<Project> theQuery = entityManager.createQuery("SELECT p FROM Project p ORDER BY p.pname ASC", Project.class);

        // return query results
        return theQuery.getResultList();
    }

	// Pageable findAll

	@Override
	public Page<Project> findAll(int pageNumber, String keyword) {
		Sort sort = Sort.by("pname").ascending();

		Pageable pageable = PageRequest.of(pageNumber - 1, 2, sort);
		if(keyword !=null){
			return projectRepository.findAll(keyword, pageable);
		}
		return projectRepository.findAll(pageable);
	}
	@Override
	public Page<Project> findAllByInspectorId(Long userId, int pageNumber, String keyword){
		Sort sort = Sort.by("pname").ascending();
		Pageable pageable = PageRequest.of(pageNumber - 1, 2, sort);

		if(keyword !=null){
			return projectRepository.findAllByInspectorId(userId, keyword, pageable);
		}
		return  projectRepository.findAllByInspectorId(userId, pageable);
	}


//	@Override
//	public List<OperationsManager> findAll() {
//		return managerRepository.findAll();
//	}
//
	@Override
	public Project findById(int theId) {
		Optional<Project> result = projectRepository.findById(theId);

		Project project = null;

		if (result.isPresent()) {
			project = result.get();
		}
		else {
			// we didn't find the project
			throw new RuntimeException("Did not find project id - " + theId);
		}

		return project;
	}


	@Override
	@Transactional
	public void save(Project project, Long UserId) {
		User existingUser = userService.findById(UserId);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date Start_date  = project.getStart_date(); //
		String sDate = formatter.format(Start_date);
		// String sDate = date.format(formatter);
		Date Current_date  = project.getTheCurrent_date(); //
		//String cDate = date2.format(formatter);
		String cDate = formatter.format(Current_date);
		project.setSdate(sDate);
		project.setCdate(cDate);
		existingUser.add(project);
		projectRepository.save(project);
	}

	@Override
	public void deleteById(int theId) {
		projectRepository.deleteById(theId);
	}

}






