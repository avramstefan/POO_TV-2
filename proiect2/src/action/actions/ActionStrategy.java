package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;

public interface ActionStrategy {

    /**
     * Method for triggering an action strategy to run.
     * @param inputData
     * @param action
     * @return
     */
    ObjectNode executeAction(Input inputData, Action action);
}
