package aggregatror.aggregator.model;

import aggregatror.aggregator.vo.Vacancy;

import java.util.List;
import java.util.stream.Collectors;

public class Provider {
    private Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<Vacancy> getJavaVacancies(String searchString) {
        return strategy
                .getVacancies(searchString)
                .stream()
                .filter(vacancy -> vacancy.getTitle().toLowerCase().contains("java"))
                .collect(Collectors.toList());
    }
}