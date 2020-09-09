package aggregatror;

import aggregatror.model.Provider;
import aggregatror.vo.Vacancy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    private Provider[] providers;

    public Controller(Provider ... providers) {
        if (providers.length == 0) {
            throw new IllegalArgumentException();
        }
        this.providers = providers;
    }

    public void scan() {
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.getJavaVacancies("java"));
        }
        System.out.println(vacancies.size());
    }

    @Override
    public String toString() {
        return "Controller{" +
                "providers=" + Arrays.toString(providers) +
                '}';
    }

}