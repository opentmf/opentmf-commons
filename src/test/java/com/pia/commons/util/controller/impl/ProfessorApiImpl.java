package com.pia.commons.util.controller.impl;

import com.pia.commons.util.controller.api.ProfessorApi;
import com.pia.commons.util.service.api.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Abdullah Beker
 */
@RestController
@RequiredArgsConstructor
public class ProfessorApiImpl implements ProfessorApi {

  private final ProfessorService professorService;

  @Override
  public Page<Map<String, Object>> get(String fields, Pageable pageable) {
    return professorService.get(fields, pageable);
  }
}
