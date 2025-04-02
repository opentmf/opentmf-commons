package org.opentmf.commons.util.service.api;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Abdullah Beker
 */
public interface ProfessorService {

  Page<Map<String, Object>> get(String fields, Pageable pageable);
}
