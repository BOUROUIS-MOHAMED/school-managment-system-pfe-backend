package com.saif.pfe.services;



import com.saif.pfe.models.Semester;
import com.saif.pfe.models.searchCriteria.SearchCriteria;

import java.util.List;

public interface SemesterService {
    Semester createSemester(Semester semester);

    Semester getSemesterById(Long id);

    List<Semester> getAllSemesters(SearchCriteria searchCriteria);

    Semester updateSemester(Long id, Semester semester);

    void deleteSemester(Long id);

}
