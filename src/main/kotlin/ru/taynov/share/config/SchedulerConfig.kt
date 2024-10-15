package ru.taynov.share.config

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.ExecutionContext
import com.github.kagkarlsson.scheduler.task.TaskInstance
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay
import com.github.kagkarlsson.scheduler.task.schedule.Schedule
import jakarta.annotation.PostConstruct
import java.time.Duration
import org.springframework.context.annotation.Configuration
import ru.taynov.share.scheduler.DeleteFilesJob
import javax.sql.DataSource


@Configuration
class SchedulerConfig(
    private val dataSource: DataSource,
    private val deleteFilesJob: DeleteFilesJob
) {

    @PostConstruct
    fun recurringTask() {
        val fixedDelay: Schedule = FixedDelay.of(Duration.ofMinutes(3))
        val task = Tasks.recurring("fixed-delay-task", fixedDelay)
            .execute { _: TaskInstance<Void?>?, _: ExecutionContext? ->
                deleteFilesJob.execute()
            }
        val scheduler: Scheduler =
            Scheduler.create(dataSource)
                .startTasks(task)
                .pollingInterval(Duration.ofSeconds(10))
                .registerShutdownHook()
                .build()

        scheduler.start()
    }
}