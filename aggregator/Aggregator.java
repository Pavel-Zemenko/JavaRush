package aggregatror.aggregator;

import aggregatror.aggregator.model.HHStrategy;
import aggregatror.aggregator.model.Model;
import aggregatror.aggregator.model.MoikrugStrategy;
import aggregatror.aggregator.model.Provider;
import aggregatror.aggregator.model.Strategy;
import aggregatror.aggregator.view.HtmlView;
import aggregatror.aggregator.view.View;

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