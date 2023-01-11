package sys.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import sys.dao.TodoRepository;
import sys.model.TodoEntity;
import sys.todo.TodoDto;
import sys.todo.TodoStatus;
import sys.util.TodoUtil;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private ObjectMapper mapper;

    @Override
    public List<TodoDto> getTodos(final TodoStatus status) {
	return TodoUtil.toDtos(mapper, findTodos(status));
    }

    private List<TodoEntity> findTodos(final TodoStatus status) {
	return status != null ? todoRepository.findAllByStatus(status) : todoRepository.findAll();
    }

    @Override
    public TodoDto update(final TodoDto todo) {
	return todoRepository.findById(todo.getId()).map(entity -> {

	    if (todo.getStatus().equals(TodoStatus.PAST_DUE)) {
		throw new IllegalArgumentException("Status change not allowed");
	    }

	    entity.setDescription(todo.getDescription());
	    entity.setStatus(todo.getStatus());
	    return toDto(todoRepository.save(entity));
	}).orElseThrow(RuntimeException::new);

    }

    @Override
    public TodoDto create(final TodoDto todo) {
	todo.setId(null);
	todo.setCompletedAt(null);
	todo.setCreatedAt(LocalDateTime.now());
	todo.setStatus(todo.getDueDate().isBefore(LocalDateTime.now()) ? TodoStatus.PAST_DUE : TodoStatus.NOT_DONE);

	final TodoEntity entity = toEntity(todo);
	return toDto(todoRepository.save(entity));
    }

    private TodoDto toDto(final TodoEntity entity) {
	return mapper.convertValue(entity, TodoDto.class);
    }

    private TodoEntity toEntity(final TodoDto todo) {
	return mapper.convertValue(todo, TodoEntity.class);
    }

    @Override
    public TodoDto getTodo(final Long id) {
	return todoRepository.findById(id).map(this::toDto).orElseThrow(RuntimeException::new);
    }

    @Override
    public void updateStatusByDate(final LocalDateTime currentDate) {
	todoRepository.findAllByDueDateLessThanEqual(currentDate).forEach(entity -> {
	    todoRepository.save(entity);
	});
    }
}
