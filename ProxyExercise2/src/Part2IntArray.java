import java.io.*;

public class Part2IntArray implements Part2Array2D {

    int[][] array;

    public Part2IntArray(int rows, int columns){
        array = new int[rows][columns];
    }

    public Part2IntArray(String fileName){
        load(fileName);
    }

    public void save(String fileName){
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(String fileName){

        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream iis = new ObjectInputStream(fis);
            array = (int[][]) iis.readObject();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setVal(int row, int column, int value) {
        array[row][column] = value;
    }

    @Override
    public int getVal(int row, int column) {
        return array[row][column];
    }
}
