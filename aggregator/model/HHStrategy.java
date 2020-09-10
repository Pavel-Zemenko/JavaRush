package aggregatror.aggregator.model;

import aggregatror.aggregator.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";
    private String attrName = "data-qa";
    private String attrValue = "vacancy-serp__vacancy";

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

            Elements elements = document.getElementsByAttributeValue(attrName, attrValue);

            if (elements == null || elements.size() == 0) {
                break;
            }

            for (Element element : elements) {
                Element e = element.select(getQuery("-title")).first();
                String url = e.attr("href");
                String title = getTextValue(element, "-title");
                String salary = getTextValue(element, "-compensation");
                String city = getTextValue(element, "-address");
                String company = getTextValue(element, "-employer");
                String site = "HeadHunter";

                Vacancy vacancy = new Vacancy();
                vacancy.setTitle(title);
                vacancy.setUrl(url);
                vacancy.setSalary(salary);
                vacancy.setCity(city);
                vacancy.setCompanyName(company);
                vacancy.setSiteName(site);
                vacancies.add(vacancy);
            }
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String city = URLEncoder.encode(searchString, "UTF-8");
        String url = String.format(URL_FORMAT, city, page);

        Document document = Jsoup.connect(url)
                .userAgent("Firefox/80.0")
                .referrer("https://www.google.ru/")
                .ignoreHttpErrors(true)
                .get();

        return document;
    }

    private String getTextValue(Element element, String suffix) {
        String query = getQuery(suffix);
        Elements result = element.select(query);
        return result.size() > 0 ? result.first().ownText() : "";
    }

    private String getQuery(String suffix) {
        return String.format("[%s=%s%s]", attrName, attrValue, suffix);
    }

}