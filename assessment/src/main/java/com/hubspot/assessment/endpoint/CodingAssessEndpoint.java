package com.hubspot.assessment.endpoint;


import com.hubspot.assessment.service.CodingAssessService;
import com.hubspot.assessment.view.Invitations;
import com.hubspot.assessment.view.Partners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/assess")
public class CodingAssessEndpoint {

    @Autowired
    private CodingAssessService codingAssessService;

    @Value("${partners.endpoint}")
    private String partnersUrl;

    @Value("${invitation.endpoint}")
    private String invitationUrl;

    @Value("${apikey}")
    private String apiKey;

    @GetMapping("")
    public HttpStatus postInvitations() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(partnersUrl);
        HttpHeaders headers = new HttpHeaders();

        headers.add("apikey",apiKey);
        HttpEntity requestEntity = new HttpEntity(null, headers);
        //get partners data
        ResponseEntity<Partners> response
                = restTemplate.exchange(uri, HttpMethod.GET,requestEntity, Partners.class);

        Invitations invitations = codingAssessService.processPartnersData(response.getBody());

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Invitations> request =
                new HttpEntity<>(invitations, headers);

        uri = new URI(invitationUrl);
        //post invitations data
        ResponseEntity<Invitations> invitationsResponseEntity =
                restTemplate.postForEntity(uri, request, Invitations.class);
        return invitationsResponseEntity.getStatusCode();


    }


   }



