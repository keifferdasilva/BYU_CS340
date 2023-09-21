import java.util.Scanner;

public class ReplaceCommand extends Command{

    private IDocument document;
    private int replaceIndex;
    private int replaceDistance;
    private String replacementString;
    private String replacedString;

    public ReplaceCommand(IDocument document){
        this.document = document;
    }
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Start index: ");
        String replaceIndexInput = scanner.next();
        replaceIndex = validateNumberInput(replaceIndexInput);
        if (replaceIndex != -1) {
            System.out.print("Number of characters to replace: ");
            String replaceDistanceInput = scanner.next();
            replaceDistance = validateNumberInput(replaceDistanceInput);
            if (replaceDistance != -1) {
                System.out.print("Replacement string: ");
                replacementString = scanner.next();
                replacedString = document.sequence().toString().substring(replaceIndex, replaceIndex + replaceDistance);
                document.delete(replaceIndex, replaceDistance);
                document.insert(replaceIndex, replacementString);
            }
        }
    }

    @Override
    public void undo() {
        document.delete(replaceIndex, replacementString.length());
        document.insert(replaceIndex, replacedString);
    }

    @Override
    public void redo() {
        document.delete(replaceIndex, replaceDistance);
        document.insert(replaceIndex, replacementString);
    }
}
