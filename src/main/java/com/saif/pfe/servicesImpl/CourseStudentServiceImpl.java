package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.CourseStudent;
import com.saif.pfe.models.Role;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.CourseStudentRepository;
import com.saif.pfe.services.CourseStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseStudentServiceImpl implements CourseStudentService {

    @Autowired
    private CourseStudentRepository repository;

    @Override
    public List<CourseStudent> findAll(SearchCriteria searchCriteria, User user) {


        if (user.getRoles().contains(Role.ADMIN)){
            return repository.findAll(searchCriteria.getPageable()).toList();
        }else if(user.getRoles().contains(Role.USER)){
            return  repository.findAllByStudentId(user.getId());

        }else return new ArrayList<>();


    }

    @Override
    public Optional<CourseStudent> findById(CourseStudentId id) {
        return repository.findById(id);
    }

    @Override
    public CourseStudent save(CourseStudent courseStudent) {
        return repository.save(courseStudent);
    }

    @Override
    public CourseStudent update(CourseStudentId id, CourseStudent courseStudent) {
        Optional<CourseStudent> existing = repository.findById(id);
        if (existing.isPresent()) {
            courseStudent.setId(id); // Ensure the composite key is preserved
            return repository.save(courseStudent);
        }
        throw new RuntimeException("CourseStudent with ID " + id + " not found.");
    }

    @Override
    public void deleteById(CourseStudentId id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("CourseStudent with ID " + id + " not found.");
        }
    }

}
