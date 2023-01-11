package sys.todo;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TodoDto {

    private Long id;

    @Size(min = 2, max = 200)
    private String description;

    private TodoStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
}
