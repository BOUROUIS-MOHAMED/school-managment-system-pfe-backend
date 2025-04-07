package com.saif.pfe.services;

import com.saif.pfe.models.PfeTeacher;
import com.saif.pfe.models.embeddedId.PfeTeacherId;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface PfeTeacherService {
    List<PfeTeacher> findAll(SearchCriteria searchCriteria);
    Optional<PfeTeacher> findById(PfeTeacherId id);
    PfeTeacher save(PfeTeacher pfeTeacher);
    void deleteById(PfeTeacherId id);
}
