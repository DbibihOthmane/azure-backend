package com.othmane.service;

import com.othmane.model.Chat;
import com.othmane.model.Project;
import com.othmane.model.User;
import com.othmane.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ChatService chatService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private User user;
    private Project inputProject;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setFullName("Othmane");
        user.setEmail("othmane@example.com");
        user.setPassword("secret");

        inputProject = new Project();
        inputProject.setName("Test Project");
        inputProject.setDescription("A test project description");
        inputProject.setCategory("Tech");
        inputProject.setTags(List.of("AI", "ML"));
    }

    @Test
    void testCreateProject_Success() throws Exception {
        // Prepare the saved project that will be returned by the repository
        Project savedProject = new Project();
        savedProject.setId(100L);
        savedProject.setName(inputProject.getName());
        savedProject.setDescription(inputProject.getDescription());
        savedProject.setCategory(inputProject.getCategory());
        savedProject.setTags(inputProject.getTags());
        savedProject.setOwner(user);
        savedProject.setTeam(new ArrayList<>(List.of(user)));

        // Stub repository and service methods
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        Chat mockChat = new Chat();
        mockChat.setId(200L);
        mockChat.setUsers(new ArrayList<>());
        when(chatService.createChat(any(Chat.class))).thenReturn(mockChat);

        // Execute the service method
        Project result = projectService.createProject(inputProject, user);

        // Assertions
        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        assertEquals(user, result.getOwner());
        assertEquals(mockChat, result.getChat());
        assertTrue(result.getTeam().contains(user));

        // Verify method calls
        verify(projectRepository, times(1)).save(any(Project.class));
        verify(chatService, times(1)).createChat(any(Chat.class));
    }


    @Test
    void testGetProjectById_Success() throws Exception {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }


    @Test
    void testGetProjectByTeam_WithCategoryAndTag() throws Exception {
        Project p1 = new Project();
        p1.setCategory("Tech");
        p1.setTags(List.of("AI", "ML"));

        Project p2 = new Project();
        p2.setCategory("Tech");
        p2.setTags(List.of("ML"));

        when(projectRepository.findByTeamContainingOrOwner(user, user)).thenReturn(List.of(p1, p2));

        List<Project> result = projectService.getProjectByTeam(user, "Tech", "ML");

        assertEquals(2, result.size());
    }

    @Test
    void testDeleteProject_Success() throws Exception {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteProject(1L, user.getId());

        verify(projectRepository, times(1)).deleteById(1L);
    }


    @Test
    void testUpdateProject_Success() throws Exception {
        Project existing = new Project();
        existing.setId(1L);

        Project updated = new Project();
        updated.setName("Updated Name");
        updated.setDescription("Updated Desc");
        updated.setTags(List.of("New"));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenReturn(existing);

        Project result = projectService.updateProject(updated, 1L);

        assertEquals("Updated Name", result.getName());
    }


    @Test
    void testAddUserToProject_Success() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setTeam(new ArrayList<>());
        Chat chat = new Chat();
        chat.setUsers(new ArrayList<>());
        project.setChat(chat);

        User newUser = new User();
        newUser.setId(2L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userService.findUserById(2L)).thenReturn(newUser);

        projectService.addUserToProject(1L, 2L);

        assertTrue(project.getTeam().contains(newUser));
        assertTrue(project.getChat().getUsers().contains(newUser));
    }


    @Test
    void testRemoveUserFromProject_Success() throws Exception {
        User u = new User();
        u.setId(2L);

        Chat chat = new Chat();
        chat.setUsers(new ArrayList<>(List.of(u)));

        Project project = new Project();
        project.setTeam(new ArrayList<>(List.of(u)));
        project.setChat(chat);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userService.findUserById(2L)).thenReturn(u);

        projectService.removeUserFromProject(1L, 2L);

        assertFalse(project.getTeam().contains(u));
        assertFalse(project.getChat().getUsers().contains(u));
    }


    @Test
    void testGetChatByProjectId_Success() throws Exception {
        Chat chat = new Chat();
        Project project = new Project();
        project.setChat(chat);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Chat result = projectService.getChatByProjectId(1L);

        assertEquals(chat, result);
    }

    @Test
    void testSearchProjects_Success() throws Exception {
        List<Project> projects = List.of(new Project(), new Project());
        when(projectRepository.findByNameContainingAndTeamContains("Test", user)).thenReturn(projects);

        List<Project> result = projectService.searchProjects("Test", user);

        assertEquals(2, result.size());
    }



}
