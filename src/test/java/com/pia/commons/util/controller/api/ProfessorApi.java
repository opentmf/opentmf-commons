package com.pia.commons.util.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
