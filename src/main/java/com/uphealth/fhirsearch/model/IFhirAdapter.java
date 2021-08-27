package com.uphealth.fhirsearch.model;


import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public interface IFhirAdapter {
    List<String> getReferencedResources();
    String getSubject();
}
