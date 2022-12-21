package com.uphealth.fhirsearch.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

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
    @JdbcTypeCode( SqlTypes.JSON )
    JsonNode body;
    public FhirRecord(){

    }
}
