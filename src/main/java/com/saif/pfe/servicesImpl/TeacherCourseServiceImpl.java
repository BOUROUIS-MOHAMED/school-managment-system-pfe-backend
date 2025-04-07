package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.TeacherCourse;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.TeacherCourseRepository;
import com.saif.pfe.services.TeacherCourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {


    private final TeacherCourseRepository teacherCourseRepository;

    public TeacherCourseServiceImpl(TeacherCourseRepository teacherCourseRepository) {
        this.teacherCourseRepository = teacherCourseRepository;
    }

    public List<TeacherCourse> findAll(SearchCriteria searchCriteria) {
        return teacherCourseRepository.findAll(searchCriteria.getPageable()).toList();
    }

    public Optional<TeacherCourse> findById(TeacherCourseId id) {
        return teacherCourseRepository.findById(id);
    }

    public TeacherCourse save(TeacherCourse teacherCourse) {
        return teacherCourseRepository.save(teacherCourse);
    }

    public void deleteById(TeacherCourseId id) {
        teacherCourseRepository.deleteById(id);
    }
}
