package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;

public interface ActionStrategy {
    ObjectNode executeAction(Input inputData, Action action);
}
