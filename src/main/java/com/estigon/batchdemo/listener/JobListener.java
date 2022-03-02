package com.estigon.batchdemo.listener;

import com.estigon.batchdemo.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobExecutionListenerSupport.class);

    private JdbcTemplate jdbcTemplate;

    // inyeccion de dependencias por constructor
    public JobListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            LOG.info("FINALIZO EL JOB!!!");
            jdbcTemplate.query("SELECT first_name, last_name, cellphone FROM person",
                    (rs, rowNum) -> new Person(rs.getString(1), rs.getString(2), rs.getString(3))

            ).forEach(persona -> LOG.info("data de la persona: {}", persona));
        }
    }
}
