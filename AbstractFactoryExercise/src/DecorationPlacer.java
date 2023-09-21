public class DecorationPlacer {

    private HolidayFactory factory;

    private TableClothProvider tableclothPattern;
    private WallHangingProvider wallHanging;
    private YardOrnamentProvider yardOrnament;

    public DecorationPlacer(HolidayFactory factory) {
        this.factory = factory;
        tableclothPattern = this.factory.getTableClothProvider();
        wallHanging = this.factory.getWallHangingProvider();
        yardOrnament = this.factory.getYardOrnamentProvider();
    }

    public interface TableClothProvider{
        String getTablecloth();
    }

    public interface WallHangingProvider{
        String getHanging();
    }

    public interface YardOrnamentProvider{
        String getOrnament();
    }

    public String placeDecorations() {
        return "Everything was ready for the party. The " + yardOrnament.getOrnament()
                + " was in front of the house, the " + wallHanging.getHanging()
                + " was hanging on the wall, and the tablecloth with " + tableclothPattern.getTablecloth()
                + " was spread over the table.";
    }
}
