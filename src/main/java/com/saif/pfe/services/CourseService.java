package com.saif.pfe.services;


import com.saif.pfe.models.Course;
import com.saif.pfe.models.User;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course);

    Course getCourseById(Long id);

    List<Course> getAllCourses(SearchCriteria searchCriteria, User user);

    Course updateCourse(Long id, Course courseDetails);

    void deleteCourse(Long id);

}
