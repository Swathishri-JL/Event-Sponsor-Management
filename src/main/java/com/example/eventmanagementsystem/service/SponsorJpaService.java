/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * 
 * import java.util.*;
 *
 */

// Write your code here

package com.example.eventmanagementsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.model.Sponsor;
import com.example.eventmanagementsystem.repository.EventJpaRepository;
import com.example.eventmanagementsystem.repository.SponsorRepository;
import com.example.eventmanagementsystem.repository.SponsorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class SponsorJpaService implements SponsorRepository {

  @Autowired
  private EventJpaRepository eventJpaRepository;

  @Autowired
  private SponsorJpaRepository sponsorJpaRepository;

  @Override
  public ArrayList<Sponsor> getSponsors() {
    List<Sponsor> sponsorsList = sponsorJpaRepository.findAll();
    ArrayList<Sponsor> sponsors = new ArrayList<>(sponsorsList);
    return sponsors;
  }

  @Override
  public Sponsor getSponsorById(int sponsorId) {
    try {
      Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
      return sponsor;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public Sponsor addSponsor(Sponsor sponsor) {
    try {
      List<Integer> eventIds = new ArrayList<>();
      List<Event> events = sponsor.getEvents();

      for (Event event : events) {
        eventIds.add(event.getEventId());
      }

      List<Event> existingEvents = eventJpaRepository.findAllById(eventIds);
      if (existingEvents.size() != eventIds.size()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some events are not found");
      }

      for (Event existingEvent : existingEvents) {
        existingEvent.getSponsors().add(sponsor);
      }

      sponsor.setEvents(existingEvents);
      Sponsor savedSponsor = sponsorJpaRepository.save(sponsor);
      return savedSponsor;
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong eventId");
    }
  }

  @Override
  public Sponsor updateSponsor(int sponsorId, Sponsor sponsor) {
    try {
      Sponsor existingSponsor = sponsorJpaRepository.findById(sponsorId)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sponsor not found"));

      if (sponsor.getSponsorName() != null) {
        existingSponsor.setSponsorName(sponsor.getSponsorName());
      }
      if (sponsor.getIndustry() != null) {
        existingSponsor.setIndustry(sponsor.getIndustry());
      }

      // Update the list of events associated with the sponsor
      if (sponsor.getEvents() != null) {
        List<Event> existingEvents = existingSponsor.getEvents();
        existingEvents.forEach(event -> event.getSponsors().remove(existingSponsor));
        for (Event event : sponsor.getEvents()) {
                Event existingEvent = eventJpaRepository.findById(event.getEventId())
                                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event not found"));
                existingSponsor.getEvents().add(existingEvent);
                existingEvent.getSponsors().add(existingSponsor);
            }
      }

      // Save the updated sponsor to the database
      Sponsor updatedSponsor = sponsorJpaRepository.save(existingSponsor);

      // Return the updated sponsor
      return updatedSponsor;
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public void deleteSponsor(int sponsorId) {
    try {
      Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
      List<Event> events = sponsor.getEvents();
      for (Event event : events) {
        event.getSponsors().remove(sponsor);
      }
      eventJpaRepository.saveAll(events);
      sponsorJpaRepository.deleteById(sponsorId);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    throw new ResponseStatusException(HttpStatus.NO_CONTENT);
  }

  @Override
  public List<Event> getSponsorEvents(int sponsorId) {
    try {
      Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
      return sponsor.getEvents();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
}