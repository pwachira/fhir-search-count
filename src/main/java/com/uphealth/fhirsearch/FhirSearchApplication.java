package com.uphealth.fhirsearch;

import ca.uhn.fhir.context.FhirContext;
import com.uphealth.fhirsearch.model.IFhirAdapter;
import com.uphealth.fhirsearch.model.adapters.CareTeamAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;


@SpringBootApplication
//@EnableAsync
public class FhirSearchApplication  {

    private static final Logger logger = LoggerFactory.getLogger(FhirSearchApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FhirSearchApplication.class, args);
    }

    @Bean
    public FhirContext appFhirContext(){
        return FhirContext.forR4();
    }

/*
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(25);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(250);
        executor.initialize();
        return executor;
    }
*/
}
