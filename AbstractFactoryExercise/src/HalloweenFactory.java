public class HalloweenFactory implements HolidayFactory{
    @Override
    public DecorationPlacer.TableClothProvider getTableClothProvider() {
        return new HalloweenTableclothPatternProvider();
    }

    @Override
    public DecorationPlacer.WallHangingProvider getWallHangingProvider() {
        return new HalloweenWallHangingProvider();
    }

    @Override
    public DecorationPlacer.YardOrnamentProvider getYardOrnamentProvider() {
        return new HalloweenYardOrnamentProvider();
    }
}
