package sys.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sys.model.TodoEntity;
import sys.todo.TodoStatus;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    List<TodoEntity> findAllByStatus(TodoStatus status);

    List<TodoEntity> findAllByDueDateLessThanEqualAndStatus(LocalDateTime dueDate, TodoStatus status);
}
