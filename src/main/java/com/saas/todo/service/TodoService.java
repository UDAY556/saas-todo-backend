package com.saas.todo.service;

import com.saas.todo.dto.TodoRequest;
import com.saas.todo.dto.TodoResponse;
import com.saas.todo.entity.Todo;
import com.saas.todo.entity.User;
import com.saas.todo.repository.TodoRepository;
import com.saas.todo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<TodoResponse> getTodos(Long userId) {
        return todoRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(TodoResponse::from)
                .toList();
    }

    public TodoResponse createTodo(Long userId, TodoRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setUser(user);

        return TodoResponse.from(todoRepository.save(todo));
    }

    public TodoResponse updateTodo(Long userId, Long todoId, TodoRequest request) {
        Todo todo = getTodoForUser(userId, todoId);
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        return TodoResponse.from(todoRepository.save(todo));
    }

    public TodoResponse toggleTodo(Long userId, Long todoId) {
        Todo todo = getTodoForUser(userId, todoId);
        todo.setCompleted(!todo.isCompleted());
        return TodoResponse.from(todoRepository.save(todo));
    }

    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = getTodoForUser(userId, todoId);
        todoRepository.delete(todo);
    }

    private Todo getTodoForUser(Long userId, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        if (!todo.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        return todo;
    }
}
