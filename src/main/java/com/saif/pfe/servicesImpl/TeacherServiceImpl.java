package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Role;
import com.saif.pfe.models.Student;
import com.saif.pfe.models.Teacher;
import com.saif.pfe.models.User;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.RoleRepository;
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
    private final RoleRepository roleRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        // Fetch managed ROLE_MODERATOR
        Role userRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                .orElseThrow(() -> new IllegalStateException("ROLE_MOD not found"));

        User user = teacher.getUser();
        user.setEmail(teacher.getEmail());
        user.setRoles(Set.of(userRole));
        // CascadeType.ALL on Student.user ensures saving both
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
