package org.opentmf.commons.util.repository;

import org.opentmf.commons.util.repository.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Abdullah Beker
 */
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {}
