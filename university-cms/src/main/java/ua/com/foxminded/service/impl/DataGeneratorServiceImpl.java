package ua.com.foxminded.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.service.DataGeneratorService;
import ua.com.foxminded.service.impl.generators.DataGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataGeneratorServiceImpl implements DataGeneratorService {

  private final List<DataGenerator> dataGenerators;

  @Autowired
  public DataGeneratorServiceImpl(List<DataGenerator> dataGenerators) {
    this.dataGenerators =
            dataGenerators.stream()
                    .sorted(Comparator.comparingInt(DataGenerator::getOrder))
                    .collect(Collectors.toList());
  }

  @Override
  @PostConstruct
  public void generateDataIfEmpty() {
    for (DataGenerator generator : dataGenerators) {
      generator.generateIfEmpty();
    }
  }
}
