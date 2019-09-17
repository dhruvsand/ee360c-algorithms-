import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//Name: Dhruv Sandesara
//EID: djs3967

public class VibraniumOreScenario {

    private int numOres, weightCapacity, volumeCapacity;
    private VibraniumOre[] vibraniumOres;

    public VibraniumOreScenario(String fileName) throws FileNotFoundException {

        Scanner sc = new Scanner(new File(fileName));
        String nextLine = sc.nextLine();
        String[] split = nextLine.split(" ");
        weightCapacity = Integer.parseInt(split[0]);
        volumeCapacity = Integer.parseInt(split[1]);
        nextLine = sc.nextLine();
        split = nextLine.split(" ");
        numOres = Integer.parseInt(split[0]);
        vibraniumOres = new VibraniumOre[numOres];
        int price, weight, volume;
        int i = 0;
        while (sc.hasNextLine() && i < numOres) {
             nextLine = sc.nextLine();
             split = nextLine.split(" ");
             price = Integer.parseInt(split[0]);
             weight = Integer.parseInt(split[1]);
             volume = Integer.parseInt(split[2]);
             VibraniumOre ore = new VibraniumOre(price, weight, volume);
             vibraniumOres[i] = ore;
             i++;
        }

    }

    public VibraniumOre getVibraniumOre(int oreIndex) {
    
        return this.vibraniumOres[oreIndex];
    }

    public int getWeightCapacity() {

        return this.weightCapacity;
    }

    public int getVolumeCapacity() {

        return this.volumeCapacity;
    }

    public int getNumOres() {

        return this.numOres;
    }
}
