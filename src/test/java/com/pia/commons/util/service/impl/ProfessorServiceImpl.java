package com.pia.commons.util.service.impl;

import com.pia.commons.util.repository.ProfessorRepository;
import com.pia.commons.util.repository.entity.Professor;
import com.pia.commons.util.service.api.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.pia.commons.util.FieldSelectionUtil.fieldsToMapList;

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
