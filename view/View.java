package aggregatror.view;

import aggregatror.Controller;
import aggregatror.vo.Vacancy;

import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies);
    void setController(Controller controller);
}