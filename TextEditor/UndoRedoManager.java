import java.util.ArrayList;
import java.util.List;

public class UndoRedoManager{

    List<ICommand> commandStack;
    int currentVersionIndex = -1;


    public UndoRedoManager(){
        commandStack = new ArrayList<>();
    }
    public void update(ICommand command) {
        if(currentVersionIndex < commandStack.size() - 1){
            commandStack = commandStack.subList(0, currentVersionIndex + 1);
        }
        commandStack.add(command);
        command.execute();
        currentVersionIndex++;
    }

    public void undo() {
        if(canUndo()) {
            commandStack.get(currentVersionIndex).undo();
            currentVersionIndex--;
        }
        else{
            System.out.println("Cannot undo");
        }
    }

    public void redo() {
        if(canRedo()) {
            currentVersionIndex++;
            commandStack.get(currentVersionIndex).redo();
        }
        else{
            System.out.println("Cannot redo");
        }
    }

    public boolean canUndo() {
        return commandStack.size() > 0 && currentVersionIndex >= 0;
    }

    public boolean canRedo() {
        return currentVersionIndex < commandStack.size() - 1;
    }
}
