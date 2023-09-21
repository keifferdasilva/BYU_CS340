public class Part2Main {
    public static void main(String[] args) {

        Part2IntArray array = new Part2IntArray(5, 4);

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 4; j++){
                array.setVal(i, j, i + j);
            }
        }
        System.out.println(array.getVal(3, 1));
        array.save("array.dat");
        System.out.println("Array saved");

        Part2Array2D newArray = new Part2ProxyIntArray("array.dat");
        System.out.println(newArray.getVal(3, 1));
        System.out.println(newArray.getVal(2, 0));

    }
}