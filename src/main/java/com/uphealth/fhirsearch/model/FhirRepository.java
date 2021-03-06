package com.uphealth.fhirsearch.model;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
public interface FhirRepository extends CrudRepository<FhirRecord,Long> {

    @Query(value="select count(r) from FhirRecord r where r.resourceId = ?1 and r.type = ?2")
    public int countByIdAndType(String id,String type);

    @Query(value = "select distinct f.type from FhirRecord f")
    public List <String> getTypes();

    @Transactional(readOnly = true)
    public List<FhirRecord> findFhirRecordByType(String type);

    @Transactional(readOnly = true)
    public List<FhirRecord> findFhirRecordByResourceId(String type);

    @Transactional(readOnly = true)
    @Query(value="select r from FhirRecord r where r.resourceId in :ids")
    @Cacheable(value = "fhir_records")
    public List<FhirRecord> findByListOfResourceIds(@Param("ids") List<String> resourceIds);
}
