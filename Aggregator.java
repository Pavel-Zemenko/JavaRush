package aggregatror;

import aggregatror.model.HHStrategy;
import aggregatror.model.Model;
import aggregatror.model.Provider;
import aggregatror.model.Strategy;
import aggregatror.view.HtmlView;
import aggregatror.view.View;

public class Aggregator {
    public static void main(String[] args) {
        Strategy hhStrategy = new HHStrategy();
        Provider[] providers = { new Provider(hhStrategy) };

        View view = new HtmlView();
        Model model = new Model(view, providers);
        Controller controller = new Controller(model);

        view.setController(controller);
        ((HtmlView) view).userCitySelectEmulationMethod();
    }
}