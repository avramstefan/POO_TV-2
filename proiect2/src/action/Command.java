package action;

public interface Command {

    /**
     * Command's execute() method, used for actually running an action.
     */
    void execute();

    /**
     * Command's undo() method, used for going backward into the chain of operations.
     */
    void undo();
}
