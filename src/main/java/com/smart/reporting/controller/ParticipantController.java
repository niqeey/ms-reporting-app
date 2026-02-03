package com.smart.reporting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.smart.reporting.dto.ParticipantBibDto;
import com.smart.reporting.dto.ParticipantDto;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.service.RaceResultService;
import com.smart.reporting.service.RaceService;
import com.smart.reporting.util.TimeFormatUtil;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    private RaceResultService raceResultService;

    @Autowired
    private RaceService raceService;

    @PostMapping("/details")
    public ParticipantDto getParticipantDetails(@RequestBody ParticipantBibDto participantBibDto) {
        try {
            // Fetch participant details from raceResultService
            List<TResults> results = raceResultService.getParticipantDetails(participantBibDto.getEventId(), participantBibDto.getBib());

            // Check if the results list is not empty
            if (results == null || results.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No participant details found for the given eventId and bib.");
            }

            // Take the first result and map it to ParticipantDto
            TResults firstResult = results.get(0);
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setName(firstResult.getName() != null ? firstResult.getName() : "Unknown");
        participantDto.setBib(firstResult.getBib());
        participantDto.setCategory(firstResult.getCategory());
        participantDto.setEventId(firstResult.getEventId() != null ? firstResult.getEventId() : "Unknown");
        participantDto.setCat(firstResult.getCat());
        participantDto.setRank1Cat(firstResult.getRank1cat() != null ? firstResult.getRank1cat() : 0);
        participantDto.setRank1Mix(firstResult.getRank1mix() != null ? firstResult.getRank1mix() : 0);
        participantDto.setRank1Tot(firstResult.getRank1tot() != null ? firstResult.getRank1tot() : 0);
        participantDto.setTimeStart(TimeFormatUtil.intToTimeString(firstResult.getTimestart()));
        participantDto.setTimeFinish(TimeFormatUtil.intToTimeString(firstResult.getTimefinish()));
        participantDto.setTimeGun(TimeFormatUtil.intToTimeString(firstResult.getTimegun()));
		participantDto.setTimeCP1(firstResult.getTimecp1() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp1()) : null);
        participantDto.setTimeCP2(firstResult.getTimecp2() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp2()) : null);
        participantDto.setTimeCP3(firstResult.getTimecp3() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp3()) : null);
        participantDto.setTimeCP4(firstResult.getTimecp4() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp4()) : null);
        participantDto.setTimeCP5(firstResult.getTimecp5() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp5()) : null);
        participantDto.setTimeCP6(firstResult.getTimecp6() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp6()) : null);
        participantDto.setTimeCP7(firstResult.getTimecp7() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp7()) : null);
        participantDto.setTimeCP8(firstResult.getTimecp8() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp8()) : null);
        participantDto.setTimeCP9(firstResult.getTimecp9() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp9()) : null);
        participantDto.setTimeCP10(firstResult.getTimecp10() != null ? TimeFormatUtil.intToTimeString(firstResult.getTimecp10()) : null);
        participantDto.setDq(firstResult.getDq());
        participantDto.setDns(firstResult.getDns());
        participantDto.setDnf(firstResult.getDnf());
        participantDto.setNr(firstResult.getNr());
        participantDto.setFs(firstResult.getFs());
        participantDto.setNsbf(firstResult.getNsbf());
        participantDto.setRemark(firstResult.getRemark());
        participantDto.setGender(firstResult.getSex() != null ? firstResult.getSex() : "Unknown");

        return participantDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching participant details: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public String updateParticipant(@RequestBody ParticipantDto participantDto) {
        try {
            // Find the existing participant record
            List<TResults> results = raceResultService.getParticipantDetails(participantDto.getEventId(), participantDto.getBib());
            
            if (results == null || results.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No participant found for the given eventId and bib.");
            }
            
            TResults participant = results.get(0);
            
            // Update the participant fields
            participant.setName(participantDto.getName());
            participant.setCategory(participantDto.getCategory());
            participant.setSex(participantDto.getGender());
            participant.setRemark(participantDto.getRemark());
            participant.setFs(participantDto.getFs());
            participant.setDns(participantDto.getDns());
            participant.setDnf(participantDto.getDnf());
            participant.setNsbf(participantDto.getNsbf());
            participant.setNr(participantDto.getNr());
            participant.setDq(participantDto.getDq());
            
            // Convert time strings to integers
            participant.setTimegun(TimeFormatUtil.timeStringToInt(participantDto.getTimeGun()));
            participant.setTimestart(TimeFormatUtil.timeStringToInt(participantDto.getTimeStart()));
            participant.setTimefinish(TimeFormatUtil.timeStringToInt(participantDto.getTimeFinish()));
            participant.setTimecp1(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP1()));
            participant.setTimecp2(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP2()));
            participant.setTimecp3(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP3()));
            participant.setTimecp4(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP4()));
            participant.setTimecp5(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP5()));
            participant.setTimecp6(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP6()));
            participant.setTimecp7(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP7()));
            participant.setTimecp8(TimeFormatUtil.timeStringToInt(participantDto.getTimeCP8()));
            
            // Save the updated participant
            raceService.updateParticipant(participant);
            
            return "Participant updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating participant: " + e.getMessage());
        }
    }

}
