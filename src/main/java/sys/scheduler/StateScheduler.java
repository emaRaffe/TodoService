package sys.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import sys.service.TodoService;

@Component
@AllArgsConstructor
public class StateScheduler {

    private TodoService todoService;

    @Scheduled(cron = "0 1 * * * *")
    public void updateTodos() {
	todoService.updateStatusByDate(LocalDateTime.now());
    }
}
