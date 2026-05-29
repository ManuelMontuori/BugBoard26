package com.bugboard.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.User;
import com.bugboard.api.observer.Observer;

@Component
public class IssueObserverComponent {

    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Issue issue, User user) {
        for (Observer observer : observers) {
            observer.update(issue, user);
        }
    }

}

