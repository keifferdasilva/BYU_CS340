public interface HolidayFactory {

    public DecorationPlacer.TableClothProvider getTableClothProvider();
    public DecorationPlacer.WallHangingProvider getWallHangingProvider();
    public DecorationPlacer.YardOrnamentProvider getYardOrnamentProvider();
}
