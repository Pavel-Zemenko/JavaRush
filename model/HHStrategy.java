package aggregatror.model;

import aggregatror.vo.Vacancy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        // Заглушка
        List<Vacancy> vacancies = new ArrayList<>();
        Vacancy[] v = new Vacancy[4];

        v[0] = new Vacancy();
        v[0].setCity("Харьков");
        v[0].setCompanyName("NIX Solutions");
        v[0].setSalary("700$");
        v[0].setTitle("Java Developer");
        v[0].setSiteName("NIX Solutions");
        v[0].setUrl("nixsolutions.com");

        v[1] = new Vacancy();
        v[1].setCity("Москва");
        v[1].setCompanyName("Яндекс");
        v[1].setSalary("1500$");
        v[1].setTitle("Java Developer");
        v[1].setSiteName("Яндекс");
        v[1].setUrl("yandex.ru");

        v[2] = new Vacancy();
        v[2].setCity("Кукуевка");
        v[2].setCompanyName("ООО Фхтагн");
        v[2].setSalary("50$");
        v[2].setTitle("C# Developer");
        v[2].setSiteName("ООО Фхтагн");
        v[2].setUrl("fhtagn.org");

        v[3] = new Vacancy();
        v[3].setCity("Берлин");
        v[3].setCompanyName("Siemens");
        v[3].setSalary("1200$");
        v[3].setTitle("Java Developer");
        v[3].setSiteName("Siemens");
        v[3].setUrl("siemens.com");

        Collections.addAll(vacancies, v);
        return vacancies;
    }
}