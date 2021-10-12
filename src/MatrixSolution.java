import java.io.FileNotFoundException;

public class MatrixSolution {
    public static void main(String[] args) {

         Matrix mat = new Matrix();
        try {
            mat.Init("src/Input.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!!!");
        }
        mat.Print();

        Results result = mat.MakeTriangle();
        mat.Print();
        GetResult(result);
        if (result == Results.MAY_BE_CONTINUED) {
            result = mat.CheckSolutions();
            if (result == Results.ONLY_SOLUTION) {
                double[] array = mat.FindSolutions();
                PrintResult(array);
            }
            else {
                GetResult(result);
            }
        }
    }

    public static void GetResult(Results result) {
        switch (result) {
            case ONLY_SOLUTION -> System.out.println("Система имеет единственное решение \n");
            case ENDLESS_SOLUTIONS -> System.out.println("Решений бесконечно много \n");
            case NO_SOLUTIONS -> System.out.println("Решений нет \n");
            case DEGENERATE_SYSTEM -> System.out.println("Система вырожденная \n");
            case MAY_BE_CONTINUED -> System.out.println("Систему можно решать дальше \n");
        }
    }

    public static void PrintResult(double[] resultArray) {
        System.out.println("Результат: ");
        for (double v : resultArray) System.out.printf("%15.6E", v);
        System.out.println();
    }

}