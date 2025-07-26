package org.tasksmanager.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class AuthController {

    @GetMapping("/app/logout-success")
    public ResponseEntity<String> logoutSuccess() {
        return ResponseEntity.ok("You Signed Out");
    }
}
