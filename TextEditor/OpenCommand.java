import java.util.Scanner;

public class OpenCommand extends Command{

    IDocument document;
    String oldSequence;
    String openFileName;

    public OpenCommand(IDocument document) {
        this.document = document;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Name of file to open: ");
        openFileName = scanner.next();
        oldSequence = document.sequence().toString();
        document.open(openFileName);
    }

    @Override
    public void undo() {
        document.clear();
        document.insert(0, oldSequence);
    }

    @Override
    public void redo() {
        document.open(openFileName);
    }
}
