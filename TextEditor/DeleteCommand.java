import java.util.Scanner;

public class DeleteCommand extends Command{

    private int deletionIndex;
    private String deletedSequence;
    private IDocument document;
    private boolean deleteSuccess;

    private int deletionDistance;

    public DeleteCommand(IDocument document) {
        this.document = document;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Start index: ");
        String deletionIndexInput = scanner.next();
        deletionIndex = validateNumberInput(deletionIndexInput);
        if (deletionIndex != -1) {
            System.out.print("Number of characters to delete: ");
            String deletionDistanceInput = scanner.next();
            deletionDistance = validateNumberInput(deletionDistanceInput);
            if (deletionDistance != -1) {
                deletedSequence = document.sequence().toString().substring(deletionIndex, deletionIndex + deletionDistance);
                if (document.delete(deletionIndex, deletionDistance) == null) {
                    deleteSuccess = false;
                    System.out.println("Deletion unsuccessful");
                }
                else{
                    deleteSuccess = true;
                }
            }
        }
    }

    @Override
    public void undo() {
        if(deleteSuccess) {
            document.insert(deletionIndex, deletedSequence);
        }
    }

    @Override
    public void redo() {
        if (deleteSuccess) {
            document.delete(deletionIndex, deletionDistance);
        }
    }
}
