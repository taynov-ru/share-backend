package ru.taynov.share.config

import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.taynov.share.scheduler.DeleteFilesJob

@Configuration
class QuartzConfig {

    @Bean
    fun jobDetail(): JobDetail {
        return JobBuilder.newJob(DeleteFilesJob::class.java)
            .withIdentity("simpleJob", "simpleJob")
            .storeDurably()
            .build()
    }

    @Bean
    fun trigger(): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail())
            .withIdentity("simpleTrigger")
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMinutes(5)
                    .repeatForever()
            )
            .build()
    }
}