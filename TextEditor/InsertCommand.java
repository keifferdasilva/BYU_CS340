import java.util.Scanner;

public class InsertCommand extends Command {

    private int insertionIndex;
    private String sequenceInput;
    private IDocument document;

    public InsertCommand(IDocument document){
        this.document = document;
    }
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Start index: ");
        String insertionIndexInput = scanner.next();
        insertionIndex = validateNumberInput(insertionIndexInput);
        if (insertionIndex != -1) {
            System.out.print("Sequence to insert: ");
            sequenceInput = scanner.next();
            document.insert(insertionIndex, sequenceInput);
        }
    }

    @Override
    public void undo() {
        if(insertionIndex != -1){
            document.delete(insertionIndex, sequenceInput.length());
        }
    }

    @Override
    public void redo() {
        document.insert(insertionIndex, sequenceInput);
    }
}
