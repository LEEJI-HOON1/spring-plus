package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface TodoRepositoryCustom {
    Todo findByIdWithUser(Long id);
    Page<TodoSearchResponse> searchTodos(String keyword, LocalDateTime startDate, LocalDateTime endDate, String managerNickname);
}
