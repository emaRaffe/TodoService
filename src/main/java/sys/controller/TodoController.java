package sys.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import sys.service.TodoService;
import sys.todo.TodoDto;
import sys.todo.TodoStatus;

@RestController
@AllArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @RequestMapping(name = "get todos", method = RequestMethod.GET, value = "/todos")
    public ResponseEntity<List<TodoDto>> getProducts(@RequestParam(required = false) final TodoStatus status) {
	return ResponseEntity.ok(todoService.getTodos(status));
    }

    @RequestMapping(name = "update todo", method = RequestMethod.PATCH, value = "/todos/{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody final TodoDto todo,
	    @PathVariable(value = "id") final Long id) {
	todo.setId(id);
	return ResponseEntity.ok(todoService.update(todo));
    }

    @RequestMapping(name = "save todo", method = RequestMethod.POST, value = "/todos")
    public ResponseEntity<TodoDto> createTodo(@RequestBody final TodoDto todo) {
	return ResponseEntity.ok(todoService.create(todo));
    }

    @RequestMapping(name = "get todo", method = RequestMethod.GET, value = "/todos/{id}")
    public ResponseEntity<TodoDto> getTodo(@PathVariable(value = "id") final Long id) {
	return ResponseEntity.ok(todoService.getTodo(id));
    }
}