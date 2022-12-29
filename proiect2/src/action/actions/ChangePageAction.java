package action.actions;

import action.Action;
import action.PageHandler;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;

public class ChangePageAction implements ActionStrategy {
    public ChangePageAction() {

    }

    /**
     * Change page strategy executeAction() function that triggers
     * the pageHandler to run its execute() function.
     * @param inputData
     * @param action
     * @return ObjectNode
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        PageHandler pageHandler = PageHandler.getInstance();
        pageHandler.setCurrentAction(action);
        pageHandler.execute();
        return pageHandler.getActionResult();
    }
}
