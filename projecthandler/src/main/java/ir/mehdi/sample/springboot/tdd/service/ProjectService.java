package ir.mehdi.sample.springboot.tdd.service;

import ir.mehdi.sample.springboot.tdd.exception.ProjectNotFoundException;
import ir.mehdi.sample.springboot.tdd.model.Project;
import ir.mehdi.sample.springboot.tdd.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project updateProjectTitle(Long id, String title) {
        Project projectToUpdate = findProjectById(id);
        projectToUpdate.setTitle(title);
        return projectRepository.save(projectToUpdate);
    }

    public Project findProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project not found by id " + id));
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project projectToDelete = findProjectById(id);
        projectRepository.delete(projectToDelete);
    }
    public Project updateProject(Long id, Project projectToUpdate) {
        Project existingProject = findProjectById(id);
        existingProject.setTitle(projectToUpdate.getTitle());
        existingProject.setDescription(projectToUpdate.getDescription());
        return projectRepository.save(existingProject);
    }
}
