/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {


    ArrayList<Integer> gpa_preference= new ArrayList<Integer>();
    ArrayList<Double> gpa_values=new ArrayList<Double>();
    ArrayList<ArrayList<Integer>> advisor_preference=new ArrayList<ArrayList<Integer>>();

    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
        /* TODO implement this function */

        ArrayList<Coordinate> student_locations= marriage.getStudentLocations();

        ArrayList<Coordinate>adviser_locations= marriage.getAdviserLocations();

        ArrayList<ArrayList<Integer>> student_preference= marriage.getStudentPreference();

        ArrayList<Double> student_GPAs= marriage.getStudentGPAs();

        ArrayList<Integer> student_matching =marriage.getStudentMatching();

        int[] residence_matching= new int[marriage.getNumberOfAdvisers()];

        for(int i=0;i<marriage.getNumberOfStudents();i++){
            if(student_matching==null)
                return false;
            if(student_matching.get(i)!=null)
                residence_matching[student_matching.get(i)]=i;
        }
     //   System.out.println(residence_matching);
        for(int i=0; i< marriage.getNumberOfStudents();i++){
            //i will iterate through each of the student
            ArrayList<Integer> current_student_preference = student_preference.get(i);

            for(int j=0; j< marriage.getNumberOfAdvisers();j++){
                //this will get all the advisors at j
                //we need to compare the current students preference to the difference of the current student and the desired advisor studnet

                if(current_student_preference.get(j).equals(student_matching.get(i)))
                    break;
                else if(student_GPAs.get(i)>student_GPAs.get(residence_matching[current_student_preference.get(j)]))
                    return false;
                else if(student_GPAs.get(i).equals(student_GPAs.get(residence_matching[current_student_preference.get(j)]))){
                    Coordinate current_student_location = student_locations.get(i);
                    Coordinate matched_student_location = student_locations.get(residence_matching[current_student_preference.get(j)]);
                    Coordinate current_advisor = adviser_locations.get(current_student_preference.get(j));
                    double matched_x=Math.abs(matched_student_location.x-current_advisor.x);
                    double matched_y=Math.abs(matched_student_location.y-current_advisor.y);
                    double current_x=Math.abs(current_student_location.x-current_advisor.x);
                    double current_y=Math.abs(current_student_location.y-current_advisor.y);
                    double matched_distance= Math.sqrt(matched_x*matched_x+matched_y*matched_y);
                    double current_distance= Math.sqrt(current_x*current_x+current_y*current_y);

                    current_distance = DistanceCalculator(marriage,current_student_preference.get(j),i);
                    matched_distance = DistanceCalculator(marriage,current_student_preference.get(j),residence_matching[current_student_preference.get(j)]);

                    if(current_distance<matched_distance)
                    return false;
                }

            }

        }


        return true; /* TODO remove this line */


//        SortedFinal(marriage);//this initailizes the preference list
//
//        for(int i=0;i<marriage.getNumberOfStudents();i++){
//
//            for (int j=0;j<marriage.getNumberOfAdvisers();j++){
//
//                if(marriage.getStudentPreference().get(i).get(j)!=marriage.getStudentMatching().get(i)){
//                    int advisor_index=   marriage.getStudentPreference().get(i).get(j);
//                    int current_married_student_index= marriage.getStudentMatching().indexOf(advisor_index);
//                    int rank_of_married_student= advisor_preference.get(advisor_index).indexOf(current_married_student_index);
//                    int rank_of_wannabe_student=advisor_preference.get(advisor_index).indexOf(j);
//
//                    if(rank_of_married_student>rank_of_wannabe_student)
//                        return false;
//
//                }
//                else
//                    break;
//
//            }
//        }
//
//
//        return true;



    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     * 
     * @return A stable Matching.
     */
    public Matching stableMarriageGaleShapley(Matching marriage) {
        ArrayList<Integer> matching= new ArrayList<Integer>();
        SortedFinal(marriage);
// this sorts the advisor_preferences

        Queue<Integer> students_available= new LinkedList<Integer>();
        Queue<Integer> advisors_available= new LinkedList<Integer>();


        for (int i=0;i<marriage.getNumberOfStudents();i++){
            students_available.add(i);

        }
        for (int i=0;i<marriage.getNumberOfAdvisers();i++){
            advisors_available.add(i);

        }


        while (students_available.peek()!=null){
            int current_student= students_available.remove();
            ArrayList<Integer> current_student_preference=marriage.getStudentPreference().get(current_student);
            int current_advisor=0;
            for (int i=0;i<marriage.getNumberOfAdvisers();i++){
                current_advisor=current_student_preference.get(i);


                if(advisors_available.contains(current_advisor)){
                    if(matching.isEmpty())
                        matching.add(current_advisor);
                    if(matching.size()<current_student+1)
                        matching.add(current_advisor);

                    matching.set(current_student,current_advisor);
                    advisors_available.remove(current_advisor);
                    students_available.remove(current_student);

                    break;


                }
                else {

                    int current_married_student_index= matching.indexOf(current_advisor);
                    int rank_of_married_student= advisor_preference.get(current_advisor).indexOf(current_married_student_index);
                    int rank_of_wannabe_student=advisor_preference.get(current_advisor).indexOf(current_student);

                    if(rank_of_married_student>rank_of_wannabe_student){
                        students_available.add(current_married_student_index);
                        if(matching.size()<current_student+1)
                            matching.add(current_advisor);
                        matching.set(current_student,current_advisor);
                        matching.set(current_married_student_index,-1);
                        break;

                    }
                }


            }

        }

        marriage.setResidentMatching(matching);
        return marriage;
    }





    public void SortedFinal(Matching marriage){

        SortedGPA(marriage);
        //System.out.println(gpa_preference);

        for (int i=0; i<marriage.getNumberOfAdvisers();i++){

            ArrayList<Integer> preference_List= new ArrayList<Integer>();//= gpa_preference;
            int min_index=0;
            int max_index =0;
            for(int j=0;j<marriage.getNumberOfStudents();j++){

                if(((j+1)<marriage.getNumberOfStudents())&&(gpa_values.get(j).equals(gpa_values.get(j+1)))) {
                    min_index =j;
                    ArrayList<Double> distance= new ArrayList<Double>();
                    ArrayList<Integer> gpa_index= new ArrayList<Integer>();
                    distance.add(DistanceCalculator(marriage,i,j));
                    gpa_index.add(gpa_preference.get(j));


                    while (((j+1)<marriage.getNumberOfStudents())&&(gpa_values.get(j).equals(gpa_values.get(j+1)))){
                        distance.add(DistanceCalculator(marriage,i,j+1));
                        gpa_index.add(gpa_preference.get(j+1));
                        max_index = j+1;
                        j=j+1;

                    }
                    //j=j-1; //makes sure it is one index ahead

                    for (int k=0;k<distance.size();k++){
                        double min= Double.MAX_VALUE;
                        int index=0;

                        for (int l=0;l<distance.size();l++){

                            if(min>distance.get(l)){
                                index=l;
                                min= distance.get(index);

                            }
                        }
                        preference_List.add(gpa_index.get(index));
                        //System.out.println(preference_List);
                        distance.set(index,Double.MAX_VALUE);
                    }

                }
                else{
                    preference_List.add(gpa_preference.get(j));
                    //System.out.println(preference_List);
                }

            }
            advisor_preference.add(preference_List);
            //System.out.println(preference_List);
            //preference_List.clear();
            //preference_List=null;
            //System.out.println(DistanceCalculator(marriage,1,1));
            //System.out.println(DistanceCalculator(marriage,1,5));


        }
    }
    public double DistanceCalculator(Matching marriage, int advisor,int student){

        Coordinate advisor_location= marriage.getAdviserLocations().get(advisor);

        //double distance=0;
        Coordinate student_location= marriage.getStudentLocations().get(student);


        double matched_x=Math.abs(student_location.x-advisor_location.x);
        double matched_y=Math.abs(student_location.y-advisor_location.y);
        double matched_distance= Math.sqrt(matched_x*matched_x+matched_y*matched_y);



        //distance= Math.sqrt(student_location.x*advisor_location.x+student_location.y*advisor_location.y);
        return matched_distance;

    }



    //sorts the gpa_preference list based on descentding order of gpa
    public void SortedGPA(Matching marriage){

        ArrayList<Double> gpa_List= new ArrayList<Double>();
        gpa_List= (ArrayList<Double>) marriage.getStudentGPAs().clone();
        int index=0;
        double max=-1;

        for(int i=0;i<marriage.getNumberOfStudents();i++){
            index=0;
            for(int j=0;j<marriage.getNumberOfStudents();j++){

                if(max<gpa_List.get(j)){
                    max=  gpa_List.get(j);
                    index= j;
                }
            }
            gpa_preference.add(index);
            gpa_values.add(gpa_List.get(index));
            gpa_List.set(index,-1.0);
            max=-1;
            //System.out.println(gpa_values);
            //System.out.println(gpa_preference);
            //System.out.println(gpa_List);

        }


    }
}
