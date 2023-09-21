public class ClearCommand extends Command{

    IDocument document;
    String oldSequence;

    ClearCommand(IDocument document){
        this.document = document;
    }
    @Override
    public void execute() {
        oldSequence = document.sequence().toString();
        document.clear();
    }

    @Override
    public void undo() {
        document.insert(0, oldSequence);
    }

    @Override
    public void redo() {
        execute();
    }
}
