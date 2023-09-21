public class Main {

    public static void main(String[] args) {

        DecorationPlacer decorationPlacer = new DecorationPlacer(new ChristmasFactory());

        System.out.println(decorationPlacer.placeDecorations());

    }
}
