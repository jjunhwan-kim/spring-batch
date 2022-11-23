package io.springbatch.springbatchlecture.part8;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@RequiredArgsConstructor
@Configuration
public class JsonConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<XmlCustomer, XmlCustomer>chunk(3)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean

    public ItemReader<? extends XmlCustomer> customItemReader() {
        return new JsonItemReaderBuilder<XmlCustomer>()
                .name("jsonReader")
                .resource(new ClassPathResource("/customer.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(XmlCustomer.class))
                .build();
    }


    @Bean
    public ItemWriter<XmlCustomer> customItemWriter() {
        return items -> {
            for (XmlCustomer item : items) {
                System.out.println(item.toString());
            }
        };
    }
}
