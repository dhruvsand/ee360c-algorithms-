import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//Name: Dhruv Sandesara
//EID: djs3967

/***
 * NOTE: DO NOT CHANGE THIS CODE
 * Program 3 contains an EconomyCalculator object that is initialized in the Driver.
 * Do not intialize a new EconomyCalculator object. Use the calculateGain function associated with the
 * EconomyCalculator object that we have already intialized for you
 */
public class EconomyCalculator {
    
    private int numVibranium;
    private int numProjects;
    private int[][] function;

    public EconomyCalculator(String fileName) throws FileNotFoundException{

        Scanner sc = new Scanner(new File(fileName));
        String nextLine = sc.nextLine();
        String[] split = nextLine.split(" ");
        numProjects = Integer.parseInt(split[0]);
        numVibranium = Integer.parseInt(split[1]);
        function = new int[numProjects][numVibranium + 1];

        for(int i = 0; i < numProjects; i++){
            function[i][0] = 0;
        }

        int i = 0;
        while(sc.hasNextLine()){
            nextLine = sc.nextLine();
            split = nextLine.split(" ");
            for(int j = 0; j < numVibranium; j++){
                function[i][j + 1] = Integer.parseInt(split[j]);
            }
            i++;
        }

    }

    /***
     * @param project: Project number
     * @param Vibranium: Amount of Vibranium
     * @return Percentage gain in the economy
     */
    public int calculateGain(int project, int Vibranium) {
        return function[project][Vibranium];
    }

    public int getNumProjects() {
        return numProjects;
    }

    public int getNumVibranium() {
        return numVibranium;
    }
}
