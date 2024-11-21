package org.example.expert.config;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepositoryCustom;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;
  QTodo todo = QTodo.todo;
  QUser user = QUser.user;
  @Override
  public Todo findByIdWithUser(Long todoid) {
    return jpaQueryFactory
        .selectFrom(todo)
        .where(todo.id.eq(todoid))
        .leftJoin(todo.user,user)
        .fetchJoin()
        .fetchOne();
  }

  @Override
  public Page<TodoSearchResponse> searchTodos(String keyword, LocalDateTime startDate, LocalDateTime endDate, String managerNickname) {
    return null;
  }

  QManager manager = QManager.manager;
  QComment comment = QComment.comment;
  List<TodoSearchResponse> results = jpaQueryFactory.select(
          Projections.constructor(TodoSearchResponse.class,
              todo.title, // 제목
              manager.countDistinct(), // 담당자 수
              comment.countDistinct() // 댓글 수
          ))
      .from(todo)
      .leftJoin(todo.managers, manager)
      .leftJoin(todo.comments, comment)
      .where(whereClause)
      .groupBy(todo.id) // 일정별로 그룹화
      .orderBy(todo.createdAt.desc()) // 최신순 정렬
      .offset(pageable.getOffset()) // 페이징 시작
      .limit(pageable.getPageSize()) // 페이징 크기
      .fetch();

}