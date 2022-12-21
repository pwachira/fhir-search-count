package com.uphealth.fhirsearch.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uphealth.fhirsearch.model.*;
import com.uphealth.fhirsearch.model.adapters.*;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Map.entry;

@Service
@Slf4j
public class FhirService {

    private final FhirRepository fhirRepository;
    private final FhirContext fhirContext;
    private final IParser parser;
    private  final Map<String,Function<String,IFhirAdapter>> adapterMap ;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public FhirService(FhirRepository fhirRepository, FhirContext fhirContext){
        this.fhirRepository = fhirRepository;
        this.fhirContext = fhirContext;
        this.parser = fhirContext.newJsonParser();
        this.adapterMap = Map.ofEntries(
                entry("Patient.ndjson",r -> new PatientAdapter(parser.parseResource(Patient.class,r))),
                entry("CarePlan.ndjson",r -> new CarePlanAdapter(parser.parseResource(CarePlan.class,r))),
                entry("CareTeam.ndjson",r -> new CareTeamAdapter(parser.parseResource(CareTeam.class,r))),
                entry("Condition.ndjson",r -> new ConditionAdapter(parser.parseResource(Condition.class,r))),
                entry("Claim.ndjson",r -> new ClaimAdapter(parser.parseResource(Claim.class,r))),
                entry("Observation.ndjson",r -> new ObservationAdapter(parser.parseResource(Observation.class,r))),
                entry("AllergyIntolerance.ndjson",r -> new AllergyIntoleranceAdapter(parser.parseResource(AllergyIntolerance.class,r))),
                entry("Encounter.ndjson",r -> new EncounterAdapter(parser.parseResource(Encounter.class,r)))
        );
    }

    public void filesToDb(List<String> files) throws IOException, URISyntaxException {
        List<FhirRecord> records = new ArrayList<FhirRecord>();
        for (String file : files){
            List<String> resources;
            try (InputStream inputStream = getClass().getResourceAsStream("/"+ file);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                resources = reader.lines().collect(Collectors.toList());
                //.collect(Collectors.joining(System.lineSeparator()));
            }
            //List<String> resources = Files.readAllLines(path);
            for (String resource:resources){
                String id = parser.parseResource(resource).getIdElement().toString();
                FhirRecord record = new FhirRecord().builder()
                        .resourceId(id)
                        .body(mapper.readTree(resource))
                        .type(file)
                        .build();
                records.add(record);
            }
        }
        fhirRepository.saveAll(records);
    }
/*
    Searches for
 */
    @Async
    public CompletableFuture<Void> patientCountForType(
            String patientId, String type, int searchDepth, ConcurrentMap<String,Integer> result) {
        var recordList  = fhirRepository.findFhirRecordByType(type);
        var pathExists =new HashMap<String,Integer>();
        //bfs for each
        for(var rec:recordList) {
            var queue = new LinkedList<FhirRecord>();
            var visited = new HashMap<String,Boolean>();
            var level = new HashMap<String,Integer>();
            queue.add(rec);
            visited.put(rec.getResourceId(),true);
            level.put(rec.getResourceId(),0);

            while (queue.size() > 0) {
                //break to next resource if searchdepth reached
                if(level.get(queue.peek().getResourceId()) > searchDepth) break;

                var popped = queue.remove();
                var adapter = adapterMap.get(popped.getType()).apply(popped.getBody().toString());
                if (adapter.getSubject().equals(patientId)){
                    pathExists.put(popped.getResourceId(), 1);
                } else {
                    var nextRes = adapter.getReferencedResources();
                    var notSeen = nextRes.stream()
                            .filter(r -> !visited.containsKey(r) || visited.get(r) == false)
                         .collect(Collectors.toList());
                    //only add to queue if not visited; then incr and save level; then mark visited
                      queue.addAll(fhirRepository.findByListOfResourceIds(notSeen));
                      notSeen.forEach(r->level.put(r,level.get(popped.getResourceId()) +1));
                }
            }
        }
        int count = pathExists.values().stream().reduce(0,Integer::sum);
        result.put(type, count);
        log.info(String.format("type: %s has count %d",type, count ));
        return CompletableFuture.completedFuture(null);

    }

    public List<String> getAllResourceTypes() {
        List<String> types = fhirRepository.getTypes();
        return types;
    }

    public List<FhirRecord> findFhirRecordByType(String type) {
        return fhirRepository.findFhirRecordByType(type);
    }
}
