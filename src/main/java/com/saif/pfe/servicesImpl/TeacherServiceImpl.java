package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Role;
import com.saif.pfe.models.Teacher;
import com.saif.pfe.models.User;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.TeacherRepository;
import com.saif.pfe.repository.UserRepository;
import com.saif.pfe.services.TeacherService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        teacher.getUser().setEmail(teacher.getEmail());
        teacher.getUser().setPassword(teacher.getUser().getPassword());
        teacher.getUser().setRoles(Set.of(Role.builder()
                .name(ERole.ROLE_MODERATOR)
                .build()));
        teacher.setUuid(teacher.getUser().getUuid());
        // cascade will save the user first, then the student with the same ID
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers(SearchCriteria searchCriteria, User user) {

        List<ERole> roles= user.getRoles().stream().map(Role::getName).toList();

        if ((roles.contains(ERole.ROLE_ADMIN)) || (roles.contains(ERole.ROLE_USER))) {
            return teacherRepository.findAll(searchCriteria.getPageable()).toList();
        }else if (
roles.contains(ERole.ROLE_MODERATOR)){
            Optional<Teacher> teacher=teacherRepository.findByUserId(user.getId());
            if (teacher.isEmpty()){
                return new ArrayList<>();
            }
            Long id=teacher.get().getId();
            return List.of(Objects.requireNonNull(teacherRepository.findById(id).orElse(null)));
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
