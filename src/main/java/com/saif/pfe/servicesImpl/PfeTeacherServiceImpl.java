package com.saif.pfe.servicesImpl;

import com.saif.pfe.models.PfeTeacher;
import com.saif.pfe.models.embeddedId.PfeTeacherId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;
import com.saif.pfe.repository.PfeTeacherRepository;
import com.saif.pfe.services.PfeTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PfeTeacherServiceImpl implements PfeTeacherService {
    private final PfeTeacherRepository pfeTeacherRepository;

    public List<PfeTeacher> findAll(SearchCriteria searchCriteria) {
        return pfeTeacherRepository.findAll(searchCriteria.getPageable()).toList();
    }

    public Optional<PfeTeacher> findById(PfeTeacherId id) {
        return pfeTeacherRepository.findById(id);
    }

    public PfeTeacher save(PfeTeacher pfeTeacher) {
        return pfeTeacherRepository.save(pfeTeacher);
    }

    public void deleteById(PfeTeacherId id) {
        pfeTeacherRepository.deleteById(id);
    }

}
