/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.util.ArrayList;
import java.math.*;
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
    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */



    ArrayList<Integer> gpa_preference= new ArrayList<Integer>();
    ArrayList<Double> gpa_values=new ArrayList<Double>();
    ArrayList<ArrayList<Integer>> advisor_preference=new ArrayList<ArrayList<Integer>>();




//    public void DistanceCalculator(Matching marriage){
//        for (int i=0; i<marriage.getNumberOfAdvisers();i++){
//            Coordinate advisor_location= marriage.getAdviserLocations().get(i);
//            for(int j=0;j<marriage.getNumberOfStudents();j++) {
//                double distance=0;
//                Coordinate student_location= marriage.getStudentLocations().get(j);
//
//                distance= Math.sqrt(student_location.x*advisor_location.x+student_location.y*advisor_location.y);
//                location.get(i).add(j,distance);
//
//            }
//
//        }
//
//
//
//    }

    public double DistanceCalculator(Matching marriage, int advisor,int student){

            Coordinate advisor_location= marriage.getAdviserLocations().get(advisor);

                double distance=0;
                Coordinate student_location= marriage.getStudentLocations().get(student);

                distance= Math.sqrt(student_location.x*advisor_location.x+student_location.y*advisor_location.y);
                return distance;

            }



//sorts the gpa_preference list based on descentding order of gpa
    public void SortedGPA(Matching marriage){

        ArrayList<Double> gpa_List= new ArrayList<Double>();
                gpa_List=marriage.getStudentGPAs();
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

        }


    }

//initializes correctly the advisor_preference for everybody and calculating distance only when necesasary conflicts arise
    public void SortedFinal(Matching marriage){

        SortedGPA(marriage);

        for (int i=0; i<marriage.getNumberOfAdvisers();i++){

            ArrayList<Integer> preference_List= new ArrayList<Integer>();//= gpa_preference;

            for(int j=1;j<marriage.getNumberOfStudents()+1;j++){



                if((j<marriage.getNumberOfStudents())&&(gpa_values.get(j-1)==gpa_values.get(j))) {
                    int temp=j-1;
                    ArrayList<Double> distance= new ArrayList<Double>();
                    distance.add(DistanceCalculator(marriage,i,j-1));


                    while ((j<marriage.getNumberOfStudents())&&(gpa_values.get(j-1) == gpa_values.get(j))){
                        distance.add(DistanceCalculator(marriage,i,j));
                        j++;


                    }
                    j++; //makes sure it is one index ahead
                    for (int k=0;k<distance.size();k++){
                        double min= Double.MAX_VALUE;
                        int index=0;

                        for (int l=0;l<distance.size();l++){

                            if(min>distance.get(l)){
                                index=l;


                            }
                        }
                        preference_List.add(gpa_preference.get(index+temp));
                        distance.set(index,Double.MAX_VALUE);

                        }

                }
                else
                    preference_List.add(j-1);

            }
            advisor_preference.add(preference_List);

        }
    }



    public boolean isStableMatching(Matching marriage) {

        SortedFinal(marriage);//this initailizes the preference list

        for(int i=0;i<marriage.getNumberOfStudents();i++){

            for (int j=0;j<marriage.getNumberOfAdvisers();j++){

                if(marriage.getStudentPreference().get(i).get(j)!=marriage.getStudentMatching().get(i)){
                    int advisor_index=   marriage.getStudentPreference().get(i).get(j);
                    int current_married_student_index= marriage.getStudentMatching().indexOf(advisor_index);
                    int rank_of_married_student= advisor_preference.get(advisor_index).indexOf(current_married_student_index);
                    int rank_of_wannabe_student=advisor_preference.get(advisor_index).indexOf(j);

                    if(rank_of_married_student>rank_of_wannabe_student)
                        return false;

                }
                else
                    break;

            }
        }


        return true;

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
}
