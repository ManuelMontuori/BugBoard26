package org.frontend.models;

import javafx.beans.property.*;
import org.frontend.models.dtos.UserWorkloadDTO;

public class UserWorkload {

    private final StringProperty uuid      = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName  = new SimpleStringProperty();
    private final LongProperty   issuesCount = new SimpleLongProperty();

    public UserWorkload(UserWorkloadDTO dto) {
        uuid.set(dto.uuid());
        firstName.set(dto.firstName());
        lastName.set(dto.lastName());
        issuesCount.set(dto.issuesCount());
    }

    public String getUuid()            { return uuid.get(); }
    public String getFirstName()       { return firstName.get(); }
    public String getLastName()        { return lastName.get(); }
    public long   getIssuesCount()     { return issuesCount.get(); }
    public String getFullName()        { return firstName.get() + " " + lastName.get(); }
    public LongProperty issuesCountProperty() { return issuesCount; }
    public StringProperty fullNameProperty()  {
        return new SimpleStringProperty(getFullName());
    }
}