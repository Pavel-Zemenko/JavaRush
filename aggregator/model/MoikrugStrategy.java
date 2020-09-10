package javarush.aggregator.model;

import javarush.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";
    private static final String HOMEPAGE;

    static {
        Pattern pattern = Pattern.compile("(.*?/){3}");
        Matcher matcher = pattern.matcher(URL_FORMAT);
        if (matcher.find()) {
            String match = matcher.group();
            HOMEPAGE = match.substring(0, match.length() - 1);
        } else {
            throw new RuntimeException("Incorrect URL string");
        }
    }

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        int page = 0;

        while (true) {
            Document document;

            try {
                document = getDocument(searchString, page++);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create HTML context", e);
            }

            Elements elements = document.select(".job");

            if (elements == null || elements.size() == 0) {
                break;
            }

            for (Element e : elements) {
                String title = e.select(".title")
                        .text();
                String url = HOMEPAGE + e.select(".title")
                        .select("a")
                        .attr("href");
                String salary = e.select(".salary")
                        .select("[title=зарплата]")
                        .text();
                String city = e.select(".location")
                        .text();
                String company = e.select(".company_name")
                        .select("a")
                        .text();
                String site = "Мой круг";

                Vacancy vacancy = new Vacancy();
                vacancy.setTitle(title);
                vacancy.setUrl(url);
                vacancy.setSalary(salary);
                vacancy.setCity(city);
                vacancy.setCompanyName(company);
                vacancy.setSiteName(site);
                vacancies.add(vacancy);
            }
            break;  // ДЛЯ ТЕСТИРОВАНИЯ
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String city = URLEncoder.encode(searchString, "UTF-8");
        String url = String.format(URL_FORMAT, city, page);
        url = "https://javarush.ru/testdata/big28data2.html";  // ДЛЯ ТЕСТИРОВАНИЯ

        Document document = Jsoup.connect(url)
                .userAgent("Firefox/80.0")
                .referrer("https://www.google.ru/")
                .ignoreHttpErrors(true)
                .get();

        return document;
    }

}
