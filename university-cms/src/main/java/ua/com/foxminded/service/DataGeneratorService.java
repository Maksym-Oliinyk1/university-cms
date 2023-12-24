package ua.com.foxminded.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;

public interface DataGeneratorService {
    void generateDataIfEmpty();
}
