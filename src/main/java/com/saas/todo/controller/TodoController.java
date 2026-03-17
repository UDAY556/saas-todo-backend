package com.saas.todo.controller;

import com.saas.todo.dto.TodoRequest;
import com.saas.todo.dto.TodoResponse;
import com.saas.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    private Long getUserId(Authentication auth) {
        return (Long) auth.getPrincipal();
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos(Authentication auth) {
        return ResponseEntity.ok(todoService.getTodos(getUserId(auth)));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(Authentication auth, @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.createTodo(getUserId(auth), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(Authentication auth, @PathVariable Long id,
                                                    @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.updateTodo(getUserId(auth), id, request));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponse> toggleTodo(Authentication auth, @PathVariable Long id) {
        return ResponseEntity.ok(todoService.toggleTodo(getUserId(auth), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(Authentication auth, @PathVariable Long id) {
        todoService.deleteTodo(getUserId(auth), id);
        return ResponseEntity.noContent().build();
    }
}
