package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Classroom;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.ClassroomRepository;
import com.saif.pfe.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public List<Classroom> getAllClassrooms(SearchCriteria searchCriteria) {
        return classroomRepository.findAll(searchCriteria.getPageable()).toList();
    }

    public Optional<Classroom> getClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    public Classroom updateClassroom(Long id, Classroom updatedClassroom) {
        return classroomRepository.findById(id).map(classroom -> {
            classroom.setName(updatedClassroom.getName());
            classroom.setCapacity(updatedClassroom.getCapacity());
            return classroomRepository.save(classroom);
        }).orElseThrow(() -> new RuntimeException("Classroom not found"));
    }

    public void deleteClassroom(Long id) {
        if (classroomRepository.existsById(id)) {
            classroomRepository.deleteById(id);
        } else {
            throw new RuntimeException("Classroom not found");
        }
    }

}
