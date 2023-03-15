import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class TestCase {
    // instance variables - replace the example below with your own
    private int caseNo;
    private static double[] caseScores = {0.5, 1.0, 1.0, 1.0, 1.0, 0.5};

    /**
     * Constructor for objects of class TestCase
     */
    public TestCase(int caseNo) {
        this.caseNo = caseNo;
    }

    public int getCaseNo() {
        return caseNo;

    }

    public String getPersonInFile() {
        return "src/cases/TestCase" + caseNo + ".persons.txt";

    }

    public String getApprovalInFile() {
        return "src/cases/TestCase" + caseNo + ".approved.txt";

    }

    public String getVBatchInFile() {
        return "src/cases/TestCase" + caseNo + ".batches.txt";

    }

    public String getTestOutFile() {
        return "src/cases/TestCase" + caseNo + ".myOutput.txt";

    }

    public double score() {
        String testfile = "src/cases/TestCase" + caseNo + ".myOutput.txt";
        String valfile = "src/cases/TestCase" + caseNo + ".valOutput.txt";
        StringBuilder tString = new StringBuilder();
        StringBuilder vString = new StringBuilder();
        try {
            Scanner tscan = new Scanner(new File(testfile));
            while (tscan.hasNext())
                tString.append(tscan.nextLine());
        } catch (IOException ioe) {
        }
        try {
            Scanner vscan = new Scanner(new File(valfile));
            while (vscan.hasNext())
                vString.append(vscan.nextLine());
        } catch (IOException ioe) {
        }

        return caseScores[getCaseNo()] * similarity(tString.toString(), vString.toString());

    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static void runCases(VaxProgram vx) {
        boolean header = false;
        String stuId = "";
        try {
            Scanner sc = new Scanner(new File("id.txt"));
            stuId = sc.nextLine();
            sc.close();
        } catch (IOException ioe) {
        }
        if (stuId.length() == 0) {
            System.out.println("ID not set. Please enter your ID before proceeding.");
            return;
        }
        ArrayList<TestCase> cases = new ArrayList<TestCase>();
        for (int i = 0; i < 6; i++) {
            System.out.println("---------------------------------------------------------");
            System.out.println("---======" + stuId + " running TEST CASE " + i + "======================---");
            System.out.println("---------------------------------------------------------");
            TestCase tc = new TestCase(i);
            vx.clearData();
            vx.loadData(i);
            if (vx.getAPlist().size() > 0) {
                vx.applyVaxes();//vx.setFVlist(

                if (vx.countVaxes() > 0)
                    vx.applyRemaining();
            }
            vx.showData(System.out);
            //tc.writeOut(vx.getPlist(),vx.getAPlist(),vx.getFVlist(),
            //    vx.getInitApproved(), outWriter);
            cases.add(tc);

            try {
                PrintStream outStream = new PrintStream(new FileOutputStream(tc.getTestOutFile()));
                switch (i) {
                    case 0: {
                        vx.printAllPersons(outStream, header);
                        break;
                    }

                    case 1: {
                        vx.printFindPerson(outStream);
                        break;
                    }

                    case 2: {
                        vx.printAllApproved(outStream, header);
                        break;
                    }


                    case 3: {
                        vx.printAllVBatches(outStream, header);
                        break;
                    }

                    case 4: {
                        vx.printAllVBatches(outStream, header);
                        vx.printAllPersons(outStream, header);
                        vx.printAllApproved(outStream, header);
                        vx.printAllVaxed(outStream, header);
                        break;
                    }

                    case 5: {
                        vx.reportAllVaxed(outStream);
                        break;
                    }

                }
                outStream.close();
            } catch (IOException ioe) {
            }
        }
        double sumScore = 0;
        for (TestCase tc : cases) {
            double tcScore = tc.score();
            sumScore += tcScore;
            System.out.println("Score on test " + tc.getCaseNo() + " is " + tcScore + "; Aggregate score is " + sumScore);
        }
        File dir = new File(System.getProperty("user.dir"));
        File[] scoreFiles = dir.listFiles((dir1, name) -> name.startsWith("score"));
        for (File file : scoreFiles)
            file.delete();


        try {
            PrintStream p = new PrintStream(new FileOutputStream("score_" + String.format("%.2f", sumScore) + "_" + stuId + ".tst"));
            p.println(sumScore);
            p.close();
        } catch (IOException ioe) {
        }
    }

}
