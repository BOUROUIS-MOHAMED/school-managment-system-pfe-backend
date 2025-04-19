package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Role;
import com.saif.pfe.models.Teacher;
import com.saif.pfe.models.TeacherClassroom;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.TeacherClassroomRepository;
import com.saif.pfe.repository.TeacherRepository;
import com.saif.pfe.services.TeacherClassroomService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherClassroomServiceImpl implements TeacherClassroomService {


    private final TeacherClassroomRepository teacherClassroomRepository;
    private final TeacherRepository teacherRepository;

    public TeacherClassroomServiceImpl(TeacherClassroomRepository teacherClassroomRepository, TeacherRepository teacherRepository) {
        this.teacherClassroomRepository = teacherClassroomRepository;
        this.teacherRepository = teacherRepository;
    }
    @Override
    public List<TeacherClassroom> findAll(SearchCriteria searchCriteria, User user) {

        List<ERole> roles= user.getRoles().stream().map(Role::getName).toList();


        if (roles.contains(ERole.ROLE_ADMIN)) {
            return teacherClassroomRepository.findAll(searchCriteria.getPageable()).toList();
        }else if (roles.contains(ERole.ROLE_MODERATOR)){
            Optional<Teacher> teacher=teacherRepository.findByUserId(user.getId());
            if (teacher.isEmpty()){
                return new ArrayList<>();
            }
            Long id=teacher.get().getId();
            return teacherClassroomRepository.findAllByTeacherId(id);
        }else return new ArrayList<>();
     }
    @Override
    public Optional<TeacherClassroom> findById(TeacherClassroomId id) {
        return teacherClassroomRepository.findById(id);
    }
    @Override
    public TeacherClassroom save(TeacherClassroom teacherClassroom) {
        return teacherClassroomRepository.save(teacherClassroom);
    }
    @Override
    public void deleteById(TeacherClassroomId id) {
        teacherClassroomRepository.deleteById(id);
    }
}
