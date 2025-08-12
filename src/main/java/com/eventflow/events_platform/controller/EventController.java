package com.eventflow.events_platform.controller;

import com.eventflow.events_platform.model.Event;
import com.eventflow.events_platform.model.RSVP;
import com.eventflow.events_platform.model.User;
import com.eventflow.events_platform.repository.EventRepository;
import com.eventflow.events_platform.repository.RSVPRepository;
import com.eventflow.events_platform.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {

    private final EventRepository eventRepository;
    private final RSVPRepository rsvpRepository;
    private final UserRepository userRepository;

    public EventController(EventRepository eventRepository, RSVPRepository rsvpRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.rsvpRepository = rsvpRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String listEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events";
    }

    @GetMapping("/events/new")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "create_event";
    }

    @PostMapping("/events/new")
    public String createEvent(@ModelAttribute Event event, @AuthenticationPrincipal UserDetails userDetails) {
        User creator = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));
        event.setCreator(creator);
        eventRepository.save(event);
        return "redirect:/";
    }

    // Naya method: Ek single event ke details dikhane ke liye
    @GetMapping("/events/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found."));
        model.addAttribute("event", event);
        model.addAttribute("rsvps", rsvpRepository.findByEvent(event));
        return "event_details";
    }

    // Naya method: RSVP submit karne ke liye
    @PostMapping("/events/{id}/rsvp")
    public String rsvpForEvent(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found."));
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Logged-in user not found."));

        if (rsvpRepository.findByEventAndUser(event, user).isEmpty()) {
            RSVP rsvp = new RSVP();
            rsvp.setEvent(event);
            rsvp.setUser(user);
            rsvpRepository.save(rsvp);
        }
        return "redirect:/events/" + id;
    }
}