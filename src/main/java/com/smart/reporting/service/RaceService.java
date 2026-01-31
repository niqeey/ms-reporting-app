package com.smart.reporting.service;

import com.smart.reporting.dto.CsvUploadResponse;
import com.smart.reporting.dto.EventCategoryResponse;
import com.smart.reporting.dto.RaceCategoryRequest;
import com.smart.reporting.dto.RaceCategoryResponse;
import com.smart.reporting.entity.TEventCat;
import com.smart.reporting.entity.TResults;
import com.smart.reporting.repository.TEventCatRepository;
import com.smart.reporting.repository.TOrgEventRepository;
import com.smart.reporting.repository.TResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.smart.reporting.entity.TEvent;

@Service
public class RaceService {

    @Autowired
    private TEventCatRepository tEventCatRepository;

    @Autowired
    private TOrgEventRepository tOrgEventRepository;

    @Autowired
    private EventService eventService;
    
    @Autowired
    private TResultsRepository tResultsRepository;

    public List<EventCategoryResponse> getCategoriesByEventResult(String eventId) {
        List<TEventCat> categories = tEventCatRepository.findByEventIdAndIsResultGreaterThan(eventId, 0);
        System.out.println("DEBUG: getCategoriesByEvent called with eventId = " + eventId);
        TEvent event = eventService.getEventById(eventId);

        return categories.stream()
                .map(cat -> new EventCategoryResponse(
                        cat.getEventId(),
                        event != null ? event.getName() : null,
                        cat.getCatId() != null ? cat.getCatId().longValue() : null,
                        cat.getCat(),
                        cat.getCategory(),
                        cat.getDistance(),
                        cat.getGender(),
                        cat.getCplist(),
                        cat.getRacemode(),
                        cat.getTop() != null ? cat.getTop() : 0,
                        cat.getIsLive() != null ? cat.getIsLive() : 0,
                        cat.getIsResult() != null ? cat.getIsResult() : 0
                ))
                .collect(Collectors.toList());
    }

    public RaceCategoryResponse createCategory(RaceCategoryRequest req) {
        // Generate next ID
        Integer maxId = tEventCatRepository.findMaxCatId();
        Integer nextId = (maxId != null ? maxId : 0) + 1;
        
        TEventCat cat = new TEventCat();
        cat.setCatId(nextId);  // Set the ID manually
        cat.setEventId(req.getEventId());
        cat.setCat(req.getCat());
        cat.setCategory(req.getCategory()); // category field from request
        cat.setDistance(req.getDistance());
        cat.setRace(req.getRace() != null ? req.getRace() : "0"); // Use race from request or default to "0"
        cat.setGender(req.getGender());
        cat.setCplist(req.getCheckpointlist());
        cat.setRacemode(req.getRaceMode());
        cat.setTop(req.getToplist());
        cat.setIsResult(req.getIsresult());
        cat.setIsLive(req.getIslive());
        
        TEventCat saved = tEventCatRepository.save(cat);
        
        RaceCategoryResponse resp = new RaceCategoryResponse();
        resp.setCatId(saved.getCatId());
        resp.setEventId(saved.getEventId());
        resp.setCat(saved.getCat());
        resp.setCategory(saved.getCategory());
        resp.setDistance(saved.getDistance());
        resp.setGender(saved.getGender());
        return resp;
    }

    public RaceCategoryResponse updateCategory(RaceCategoryRequest req) {
        Optional<TEventCat> existingOpt = tEventCatRepository.findById(req.getCatId().longValue());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Category not found with ID: " + req.getCatId());
        }
        
        TEventCat cat = existingOpt.get();
        
        // Update fields
        if (req.getDistance() != null) {
            cat.setDistance(req.getDistance());
        }
        if (req.getGender() != null) {
            cat.setGender(req.getGender());
        }
        if (req.getRaceMode() != null) {
            cat.setRacemode(req.getRaceMode());
        }
        if (req.getCheckpointlist() != null) {
            cat.setCplist(req.getCheckpointlist());
        }
        cat.setTop(req.getToplist());
        cat.setIsResult(req.getIsresult());
        
        TEventCat saved = tEventCatRepository.save(cat);
        
        RaceCategoryResponse resp = new RaceCategoryResponse();
        resp.setCatId(saved.getCatId());
        resp.setEventId(saved.getEventId());
        resp.setCat(saved.getCat());
        resp.setCategory(saved.getCategory());
        resp.setDistance(saved.getDistance());
        resp.setGender(saved.getGender());
        return resp;
    }

    public void deleteCategory(Long id) {
        tEventCatRepository.deleteById(id);
    }
    
    public CsvUploadResponse uploadParticipantsCsv(MultipartFile file, String eventId, String cat) throws Exception {
        int totalRows = 0;
        int inserted = 0;
        int updated = 0;
        int deleted = 0;
        
        // Get category details for distance and category name
        Optional<TEventCat> categoryOpt = tEventCatRepository.findByEventIdAndCat(eventId, cat).stream().findFirst();
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found for eventId: " + eventId + ", cat: " + cat);
        }
        
        TEventCat category = categoryOpt.get();
        BigDecimal distance = category.getDistance();
        String categoryName = category.getCategory();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] columns = line.split(",");
                if (columns.length < 4) {
                    continue; // Skip invalid rows
                }
                
                totalRows++;
                
                // CSV format: pid, chipcode, bib, name, sex (optional)
                // Remove quotes and trim
                String csvPid = columns[0].trim().replace("\"", "");
                String chipCode = columns[1].trim().replace("\"", "");
                String bib = columns[2].trim().replace("\"", "");
                String name = columns[3].trim().replace("\"", "");
                String sex = columns.length > 4 ? columns[4].trim().replace("\"", "") : null; // Optional sex field
                
                // Check if this is a deletion request (pid = 0)
                boolean isDeletion = "0".equals(csvPid);
                
                // Check if participant exists (match by eventId + cat + bib)
                Optional<TResults> existingOpt = tResultsRepository.findByEventIdAndCatAndBib(eventId, cat, bib);
                
                if (existingOpt.isPresent()) {
                    if (isDeletion) {
                        // Delete existing record
                        tResultsRepository.delete(existingOpt.get());
                        deleted++;
                    } else {
                        // Update existing record
                        TResults existing = existingOpt.get();
                        existing.setChipCode(chipCode);
                        existing.setName(name);
                        existing.setCategory(categoryName);
                        existing.setDistance(distance);
                        existing.setRace((short) 0);
                        // Initialize Boolean flags
                        existing.setDns(false);
                        existing.setDnf(false);
                        existing.setDq(false);
                        existing.setFs(false);
                        existing.setNsbf(false);
                        // Initialize rank fields to 0
                        existing.setRank1cat(0);
                        existing.setRank1mix(0);
                        existing.setRank1tot(0);
                        if (sex != null && !sex.isEmpty()) {
                            existing.setSex(sex);
                        }
                        tResultsRepository.save(existing);
                        updated++;
                    }
                } else {
                    // Only insert if not a deletion request
                    if (!isDeletion) {
                        Integer maxPid = tResultsRepository.findMaxPid();
                        Integer newPid = (maxPid != null ? maxPid : 0) + 1;
                        
                        TResults newResult = new TResults();
                        newResult.setPid(newPid);
                        newResult.setEventId(eventId);
                        newResult.setCat(cat);
                        newResult.setCategory(categoryName);
                        newResult.setDistance(distance);
                        newResult.setRace((short) 0);
                        // Initialize Boolean flags
                        newResult.setDns(false);
                        newResult.setDnf(false);
                        newResult.setDq(false);
                        newResult.setFs(false);
                        newResult.setNsbf(false);
                        // Initialize rank fields to 0
                        newResult.setRank1cat(0);
                        newResult.setRank1mix(0);
                        newResult.setRank1tot(0);
                        if (sex != null && !sex.isEmpty()) {
                            newResult.setSex(sex);
                        }
                        newResult.setChipCode(chipCode);
                        newResult.setBib(bib);
                        newResult.setName(name);
                        
                        tResultsRepository.save(newResult);
                        inserted++;
                    }
                }
            }
        }
        
        String message = String.format("Successfully processed %d rows: %d inserted, %d updated, %d deleted", 
                                      totalRows, inserted, updated, deleted);
        return new CsvUploadResponse(totalRows, inserted, updated, message);
    }

    public void updateParticipant(TResults participant) {
        tResultsRepository.save(participant);
    }
}