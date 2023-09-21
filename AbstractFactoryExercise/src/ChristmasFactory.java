public class ChristmasFactory implements HolidayFactory{
    @Override
    public DecorationPlacer.TableClothProvider getTableClothProvider() {
        return new ChristmasTableclothPatternProvider();
    }

    @Override
    public DecorationPlacer.WallHangingProvider getWallHangingProvider() {
        return new ChristmasWallHangingProvider();
    }

    @Override
    public DecorationPlacer.YardOrnamentProvider getYardOrnamentProvider() {
        return new ChristmasYardOrnamentProvider();
    }
}
