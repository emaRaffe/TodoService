package sys.util;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import sys.model.TodoEntity;
import sys.todo.TodoDto;
import sys.todo.TodoStatus;

public class TestUtils {

    public final static TodoEntity buildEntity(final String description, final LocalDateTime dueDate) {
	final TodoEntity entity = new TodoEntity();
	entity.setStatus(TodoStatus.NOT_DONE);
	entity.setCompletedAt(null);
	entity.setDueDate(dueDate);
	entity.setCreatedAt(dueDate);
	entity.setDescription(description);
	return entity;
    }

    public final static TodoDto createTodoDto(final String description, final LocalDateTime dueDate) {
	return TodoDto.builder().description(description).status(TodoStatus.NOT_DONE).dueDate(dueDate).build();
    }

    public static ObjectMapper createObjectMapper() {
	final ObjectMapper mapper = new ObjectMapper();
	mapper.registerModule(new JavaTimeModule());
	return mapper;
    }
}
