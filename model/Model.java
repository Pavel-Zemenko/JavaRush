package aggregatror.model;

import aggregatror.view.View;
import aggregatror.vo.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private View view;
    private Provider[] providers;

    public Model(View view, Provider[] providers) {
        if (view == null || providers == null || providers.length == 0) {
            throw new IllegalArgumentException();
        }

        for (Provider provider : providers) {
            if (provider == null) {
                throw new IllegalArgumentException();
            }
        }

        this.view = view;
        this.providers = providers;
    }

    public void selectCity(String city) {
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.getJavaVacancies(city));
        }
        view.update(vacancies);
    }
}