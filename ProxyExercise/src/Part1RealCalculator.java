public class Part1RealCalculator implements Part1Calculator {

    @Override
    public float add(float num1, float num2) {
        return num1 + num2;
    }

    @Override
    public float subtract(float num1, float num2) {
        return num1 - num2;
    }

    @Override
    public float multiply(float num1, float num2) {
        return num1 * num2;
    }

    @Override
    public float divide(float num1, float num2) {
        return num1 / num2;
    }
}
