package com.hubspot.assessment.service;

import com.hubspot.assessment.view.Invitation;
import com.hubspot.assessment.view.Invitations;
import com.hubspot.assessment.view.Partner;
import com.hubspot.assessment.view.Partners;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CodingAssessService {

    public Invitations processPartnersData(Partners partners){
        List filteredPartnersByDate = filterPartnersByAvailableDates(partners);
        Map<String,Partners> partnersByCountries = getPartnersByCountries(filteredPartnersByDate);
        Invitations invitations = new Invitations();
        invitations.setCountries(findAppropriateStartDate(partnersByCountries));
        return invitations;
    }

    private List<Invitation> findAppropriateStartDate(Map<String, Partners> partnersMap) {
        List<Invitation> invitations = new ArrayList<>();
            partnersMap.forEach((Key,Value) -> {
                Map<String,List<Partner>> partnersWithAppropriateDateMap = new HashMap<>();
                Value.getPartners().stream()
                        .forEach(partner -> {
                            partner.getAvailableDates().stream()
                                    .forEach(date -> {
                                        if(partnersWithAppropriateDateMap.containsKey(date)){
                                            partnersWithAppropriateDateMap.get(date).add(partner);
                                        }else{
                                            partnersWithAppropriateDateMap.put(date,new ArrayList<>(Arrays.asList(partner)));
                                        }

                                    });

                        });
                if(partnersWithAppropriateDateMap.isEmpty()){
                    invitations.add(new Invitation(0,new ArrayList<>(),Key,null));
                }
                List<Map.Entry<String, List<Partner>> > listToSort =
                        new LinkedList<>(partnersWithAppropriateDateMap.entrySet());

                listToSort.sort((o1, o2) -> {
                    try {
                        return compareBySizeAndThenDate(o1, o2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });

                if(listToSort.size() >0) {

                    invitations.add(getInvitation(Key,listToSort.get(0)));

                }});

        return invitations;

    }

    private Invitation getInvitation(String country, Map.Entry<String,List<Partner>> id ){
        Invitation invitation =  new Invitation();
        invitation.setAttendeeCount(id.getValue().size());
        invitation.setName(country);
        invitation.setAttendees(id.getValue().stream().map(Partner::getEmail).collect(Collectors.toList()));
        invitation.setStartDate(id.getKey());
        return invitation;
    }


    public List filterPartnersByAvailableDates(Partners partners){
        return partners.getPartners().stream().
                map(partner -> {
                    List<String> availableDates = filterForConsecutiveDates(partner);
                    partner.setAvailableDates(availableDates);
                    return partner;
                }).collect(Collectors.toList());

    }



    private List<String> filterForConsecutiveDates(Partner partner){
        List<String> dates = partner.getAvailableDates();
        List<String> possibleStartDates = new ArrayList<>();
        for(String s: dates) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(s, formatter);
            if(dates.contains(date.plusDays(1L).format(formatter))){
                possibleStartDates.add(s);
            }
        }
        return possibleStartDates;

    }

    private Map getPartnersByCountries(List<Partner> partners){
        Map<String,Partners> partnersMap = new HashMap<>();
        partners
                .stream()
                .forEach(partner -> {
                    if(partnersMap.containsKey(partner.getCountry())){
                        partnersMap.get(partner.getCountry()).
                                getPartners().add(partner);
                    }else{
                        partnersMap.put(partner.getCountry(),
                                new Partners(new ArrayList<>(Arrays.asList(partner))));
                    }
                });

        return partnersMap;

    }

    private static int compareBySizeAndThenDate(Map.Entry<String, List<Partner>> o1,
                                                Map.Entry<String, List<Partner>> o2) throws ParseException {
        if ((o1.getValue().size()) - o2.getValue().size() == 0){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = format.parse(o1.getKey());
            Date date2 = format.parse(o2.getKey());

            return date1.compareTo(date2) ;
        }else{
            return (o2.getValue().size()) - o1.getValue().size();
        }

    }

}
