package javarush.aggregator.model;

import javarush.aggregator.vo.Vacancy;

import java.util.List;

public interface Strategy {
    List<Vacancy> getVacancies(String searchString);
}