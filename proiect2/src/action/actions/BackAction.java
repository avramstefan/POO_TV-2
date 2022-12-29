package action.actions;

import action.Action;
import action.PageHandler;
import action.actions.ActionStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;

public class BackAction implements ActionStrategy {

    public BackAction() {

    }

    public ObjectNode executeAction(Input inputData, Action action) {
        PageHandler pageHandler = PageHandler.getInstance(inputData);
        pageHandler.setCurrentAction(action);
        pageHandler.undo();
        return pageHandler.getActionResult();
    }
}
