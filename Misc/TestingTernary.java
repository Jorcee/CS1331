public class TestingTernary {
    public static void main(String[] args) {
        int myNum = Integer.parseInt(args[0]);
        System.out.println((myNum > 4) ? myNum + " is greater than 4" : myNum
                           + " is less than 4");
    }
}
