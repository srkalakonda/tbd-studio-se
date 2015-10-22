package org.talend.cloudera.navigator.api.entity;

import java.util.ArrayList;
import java.util.List;

import org.talend.cloudera.navigator.api.util.GeneratorID;

import com.cloudera.nav.sdk.model.SourceType;
import com.cloudera.nav.sdk.model.annotations.MClass;
import com.cloudera.nav.sdk.model.annotations.MRelation;
import com.cloudera.nav.sdk.model.entities.EndPointProxy;
import com.cloudera.nav.sdk.model.entities.EntityType;
import com.cloudera.nav.sdk.model.relations.RelationRole;

/*
 * Cloudera navigator entity to represent a Talend input component
 */
@MClass(model = "talend")
public class TalendInputEntity extends TalendEntity {

    private List<String> nextEntitiesId;

    @MRelation(role = RelationRole.TARGET)
    private List<EndPointProxy> targetProxies;

    public TalendInputEntity(String jobId, String componentName) {
        super(jobId, componentName);
        targetProxies = new ArrayList<EndPointProxy>();
        nextEntitiesId = new ArrayList<String>();
    }

    public void addNextEntity(String entityId) {
        this.nextEntitiesId.add(entityId);
        EndPointProxy endpointProxy = new EndPointProxy(entityId, SourceType.SDK, EntityType.OPERATION_EXECUTION);
        this.targetProxies.add(endpointProxy);
    }

    public List<String> getNextEntitiesId() {
        return nextEntitiesId;
    }

    public List<EndPointProxy> getTargetProxies() {
        return targetProxies;
    }

    @Override
    public String toString() {
        return getName() + " (" + getEntityId() + ")" + " --->" + this.nextEntitiesId;
    }

    /**
     * Connects a parent entity to its output using SOURCE -> TARGET relations
     */
    @Override
    public void connectToEntity(List<String> inputs, List<String> outputs) {
        for (String output : outputs) {
            // generate the id of the component to connect to
            String id = GeneratorID.generateNodeID(this.getJobId(), output);
            this.addNextEntity(id);
        }

    }

}
