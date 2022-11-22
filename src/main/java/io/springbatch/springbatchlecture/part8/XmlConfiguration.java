package io.springbatch.springbatchlecture.part8;

import com.thoughtworks.xstream.XStream;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class XmlConfiguration {

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
        return new StaxEventItemReaderBuilder<XmlCustomer>()
                .name("staxXml")
                .resource(new ClassPathResource("/customer.xml"))
                .addFragmentRootElements("customer")
                .unmarshaller(itemUnmarshaller())
                .build();
    }

    private Unmarshaller itemUnmarshaller() {

        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("customer", XmlCustomer.class);
        aliases.put("id", Long.class);
        aliases.put("name", String.class);
        aliases.put("age", Integer.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        XStream xStream = xStreamMarshaller.getXStream();
        xStream.alias("customer", XmlCustomer.class);
        xStream.allowTypes(new Class[]{
                XmlCustomer.class
        });

        xStreamMarshaller.setAliases(aliases);

        return xStreamMarshaller;
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
