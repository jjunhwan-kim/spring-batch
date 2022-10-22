package io.springbatch.springbatchlecture.part3;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.Date;

@RequiredArgsConstructor
//@RestController
public class JobLauncherController {

    private final Job job;
    private final JobLauncher jobLauncher;
    private final BasicBatchConfigurer basicBatchConfigurer;

    //@PostMapping("/batch")
    public String launch(/*@RequestBody*/ Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .toJobParameters();

        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher(); // 원본 객체 반환
        //SimpleJobLauncher jobLauncher = (SimpleJobLauncher) this.jobLauncher; // this.jobLauncher 는 프록시 객체이므로 타입 캐스트 불가능

        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor()); // 비동기적 방식 설정
        jobLauncher.run(job, jobParameters);

        return "batch completed";
    }
}
