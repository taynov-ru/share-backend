package ru.taynov.share.config

import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.ExecutionContext
import com.github.kagkarlsson.scheduler.task.TaskInstance
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay
import jakarta.annotation.PostConstruct
import java.time.Duration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import ru.taynov.share.scheduler.Job
import javax.sql.DataSource


@Configuration
class SchedulerConfig(
    private val dataSource: DataSource,
    private val jobs: List<Job>,
) {

    @PostConstruct
    fun recurringTask() {
        val enabledJobs = jobs.filter { job -> job.getProperties().enabled }
        val tasks = enabledJobs.map { job ->
            val prop = job.getProperties()
            val fixedDelay = FixedDelay.of(Duration.ofSeconds(prop.timeout))
            Tasks.recurring(prop.name, fixedDelay)
                .execute { _: TaskInstance<Void?>?, _: ExecutionContext? ->
                    runCatching {
                        job.execute()
                    }.onFailure { log.error("Job failed: ${job.javaClass.name}", it) }
                }
        }
        val scheduler = Scheduler.create(dataSource)
            .startTasks(tasks)
            .pollingInterval(Duration.ofSeconds(60))
            .registerShutdownHook()
            .build()

        scheduler.start()
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SchedulerConfig::class.java)
    }
}