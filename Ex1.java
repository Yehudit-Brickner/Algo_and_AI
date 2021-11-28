
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Ex1 {

    public static void main(String[] args) {

        String s1 = "input.txt";
        readFile(s1);
        
    }


    public static void readFile(String str) {

        try{
            //open  writer to file
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

            ArrayList<Nodes> nodearr=new ArrayList<Nodes>();
            String s1 = "";
            String s2 = "";
            String s3 = "";
            String s4 = "";
            int count = 0;
            int count1 = 0;
            int count2 = 0;
            String arr1[] = {" "};
            String hidden[] = {" "};
            String given[] = {" "};
            File f = new File(str);

            try {
                Scanner s = new Scanner(f); // open scanner to read the file
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    System.out.println(line);
                    if (line.contains("xml")) {
                        // read the xml file
                        nodearr = readxml(line);

                    } else if (line.contains("P(")) { // this line will be variable elimination
                        s1 = line;
                        count = 0;
                        // splitting the line into different parts
                        for (int i = 0; i < s1.length(); i++) {
                            if (s1.charAt(i) == '|') {
                                break;
                            }
                            count = count + 1;
                        }
                        s2 = s1.substring(2, count);
                        s1 = s1.substring(count + 1);
                        //	System.out.println( "s2: " +s2);
                        //	System.out.println("s1: "+s1);
                        count = 0;
                        for (int i = 0; i < s1.length(); i++) {
                            if (s1.charAt(i) == ')') {
                                break;
                            }
                            count = count + 1;
                        }
                        //creating the arrays of given and hidden nodes
                        s3 = s1.substring(0, count);
                        s4 = s1.substring(count + 2);
                        if (s3.length() > 1) {
                            given = s3.split(",");
                        }
                        if (s4.length() > 1) {
                            hidden = s4.split("-");
                        }
                        // System.out.println("given: "+s3);
                        // System.out.println("hidden: "+s4);
                        // System.out.println("hidden: "+Arrays.toString(hidden));
                        // System.out.println("given: " +Arrays.toString(given));

                        // running the function to find the answer to the question
                        String ans = variableElimination1(s2, given, hidden, nodearr);
                        //printing the answer to the file
                        writer.println(ans);
                    } else if (line.contains("|")) {  // this line ia a Bayse Ball question
                        s1 = line;
                        count = 0;
                        for (int i = 0; i < s1.length(); i++) {
                            if (s1.charAt(i) == '|') {
                                break;
                            }
                            count = count + 1;
                        }
                        // splitting the line into different parts
                        s2 = s1.substring(0, count);
                        s3 = s1.substring(count);
                        //System.out.println(s3);
                        arr1 = s2.split("-");
                        //	System.out.println(Arrays.toString(arr1));
                        count1 = 0;
                        count2 = 0;
                        //finding start and end node for Bayes ball
                        for (int i = 0; i < nodearr.size(); i++) {
                            if (nodearr.get(i).name.equals(arr1[0])) {
                                break;
                            } else {
                                count1 = count1 + 1;
                            }
                        }
                        for (int i = 0; i < nodearr.size(); i++) {
                            if (nodearr.get(i).name.equals(arr1[1])) {
                                break;
                            } else {
                                count2 = count2 + 1;
                            }
                        }
                        // finding the given-evidence nodes
                        ArrayList<Nodes> evidence = new ArrayList<Nodes>();
                        if (s3.length() > 1) {
                            s3 = s1.substring(count + 1);
                            //System.out.println("s3:" +s3);
                            arr1 = s3.split(",");
                        }
                        //System.out.println("evidence" +Arrays.toString(arr1));
                        // going through the array and finding the evidence nodes
                        // the boolean seen_end makes sure that we take thw whole name of the node
                        if (arr1[0] != " ") {
                            for (int i = 0; i < arr1.length; i++) {
                                for (int j = 0; j < nodearr.size(); j++) {
                                    //  s4=" ";
                                    s4 = String.valueOf(arr1[i].charAt(0));
                                    boolean seen_end = false;
                                    for (int k = 1; k < arr1[i].length() && !seen_end; k++) {
                                        //System.out.println(arr1[i].charAt(k));
                                        if (arr1[i].charAt(k) != '=') {
                                            //System.out.println(arr1[i].charAt(k));
                                            s4 += String.valueOf(arr1[i].charAt(k));
                                        } else {
                                            seen_end = true;
                                        }
                                    }
                                    //System.out.println("s4="+s4);
                                    if (s4.equals(nodearr.get(j).name)) {
                                        evidence.add(nodearr.get(j));
                                    }
                                }
                            }
                        }
						/*	System.out.println("size of e:" +e.size());
					for (int i=0; i<e.size(); i++) {
						System.out.println("e:"+e.get(i).name);
					}
						 */
                        // running the function to find the answer to the question
                        String ans = bayesBall(nodearr.get(count1), nodearr.get(count2), evidence, nodearr);
                        System.out.println(ans);
                        System.out.println();
                        writer.println(ans);  //printing the answer to the file
                    } else {
                        // if there is a line n the file that doesn't contain any of 3 things above we won't do anything
                    }
                }
                s.close();  // closing the scanner
            }
            catch(Exception e){
                System.out.println(e);
            }
            writer.close(); // close the writer
        }
        catch(Exception e){
            System.out.println(e);
        }
    }


    public static ArrayList<Nodes> readxml(String S1) {

        ArrayList<Nodes> nodearr = new ArrayList<Nodes>();
        File file = new File(S1);
        int StrLen = 0;
        String s1 = "";
        String s2 = "";
        double x = 0;
        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                //System.out.println(line);
                if (line.contains("<NAME>")) {
                    s2 = line;
                    StrLen = s2.length();
                    s2 = s2.substring(7, StrLen - 7); // getting the body of the line
                    //System.out.println(s2);
                    Nodes node1 = new Nodes(s2); // creating a Nodes
                    nodearr.add(node1); // adding the Nodes to the ArrayList of Nodes
                }
                if (line.contains("<OUTCOME>")) {
                    s1 = line;
                    StrLen = s1.length();
                    s1 = s1.substring(10, StrLen - 10); // getting the body of the line
                    //System.out.println(s1);
                    for (int i = 0; i < nodearr.size(); i++) {
                        if (s2.equals(nodearr.get(i).getName())) {
                            nodearr.get(i).outcomes.add(s1); // adding the outcome to the NOdes ArrayList of outcomes
                        }
                    }
                }
                if (line.contains("<FOR>")) {
                    s2 = line;
                    StrLen = s2.length();
                    s2 = s2.substring(6, StrLen - 6); // getting the body of the line this is the Nodes
                    //System.out.println(s2);
                }
                if (line.contains("<GIVEN>")) {
                    s1 = line;
                    StrLen = s1.length();
                    s1 = s1.substring(8, StrLen - 8); // getting the body of the line
                    //System.out.println(s1);
                    for (int i = 0; i < nodearr.size(); i++) {
                        if (s2.equals(nodearr.get(i).getName())) { //for
                            for (int j = 0; j < nodearr.size(); j++) {
                                if (s1.equals(nodearr.get(j).getName())) { //given
                                    nodearr.get(i).parents.add(nodearr.get(j)); // adding the given Nodes as Parents to the for Nodes
                                    nodearr.get(j).kids.add(nodearr.get(i)); // adding the for Nodes as kids to the given Nodes
                                }
                            }
                        }
                    }
                }
                if (line.contains("<TABLE>")) {
                    s1 = line;
                    StrLen = s1.length();
                    s1 = s1.substring(8, StrLen - 8); // getting the body of the line
                    //System.out.println(s1);
                    String[] arr = s1.split(" "); // creating an array of the values
                    //System.out.println(Arrays.toString(arr));
                    for (int i = 0; i < nodearr.size(); i++) {
                        if (s2.equals(nodearr.get(i).getName())) {
                            for (int j = 0; j < arr.length; j++) {
                                x = Double.valueOf(arr[j]); // turning the double into a string
                                nodearr.get(i).percents.add(x); // adding the value to the Nodes percent ArrayList
                            }
                        }
                    }
                }
            }
            s.close(); // close the scanner
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println();
        for (int i = 0; i < nodearr.size(); i++) {
            System.out.println(nodearr.get(i));
        }
        return nodearr;
    }



    public static String bayesBall(Nodes n1, Nodes n2, ArrayList<Nodes> evidence, ArrayList<Nodes> nodesarr) {
        String S1 = "yes";
        String S2 = "no";
        boolean seenEvidence = false;
        boolean ans = false;
        ArrayList<Boolean> isIndependent = new ArrayList<Boolean>();
        // mark the nodes as seen
        if (evidence.size() > 0) {
            for (int i = 0; i < evidence.size(); i++) {
                evidence.get(i).setSeen(true);
            }
        }
        //make sure that the state of all the nodes is 0
        for (int i = 0; i < nodesarr.size(); i++) {
            nodesarr.get(i).setState(0);
        }

        boolean hasBeenSeen = false; // if we have seen a seen node
        bayesBalldown1(n1, n2, isIndependent, seenEvidence, hasBeenSeen);

        //make sure that the state of all the nodes is 0
        for (int i = 0; i < nodesarr.size(); i++) {
            nodesarr.get(i).setState(0);
        }
        bayesBallup1(n1, n2, isIndependent, seenEvidence, hasBeenSeen);


        // mark the nodes as unseen
        if (evidence.size() > 0) {
            for (int i = 0; i < evidence.size(); i++) {
                evidence.get(i).setSeen(false);
            }
        }
        // go through the ArrayList of values, if 1 of them is true,
        // than there is a path between the nodes, and the nodes are not independent
        for (int i = 0; i < isIndependent.size(); i++) {
            if (isIndependent.get(i) == true) {
                ans = true;
            }
        }

        if (ans == true) {
            return S2;
        } else {
            return S1;
        }
    }

    /*
     * bayes ball algorithm
     * if you are coming from above and the node you are going to is not seen, you have to continue to go down.
     * if you are coming from above and the node you are going to is seen, then you can only go to the parents of the seen node,
     * and can't go back to where you came from.
     * if you are coming from below and the node you are going to is not seen, you up to a parent of the node,
     * or down to a kid of the node(not back to himself)
     * if you are coming from below and the node you are going to is seen, you can't go anywhere.
     *
     * my algorithm is recursive and goes till it stops than goes to where it last stopped till if finishes checking everywhere it can go,
     * with the rules above.
     *
     * we will run the algorithm twice, once starting going down and next starting going up.(this is done in the BayesBall function)
     */


    public static void bayesBalldown1(Nodes n1, Nodes n2, ArrayList<Boolean> isdependent, boolean seenE, boolean hasBeenSeen) {
        boolean breeaak = false; // we will use this when we find the node in the middle of loop and want to exit the loop
        if (n1.equals(n2)) {
            isdependent.add(true); // we found the node
        }
        if (n1.state != 2) {
            n1.setState(1);
            for (int i = 0; i < n1.kids.size(); i++) {
                // mark that we've finished with node
                if (i == n1.kids.size() - 1) {
                    n1.setState(2); // this is the last kid mark that we are done with this node
                }
                // what we wre looking for
                if (n1.kids.get(i).equals(n2)) {
                    isdependent.add(true);
                    breeaak = true;
                    break;
                }
                // not what we are looking for, not seen can only go down
                else if (n1.kids.get(i).seen == false && !hasBeenSeen) {
                    seenE = false;
                    bayesBalldown1(n1.kids.get(i), n2, isdependent, seenE, hasBeenSeen);
                }

                // not what we are looking for, not seen but, we have seen before can go down
                else if (n1.kids.get(i).seen == false && hasBeenSeen) {
                    seenE = false;
                    if (n1.kids.get(i).state != 2) {
                        bayesBalldown1(n1.kids.get(i), n2, isdependent, seenE, hasBeenSeen);
                    }
                }
            else {
                    // not what we are looking for, has been seen can only go up.
                    seenE = true;
                    hasBeenSeen = true;
                    for (int j = 0; j < n1.kids.get(i).parents.size(); j++) {
                        if (breeaak) { //if before we've got true we don't need to do this
                            break;
                        }
                        // the only parent is where we came from
                        if (n1.kids.get(i).parents.size() == 1) {
                            n1.setState(2);
                            for (int k = 0; k < n1.parents.size(); k++) {
                                if (breeaak) { //if before we've got true we don't need to do this
                                    break;
                                }
                                if (k == n1.parents.size() - 1) {
                                    n1.setState(2); // this is the last parent mark that we are done with this node
                                }
                                if (n1.parents.get(k).equals(n2)) {
                                    isdependent.add(true);
                                    breeaak = true;
                                    break;
                                }
                                // the parent is seen cant continue;
                                if (n1.parents.get(k).seen == true) {
                                    isdependent.add(false); // can't go anywhere
                                }
                            }
                        }
                        if (breeaak) {
                            break; //if before we've got true we don't need to do this
                        }

                        if (j == n1.kids.get(i).parents.size()) {
                            n1.setState(2); // this is the last parent mark that we are done with this node
                        }
                        // the parent is n2
                        if (n1.kids.get(i).parents.get(j).equals(n2)) {
                            isdependent.add(true);
                        }
                        // the parent is seen cant continue;
                        else if (n1.kids.get(i).parents.get(j).seen == true) {
                            isdependent.add(false); // can't go anywhere
                        } else {
                            // not what we are looking for go up and down to continue looking
                            if (n1.kids.get(i).parents.get(j).state != 2) {
                                seenE = false;
                                bayesBallup1(n1.kids.get(i).parents.get(j), n2, isdependent, seenE, hasBeenSeen);
                            }
                            if (n1.kids.get(i).parents.get(j).state != 2) {
                                seenE = false;
                                bayesBalldown1(n1.kids.get(i).parents.get(j), n2, isdependent, seenE, hasBeenSeen);
                            }
                        }
                    }
                }
            }
        }
    }


    public static void bayesBallup1(Nodes n1, Nodes n2, ArrayList<Boolean> isdependent, boolean seenE, boolean hasBeenSeen) {
        boolean breeaak = false; // we will use this when we find the node in the middle of loop and want to exit the loop
        if (n1.equals(n2)) { // we've found what we are looking for
            isdependent.add(true);
        }

        if (n1.state != 2) {
            n1.setState(1);
            for (int i = 0; i < n1.parents.size(); i++) {
                if (i == n1.parents.size() - 1) {
                    n1.setState(2); // mark that we've finished with node
                }
                // what we are looking for
                if (n1.parents.get(i).equals(n2)) {
                    isdependent.add(true);
                    breeaak = true;
                    break;
                }
                // not what we are looking for, not seen can go up or down;
                else if (n1.parents.get(i).seen == false) {
                    if (n1.parents.get(i).state != 2) {
                        seenE = false;
                        bayesBallup1(n1.parents.get(i), n2, isdependent, seenE, hasBeenSeen);
                    }
                } else {
                    seenE = true;
                    break;
                }
            }
            for (int i = 0; i < n1.kids.size(); i++) {
                if (breeaak) {
                    break; // if we have already got a true answer no need to continue looking
                }
                if (i == n1.kids.size() - 1) {
                    n1.setState(2);  // mark that we've finished with node
                }
                // what we are looking for
                if (n1.kids.get(i).equals(n2)) {
                    isdependent.add(true);
                    breeaak = true;
                    break;
                }
                if (n1.kids.get(i).seen != true) {
                    if (n1.kids.get(i).state != 2) {
                        seenE = false;
                        //changed up to down
                        bayesBalldown1(n1.kids.get(i), n2, isdependent, seenE, hasBeenSeen);
                    }
                }
            }
        }
    }


    public static String variableElimination1(String s, String[] given, String[] hidden, ArrayList<Nodes> nodearr) {

        int count1 = 0;
        for (int i = 0; i < s.length(); i++) { // find the name of the node that we are looking for and its percent
            count1 = count1 + 1;
            if (s.charAt(i) == '=') {
                break;
            }
        }
        System.out.println(s);
        String Qwry = s.substring(0, count1 - 1);
        String val=s.substring(count1);
        System.out.println("QWRY: " + Qwry + " val:  "+ val); // print the name of that node


        int mult = 0; // this variable will save the count of multiplications we do
        int add = 0; // this variable will save the count of additions we do
        int div = 0; // this is a variable that represents a number we will use when dividing by something
        ArrayList<Node_mat> mats = new ArrayList<Node_mat>(); // this ArrayList will hold all the nodes of the matrices
        ArrayList<String> del_later = new ArrayList<String>(); // this ArrayList will hold the names of the nodes that we don't need in any of the matrices
        for (int i = 0; i < nodearr.size(); i++) {
            Node_mat mat = create_mat(nodearr.get(i), nodearr, given, Qwry, del_later); // creating the matrix
            if (mat != null) {
                mats.add(mat); // if the node+mat is null we don't want it
            }
        }
        // deleting columns that contains nodes we don't need;
        while (del_later.size() >= 1) { // going through the arraylist
            int size = del_later.size() - 1;
            for (int j = 0; j < mats.size(); j++) { // go through all the matrices in mats
                for (int k = 0; k < mats.get(j).getMat()[0].length; k++) { // go through the columns of the mat
                    if (mats.get(j).getMat()[0][k].equals(del_later.get(size))) { // if the column name is 1 we dont need
                        Node_mat mat1 = get_rid_of_cols(mats.get(j).getMat(), del_later.get(size)); // we will create a new matrix without thwt column
                        mats.remove(j); // remove the old matrix from mats
                        if (mat1 != null) {
                            mats.add(mat1); // if the new matrix ist null, add it to mats
                        }
                    }
                }
            }
            del_later.remove(size); // remove the node name at spot size form the ArrayList
        }
        //printing all the mats
        System.out.println("printing all the mats");
        for (int i = 0; i < mats.size(); i++) {
            for (int j = 0; j < mats.get(i).mat.length; j++) {
                System.out.println(Arrays.toString(mats.get(i).mat[j]));
            }
            System.out.println();
        }
        // if there is only 1 mat then we need to find the correct value and return it
        if (mats.size() == 1) {
            count1 = s.length(); // find the truth value that we are looking for
            for (int i = s.length() - 1; i >= 0; i--) {
                count1 = count1 - 1;
                if (s.charAt(i) == '=') {
                    break;
                }
            }
            double last = 0;
            Object[][] finale_mat = mats.get(0).getMat();
            for (int i = 1; i < finale_mat.length; i++) {
                if (finale_mat[i][0].equals(val)) { // find the value of the truth val in the mat, last=its percent
                    last = (Double) finale_mat[i][finale_mat[0].length - 1];
                }
            }
            String ans = last + "," + 0 + "," + 0; // we will return a string with its percent and 0,0 because we did 0 additions and 0 multiplications
            return ans;
        }



		/*
		System.out.println("printing the mats in the arraylist");
		for (int i=0;i<mats.size();i++) {
			System.out.println(mats.get(i));
		}
		 */


        // after we have all the tables we need in the ArrayList mats,
        // we will start to eliminate variables in order
        ArrayList<Node_mat> hidden_mats = new ArrayList<Node_mat>();// this array list will hold all the nome_mats that have the variable we are now eliminating
        // going through mats and putting all the mats that contain a hidden together in a ArrayList
        // so that i can start the joining.
        Node_mat.Node_mat_Comparator comp = new Node_mat.Node_mat_Comparator(); // we will use this comparator to decide the order of joining matrices
        int count = 0;
        for (int k = 0; k < hidden.length; k++) { // go through the array of the names of nodes we need to eliminate
            hidden_mats.clear(); // we will clear the arrayList, because we are now adding new values and dont need the pld ones
            for (int i = 0; i < mats.size(); i++) { // go through the ArrayList mats
                for (int l = 0; l < mats.get(i).mat[0].length; l++) { // go through the column names of the matrix
                    if (mats.get(i).mat[0][l].equals(hidden[k])) { // if the column name equals the name of the variable we are looking for
                        hidden_mats.add(mats.get(i)); // we will add the node_mat to the hidden_mat Arraylist
                    }
                }
            }
            boolean removed = false;
            for (int i = mats.size() - 1; i >= 0; i--) { // going through the mats arraylist and removing the mats that we put in the hidden_mats ArrayList
                removed = false;
                for (int l = 0; !removed && l < mats.get(i).mat[0].length; l++) { // go through the columns of the matrix if we haven't changed the boolean removed to false
                    if (mats.get(i).mat[0][l].equals(hidden[k])) {
                        mats.remove(mats.get(i)); // if we found the column name that eaquals the node name that we rae looking for  we will remove the node_mat
                        removed = true; // and change the boolean toi true
                    }
                }
            }

            while (hidden_mats.size() > 1) { // if the size is bigger than 1 than we have at least 2 matrices
                Collections.sort(hidden_mats, comp); // sort the matrices
                Object[][] mat_new = join_matrix(hidden_mats.get(0).getMat(), hidden_mats.get(1).getMat(), hidden[k], nodearr); // join the first 2
                Node_mat n = new Node_mat(mat_new); //create a new node_mat
                mult = mult + (n.getRows() - 1); // add the amount of multiplication we did to the multiplication counter
                hidden_mats.remove(1); // remove tha node_mat from the arrayList because we don't need it anymore
                hidden_mats.remove(0); // same
                hidden_mats.add(n); // add the new node_mat to the arrayList

            }
            if (hidden_mats.size() > 0) { // if the size is bigger than 0 then there is 1 matrix in the array list
                for (int i = 0; i < nodearr.size(); i++) { // go through the arrayList of nodes
                    if (nodearr.get(i).getName().equals(hidden[k])) { // find the node that we are eliminating by
                        div = nodearr.get(i).outcomes.size(); // div= the amount of outcomes the node has
                    }
                }
                Object[][] m = eliminate_matrix(hidden_mats.get(0).getMat(), hidden[k], div, nodearr); // eliminating the node
                Node_mat n = new Node_mat(m); // creating a newn ode_mat
                mats.add(n); // adding the new node_mat to the arrayList mats
                add = add + (((hidden_mats.get(0).getRows()-1) / div) * (div - 1)); // adding the amount of addition we did to the addition counter
                // to count the amount of addition you did you take the rows fom the matrix-1(that's the 'column name' row)
                // and divide by the amount of outcomes the node you are eliminating has
                // than you take that number and times by the amount of outcomes-1,
                // that because when you add x numbers together you use x-1 addition signs
            }
        }
        // we have eliminated all the hidden values
        while (mats.size() > 1) { // if mats has more than 1 mat on i we need to join by the qwry node
            Collections.sort(mats, comp); // sort the arrayList
            Object[][] new_mat = join_matrix(mats.get(0).getMat(), mats.get(1).getMat(), Qwry, nodearr); // join
            mult = mult + mats.get(1).getRows() - 1; // add the amount of multiplication we did to the multiplication counter
            mats.remove(1); // remove the mat
            mats.remove(0); // same
            Node_mat new_mat1 = new Node_mat(new_mat); // create a new node_mat
            mats.add(new_mat1); // add it to mats
        }

        Object[][] finale_mat = mats.get(0).getMat(); // this is the finale mat

        // normalizing the answer
        double last = 0; // this will be the percent value that we will return
        for (int i = 1; i < finale_mat.length; i++) { // go through the matrix
            last = last + (Double) finale_mat[i][finale_mat[0].length - 1]; // add all the percent to the together
            add = add + 1; // eavh time we do this we need to bring the count uo by
        }
        add = add - 1; // lower the count by 1 because we counted it too many times
        last = 1.0 / last; // take 1.0 and divide by last so that we get are normalizer


        for (int i = 1; i < finale_mat.length; i++) {
            if (finale_mat[i][0].equals(val)) { // go through the mats row to find the value we are looking for
                last = last * (Double) finale_mat[i][finale_mat[0].length - 1]; //and times it by the normalizer
            }
        }
        last = (double) Math.round(last * 100000d) / 100000d; // round the number to 5 spots after the decimal point
        System.out.println("mult=" + mult + " add= " + add + " final ans: " + last);
        System.out.println();
        String ans = last + "," + add + "," + mult; // we will return a string with its percent and addition and multiplication counters
        return ans;
    }


    public static Node_mat create_mat(Nodes node, ArrayList<Nodes> nodearr, String[] given, String Qwry, ArrayList<String> del_later) {

        int count1 = 0;
        String s1;
        String s2;

        ArrayList<String> givs = new ArrayList<String>(); // arrayList of all the names and truth values of the given nodes;
        if (given[0] != " ") { // if the first value is " " there are no given nodes
            for (int i = 0; i < given.length; i++) {
                count1 = 0;
                for (int j = 0; j < given[i].length(); j++) { // go through the string that contains the node name and its vales
                    if (given[i].charAt(j) == '=') {
                        break;
                    }
                    count1 = count1 + 1;
                }
                s1 = given[i].substring(0, count1); // this is the node name
                s2 = given[i].substring(count1 + 1); // this is its value
                givs.add(s1); // add to the arrayList
                givs.add(s2); // add to the arrayList
            }
        }
        ArrayList<Nodes> given_nodes = new ArrayList<Nodes>(); // create an arraylist of the given nodes
        for (int j = 0; j < nodearr.size(); j++) {
            for (int k = 0; k < givs.size(); k = k + 2) { // gp through the givs arrayList to compare the nodes to ro see if we need them in this Arraylist
                if (nodearr.get(j).getName().equals(givs.get(k))) {
                    given_nodes.add(nodearr.get(j));
                }
            }
        }

        boolean is_Q_or_g = false; // is the node the qwry node or a given node
        if (node.getName().equals(Qwry)) {
            is_Q_or_g = true;
        }
        for (int i = 0; i < givs.size(); i = i + 2) {
            if (node.getName().equals(givs.get(i))) {
                is_Q_or_g = true;
            }
        }

        Nodes n1; // declaring a variable
        ArrayList<Boolean> ancestory = new ArrayList<Boolean>(); // this arrayList will hold the answer to if the node is an ancestor to the qwry or given
        String isIndependent = "";
        boolean isAncestor = false;
        if (!is_Q_or_g) { // if the node isn't the qwry or a given node
            for (int j = 0; j < nodearr.size(); j++) {
                if (nodearr.get(j).getName().equals(Qwry)) { // if the node is the qwry
                    n1 = nodearr.get(j);
                    // is the node an ancestor of qwry
                    isAncestor = ancestor(n1, node, nodearr); // we will chck if it is an ansetor of the qwry node
                    //System.out.println(node.name+ " is a ancetor of "+ Qwry+" : " +isAncestor );
                    ancestory.add(isAncestor); // and add the answer to the arrayList
                    isIndependent = bayesBall(node, n1, given_nodes, nodearr); // we will check if the node is dependent on the qwry with bayesball
                    //System.out.println("is " +Qwry + " isIndependent: form "+node.getName()+ "? "+isIndependent);
                } else {
                    // is node an ancestor of given
                    for (int k = 0; k < givs.size(); k = k + 2) { // if the node isn't the qwry
                        if (nodearr.get(j).getName().equals(givs.get(k))) { // if the node is given
                            n1 = nodearr.get(j);
                            isAncestor = ancestor(n1, node, nodearr); // is the node an ansestor of the given node
                            //System.out.println(node.name+ " is a ancetor of "+ givs.get(k)+" : " +isAncestor );
                            ancestory.add(isAncestor); // add the answer to the arrayList
                        }
                    }
                }
                //System.out.println("is " +Qwry + " isIndependent: form "+node.getName()+ "? "+isIndependent);
            }
        }
        isAncestor = false;
        for (int i = 0; i < ancestory.size(); i++) { // go through the arrayList of answers , if the node is an ancestor of the given or qwry
            if (ancestory.get(i).equals(true)) {
                isAncestor = true; // change the boolean isAncestor to true
            }
        }

        if (is_Q_or_g || (isAncestor && isIndependent.equals("no"))) { // the node is a (qwry or given node) or(a ancestor and is dependent on the qwry)

            int outcomes_spot = 0;
            int num = 1;
            //int count1=0;
            int rows = node.percents.size() + 1; // we need 1 row for each percent and a row for the column names
            int columns = 2 + node.parents.size(); // we need a row fore each parent, the node itself and for the percents
            int fill_in_rows = rows - 1; // we need to fill in the percent values this variable will help us keep track how many to fill in as a certain type
        //    boolean redo = true;


            Object[][] mat = new Object[rows][columns]; // creating the mat
            mat[0][columns - 2] = node.name; // filling the column name for this node
            mat[0][columns - 1] = "p(...)"; // filling in the column name for the percents

            //filling in the percents
            for (int i = 1; i < rows; i++) {
                mat[i][columns - 1] = node.percents.get(i - 1);
            }
            // filling in the column names
            for (int i = 0; i < columns - 2; i++) {
                if (i < node.parents.size()) {
                    mat[0][i] = node.parents.get(i).name;
                }
            }
            // getting rid of extra columns
			/*
			while (redo) {
				redo = need_to_create_new_mat(mat);
				if (redo) {
					mat=get_rid_of_mid_columns(mat);
				}
			}
			 */
            columns = mat[0].length;
            /*
            filling in the truth values
            we will start filling in the truth values starting wih the left most column,
            we will take the fill_in_rows var and divide by the amount of outcomes this column's node has
            we will start with the first outcome and each time we add it in we will bring up the counter by 1
            once the counter equals the var fill_in_rows we will reset the counter to 0, and start filling in the next node outcome,
            if we finished the nodes last outcome and there are more rows we will repeat the process
             */
            for (int i = 0; i < columns - 1; i++) {
                String s = String.valueOf(mat[0][i]);
                count1 = 0;
                for (int n = 0; n < nodearr.size(); n++) {
                    if (nodearr.get(n).name.equals(s)) {
                        num = nodearr.get(n).getOutcomes().size();
                        outcomes_spot = 0;
                        fill_in_rows = fill_in_rows / num;
                        for (int j = 1; j < rows; j++) {
                            if (count1 < fill_in_rows) {
                                mat[j][i] = nodearr.get(n).outcomes.get(outcomes_spot);
                                count1 = count1 + 1;
                            } else {
                                count1 = 0;
                                outcomes_spot = outcomes_spot + 1;
                                if (outcomes_spot >= num) {
                                    outcomes_spot = 0;
                                }
                                mat[j][i] = nodearr.get(n).outcomes.get(outcomes_spot);
                                count1 = count1 + 1;
                                ;
                            }
                        }
                    }
                }
            }
            /*
            getting rid of the rows and columns of given var

            we will go through the givs Arraylist ans see if the column names is the sane as the givs,
            if so we will get rid of the rows that contain an outcome that isnt given,
            and get rid of the column of that node and create a new matrix,
            if the matrix that was created is null we wil return null
             */
            boolean change = false;
            String first = "";
            int col = 0;
            for (int i = 0; i < givs.size(); i = i + 2) {
                change = false;
                for (int j = 0; mat != null && j < mat[0].length; j++) {
                    if (mat[0][j].equals(givs.get(i))) {
                        change = true;
                        String truth_val = givs.get(i + 1);
                        first = (String) mat[0][j];
                    }
                    for (int k = 0; k < mat[0].length; k++) {
                        if (mat[0][k].equals(first)) {
                            col = k;
                        }
                    }
                    if (change) {
                        for (int n = 1; n < mat.length; n++) {
                            if (!mat[n][col].equals(givs.get(i + 1))) {
                                for (int l = 0; l < mat[0].length; l++) {
                                    mat[n][l] = null;
                                }
                            } else {
                                mat[n][col] = null;
                            }
                        }
                        mat[0][col] = null;
                        int new_col = mat[0].length - 1;
                        int new_row = 0;
                        for (int n = 0; n < mat.length; n++) {
                            if (mat[n][mat[0].length - 1] != null) {
                                new_row = new_row + 1;
                            }
                        }
                        mat = make_smaller(mat, new_col, new_row);
                        change = false;
                    }
                }
            }
            if (mat != null) {
                for (int i = 0; i < mat.length; i++) {
                    //System.out.println(Arrays.toString(mat[i]));
                }
                //System.out.println();
                Node_mat m = new Node_mat(mat);
                return m;
            } else {
                return null;
            }
        } else { // we didnt create a mat for this node
            del_later.add(node.name); // we will add the nodes name to this array list
            // so that later we can get rid of the columns with this nodes name from other matrices
            return null;
        }
    }


    public static Object[][] get_rid_of_mid_columns(Object[][] mat) {
        int col = mat[0].length - 1;
        int row = mat.length;
        if (col > 1) {
            int count_row = 0;
            int count_col = 0;
            // start filling in the non-null values from the original mat
            // as we fill in the values we will make the count-col bigger
            // if it gets bugger or equals col we will change to 0
            // and make the count_row bigger

            Object[][] new_mat = new Object[row][col];
            for (int j = 0; j < mat.length; j++) { //row
                for (int i = 0; i < mat[0].length; i++) { //column
                    if (mat[j][i] != null) {
                        new_mat[count_row][count_col] = mat[j][i];
                        count_col = count_col + 1;
                        if (count_col >= col) {
                            count_col = 0;
                            count_row = count_row + 1;
                        }
                    }
                }
            }
            return new_mat;
        } else {
            // if there is only 1 column then we have no node
            return null;
        }
    }


    public static Object[][] make_smaller(Object[][] mat, int col, int row) {
        if (col > 1) {
            int count_row = 0;
            int count_col = 0;
            Object[][] new_mat = new Object[row][col];
            // start filling in the non-null values from the original mat
            // as we fill in the values we will make the count-col bigger
            // if it gets bugger or equals col we will change to 0
            // and make the count_row bigger
            for (int j = 0; j < mat.length; j++) { //row
                for (int i = 0; i < mat[0].length; i++) { //column
                    if (mat[j][i] != null) {
                        new_mat[count_row][count_col] = mat[j][i];
                        count_col = count_col + 1;
                        if (count_col >= col) {
                            count_col = 0;
                            count_row = count_row + 1;
                        }
                    }
                }
            }
            return new_mat;
        } else {
            // if there is only 1 column then we have no node
            return null;
        }
    }


    public static Object[][] join_matrix(Object[][] mat_smaller, Object[][] mat_bigger, String s, ArrayList<Nodes> nodearr) {
        System.out.println("join on " + s);
        ArrayList<String> col = new ArrayList<String>();// arrayList to hold the column names
        int rows = 1;


        // adding column names from the bigger matrix not including the column name we are joining on and the percent column name to col ArrayList
        for (int i = 0; i < mat_bigger[0].length - 1; i++) {
            if (!col.contains(mat_bigger[0][i])) {
                col.add((String) mat_bigger[0][i]);
            }
        }
        // adding column names to col ArrayList if aren't there
        for (int i = 0; i < mat_smaller[0].length; i++) {
            if (!col.contains(mat_smaller[0][i])) {
                col.add((String) mat_smaller[0][i]);
            }
        }
        // finding the amount of rows needed;
        for (int i = 0; i < col.size() - 1; i++) { // going through the col arrayList
            for (int j = 0; j < nodearr.size(); j++) { // going through the node arrayList
                if (nodearr.get(j).name.equals(col.get(i))) { // finding the matchers
                    rows = rows * nodearr.get(j).outcomes.size(); // rows = rows* the amount of outcomes the node has
                }
            }
        }
        rows = rows + 1; // add 1 more row for the column names

        // putting column names in new mat
        Object[][] new_mat = new Object[rows][col.size()];
        for (int i = 0; i < col.size(); i++) {
            new_mat[0][i] = col.get(i);
        }
       /*
            filling in the truth values
            we will start filling in the truth values starting wih the left most column,
            we will take the fill_in_rows var and divide by the amount of outcomes this column's node has
            we will start with the first outcome and each time we add it in we will bring up the counter by 1
            once the counter equals the var fill_in_rows we will reset the counter to 0, and start filling in the next node outcome,
            if we finished the nodes last outcome and there are more rows we will repeat the process
             */
        int fill_in_rows = rows - 1;
        for (int i = 0; i < new_mat[0].length - 1; i++) {
            String st = (String) new_mat[0][i];
            int count1 = 0;
            for (int n = 0; n < nodearr.size(); n++) {
                if (nodearr.get(n).name.equals(st)) {
                    int num = nodearr.get(n).getOutcomes().size();
                    int outcomes_spot = 0;
                    fill_in_rows = fill_in_rows / num;
                    for (int j = 1; j < rows; j++) {
                        if (count1 < fill_in_rows) {
                            new_mat[j][i] = nodearr.get(n).outcomes.get(outcomes_spot);
                            count1 = count1 + 1;
                        } else {
                            count1 = 0;
                            outcomes_spot = outcomes_spot + 1;
                            if (outcomes_spot >= num) {
                                outcomes_spot = 0;
                            }
                            new_mat[j][i] = nodearr.get(n).outcomes.get(outcomes_spot);
                            count1 = count1 + 1;
                            ;
                        }
                    }
                }
            }
        }
        // putting in percents
        ArrayList<Integer> col_small_in_new = new ArrayList<Integer>(); // the truth values spots from the small matrix in the new matrix
        ArrayList<Integer> col_big = new ArrayList<Integer>(); // the truth values spots from the big matrix in the new matrix= same as in the big matrix
        ArrayList<Integer> col_small = new ArrayList<Integer>(); // the truth values spots from the small matrix in the small matrix
        ArrayList<String> truth_val_row = new ArrayList<String>(); // this arrayList will hold the truth values of the row we rae filling
        boolean added = false;
        boolean wrong = false;
        /*
        we will go through the rows columns of the new matrix,
        we will add the values of the relevant columns to all the arrayList listed above
        we will add the the truth vals of the row into the arrayList
        we will find the correct row in the big matrix and put the percent into the new mat
        we will find the correct row in the smaller matrix and take the value from the big matrix and times them together and put into the new matrix
        */
        for (int i = 0; i < new_mat[0].length - 1; i++) {//columns
            for (int j = 0; j < mat_bigger[0].length - 1; j++) { // columns
                if (new_mat[0][i].equals(mat_bigger[0][j])) {
                    col_big.add(i);
                }
            }
            for (int j = 0; j < mat_smaller[0].length - 1; j++) { // columns
                if (new_mat[0][i].equals(mat_smaller[0][j])) {
                    col_small_in_new.add(i);
                }
            }
        }
        for (int j = 0; j < col_small_in_new.size(); j++) {
            String x = (String) new_mat[0][col_small_in_new.get(j)];
            for (int k = 0; k < mat_smaller[0].length - 1; k++) {// columns
                if (mat_smaller[0][k].equals(x)) {
                    col_small.add(k);
                }
            }
        }
        // adding the percent from the bigger matrix
        for (int i = 1; i < new_mat.length; i++) { //rows
            truth_val_row.clear();
            for (int j = 0; j < col_big.size(); j++) {
                truth_val_row.add((String) new_mat[i][col_big.get(j)]);
            }
            added = false;
            for (int k = 1; k < mat_bigger.length && !added; k++) { // rows
                wrong = false;
                for (int l = 0; l < mat_bigger[0].length - 1 && !wrong; l++) { // columns
                    if (!truth_val_row.get(l).equals(mat_bigger[k][l])) {
                        wrong = true;
                    }
                }
                if (!wrong) {
                    new_mat[i][col.size() - 1] = mat_bigger[k][mat_bigger[0].length - 1];
                    added = true;
                }
            }
        }
        // adding the percent from the smaller mat
        for (int i = 1; i < new_mat.length; i++) { //rows
            truth_val_row.clear();
            for (int j = 0; j < col_small_in_new.size(); j++) {
                truth_val_row.add((String) new_mat[i][col_small_in_new.get(j)]);
            }
            added = false;
            for (int k = 1; k < mat_smaller.length && !added; k++) { // rows
                wrong = false;
                for (int l = 0; l < col_small.size() && !wrong; l++) {
                    String n = (String) truth_val_row.get(l);
                    String o = (String) mat_smaller[k][col_small.get(l)];
                    if (!truth_val_row.get(l).equals(mat_smaller[k][col_small.get(l)])) {
                        wrong = true;
                    }
                }
                if (!wrong) {
                    double d1 = (double) new_mat[i][col.size() - 1];
                    double d2 = (double) mat_smaller[k][mat_smaller[0].length - 1];
                    new_mat[i][col.size() - 1] = d1 * d2;
                    added = true;
                    break;
                }
            }
        }

        System.out.println("printing mat after join");
        for (int i = 0; i < new_mat.length; i++) {
            System.out.println(Arrays.toString(new_mat[i]));
        }
        System.out.println();
        return new_mat;
    }


    public static Object[][] eliminate_matrix(Object[][] mat_a, String s, int div, ArrayList<Nodes> nodearr) {
        System.out.println("eliminate " + s);
        int rows = mat_a.length; // the amount of rows in the given matrix
        int rows1 = ((rows - 1) / div) + 1; // the amount of rows in the new matrix,
        // taking the rows in the given matrix-1 and dividing by the amount of outcomes the node we are eliminating by has then adding 1.
        int col = mat_a[0].length; // the amount of columns in the given matrix
        int col1 = mat_a[0].length - 1; // the amount of column in the new matrix, 1 less
        boolean skip = false; // this bollen represents if we need to skip this column

        Object[][] new_mat = new Object[rows1][col1]; // creating the new mat
        // filling in the columns
        for (int i = 0; i < col1; i++) { // going through the columns
            if (!skip) {
                if (mat_a[0][i].equals(s)) { // if we see the column we want to get rid of
                    skip = true;
                    new_mat[0][i] = mat_a[0][i + 1]; // fill in the next value
                } else {
                    new_mat[0][i] = mat_a[0][i]; // fill in this value
                }
            } else {
                new_mat[0][i] = mat_a[0][i + 1]; // fill in the next value
            }
        }
         /*
            filling in the truth values
            we will start filling in the truth values starting wih the left most column,
            we will take the fill_in_rows var and divide by the amount of outcomes this column's node has
            we will start with the first outcome and each time we add it in we will bring up the counter by 1
            once the counter equals the var fill_in_rows we will reset the counter to 0, and start filling in the next node outcome,
            if we finished the nodes last outcome and there are more rows we will repeat the process
             */
        int fill_in_rows = rows1 - 1;
        for (int i = 0; i < col1 - 1; i++) {
            String s1 = String.valueOf(new_mat[0][i]);
            int count1 = 0;
            for (int n = 0; n < nodearr.size(); n++) {
                if (nodearr.get(n).name.equals(s1)) {
                    int num = nodearr.get(n).getOutcomes().size();
                    int outcomes_spot = 0;
                    fill_in_rows = fill_in_rows / num;
                    for (int j = 1; j < rows1; j++) {
                        if (count1 < fill_in_rows) {
                            new_mat[j][i] = nodearr.get(n).outcomes.get(outcomes_spot);
                            count1 = count1 + 1;
                        } else {
                            count1 = 0;
                            outcomes_spot = outcomes_spot + 1;
                            if (outcomes_spot >= num) {
                                outcomes_spot = 0;
                            }
                            new_mat[j][i] = nodearr.get(n).outcomes.get(outcomes_spot);
                            count1 = count1 + 1;
                        }
                    }
                }
            }
        }
        double sum = 0;
        String v = "";
        double percent = 0;
        ArrayList<String> val1 = new ArrayList<String>(); // contains the row truth values in the new matrix
        ArrayList<String> val2 = new ArrayList<String>(); // contains the row truth values from the old matrix without the column we wnt to eliminate
        /*
        go through the rows in the new matrix
        add the column's truth values to val1 arrayList
        go through the rows in the old matrix
        add the column's truth values to val2 arrayList if it isn't for the value we want to eliminate
        if val1 equals val2, var percent equals that rows percent
        var sum will contain the sum of all the percents
        put the var sum in the new matrix
         */
        for (int i = 1; i < new_mat.length; i++) { // needs to be row
            val1.clear();
            for (int j = 0; j < new_mat[0].length - 1; j++) { // needs to be column
                v = (String) (new_mat[i][j]);
                val1.add(v);
            }
            sum = 0;
            for (int k = 1; k < mat_a.length; k++) { // needs to be row
                val2.clear();
                for (int l = 0; l < mat_a[0].length - 1; l++) { // needs to be column
                    if (!mat_a[0][l].equals(s)) {
                        v = (String) (mat_a[k][l]);
                        val2.add(v);
                    }
                }
                if (val1.toString().equals(val2.toString())) {
                    percent = (Double) mat_a[k][col - 1];
                    sum = sum + percent;
                }
            }
            new_mat[i][col1 - 1] = sum;
        }
        System.out.println("printing mat after elimination");
        for (int i = 0; i < new_mat.length; i++) {
            System.out.println(Arrays.toString(new_mat[i]));
        }
        System.out.println();
        return new_mat;
    }


    public static boolean ancestor(Nodes start, Nodes end, ArrayList<Nodes> nodearr) {
        ArrayList<Boolean> ans = new ArrayList<Boolean>();
        is_ancestor(start, end, nodearr, ans);
       // if the arrayList has the boolean true then end is an ancestor of start.
        boolean answer = false;
        for (int i = 0; i < ans.size(); i++) {
            if (ans.get(i).equals(true)) {
                answer = true;
            }
        }
        return answer;

    }


    public static void is_ancestor(Nodes start, Nodes end, ArrayList<Nodes> nodearr, ArrayList<Boolean> ans) {
        if (start.parents.size() < 1) // if start has no parents end can't be an ancestor
            ans.add(false); // we didn't find an ancestor we will add false to the arraylist
        else {
            // recursively call this function till we get to the tops of the graph
            for (int i = 0; i < start.parents.size(); i++) {
                if (start.parents.get(i).equals(end)) {
                    ans.add(true); // we found an ancestor we will add true to the arraylist
                } else {
                    is_ancestor(start.parents.get(i), end, nodearr, ans);
                }
            }
        }
    }


    public static Node_mat get_rid_of_cols(Object[][] mat, String val) {
        int col = 0;
        //find the column that we wnat to get rid of
        for (int i = 0; i < mat[0].length - 1; i++) {
            if (mat[0][i].equals(val)) {
                col = i;
            }
        }
        for (int i = 0; i < mat.length; i++) {
            mat[i][col] = null; // turn all the values of the column to null
        }
        Object[][] new_mat = get_rid_of_mid_columns(mat);
        if (new_mat == null) {
            Node_mat n = null;
            return n;
        } else {
            Node_mat n = new Node_mat(new_mat);
            return n;
        }


    }


}