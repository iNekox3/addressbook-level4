package seedu.progresschecker.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.progresschecker.commons.core.ComponentManager;
import seedu.progresschecker.commons.core.LogsCenter;
import seedu.progresschecker.logic.commands.Command;
import seedu.progresschecker.logic.commands.CommandResult;
import seedu.progresschecker.logic.commands.exceptions.CommandException;
import seedu.progresschecker.logic.parser.ProgressCheckerParser;
import seedu.progresschecker.logic.parser.exceptions.ParseException;
import seedu.progresschecker.model.Model;
import seedu.progresschecker.model.person.Person;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final ProgressCheckerParser progressCheckerParser;
    private final UndoRedoStack undoRedoStack;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        progressCheckerParser = new ProgressCheckerParser();
        undoRedoStack = new UndoRedoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = progressCheckerParser.parseCommand(commandText);
            command.setData(model, history, undoRedoStack);
            CommandResult result = command.execute();
            undoRedoStack.push(command);
            return result;
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}