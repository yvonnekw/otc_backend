package com.otc.backend.event;

import java.util.List;

import com.otc.backend.models.Call;
//import opticaltelephonecompany.otc.models.CurrentCall;

public class CallsCompletedEvent {
    
   private String username;
    private List<Call> completedCalls;

    public CallsCompletedEvent() {
    }

    public CallsCompletedEvent(String username, List<Call> completedCalls) {
        this.username = username;
        this.completedCalls = completedCalls;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Call> getCompletedCalls() {
        return completedCalls;
    }

    public void setCompletedCalls(List<Call> completedCalls) {
        this.completedCalls = completedCalls;
    }

    @Override
    public String toString() {
        return "CallsCompletedEvent [username=" + username + ", completedCalls=" + completedCalls + "]";
    }
 
    
}
