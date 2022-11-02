public class Main {
    public static void main(String[] args) {
        Calculation calculate = new Calculation();

        try {
            calculate.calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}