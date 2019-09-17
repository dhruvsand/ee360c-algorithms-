import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Lab1Tester {    
    public static void runTest(Boolean bruteforce, String filename) {
        Matching problem = null;
        try {
            problem = parseMatchingProblem(filename);
        } catch (Exception e) {
            return;
        }

        try {
            testRun(problem, bruteforce);
        } catch (Exception e) {
           System.out.println("Test case failed!");
        }
    }

    public static Matching parseMatchingProblem(String inputFile)
            throws Exception {
        int m = 0;
        int n = 0;
        ArrayList<ArrayList<Integer>> studentPrefs;
        ArrayList<Double> studentGPAs;
        ArrayList<Coordinate> adviserLocs, studentLocs;

        Scanner sc = new Scanner(new File(inputFile));
        String[] inputSizes = sc.nextLine().split(" ");

        m = Integer.parseInt(inputSizes[0]);
        n = Integer.parseInt(inputSizes[1]);
        studentPrefs = readPreferenceLists(sc, n);
        studentGPAs = readGPAs(sc, m);
        adviserLocs = readLocationsLists(sc, m);
        studentLocs = readLocationsLists(sc, m);

        Matching problem = new Matching(m, n, studentLocs, adviserLocs, studentPrefs, studentGPAs);

        return problem;
    }

    private static ArrayList<Coordinate> readLocationsLists(Scanner sc, int m) {
        ArrayList<Coordinate> locations = new ArrayList<Coordinate>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < m * 2; i += 2) {
            double x = Double.parseDouble(slots[i]);
            double y = Double.parseDouble(slots[i + 1]);
            locations.add(new Coordinate(x, y));
        }

        return locations;
    }

    private static ArrayList<Double> readGPAs(Scanner sc, int m) {
        ArrayList<Double> GPAs = new ArrayList<Double>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < m; i++) {
            GPAs.add(Double.parseDouble(slots[i]));
        }

        return GPAs;
    }

    private static ArrayList<ArrayList<Integer>> readPreferenceLists(
            Scanner sc, int m) {
        ArrayList<ArrayList<Integer>> preferenceLists;
        preferenceLists = new ArrayList<ArrayList<Integer>>(0);

        for (int i = 0; i < m; i++) {
            String line = sc.nextLine();
            String[] preferences = line.split(" ");
            ArrayList<Integer> preferenceList = new ArrayList<Integer>(0);
            for (Integer j = 0; j < preferences.length; j++) {
                preferenceList.add(Integer.parseInt(preferences[j]));
            }
            preferenceLists.add(preferenceList);
        }

        return preferenceLists;
    }

    public static void testRun(Matching problem, boolean doBruteforce) {
        Program1 program = new Program1();
        boolean isStable;

        if (!doBruteforce) {
            Matching GSMatching = program.stableMarriageGaleShapley(problem);
            System.out.println(GSMatching);
            isStable = program.isStableMatching(GSMatching);
            System.out.printf("%s: stable? %s\n", "Gale-Shapley", isStable);
            System.out.println();
        }

        if (doBruteforce) {
            Matching BFMatching = program.stableMarriageBruteForce(problem);
            System.out.println(BFMatching);
            isStable = program.isStableMatching(BFMatching);
            System.out.printf("%s: stable? %s\n", "Brute Force", isStable);
            System.out.println();
        }
    }
}
