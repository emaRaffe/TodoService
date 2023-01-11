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

}
