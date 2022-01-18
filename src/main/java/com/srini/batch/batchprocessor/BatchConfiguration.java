package com.srini.batch.batchprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    public static Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Bean
    public FlatFileItemReader<LowerPerson> reader(){
        logger.info("Entering into reader----------------->>>>>>>");
        return new FlatFileItemReaderBuilder<LowerPerson>()
                .name("lowerPersonItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"lowerFirstName", "lowerLastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<LowerPerson>(){{
                    setTargetType(LowerPerson.class);
                }})
                .build();
    }

    @Bean
    public PersonItemProcessor processor(){
        logger.info("Entering into processor----------------->>>>>>>");
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<LowerPerson> writer(DataSource dataSource){
        logger.info("Entering into writer----------------->>>>>>>"+dataSource);
        return new JdbcBatchItemWriterBuilder<LowerPerson>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:lowerFirstName, :lowerLastName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener jobCompletionNotificationListener, Step step1){
        logger.info("Entering into importUserJob----------------importUserJob->>>>>>>");
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<LowerPerson> writer){
        logger.info("Entering into step1-----------of-----importUserJob->>>>>>>");
        return stepBuilderFactory.get("step1")
                .<LowerPerson, LowerPerson> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
//                .faultTolerant()
//                .skipLimit(10)
//                .skip(FlatFileParseException.class)
                .build();
    }
}
