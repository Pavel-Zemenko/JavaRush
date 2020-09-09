package aggregatror.view;

import aggregatror.Controller;
import aggregatror.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;
    private final String filePath =
            "./src/" + this.getClass().getPackage().getName().replace('.', '/')
                    + "/" + "vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String content = getUpdatedFileContent(vacancies);
            updateFile(content);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Some exception occurred");
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) throws IOException {
        Document document = getDocument();
        document.select(".vacancy")
                .stream()
                .filter(e -> e.className().matches("vacancy"))
                .forEach(Node::remove);

        Element template = document.select(".template").first();

        Element vacancy = template.clone();
        vacancy.removeClass("template").removeAttr("style");

        for (Vacancy v : vacancies) {
            Element vacancyElem = vacancy.clone();

            String title = v.getTitle();
            String url = v.getUrl();
            String city = v.getCity();
            String company = v.getCompanyName();
            String salary = v.getSalary();

            vacancyElem.select(".title").first().select("a")
                    .attr("href", url);
            vacancyElem.select(".title").first().select("a")
                    .first().text(title);
            vacancyElem.select(".city").first().text(city);
            vacancyElem.select(".companyName").first().text(company);
            vacancyElem.select(".salary").first().text(salary);
            template.before(vacancyElem);
        }
        return document.html();
    }

    private void updateFile(String content) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath))) {
            writer.write(content);
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }
}