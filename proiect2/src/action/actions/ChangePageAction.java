package action.actions;

import action.Action;
import action.PageHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;

public class ChangePageAction implements ActionStrategy {
    public ChangePageAction() {

    }

    public ObjectNode executeAction(Input inputData, Action action) {
        PageHandler pageHandler = PageHandler.getInstance(inputData);
        pageHandler.setCurrentAction(action);
        pageHandler.execute();
        return pageHandler.getActionResult();
    }
}
