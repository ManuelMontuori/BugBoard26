package org.frontend.models;

import javafx.beans.property.*;
import org.frontend.models.dtos.IssueDTO;
import java.time.LocalDateTime;


public class Issue {

    private final StringProperty uuid =
            new SimpleStringProperty();

    private final StringProperty title =
            new SimpleStringProperty();


    private final StringProperty description =
            new SimpleStringProperty();


    private final StringProperty type =
            new SimpleStringProperty();


    private final StringProperty priority =
            new SimpleStringProperty();


    private final StringProperty status =
            new SimpleStringProperty();


    private final ObjectProperty<LocalDateTime> createdAt =
            new SimpleObjectProperty<>();



    public Issue(IssueDTO dto){
        uuid.set(dto.uuid());
        title.set(dto.title());
        description.set(dto.description());
        type.set(dto.type());
        priority.set(dto.priority());
        status.set(dto.status());
        createdAt.set(dto.createdAt());
    }

    public String getUuid(){
        return uuid.get();
    }

    public StringProperty uuidProperty(){
        return uuid;
    }

    public String getTitle(){
        return title.get();
    }

    public StringProperty titleProperty(){
        return title;
    }


    public String getType(){
        return type.get();
    }

    public StringProperty typeProperty(){
        return type;
    }


    public String getPriority(){
        return priority.get();
    }

    public StringProperty priorityProperty(){
        return priority;
    }


    public String getStatus(){
        return status.get();
    }

    public StringProperty statusProperty(){
        return status;
    }

    public ObjectProperty<LocalDateTime> createdAtProperty(){
        return createdAt;
    }
}