package javarush.aggregator.view;

import javarush.aggregator.Controller;
import javarush.aggregator.vo.Vacancy;

import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies);
    void setController(Controller controller);
}