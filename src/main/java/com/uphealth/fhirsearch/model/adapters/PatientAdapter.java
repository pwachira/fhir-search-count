package com.uphealth.fhirsearch.model.adapters;

import ca.uhn.fhir.parser.IParser;
import com.uphealth.fhirsearch.model.IFhirAdapter;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter implements IFhirAdapter {

    private final Patient delegate;

    public PatientAdapter(Patient delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getReferencedResources() {
        return new ArrayList<String>();
    }


    @Override
    public String getSubject() {
        return delegate.getId();
    }
}
