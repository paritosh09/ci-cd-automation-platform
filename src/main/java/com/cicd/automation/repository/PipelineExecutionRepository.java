package com.cicd.automation.repository;

import com.cicd.automation.model.Pipeline;
import com.cicd.automation.model.PipelineExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PipelineExecutionRepository extends JpaRepository<PipelineExecution, Long> {
    Page<PipelineExecution> findByPipelineOrderByCreatedAtDesc(Pipeline pipeline, Pageable pageable);

    Optional<PipelineExecution> findTopByPipelineOrderByCreatedAtDesc(Pipeline pipeline);
}