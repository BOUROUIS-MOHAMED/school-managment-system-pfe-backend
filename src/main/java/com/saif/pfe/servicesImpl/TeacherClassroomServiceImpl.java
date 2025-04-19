package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Role;
import com.saif.pfe.models.TeacherClassroom;
import com.saif.pfe.models.User;
import com.saif.pfe.models.embeddedId.TeacherClassroomId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.TeacherClassroomRepository;
import com.saif.pfe.services.TeacherClassroomService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherClassroomServiceImpl implements TeacherClassroomService {


    private final TeacherClassroomRepository teacherClassroomRepository;

    public TeacherClassroomServiceImpl(TeacherClassroomRepository teacherClassroomRepository) {
        this.teacherClassroomRepository = teacherClassroomRepository;
    }
    @Override
    public List<TeacherClassroom> findAll(SearchCriteria searchCriteria, User user) {

        if (user.getRoles().contains(Role.ADMIN)){
            return teacherClassroomRepository.findAll(searchCriteria.getPageable()).toList();
        }else if (user.getRoles().contains(Role.MODERATOR)){
            return teacherClassroomRepository.findAllByTeacherId(user.getId());
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
