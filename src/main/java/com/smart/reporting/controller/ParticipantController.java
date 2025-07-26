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

    @PostMapping("/details")
    public ParticipantDto getParticipantDetails(@RequestBody ParticipantBibDto participantBibDto) {
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
        participantDto.setRank1Cat(firstResult.getRank1cat());
        participantDto.setRank1Mix(firstResult.getRank1mix());
        participantDto.setRank1Tot(firstResult.getRank1tot());
        participantDto.setTimeStart(TimeFormatUtil.intToTimeString(firstResult.getTimestart()));
        participantDto.setTimeFinish(TimeFormatUtil.intToTimeString(firstResult.getTimefinish()));
        participantDto.setTimeGun(TimeFormatUtil.intToTimeString(firstResult.getTimegun()));
		participantDto.setTimeCP1(TimeFormatUtil.intToTimeString(firstResult.getTimecp1()));
        participantDto.setTimeCP2(TimeFormatUtil.intToTimeString(firstResult.getTimecp2()));
        participantDto.setTimeCP3(TimeFormatUtil.intToTimeString(firstResult.getTimecp3()));
        participantDto.setTimeCP4(TimeFormatUtil.intToTimeString(firstResult.getTimecp4()));
        participantDto.setTimeCP5(TimeFormatUtil.intToTimeString(firstResult.getTimecp5()));
        participantDto.setTimeCP6(TimeFormatUtil.intToTimeString(firstResult.getTimecp6()));
        participantDto.setTimeCP7(TimeFormatUtil.intToTimeString(firstResult.getTimecp7()));
        participantDto.setTimeCP8(TimeFormatUtil.intToTimeString(firstResult.getTimecp8()));
        participantDto.setTimeCP9(TimeFormatUtil.intToTimeString(firstResult.getTimecp9()));
        participantDto.setTimeCP10(TimeFormatUtil.intToTimeString(firstResult.getTimecp10()));
        participantDto.setDq(firstResult.getDq());
        participantDto.setDns(firstResult.getDns());
        participantDto.setDnf(firstResult.getDnf());
        participantDto.setNr(firstResult.getNr());
        participantDto.setFs(firstResult.getFs());
        participantDto.setNsbf(firstResult.getNsbf());
        participantDto.setRemark(firstResult.getRemark());
        participantDto.setGender(firstResult.getSex() != null ? firstResult.getSex() : "Unknown");

        return participantDto;
    }

}
