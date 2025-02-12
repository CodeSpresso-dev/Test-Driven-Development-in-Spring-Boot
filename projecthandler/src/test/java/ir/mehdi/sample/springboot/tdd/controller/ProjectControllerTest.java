package ir.mehdi.sample.springboot.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mehdi.sample.springboot.tdd.exception.ProjectNotFoundException;
import ir.mehdi.sample.springboot.tdd.model.Project;
import ir.mehdi.sample.springboot.tdd.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @MockBean
    ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getAllProjects() throws Exception {
        //arrange
        List<Project> projects = List.of(
                new Project(1L, "Project 1", "Description 1"),
                new Project(2L, "Project 2", "Description 2")
        );
        when(projectService.getAllProjects()).thenReturn(projects);

        //act & assert
        mockMvc.perform(get("/projects").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Project 1"))
                .andExpect(jsonPath("$[1].title").value("Project 2"));
    }
    @Test
    public void createProject() throws Exception {
        //arrange
        Project newProject = new Project("project 1", "project was created by controller layer");
        when(projectService.createProject(any(Project.class))).thenReturn(newProject);

        //act & assert
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("project 1"));
    }

    @Test
    public void createProject_InvalidInput() throws Exception {
        //arrange
        Project newProject = new Project("", "project was created by controller layer");

        //act & assert
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getProjectById() throws Exception {
        //arrange
        Project newProject = new Project(1L, "project 1", "This project was completed");
        when(projectService.findProjectById(1L)).thenReturn(newProject);

        //act & assert
        mockMvc.perform(get("/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("project 1"));
    }

    @Test
    public void getProjectById_ProjectNotFound() throws Exception {
        //arrange
        when(projectService.findProjectById(1L)).thenThrow(new ProjectNotFoundException("Project not found."));

        //act & assert
        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isNotFound());
        verify(projectService).findProjectById(1L);

    }

    @Test
    public void updateProject() throws Exception {
        //arrange
        Project updatedProject = new Project(1L, "Updated Project", "This project was updated");
        when(projectService.updateProject(eq(1L),any(Project.class))).thenReturn(updatedProject);

        //act & assert
        mockMvc.perform(put("/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Project"));
        verify(projectService).updateProject(eq(1L),any(Project.class));
    }

    @Test
    public void DeleteProject() throws Exception {
        //arrange
        doNothing().when(projectService).deleteProject(1L);

        //act & assert
        mockMvc.perform(delete("/projects/1"))
                .andExpect(status().isNoContent());
        verify(projectService).deleteProject(1L);
    }
}
