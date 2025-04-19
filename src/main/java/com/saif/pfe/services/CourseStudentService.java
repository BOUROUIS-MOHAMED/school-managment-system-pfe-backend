package com.saif.pfe.services;

import com.saif.pfe.models.CourseStudent;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.CourseStudentId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface CourseStudentService {

    List<CourseStudent> findAll(SearchCriteria searchCriteria, User user);

    Optional<CourseStudent> findById(CourseStudentId id);

    CourseStudent save(CourseStudent courseStudent);

    CourseStudent update(CourseStudentId id, CourseStudent courseStudent);

    void deleteById(CourseStudentId id);

}
