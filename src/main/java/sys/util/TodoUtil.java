package sys.util;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sys.model.TodoEntity;
import sys.todo.TodoDto;

public class TodoUtil {

    public static List<TodoDto> toDtos(final ObjectMapper mapper, final List<TodoEntity> todos) {
	return mapper.convertValue(todos, new TypeReference<List<TodoDto>>() {
	});
    }

    public static TodoDto toDto(final ObjectMapper mapper, final TodoEntity entity) {
	return mapper.convertValue(entity, TodoDto.class);
    }

    public static TodoEntity toEntity(final ObjectMapper mapper, final TodoDto todo) {
	return mapper.convertValue(todo, TodoEntity.class);
    }

}
