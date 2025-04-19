package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.*;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.PfeRepository;
import com.saif.pfe.repository.StudentRepository;
import com.saif.pfe.repository.TeacherRepository;
import com.saif.pfe.services.PfeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PfeServiceImpl implements PfeService {

    @Autowired
    private PfeRepository pfeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    // Create or Update
    public Pfe saveOrUpdatePfe(Pfe pfe) {
        return pfeRepository.save(pfe);
    }

    // Find by ID
    public Optional<Pfe> getPfeById(Long id) {
        return pfeRepository.findById(id);
    }

    // Find all
    public List<Pfe> getAllPfes(SearchCriteria searchCriteria, User user) {



        List<ERole> roles= user.getRoles().stream().map(Role::getName).toList();


        if (roles.contains(ERole.ROLE_ADMIN)) {
            return pfeRepository.findAll(searchCriteria.getPageable()).toList();
        }else if(roles.contains(ERole.ROLE_USER)){
            Optional<Student> student=studentRepository.findByUserId(user.getId());
            if (student.isEmpty()){
                return new ArrayList<>();
            }
            Long id=student.get().getId();
            return  pfeRepository.findAllByStudentOneIdOrStudentTwoId(id,id);
        }else if (roles.contains(ERole.ROLE_MODERATOR)){
            Optional<Teacher> teacher=teacherRepository.findByUserId(user.getId());
            if (teacher.isEmpty()){
                return new ArrayList<>();
            }
            Long id=teacher.get().getId();
            return pfeRepository.findAllBySupervisorIdOrPresidentIdOrRapporteurId(id,id,id);
        }else return new ArrayList<>();
    }

    // Delete
    public void deletePfe(Long id) {
        pfeRepository.deleteById(id);
    }

}
