package org.opentmf.commons.util.controller.api;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Abdullah Beker
 */
@RequestMapping("/professor")
public interface ProfessorApi {

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Page<Map<String, Object>> get(
      @RequestParam(name = "fields", required = false) String fields, @PageableDefault(size = 5) Pageable pageable);
}
