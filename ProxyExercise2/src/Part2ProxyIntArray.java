public class Part2ProxyIntArray implements Part2Array2D {

    private Part2IntArray array;

    private String fileName;

    public Part2ProxyIntArray(String fileName){
        this.fileName = fileName;
        array = null;
    }
    @Override
    public void setVal(int row, int column, int value) {
        if(array == null){
            array = new Part2IntArray(fileName);
        }
        array.setVal(row, column, value);
    }

    @Override
    public int getVal(int row, int column) {
        if(array == null){
            array = new Part2IntArray(fileName);
        }
        return array.getVal(row, column);
    }
}
