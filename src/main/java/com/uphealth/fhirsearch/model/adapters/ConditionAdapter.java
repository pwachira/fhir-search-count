package com.uphealth.fhirsearch.model.adapters;

import ca.uhn.fhir.parser.IParser;
import com.uphealth.fhirsearch.model.IFhirAdapter;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Condition;

import java.util.ArrayList;
import java.util.List;

public class ConditionAdapter implements IFhirAdapter {

    private final Condition delegate;

    public ConditionAdapter(Condition delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getReferencedResources() {
        List<String> refs = new ArrayList<String>();
        refs.add(delegate.getEncounter().getReference());
        return refs;
    }


    @Override
    public String getSubject() {
        return delegate.getSubject().getReference();
    }
}
