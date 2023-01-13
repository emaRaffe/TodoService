package sys.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import sys.service.TodoService;

public class StateSchedulerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private StateScheduler stateScheduler;

    @BeforeEach
    public void init() {
	MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateTodos() throws Exception {
	stateScheduler.updateTodos();
	Mockito.verify(todoService).updateStatusByDate(Mockito.any());
    }

}
