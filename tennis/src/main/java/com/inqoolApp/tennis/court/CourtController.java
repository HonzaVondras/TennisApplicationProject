package com.inqoolApp.tennis.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inqoolApp.tennis.GeneralRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class CourtController {
    
    @Autowired
    private final GeneralRepository<Court> courtRepository;

    @Autowired
    public CourtController(GeneralRepository<Court> courtRepository) {
        this.courtRepository = courtRepository;
    }

    @GetMapping("/getAllCourts")
    public ResponseEntity<List<Court>> getAllCourts() {
        try {
            List<Court> courtList = new ArrayList<>();
            courtRepository.findAll().forEach(courtList::add);

            if (courtList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(courtList, HttpStatus.OK);

        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCourtById/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable Long id) {
        try {

            Court courtObj = courtRepository.findById(id);

            if (courtObj == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(courtObj, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PostMapping("/addCourt")
    public ResponseEntity<Court> addCourt(@RequestBody Court court) {
    
        try {

            Court courtObj = courtRepository.save(court);
            return new ResponseEntity<>(courtObj, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @Transactional
    @PutMapping("/updateCourt/{id}")
    public ResponseEntity<Court> updateCourt(@PathVariable Long id, @RequestBody Court court) {
        try {

            Court courtData = courtRepository.findById(id);

            if(courtData == null){
               return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    
            Court updatedCourtData = courtData;
            updatedCourtData.setName(court.getName());
            updatedCourtData.setSurface(court.getSurface());
            updatedCourtData.setDeleted(court.getDeleted());
            Court courtObj = courtRepository.save(updatedCourtData);

            return new ResponseEntity<>(courtObj, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @Transactional
    @DeleteMapping("/deleteCourtById/{id}")
    public ResponseEntity<HttpStatus> deleteCourt(@PathVariable Long id) {
        try {

            Court court = courtRepository.findById(id);

            if (court == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            court.setDeleted(true);
            ResponseEntity<Court> courtResponse = this.updateCourt(id, court);

            if(courtResponse.equals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @Transactional
    @DeleteMapping("/deleteAllCourts")
    public ResponseEntity<HttpStatus> deleteAllCourts() {
        try {
            List<Court> courts = courtRepository.findAll();
            if (courts == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for(Court court : courts){
                ResponseEntity<HttpStatus> courtResponse = this.deleteCourt(court.getId());
                if(courtResponse.equals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>(HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}