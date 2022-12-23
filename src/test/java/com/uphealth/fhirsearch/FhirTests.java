package com.uphealth.fhirsearch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uphealth.fhirsearch.model.FhirRecord;
import com.uphealth.fhirsearch.model.FhirRepository;
import com.uphealth.fhirsearch.service.FhirService;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Testcontainers
@SpringBootTest
class FhirTests {
    private static final String PATIENT = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"profile\":[\"http://standardhealthrecord.org/fhir/StructureDefinition/shr-entity-Patient\"]},\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">Generated by <a href=\\\"https://github.com/synthetichealth/synthea\\\">Synthea</a>.Version identifier: v2.5.0-409-g6a2d5846\\n .   Person seed: -4618295442963808721  Population seed: 123456789</div>\"},\"extension\":[{\"url\":\"http://hl7.org/fhir/us/core/StructureDefinition/us-core-race\",\"extension\":[{\"url\":\"ombCategory\",\"valueCoding\":{\"system\":\"urn:oid:2.16.840.1.113883.6.238\",\"code\":\"2054-5\",\"display\":\"Black or African American\"}},{\"url\":\"text\",\"valueString\":\"Black or African American\"}]},{\"url\":\"http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity\",\"extension\":[{\"url\":\"ombCategory\",\"valueCoding\":{\"system\":\"urn:oid:2.16.840.1.113883.6.238\",\"code\":\"2186-5\",\"display\":\"Not Hispanic or Latino\"}},{\"url\":\"text\",\"valueString\":\"Not Hispanic or Latino\"}]},{\"url\":\"http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName\",\"valueString\":\"Zada604 Kohler843\"},{\"url\":\"http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex\",\"valueCode\":\"F\"},{\"url\":\"http://hl7.org/fhir/StructureDefinition/patient-birthPlace\",\"valueAddress\":{\"city\":\"Bellingham\",\"state\":\"Massachusetts\",\"country\":\"US\"}},{\"url\":\"http://standardhealthrecord.org/fhir/StructureDefinition/shr-actor-FictionalPerson-extension\",\"valueBoolean\":true},{\"url\":\"http://standardhealthrecord.org/fhir/StructureDefinition/shr-entity-FathersName-extension\",\"valueHumanName\":{\"text\":\"Adolph80 Russel238\"}},{\"url\":\"http://standardhealthrecord.org/fhir/StructureDefinition/shr-demographics-SocialSecurityNumber-extension\",\"valueString\":\"999-74-3015\"},{\"url\":\"http://synthetichealth.github.io/synthea/disability-adjusted-life-years\",\"valueDecimal\":1.3169175665191837},{\"url\":\"http://synthetichealth.github.io/synthea/quality-adjusted-life-years\",\"valueDecimal\":30.683082433480816}],\"identifier\":[{\"system\":\"https://github.com/synthetichealth/synthea\",\"value\":\"c4768f2a-f932-4ab6-a4a5-6e8ae0f9da8d\"},{\"type\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v2-0203\",\"code\":\"MR\",\"display\":\"Medical Record Number\"}],\"text\":\"Medical Record Number\"},\"system\":\"http://hospital.smarthealthit.org\",\"value\":\"c4768f2a-f932-4ab6-a4a5-6e8ae0f9da8d\"},{\"type\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v2-0203\",\"code\":\"SS\",\"display\":\"Social Security Number\"}],\"text\":\"Social Security Number\"},\"system\":\"http://hl7.org/fhir/sid/us-ssn\",\"value\":\"999-74-3015\"},{\"type\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v2-0203\",\"code\":\"DL\",\"display\":\"Driver's License\"}],\"text\":\"Driver's License\"},\"system\":\"urn:oid:2.16.840.1.113883.4.3.25\",\"value\":\"S99965047\"},{\"type\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v2-0203\",\"code\":\"PPN\",\"display\":\"Passport Number\"}],\"text\":\"Passport Number\"},\"system\":\"http://standardhealthrecord.org/fhir/StructureDefinition/passportNumber\",\"value\":\"X53837596X\"}],\"name\":[{\"use\":\"official\",\"family\":\"Abernathy524\",\"given\":[\"Treena759\"],\"prefix\":[\"Mrs.\"]},{\"use\":\"maiden\",\"family\":\"Russel238\",\"given\":[\"Treena759\"],\"prefix\":[\"Mrs.\"]}],\"telecom\":[{\"system\":\"phone\",\"value\":\"555-169-2844\",\"use\":\"home\"}],\"gender\":\"female\",\"birthDate\":\"1988-01-05\",\"address\":[{\"extension\":[{\"url\":\"http://hl7.org/fhir/StructureDefinition/geolocation\",\"extension\":[{\"url\":\"latitude\",\"valueDecimal\":41.75562280795965},{\"url\":\"longitude\",\"valueDecimal\":-71.24234884403893}]}],\"line\":[\"415 Davis Neck Unit 94\"],\"city\":\"Swansea\",\"state\":\"MA\",\"country\":\"US\"}],\"maritalStatus\":{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-MaritalStatus\",\"code\":\"M\",\"display\":\"M\"}],\"text\":\"M\"},\"multipleBirthBoolean\":false,\"communication\":[{\"language\":{\"coding\":[{\"system\":\"urn:ietf:bcp:47\",\"code\":\"en-US\",\"display\":\"English\"}],\"text\":\"English\"}}]}";
    private static final String CARE_PLAN = "{\"resourceType\":\"CarePlan\",\"id\":\"2\",\"meta\":{\"profile\":[\"http://hl7.org/fhir/us/core/StructureDefinition/us-core-careplan\"]},\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">Care Plan for Lifestyle education regarding hypertension.<br/>Activities: <ul><li>Lifestyle education regarding hypertension</li><li>Lifestyle education regarding hypertension</li><li>Lifestyle education regarding hypertension</li><li>Lifestyle education regarding hypertension</li></ul><br/>Care plan is meant to treat Hypertension.</div>\"},\"status\":\"active\",\"intent\":\"order\",\"category\":[{\"coding\":[{\"system\":\"http://hl7.org/fhir/us/core/CodeSystem/careplan-category\",\"code\":\"assess-plan\"}]},{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"443402002\",\"display\":\"Lifestyle education regarding hypertension\"}],\"text\":\"Lifestyle education regarding hypertension\"}],\"subject\":{\"reference\":\"Patient/c4768f2a-f932-4ab6-a4a5-6e8ae0f9da8d\"},\"encounter\":{\"reference\":\"Encounter/3\"},\"period\":{\"start\":\"2006-02-28T16:38:27-05:00\"},\"careTeam\":[{\"reference\":\"CareTeam/3440343d-b676-432c-a30b-9eb6d8bbbf15\"}],\"addresses\":[{\"reference\":\"Condition/524ea92b-74ca-4b23-94ba-aa2742410335\"}],\"activity\":[{\"detail\":{\"code\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"386463000\",\"display\":\"Prescribed activity/exercise education\"}],\"text\":\"Prescribed activity/exercise education\"},\"status\":\"in-progress\",\"location\":{\"reference\":\"Location/4c4a9ba8-8cde-4e3e-8b3b-f4220d85da84\",\"display\":\"PCP26008\"}}},{\"detail\":{\"code\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"413473000\",\"display\":\"Counseling about alcohol consumption\"}],\"text\":\"Counseling about alcohol consumption\"},\"status\":\"in-progress\",\"location\":{\"reference\":\"Location/4c4a9ba8-8cde-4e3e-8b3b-f4220d85da84\",\"display\":\"PCP26008\"}}},{\"detail\":{\"code\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"1151000175103\",\"display\":\"Dietary approaches to stop hypertension diet\"}],\"text\":\"Dietary approaches to stop hypertension diet\"},\"status\":\"in-progress\",\"location\":{\"reference\":\"Location/4c4a9ba8-8cde-4e3e-8b3b-f4220d85da84\",\"display\":\"PCP26008\"}}},{\"detail\":{\"code\":{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"225323000\",\"display\":\"Smoking cessation education\"}],\"text\":\"Smoking cessation education\"},\"status\":\"in-progress\",\"location\":{\"reference\":\"Location/4c4a9ba8-8cde-4e3e-8b3b-f4220d85da84\",\"display\":\"PCP26008\"}}}]}";
    //private static final String PATIENT_INDIRECT = "{\"resourceType\":\"Patient\",\"id\":\"1\"}";
    //private static final String CARE_PLAN_INDIRECT = "{\"resourceType\":\"CarePlan\",\"id\":\"2\",\"encounter\":{\"reference\":\"Encounter/3\"}";
    private static final String ENCOUNTER_INDIRECT = "{\"resourceType\":\"Encounter\",\"id\":\"3\",\"meta\":{\"profile\":[\"http://hl7.org/fhir/us/core/StructureDefinition/us-core-encounter\"]},\"identifier\":[{\"use\":\"official\",\"system\":\"https://github.com/synthetichealth/synthea\",\"value\":\"9dccd96a-a728-48ca-b6f1-fd4934b622c5\"}],\"status\":\"finished\",\"class\":{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-ActCode\",\"code\":\"AMB\"},\"type\":[{\"coding\":[{\"system\":\"http://snomed.info/sct\",\"code\":\"185345009\",\"display\":\"Encounter for symptom (procedure)\"}],\"text\":\"Encounter for symptom (procedure)\"}],\"subject\":{\"reference\":\"Patient/1\",\"display\":\"Mrs. Johnny786 Pollich983\"},\"participant\":[{\"type\":[{\"coding\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\",\"code\":\"PPRF\",\"display\":\"primary performer\"}],\"text\":\"primary performer\"}],\"period\":{\"start\":\"2020-03-07T19:50:14-05:00\",\"end\":\"2020-03-07T21:00:14-05:00\"},\"individual\":{\"reference\":\"Practitioner/07ebaf9b-8d6b-3b16-8148-e43e2fb76f32\",\"display\":\"Dr. Blake449 Nicolas769\"}}],\"period\":{\"start\":\"2020-03-07T19:50:14-05:00\",\"end\":\"2020-03-07T21:00:14-05:00\"},\"location\":[{\"location\":{\"reference\":\"Location/4b1a8b31-00ed-4d2d-960d-69aa2da138c9\",\"display\":\"HALLMARK HEALTH SYSTEM\"}}],\"serviceProvider\":{\"reference\":\"Organization/d692e283-0833-3201-8e55-4f868a9c0736\",\"display\":\"HALLMARK HEALTH SYSTEM\"}}";

    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest")
                                                        .withDatabaseName("test");

    @Autowired
    private FhirContext fhirContext;

    @Autowired
    FhirRepository fhirRepository;

    @Autowired
    private FhirService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void setup(){
        //container.withReuse(true);
        container.start();
    }

    @BeforeEach
    void init(){
        fhirRepository.deleteAll();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",container::getJdbcUrl);
        registry.add("spring.datasource.username",container::getUsername);
        registry.add("spring.datasource.password",container::getPassword);
        registry.add("spring.datasource.driver-class-name",container::getDriverClassName);

    }
    @Test
    void converts_to_fhir_resource() {
        IParser parser = fhirContext.newJsonParser();
        Patient patient = parser.parseResource(Patient.class, PATIENT);
        assertThat(patient.getId(),is("Patient/1") );
    }

    @Test
    void reads_resource_to_database(@Value("${1up.files}") List<String> files)
            throws IOException, URISyntaxException {
        service.filesToDb(files);
        assertThat(fhirRepository.count(), is(4L));
    }


    @Test
    void can_search_references() throws JsonProcessingException, ExecutionException, InterruptedException {
        fhirRepository.save(new FhirRecord().builder().resourceId("CarePlan/2").type("CarePlan.ndjson")
                .body(mapper.readTree(CARE_PLAN)).build());
        fhirRepository.save(new FhirRecord().builder().resourceId("Patient/1").type("Patient.ndjson")
                .body(mapper.readTree(PATIENT)).build());
        fhirRepository.save(new FhirRecord().builder().resourceId("Encounter/3").type("Encounter.ndjson")
                .body(mapper.readTree(ENCOUNTER_INDIRECT)).build());

        ConcurrentMap<String,Integer> result = new ConcurrentHashMap<>();
        service.patientCountForType(
                "Patient/1","CarePlan.ndjson",0, result).get();
        assertThat(result.get("CarePlan.ndjson"),is(0));
        service.patientCountForType(
                "Patient/1","CarePlan.ndjson",1,result).get();
        assertThat(result.get("CarePlan.ndjson"),is(1));
    }

}
