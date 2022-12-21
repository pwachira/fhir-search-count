package com.uphealth.fhirsearch;

import com.uphealth.fhirsearch.service.FhirService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Configuration
@Profile("search")
@Slf4j
public class SearchCommand implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCommand.class);

    private final ApplicationContext ctx;
    private static ConcurrentMap<String,Integer> result = new ConcurrentHashMap<>();

    private static  List<CompletableFuture<Void>> futures = new ArrayList<>();


    public SearchCommand(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Running  search Command");
        var svc = ctx.getBean(FhirService.class);
        var types = svc.getAllResourceTypes();
        for (String type :types) {
            int searchDepth;
            if (args.length == 1) {searchDepth = 1;
            } else {
                searchDepth =Integer.valueOf(args[1]); }
            var countFuture= svc.patientCountForType(
                    "Patient/"+args[0],type,searchDepth, result);
            countFuture.thenRun(()-> log.info(String.format("Completed count for type %s", type)));
            futures.add(countFuture);
        }
        var done = CompletableFuture.
                allOf(futures.toArray(new CompletableFuture[futures.size()])).join();

        result.forEach((k,v)->{
            LOGGER.info(String.format("%s--------------%s",k,v ));
        });
    }
}
