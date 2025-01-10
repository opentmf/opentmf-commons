package com.pia.commons.util.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author Abdullah Beker
 */
public interface ProfessorService {

  Page<Map<String, Object>> get(String fields, Pageable pageable);
}
