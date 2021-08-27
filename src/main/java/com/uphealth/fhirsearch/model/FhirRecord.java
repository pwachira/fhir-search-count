package com.uphealth.fhirsearch.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Builder
public class FhirRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String resourceId;
    String type;
    @Lob
    String body;

    public FhirRecord(){

    }
}
