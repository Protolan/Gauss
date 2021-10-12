import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Matrix {
    private double[][] matrixArray;
    private int rowAmount, columnAmount;
    private double epsilon;
    private static final int NotFounded = -1;

    public void Print() {
        for (int i = 0; i < rowAmount; i++) {
            for (int j = 0; j < columnAmount; j++)
                System.out.printf("%15.6E", matrixArray[i][j]);
            System.out.println();
        }
        System.out.println();
    }

    public void Init(String s) throws FileNotFoundException {
        File file = new File(s);
        Scanner scan = new Scanner(file);
        Pattern pat = Pattern.compile("[ \t]+");
        String str = scan.nextLine();
        String[] sn = pat.split(str);
        rowAmount = Integer.parseInt(sn[0]);
        columnAmount = Integer.parseInt(sn[1]) + 1;
        epsilon = Math.pow(10, -Double.parseDouble(sn[2]) - 1);
        this.Create(rowAmount, columnAmount);
        int i, j;
        for (i = 0; i < rowAmount; i++) {
            str = scan.nextLine();
            sn = pat.split(str);
            for (j = 0; j < columnAmount; j++)
                matrixArray[i][j] = Double.parseDouble(sn[j]);
        }
        scan.close();
    }

    public Results MakeTriangle() {
        boolean swapResult = SwapFirstNotZeroLine(0);
        if (!swapResult) return Results.DEGENERATE_SYSTEM;
        for (int i = 1; i < rowAmount; i++) {
            swapResult = SwapFirstNotZeroLine(i);
            if (!swapResult) return Results.DEGENERATE_SYSTEM;
            MakeColumnZero(i);
        }
        return Results.MAY_BE_CONTINUED;
    }

    public Results CheckSolutions() {
        if (IsZeroElement(matrixArray[rowAmount - 1][columnAmount - 2]))
            if (IsZeroElement(matrixArray[rowAmount - 1][columnAmount - 1]))
                return Results.ENDLESS_SOLUTIONS;    
            else
                return Results.NO_SOLUTIONS;  
        return Results.ONLY_SOLUTION;          
    }

    public double[] FindSolutions() {
        double[] solution = new double[rowAmount];
        for (int i = rowAmount - 1; i >= 0; i--) {
            double summary = GetSolution(solution, i);
            solution[i] += (matrixArray[i][rowAmount] - summary) / matrixArray[i][i];
        }
        return solution;
    }

    private void Create(int rowAmount, int columnAmount) {
        this.matrixArray = new double[rowAmount][];
        for (int i = 0; i < rowAmount; i++)
            this.matrixArray[i] = new double[columnAmount];
    }

    private boolean SwapFirstNotZeroLine(int lineNumber) {
        if (IsZeroElement(matrixArray[lineNumber][lineNumber])) {
            int lineToSwap = FindLineWithNotZeroElement(lineNumber);
            if (lineToSwap == NotFounded) return false;
            SwapLines(lineToSwap, lineNumber);
        }
        return true;
    }

    private int FindLineWithNotZeroElement(int lineNumber) {
        for (int resultLine = lineNumber; resultLine < rowAmount - lineNumber; resultLine++)
            if (!IsZeroElement(matrixArray[resultLine][lineNumber]))
                return resultLine;
        return NotFounded;
    }

    private void MakeColumnZero(int columnNumber) {
        int k = columnNumber;
        for (int j = columnNumber; j < rowAmount; j++) {
            double multiplier = matrixArray[k][columnNumber - 1] / matrixArray[columnNumber - 1][columnNumber - 1];
            MultiplicationAndSubtractionOfLine(multiplier, columnNumber, j);
            k++;
        }
    }

    private void SwapLines(int firstLine, int secondLine) {
        double[] temp = matrixArray[secondLine];
        matrixArray[secondLine] = matrixArray[firstLine];
        matrixArray[firstLine] = temp;
    }

    private boolean IsZeroElement(double element) {
        return (Math.abs(element) < epsilon);
    }

    private void MultiplicationAndSubtractionOfLine(double multiplier, int iterationNumber, int lineNumber) {
        for (int j = 0; j < columnAmount; j++) {
            matrixArray[lineNumber][j] -= (multiplier * matrixArray[iterationNumber - 1][j]);
            if (IsZeroElement(matrixArray[lineNumber][j]))
                matrixArray[lineNumber][j] = 0;
        }
    }

    private double GetSolution(double[] solution, int i) {
        double summary = 0;
        for (int j = i + 1; j < rowAmount; j++) {
            summary += solution[j] * matrixArray[i][j];
        }
        return summary;
    }

}