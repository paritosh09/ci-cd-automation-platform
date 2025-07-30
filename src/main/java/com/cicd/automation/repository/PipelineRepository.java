package com.cicd.automation.repository;

import com.cicd.automation.model.Pipeline;
import com.cicd.automation.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {

    Page<Pipeline> findByUserAndStatusNot(User user, Pipeline.PipelineStatus status, Pageable pageable);

    Optional<Pipeline> findByIdAndUserAndStatusNot(Long id, User user, Pipeline.PipelineStatus status);

    Optional<Pipeline> findByRepositoryOwnerAndRepositoryNameAndUser(String owner, String name, User user);
}
