package aggregatror.aggregator.model;

import aggregatror.aggregator.vo.Vacancy;

import java.util.List;

public interface Strategy {
    List<Vacancy> getVacancies(String searchString);
}