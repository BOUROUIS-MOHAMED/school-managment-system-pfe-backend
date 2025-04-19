package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.Pfe;
import com.saif.pfe.models.Role;
import com.saif.pfe.models.User;
import com.saif.pfe.models.ennum.ERole;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.PfeRepository;
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





        if (user.getRoles().contains(Role.ADMIN)){
            return pfeRepository.findAll(searchCriteria.getPageable()).toList();
        }else if(user.getRoles().contains(Role.USER)){
            return  pfeRepository.findAllByStudentOneIdOrStudentTwoId(user.getId(), user.getId());
        }else if (user.getRoles().contains(Role.MODERATOR)){
            return pfeRepository.findAllBySupervisorIdOrPresidentIdOrRapporteurId(user.getId(), user.getId(), user.getId());
        }else return new ArrayList<>();
    }

    // Delete
    public void deletePfe(Long id) {
        pfeRepository.deleteById(id);
    }

}
