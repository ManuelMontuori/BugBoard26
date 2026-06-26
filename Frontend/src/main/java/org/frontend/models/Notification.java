package org.frontend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
    private final StringProperty uuid = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();
    private final BooleanProperty read = new SimpleBooleanProperty();

    public Notification() {}

    // Getter e Property standard per JavaFX
    public String getId() { return uuid.get(); }
    public StringProperty idProperty() { return uuid; }
    public void setId(String id) { this.uuid.set(id); }

    public String getMessage() { return message.get(); }
    public StringProperty messageProperty() { return message; }
    public void setMessage(String message) { this.message.set(message); }

    public boolean isRead() { return read.get(); }
    public BooleanProperty readProperty() { return read; }
    public void setRead(boolean read) { this.read.set(read); }
}