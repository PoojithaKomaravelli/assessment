package com.hubspot.assessment.view;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partner {

    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private List<String> availableDates;
}
