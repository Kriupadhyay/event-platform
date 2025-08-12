package com.eventflow.events_platform.repository;

import com.eventflow.events_platform.model.Event;
import com.eventflow.events_platform.model.RSVP;
import com.eventflow.events_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RSVPRepository extends JpaRepository<RSVP, Long> {
    Optional<RSVP> findByEventAndUser(Event event, User user);
    List<RSVP> findByEvent(Event event);
}