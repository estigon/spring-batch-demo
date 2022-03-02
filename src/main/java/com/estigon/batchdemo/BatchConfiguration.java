package com.estigon.batchdemo;

import com.estigon.batchdemo.listener.JobListener;
import com.estigon.batchdemo.model.Person;
import com.estigon.batchdemo.procesor.PersonItemProcesor;
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
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Person> reader() {        // ItemReader del job
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[] { "firstName", "lastName", "cellphone" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                    setTargetType(Person.class);
                }})
                .build();
    }

    @Bean
    public PersonItemProcesor processor() {      // ItemProcesor del job. Este es opcional, si no vamos a hacer algo con la data no lo necesitamos
        return new PersonItemProcesor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource){ // ItemWriter del job, pueden ser de distintos tipos en este caso es JdbcBatchItemWriter
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO person (first_name, last_name, cellphone) VALUES (:firstName, :lastName, :cellphone)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importPersonJob(JobListener listener, Step step1){
        return jobBuilderFactory.get("importPersonJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor()) // no lo colcoamos si no se va a utilizar
                .writer(writer)
                .build();
    }

}
