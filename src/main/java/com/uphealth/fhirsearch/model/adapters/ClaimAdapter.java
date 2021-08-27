package com.uphealth.fhirsearch.model.adapters;

import ca.uhn.fhir.parser.IParser;
import com.uphealth.fhirsearch.model.IFhirAdapter;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Claim;

import java.util.ArrayList;
import java.util.List;

public class ClaimAdapter implements IFhirAdapter {

    private final Claim delegate;

    public ClaimAdapter(Claim delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getReferencedResources() {
        List<String> refs = new ArrayList<String>();
        delegate.getRelated().forEach(c-> refs.add(c.getReference().getValue()));
        delegate.getDiagnosis().forEach(c-> refs.add(c.getDiagnosisReference().getReference()));
        delegate.getProcedure().forEach(c-> refs.add(c.getProcedureReference().getReference()));
        return refs;
    }


    @Override
    public String getSubject() {
        return delegate.getPatient().getReference();
    }
}
