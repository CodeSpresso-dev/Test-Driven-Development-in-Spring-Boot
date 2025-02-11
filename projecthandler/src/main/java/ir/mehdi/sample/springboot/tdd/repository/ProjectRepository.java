package ir.mehdi.sample.springboot.tdd.repository;

import ir.mehdi.sample.springboot.tdd.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
