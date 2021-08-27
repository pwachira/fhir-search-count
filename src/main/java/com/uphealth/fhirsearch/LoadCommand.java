package com.uphealth.fhirsearch;

import com.uphealth.fhirsearch.service.FhirService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;


@Configuration
@Profile("load")
public class LoadCommand implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(LoadCommand.class);

    private final ApplicationContext ctx;
    private final List<String> files;

    public LoadCommand(ApplicationContext ctx, @Value("${1up.files}") List<String> files) {
        this.ctx = ctx;
        this.files = files;
    }

    @Override
    public void run(String... args) throws Exception {
            logger.info("Running initdb Command");
            FhirService svc = ctx.getBean(FhirService.class);
            svc.filesToDb(files);
        }
}
