package com.uphealth.fhirsearch.model.adapters;

import ca.uhn.fhir.parser.IParser;
import com.uphealth.fhirsearch.model.IFhirAdapter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.Claim;

import java.util.ArrayList;
import java.util.List;

public class CareTeamAdapter implements IFhirAdapter {

    private final CareTeam delegate;

    public CareTeamAdapter(CareTeam delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getReferencedResources() {
        List<String> refs = new ArrayList<String>();
        refs.add(delegate.getEncounter().getReference());
        delegate.getParticipant().forEach(p-> refs.add(p.getMember().getReference()));
        return refs;
    }


    @Override
    public String getSubject() {
        return delegate.getSubject().getReference();
    }

}
