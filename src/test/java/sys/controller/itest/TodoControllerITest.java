package sys.controller.itest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import sys.dao.TodoRepository;
import sys.model.TodoEntity;
import sys.service.TodoService;
import sys.todo.TodoDto;
import sys.todo.TodoStatus;
import sys.util.TestUtils;
import sys.util.TodoUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerITest {
    private static final LocalDateTime DATETIME = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
	mockMvc = webAppContextSetup(applicationContext).build();
	todoRepository.deleteAll();
    }

    @Test
    public void testGetTodos() throws Exception {
	final TodoEntity entity = createTodoEntity("description", DATETIME.plusDays(2L));
	final MvcResult result = mockMvc.perform(get("/todos")).andExpect(status().isOk()).andReturn();
	final TodoDto[] todos = objectMapper.readValue(result.getResponse().getContentAsString(), TodoDto[].class);

	assertThat(Arrays.asList(todos), hasSize(1));
	assertThat(todos[0], equalTo(TodoUtil.toDto(objectMapper, entity)));
    }

    @Test
    public void testGetTodosByState() throws Exception {
	final TodoEntity entity = createTodoEntity("description", DATETIME.plusDays(2L));

	final MvcResult result = mockMvc.perform(get("/todos?status=NOT_DONE")).andExpect(status().isOk()).andReturn();
	final TodoDto[] todos = objectMapper.readValue(result.getResponse().getContentAsString(), TodoDto[].class);
	assertThat(Arrays.asList(todos), hasSize(1));
	assertThat(todos[0], equalTo(TodoUtil.toDto(objectMapper, entity)));
    }

    @Test
    public void testGetTodo() throws Exception {
	final TodoEntity entity = createTodoEntity("description", DATETIME.plusDays(2L));
	final MvcResult result = mockMvc.perform(get("/todos/" + entity.getId())).andExpect(status().isOk())
		.andReturn();
	final TodoDto todo = objectMapper.readValue(result.getResponse().getContentAsString(), TodoDto.class);

	assertThat(todo, equalTo(TodoUtil.toDto(objectMapper, entity)));
    }

    @Test
    public void testUpdateTodo() throws Exception {
	final TodoEntity entity = createTodoEntity("description", DATETIME.plusDays(2L));
	final TodoDto todoDto = TodoUtil.toDto(objectMapper, entity);
	todoDto.setDescription("new description");
	todoDto.setStatus(TodoStatus.DONE);

	final MvcResult result = mockMvc.perform(patch("/todos/" + entity.getId())
		.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(todoDto)))
		.andExpect(status().isOk()).andReturn();

	final TodoDto todo = objectMapper.readValue(result.getResponse().getContentAsString(), TodoDto.class);
	assertThat(todo.getDescription(), equalTo(todoDto.getDescription()));
	assertThat(todo.getStatus(), equalTo(todoDto.getStatus()));
	assertTrue(todo.getCompletedAt().isAfter(DATETIME));

    }

    @Test
    public void testUpdateTodoNotAllowed() throws Exception {
	final TodoEntity entity = createTodoEntity("description", DATETIME.plusDays(2L));
	final TodoDto todoDto = TodoUtil.toDto(objectMapper, entity);
	todoDto.setDescription("new description");
	todoDto.setStatus(TodoStatus.PAST_DUE);
	todoDto.setCompletedAt(DATETIME);

	mockMvc.perform(patch("/todos/" + entity.getId()).contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(todoDto))).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void testGetTodoNotExistingId() throws Exception {
	mockMvc.perform(get("/todos/999")).andExpect(status().isNotFound());
    }

    @Test
    public void testAddTodo() throws Exception {
	final TodoDto todoDto = TestUtils.createTodoDto("desc", DATETIME.plusDays(7L));
	final MvcResult result = mockMvc.perform(post("/todos/").contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(todoDto))).andExpect(status().isOk()).andReturn();

	final TodoDto todo = objectMapper.readValue(result.getResponse().getContentAsString(), TodoDto.class);

	assertThat(todo.getDueDate(), equalTo(todoDto.getDueDate()));
	assertThat(todo.getDescription(), equalTo(todoDto.getDescription()));
	assertThat(todo.getStatus(), equalTo(todoDto.getStatus()));
    }

    @Test
    public void testUpdateState() throws Exception {

	final TodoEntity entity = new TodoEntity();
	entity.setStatus(TodoStatus.NOT_DONE);
	entity.setCompletedAt(DATETIME);
	entity.setCreatedAt(DATETIME);
	entity.setDueDate(DATETIME.minusDays(1L));

	entity.setDescription("desc");
	final TodoEntity savedEntity = todoRepository.save(entity);

	todoService.updateStatusByDate(DATETIME);
    }

    private TodoEntity createTodoEntity(final String description, final LocalDateTime dueDate) {
	final TodoEntity entity = TestUtils.buildEntity(description, dueDate);
	return todoRepository.save(entity);
    }

}
