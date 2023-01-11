package sys.service;

import java.time.LocalDateTime;
import java.util.List;

import sys.todo.TodoDto;
import sys.todo.TodoStatus;

public interface TodoService {

    List<TodoDto> getTodos(TodoStatus status);

    TodoDto update(TodoDto todo);

    TodoDto create(TodoDto todo);

    TodoDto getTodo(Long id);

    void updateStatusByDate(LocalDateTime now);
}
