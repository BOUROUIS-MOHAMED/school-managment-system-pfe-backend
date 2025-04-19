package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Role;
import com.saif.pfe.models.Teacher;
import com.saif.pfe.models.TeacherCourse;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.TeacherCourseId;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.TeacherCourseRepository;
import com.saif.pfe.repository.TeacherRepository;
import com.saif.pfe.services.TeacherCourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {


    private final TeacherCourseRepository teacherCourseRepository;
    private final TeacherRepository teacherRepository;

    public TeacherCourseServiceImpl(TeacherCourseRepository teacherCourseRepository, TeacherRepository teacherRepository) {
        this.teacherCourseRepository = teacherCourseRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<TeacherCourse> findAll(SearchCriteria searchCriteria, User user) {

        List<ERole> roles= user.getRoles().stream().map(Role::getName).toList();


        if (roles.contains(ERole.ROLE_ADMIN)) {
            return teacherCourseRepository.findAll(searchCriteria.getPageable()).toList();
        }else if (roles.contains(ERole.ROLE_MODERATOR)){
            Optional<Teacher> teacher=teacherRepository.findByUserId(user.getId());
            if (teacher.isEmpty()){
                return new ArrayList<>();
            }
            Long id=teacher.get().getId();
            return teacherCourseRepository.findAllByTeacherId(id);
        }else return new ArrayList<>();
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
