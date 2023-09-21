public class Main {
    public static void main(String[] args) {
        ISequence sequence = new Sequence();
        Document document = new Document(sequence);


        UndoRedoManager undoRedoManager = new UndoRedoManager();
        TextEditor editor = new TextEditor(document, undoRedoManager);

        editor.run();
    }
}
