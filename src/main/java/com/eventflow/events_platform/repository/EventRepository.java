package com.eventflow.events_platform.repository;

import com.eventflow.events_platform.model.Event;
import com.eventflow.events_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    // Ye method ek specific user ke saare events dhoondne mein madad karega
    List<Event> findByCreator(User creator);
}