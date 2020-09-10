package javarush.aggregator;

import javarush.aggregator.model.HHStrategy;
import javarush.aggregator.model.Model;
import javarush.aggregator.model.MoikrugStrategy;
import javarush.aggregator.model.Provider;
import javarush.aggregator.model.Strategy;
import javarush.aggregator.view.HtmlView;
import javarush.aggregator.view.View;

public class Aggregator {
    public static void main(String[] args) {
        Strategy hhStrategy = new HHStrategy();
        Strategy moiKrugStrategy = new MoikrugStrategy();
        Provider[] providers = {
                new Provider(hhStrategy),
                new Provider(moiKrugStrategy)
        };

        View view = new HtmlView();
        Model model = new Model(view, providers);
        Controller controller = new Controller(model);

        view.setController(controller);
        ((HtmlView) view).userCitySelectEmulationMethod();
    }
}