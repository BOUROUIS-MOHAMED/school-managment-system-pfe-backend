package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Role;
import com.saif.pfe.models.Teacher;
import com.saif.pfe.models.User;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.TeacherRepository;
import com.saif.pfe.services.TeacherService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }


    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers(SearchCriteria searchCriteria, User user) {

        if ((user.getRoles().contains(Role.ADMIN)) || (user.getRoles().contains(Role.USER))) {
            return teacherRepository.findAll(searchCriteria.getPageable()).toList();
        }else if (user.getRoles().contains(Role.MODERATOR)){
            return List.of(Objects.requireNonNull(teacherRepository.findById(user.getId()).orElse(null)));
        }else return new ArrayList<>();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not found."));
    }

    public Teacher updateTeacher(Long id, Teacher teacher) {
        Teacher existingTeacher = getTeacherById(id);
        existingTeacher.setName(teacher.getName());
        existingTeacher.setEmail(teacher.getEmail());
        existingTeacher.setPhone(teacher.getPhone());
        return teacherRepository.save(existingTeacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

}
