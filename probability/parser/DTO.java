package javarush.probability.parser;

import javarush.probability.command.Command;
import javarush.probability.entity.Dice;

import java.util.List;


public class DTO {
    private List<Dice> dices;
    private Command command;

    public DTO(List<Dice> dices, Command command) {
        this.dices = dices;
        this.command = command;
    }

    public List<Dice> getDices() {
        return dices;
    }

    public Command getCommand() {
        return command;
    }
}
