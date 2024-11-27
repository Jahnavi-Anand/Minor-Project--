package com.collegeclubs.ecosystem_of_clubs.controllers;

import com.collegeclubs.ecosystem_of_clubs.model.Events;
import com.collegeclubs.ecosystem_of_clubs.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@Validated
public class EventsController {

    @Autowired
    private EventsService eventsService;

    // Get all events
    @GetMapping
    public ResponseEntity<List<Events>> getAllEvents() {
        return ResponseEntity.ok(eventsService.getAllEvents());
    }

    // Get events by club
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Events>> getEventsByClub(@PathVariable String clubId) {
        List<Events> events = eventsService.getEventsByClub(clubId);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    // Get ongoing events
    @GetMapping("/ongoing")
    public ResponseEntity<List<Events>> getOngoingEvents() {
        return ResponseEntity.ok(eventsService.getOngoingEvents(LocalDateTime.now()));
    }

    // Get event by ID
    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventById(@PathVariable String eventId) {
        return eventsService.getEventById(eventId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create or update an event
    @PostMapping
    public ResponseEntity<Events> createOrUpdateEvent(@Valid @RequestBody Events event) {
        Events savedEvent = eventsService.saveOrUpdateEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    // Delete an event by ID
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        eventsService.deleteEventById(eventId);
        return ResponseEntity.noContent().build();
    }
}
