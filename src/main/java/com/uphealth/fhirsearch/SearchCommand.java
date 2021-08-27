package com.uphealth.fhirsearch;

import com.uphealth.fhirsearch.service.FhirService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Configuration
@Profile("search")
public class SearchCommand implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCommand.class);

    private final ApplicationContext ctx;


    public SearchCommand(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run(String... args) throws Exception {
            LOGGER.info("Running  search Command");
            Map<String,Integer> result = new HashMap<>();
            var svc = ctx.getBean(FhirService.class);
            var types = svc.getAllResourceTypes();
            List<CompletableFuture<Integer>> results= new ArrayList<>();
            for (String type :types) {
                int searchDepth;
                if (args.length == 1) {searchDepth = 1;
                } else {
                    searchDepth =Integer.valueOf(args[1]); }
                int count = svc.patientCountForType("Patient/"+args[0],type,searchDepth);
                //countFuture.thenApply(c-> result.put(type,c));
                result.put(type,count);
            }
        result.forEach((k,v)->{
                LOGGER.info(String.format("%s--------------%s",k,v ));
            });
        }
}
