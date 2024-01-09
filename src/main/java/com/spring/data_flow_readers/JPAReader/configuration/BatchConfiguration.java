package com.spring.data_flow_readers.JPAReader.configuration;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration<T> {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final String entityClassName;

    @Value("${entity.class.name}")
    private String beanName;
    @Autowired
    private Gson gson;

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              EntityManagerFactory entityManagerFactory,
                              @Value("${entity.class.name}") String entityClassName) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.entityClassName = entityClassName;
    }

    @Bean
    public JpaPagingItemReader<T> jpaPagingItemReader() {
        LOGGER.info("Reader called====================");

        JpaPagingItemReader<T> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT e FROM " + entityClassName + " e");
        reader.setPageSize(10);
        reader.setSaveState(false);
        return reader;
    }

    @Bean
    public ItemProcessor<T, T> itemProcessor() {
        LOGGER.info("ItemProcessor called====================");

        return item -> {
            return item;
        };
    }

    @Bean
    public ItemWriter<T> consoleItemWriter() {
        LOGGER.info("ItemWriter called====================");

        return items -> {
            LOGGER.info("items SIZE= {}", items.size());

            for (T item : items) {
                LOGGER.info("item  {}", item);
            }
        };
    }

    @Bean
    public Step myStep(JpaPagingItemReader<T> reader,
                       ItemProcessor<T, T> processor,
                       ItemWriter<T> consoleItemWriter) {
        return stepBuilderFactory.get("myStep")
                .<T, T>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(consoleItemWriter)
                .build();
    }

    @Bean
    public Job myJob(Step myStep) {
        return jobBuilderFactory.get("myJob")
                .incrementer(new RunIdIncrementer()).flow(myStep).end().build();
    }
}
