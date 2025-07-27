package org.tasksmanager.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.tasksmanager.common.dto.ProjectDTO;
import org.tasksmanager.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/projects")
public class ProjectController {


    private final ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health(OAuth2AuthenticationToken token){
        log.info("Authenticated user: {}", token.getPrincipal().getAttributes());
        return ResponseEntity.ok("Server is running " + token.getCredentials());}

    @PostMapping
    public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + auth.getAuthorities());

        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(@PathVariable Long id, @RequestBody ProjectDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAll(page, size));
    }
}
