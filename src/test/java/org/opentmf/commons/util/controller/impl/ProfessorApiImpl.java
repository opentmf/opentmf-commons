package org.opentmf.commons.util.controller.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.opentmf.commons.util.controller.api.ProfessorApi;
import org.opentmf.commons.util.service.api.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

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
