package sys.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import sys.dao.TodoRepository;
import sys.model.TodoEntity;
import sys.todo.TodoDto;
import sys.todo.TodoStatus;
import sys.util.TestUtils;

public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    private final ObjectMapper objectMapper = TestUtils.createObjectMapper();

    private TodoServiceImpl todoService;

    @BeforeEach
    public void init() {
	MockitoAnnotations.initMocks(this);
	todoService = new TodoServiceImpl(todoRepository, objectMapper);
    }

    @Test
    public void testCreateTodo() throws Exception {

	final String description = "desc";
	final LocalDateTime dueDate = LocalDateTime.now();
	final TodoDto todoDto = TestUtils.createTodoDto(description, dueDate);
	final TodoEntity todoEntity = TestUtils.buildEntity(description, dueDate);
	Mockito.when(todoRepository.save(Mockito.any())).thenReturn(todoEntity);

	final TodoDto savedDto = todoService.create(todoDto);

	assertThat(savedDto.getDescription(), equalTo(description));
    }

    @Test
    public void testUpdateTodo() throws Exception {

	final String description = "desc";
	final LocalDateTime dueDate = LocalDateTime.now();
	final TodoDto todoDto = TestUtils.createTodoDto(description, dueDate);
	todoDto.setId(1L);
	todoDto.setDescription(description + "-new");
	todoDto.setStatus(TodoStatus.DONE);

	final TodoEntity todoEntity = TestUtils.buildEntity(description, dueDate);
	todoEntity.setId(1L);

	Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todoEntity));
	Mockito.when(todoRepository.save(Mockito.any())).thenReturn(todoEntity);

	final TodoDto savedDto = todoService.update(todoDto);

	assertThat(savedDto.getDescription(), equalTo(description + "-new"));
    }

    @Test
    public void testGetTodo() throws Exception {
	final String description = "desc";
	final LocalDateTime dueDate = LocalDateTime.now();

	final TodoEntity todoEntity = TestUtils.buildEntity(description, dueDate);
	todoEntity.setId(1L);

	Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todoEntity));

	final TodoDto dto = todoService.getTodo(1L);

	assertThat(dto.getDescription(), equalTo(todoEntity.getDescription()));
    }

    @Test
    public void testGetTodos() throws Exception {
	final String description = "desc";
	final LocalDateTime dueDate = LocalDateTime.now();

	final TodoEntity todoEntity = TestUtils.buildEntity(description, dueDate);
	todoEntity.setId(1L);

	Mockito.when(todoRepository.findAll()).thenReturn(List.of(todoEntity));

	final List<TodoDto> dtos = todoService.getTodos(null);

	assertThat(dtos.get(0).getDescription(), equalTo(todoEntity.getDescription()));
    }

    @Test
    public void testUpdateStatus() throws Exception {
	final String description = "desc";
	final LocalDateTime date = LocalDateTime.now();

	final TodoEntity todoEntity = TestUtils.buildEntity(description, date);
	todoEntity.setId(1L);

	Mockito.when(todoRepository.findAllByDueDateLessThanEqualAndStatus(date, TodoStatus.NOT_DONE))
		.thenReturn(List.of(todoEntity));

	todoService.updateStatusByDate(date);

	Mockito.verify(todoRepository).save(todoEntity);
    }

}
