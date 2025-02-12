package ir.mehdi.sample.springboot.tdd.service;

import ir.mehdi.sample.springboot.tdd.exception.ProjectNotFoundException;
import ir.mehdi.sample.springboot.tdd.model.Project;
import ir.mehdi.sample.springboot.tdd.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService;

    @Test
    void testUpdateProjectTitle() {
        //arrange
        Project project = new Project(1L, "Under Review", "running for test");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        //act
        Project updatedProject = projectService.updateProjectTitle(1L, "In Progress");

        //assert
        assertNotNull(updatedProject);
        assertEquals("In Progress", updatedProject.getTitle());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testFindProjectById_ProjectNotFound() {
        //arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(ProjectNotFoundException.class, () -> projectService.findProjectById(1L));
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void testFindProjectById() {
        //arrange
        Project project = new Project(1L, "Under Review", "running for test");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        //act
        Project foundProject = projectService.findProjectById(1L);

        //assert
        assertNotNull(foundProject);
        assertEquals(1L, foundProject.getId());
        assertEquals("Under Review", foundProject.getTitle());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    public void getAllProjects() {
        // Arrange
        Project project1 = new Project(1L, "Project 1", "Description 1");
        Project project2 = new Project(2L, "Project 2", "Description 2");
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        // Act
        List<Project> retrievedProjects = projectService.getAllProjects();

        // Assert
        assertNotNull(retrievedProjects);
        assertEquals(2, retrievedProjects.size());
        assertEquals("Project 1", retrievedProjects.get(0).getTitle());
        assertEquals("Project 2", retrievedProjects.get(1).getTitle());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void saveProject(){
        //arrange
        Project project = new Project("Project 1", "Description 1");
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        //act
        Project savedProject = projectService.createProject(project);

        //assert
        assertNotNull(savedProject);
        assertEquals("Project 1", savedProject.getTitle());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void deleteProject() {
        //arrange
        Project existingProject = new Project(1L, "Project 1", "Description 1");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));

        //act
        projectService.deleteProject(1L);

        //assert
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).delete(existingProject);
    }

    @Test
    public void deleteProject_WhenProjectNotFound() {
        //arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProject(1L));
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    public void updateProject(){
        //arrange
        Project existingProject = new Project(1L, "test Project", "Description for test");
        Project projectToUpdate = new Project("Completed", "done");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation ->
                    invocation.getArgument(0));

        //act
        Project updatedProject = projectService.updateProject(1L, projectToUpdate);

        //assert
        assertNotNull(updatedProject);
        assertEquals("Completed", updatedProject.getTitle());
        assertEquals("done", updatedProject.getDescription());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }
}
