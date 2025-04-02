package org.opentmf.commons.util.service.impl;

import static org.opentmf.commons.util.fieldselection.FieldSelectionUtil.fieldsToMapList;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.opentmf.commons.util.repository.ProfessorRepository;
import org.opentmf.commons.util.repository.entity.Professor;
import org.opentmf.commons.util.service.api.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abdullah Beker
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

  private final ProfessorRepository professorRepository;

  @Override
  @Transactional
  public Page<Map<String, Object>> get(String fields, Pageable pageable) {
    Page<Professor> page = professorRepository.findAll(pageable);
    return new PageImpl<>(
        fieldsToMapList(page.getContent(), fields), pageable, page.getTotalElements());
  }
}
