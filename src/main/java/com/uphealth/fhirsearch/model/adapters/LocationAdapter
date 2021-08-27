package com.uphealth.fhirsearch.model.adapters;

import ca.uhn.fhir.parser.IParser;
import com.uphealth.fhirsearch.model.IFhirAdapter;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Encounter;

import java.util.ArrayList;
import java.util.List;

public class EncounterAdapter implements IFhirAdapter {

    private final Encounter delegate;

    public EncounterAdapter(Encounter delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getReferencedResources() {
        List<String> refs = new ArrayList<String>();
        refs.add(delegate.getServiceProvider().getReference());
        delegate.getParticipant().forEach( c -> refs.add(c.getIndividual().getReference()));
        return refs;
    }


    @Override
    public String getSubject() {
        return delegate.getSubject().getReference();
    }
}
