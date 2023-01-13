package sys.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sys.dao.TodoRepository;
import sys.exception.TodoNotFoundException;
import sys.model.TodoEntity;
import sys.todo.TodoDto;
import sys.todo.TodoStatus;
import sys.util.TodoUtil;

@Service
@AllArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private ObjectMapper mapper;

    @Override
    public List<TodoDto> getTodos(final TodoStatus status) {
	return TodoUtil.toDtos(mapper, findTodos(status));
    }

    private List<TodoEntity> findTodos(final TodoStatus status) {
	log.info(String.format("Fetching todos with search parameter %s", status));
	return status != null ? todoRepository.findAllByStatus(status) : todoRepository.findAll();
    }

    @Override
    public TodoDto update(final TodoDto todo) {
	return todoRepository.findById(todo.getId()).map(entity -> {

	    log.info(String.format("Patching entity %s with values %s", entity, todo));

	    if (todo.getStatus().equals(TodoStatus.PAST_DUE)) {
		log.error(String.format("Status change to PAST_DUE not allowed for entity %s with updated values %s",
			entity, todo));
		throw new IllegalArgumentException("Status change not allowed");
	    }

	    entity.setDescription(todo.getDescription());
	    updateStatus(entity, todo);

	    log.info(String.format("Saving updated entity %s", entity));

	    return TodoUtil.toDto(mapper, todoRepository.save(entity));
	}).orElseThrow(() -> new TodoNotFoundException("Todo not found " + todo.getId()));

    }

    private void updateStatus(final TodoEntity entity, final TodoDto todoDto) {
	if (todoDto.getStatus().equals(TodoStatus.DONE)) {
	    final LocalDateTime currentDate = getCurrentDate();
	    entity.setCompletedAt(currentDate);
	}
	if (todoDto.getStatus().equals(TodoStatus.NOT_DONE)) {
	    entity.setCompletedAt(null);
	}
	entity.setStatus(todoDto.getStatus());
    }

    @Override
    public TodoDto create(final TodoDto todo) {
	todo.setId(null);
	todo.setCompletedAt(null);
	final LocalDateTime currentDate = getCurrentDate();
	todo.setCreatedAt(currentDate);
	todo.setStatus(TodoStatus.NOT_DONE);

	final TodoEntity entity = TodoUtil.toEntity(mapper, todo);

	log.info(String.format("Adding new todo entity %s", entity));

	return TodoUtil.toDto(mapper, todoRepository.save(entity));
    }

    private LocalDateTime getCurrentDate() {
	return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public TodoDto getTodo(final Long id) {
	return todoRepository.findById(id).map(entity -> TodoUtil.toDto(mapper, entity))
		.orElseThrow(() -> new TodoNotFoundException("Todo not found"));
    }

    @Override
    public void updateStatusByDate(final LocalDateTime currentDate) {
	todoRepository.findAllByDueDateLessThanEqualAndStatus(currentDate, TodoStatus.NOT_DONE).forEach(entity -> {
	    log.info(String.format("Updating status for entity %s", entity));

	    entity.setStatus(TodoStatus.PAST_DUE);
	    todoRepository.save(entity);
	});
    }
}
