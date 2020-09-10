package aggregatror.aggregator.view;

import aggregatror.aggregator.Controller;
import aggregatror.aggregator.vo.Vacancy;

import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies);
    void setController(Controller controller);
}