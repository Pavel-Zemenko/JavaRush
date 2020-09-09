package aggregatror.view;

import aggregatror.Controller;
import aggregatror.vo.Vacancy;

import java.util.List;

public class HtmlView implements View {
    private Controller controller;
    private final String filePath =
            "./" + this.getClass().getPackage().getName().replace('.', '/')
                    + "/" + "vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String content = getUpdatedFileContent(vacancies);
            updateFile(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        return "";
    }

    private void updateFile(String content) {

    }
}