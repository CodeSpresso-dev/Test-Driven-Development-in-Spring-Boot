package ir.mehdi.sample.springboot.tdd.repository;

import ir.mehdi.sample.springboot.tdd.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void testSaveProject() {
        //arrange
        Project project = new Project("test","test");

        //act
        Project savedProject = projectRepository.save(project);

        //assert
        assertNotNull(savedProject);
        assertEquals("test", savedProject.getDescription());
    }

    @Test
    void testFindAllProjects() {
        //arrange
        Project project1 = new Project("project 1","for customer1");
        projectRepository.save(project1);

        Project project2 = new Project("project 2","for customer2");
        projectRepository.save(project2);

        //act
        List<Project> projects = projectRepository.findAll();

        //assert
        assertEquals(2, projects.size());
    }
    @Test
    void testFindProjectById() {
        //arrange
        Project project = new Project("project 1","for customer1");
        projectRepository.save(project);

        //act
        Optional<Project> foundProject = projectRepository.findById(project.getId());

        //assert
        assertNotNull(foundProject.isPresent());
        assertEquals(project.getId(), foundProject.get().getId());
    }

    @Test
    void testUpdateProject() {
        //arrange
        Project project = new Project("project 1","started");
        projectRepository.save(project);

        //act
        project.setTitle("project 2");
        projectRepository.save(project);
        Optional<Project> foundProject = projectRepository.findById(project.getId());

        //assert
        assertFalse(foundProject.isEmpty());
        assertEquals("project 2", foundProject.get().getTitle());
    }

    @Test
    void testDeleteTask(){
        //arrange
        Project project = new Project("Test","Test for delete");
        projectRepository.save(project);

        //act
        projectRepository.delete(project);
        Optional<Project> deletedProject = projectRepository.findById(project.getId());

        //assert
        assertFalse(deletedProject.isPresent());
    }
}
