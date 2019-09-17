
//Name: Dhruv Sandesara
//EID: djs3967


public class Program3 {

    EconomyCalculator calculator;
    VibraniumOreScenario vibraniumScenario;



  //  int[] vibranum_per_project = new int[n+1];

    public Program3() {
        this.calculator = null;
        this.vibraniumScenario = null;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 1.
     */
    public void initialize(EconomyCalculator ec) {
        this.calculator = ec;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 2.
     */
    public void initialize(VibraniumOreScenario vs) {
        this.vibraniumScenario = vs;
    }

    /*
     * This method returns an integer that is maximum possible gain in the Wakandan economy
     * given a certain amount of Vibranium
     */
    //TODO: Complete this function
    public int computeGain() {
        int n= calculator.getNumProjects();
        int v= calculator.getNumVibranium();

        int[][] opt_array = new int [n+1][v+1];


        //initalizing the base cases
        for(int i=0; i<=n;i++)
            opt_array[i][0]=0;

        for (int i=0;i<=v;i++)
            opt_array[0][i]=0;

        for (int i=1;i<=n;i++){
            for (int j=1; j<=v;j++){
                //now calculating the max gain
                int max=0;
                int temp_gain=0;
                for (int temp=0;temp<=j;temp++){
                    //computing the gain for all the combination of the vibranium for curent project and the provious most optimal solution
                    temp_gain = calculator.calculateGain(i-1,temp)+ opt_array[i-1][j-temp];

                    if(temp_gain>max)
                        max=temp_gain;
                    //if gain is bigger setting it as the new one
                }
                opt_array[i][j]= max;
            }
        }
        return opt_array[n][v];//returing the gain with the provided constrains
    }

    /*
     * This method returns an integer that is the maximum possible dollar value that a thief 
     * could steal given the weight and volume capacity of his/her bag by using the 
     * VibraniumOreScenario instance.
     */
     //TODO: Complete this method
     public int computeLoss() {
         int total_volume= vibraniumScenario.getVolumeCapacity();
         int total_weight = vibraniumScenario.getWeightCapacity();
         int n= vibraniumScenario.getNumOres();

        int opt_loss[][]= new int[total_volume+1][total_weight+1];

        for (int i=0;i<n;i++){
            int temp_volume = vibraniumScenario.getVibraniumOre(i).getVolume();
            int temp_weight = vibraniumScenario.getVibraniumOre(i).getWeight();
            int price = vibraniumScenario.getVibraniumOre(i).getPrice();
//getting all the values of the item being considered
            for(int j=total_volume;j>=temp_volume;j--){
                for (int k=total_weight;k>=temp_weight;k--){

                    opt_loss[j][k]= Integer.max(opt_loss[j][k],price+opt_loss[j-temp_volume][k-temp_weight]);
                    //seeing if it is better to steal the current item of to stick with the previous most optimlal solution
                    //if decied to steal the current item, adding the optimal solution of the knapsack with the new reduced constrains.



                }




            }





        }


        
        return opt_loss[total_volume][total_weight];//returning the loss with the proveded constrains
     }
}


