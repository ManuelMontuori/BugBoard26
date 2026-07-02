package org.frontend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
    private final StringProperty uuid      = new SimpleStringProperty();
    private final StringProperty message   = new SimpleStringProperty();
    private final BooleanProperty read     = new SimpleBooleanProperty();
    private final StringProperty type      = new SimpleStringProperty();
    private final StringProperty createdAt = new SimpleStringProperty();
    private final StringProperty issueUuid = new SimpleStringProperty();

    public Notification() {}

    @JsonProperty("uuid")
    public String getId() { return uuid.get(); }
    public StringProperty idProperty() { return uuid; }
    @JsonProperty("uuid")
    public void setId(String id) { this.uuid.set(id); }

    @JsonProperty("message")
    public String getMessage() { return message.get(); }
    public StringProperty messageProperty() { return message; }
    @JsonProperty("message")
    public void setMessage(String m) { this.message.set(m); }

    @JsonProperty("read")
    public boolean isRead() { return read.get(); }
    public BooleanProperty readProperty() { return read; }
    @JsonProperty("read")
    public void setRead(boolean r) { this.read.set(r); }

    @JsonProperty("type")
    public String getType() { return type.get(); }
    public StringProperty typeProperty() { return type; }
    @JsonProperty("type")
    public void setType(String t) { this.type.set(t); }

    @JsonProperty("createdAt")
    public String getCreatedAt() { return createdAt.get(); }
    public StringProperty createdAtProperty() { return createdAt; }
    @JsonProperty("createdAt")
    public void setCreatedAt(String ca) { this.createdAt.set(ca); }

    @JsonProperty("issueUuid")
    public String getIssueUuid() { return issueUuid.get(); }
    public StringProperty issueUuidProperty() { return issueUuid; }
    @JsonProperty("issueUuid")
    public void setIssueUuid(String iu) { this.issueUuid.set(iu); }
}