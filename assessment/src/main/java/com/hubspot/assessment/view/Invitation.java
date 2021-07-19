package com.hubspot.assessment.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
public class Invitation {

    private int attendeeCount;
    private List<String> attendees;
    private String name;
    private String startDate;


}
