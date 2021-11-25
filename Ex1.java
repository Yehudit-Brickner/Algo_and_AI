
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Ex1 {

    public static void main(String[] args) {

        String s1 = "algo2.txt";
        readFile(s1);

        /*
        * add comments to
        * bayesballdown1
        * bayesballup1
        * variable elimination
        * create mat
        * join matrix
        * eliminate matrix
         */
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
        boolean breeaak = false; // we will use this when we find the node in the middle of loop
        if (n1.equals(n2)) {
            isdependent.add(true); // we found the node
        }
        if (n1.state != 2) {
            n1.setState(1);
            //if(n1.kids.size()>=1) {
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
                            //break;
                        }
                        // the parent is seen cant continue;
                        else if (n1.kids.get(i).parents.get(j).seen == true) {
                            isdependent.add(false); // can't go anywhere
                            //break;
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
            //}
        }
    }


    public static void bayesBallup1(Nodes n1, Nodes n2, ArrayList<Boolean> isdependent, boolean seenE, boolean hasBeenSeen) {
        boolean breeaak = false;
        if (n1.equals(n2)) {
            isdependent.add(true);
        }

        if (n1.state != 2) {
            n1.setState(1);
            //if (n1.parents.size()>=1) {
            for (int i = 0; i < n1.parents.size(); i++) {
                // mark that weve finished with nide
                if (i == n1.parents.size() - 1) {
                    n1.setState(2);
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
                    break;
                }
                // mark that weve finished with nide
                if (i == n1.kids.size() - 1) {
                    n1.setState(2);
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
                        ////
                        ////
                        ///changed up to down
                        bayesBalldown1(n1.kids.get(i), n2, isdependent, seenE, hasBeenSeen);
                    }
                }
            }
            //}
        }
    }


    public static String variableElimination1(String s, String[] given, String[] hidden, ArrayList<Nodes> nodearr) {

        int count1 = 0;
        for (int i = 0; i < s.length(); i++) {
            count1 = count1 + 1;
            if (s.charAt(i) == '=') {
                break;
            }
        }

        String Qwry = s.substring(0, count1 - 1);
        //System.out.println();
        System.out.println("QWRY: " + Qwry);
        //	System.out.println("main node:" +s);
        //	System.out.println("hidden: "+Arrays.toString(hidden));
        //	System.out.println("given: "+Arrays.toString(given));
        //	System.out.println();

        int mult = 0;
        int add = 0;
        int div = 0;
        ArrayList<Node_mat> mats = new ArrayList<Node_mat>();
        ArrayList<String> del_later = new ArrayList<String>();
        for (int i = 0; i < nodearr.size(); i++) {
            //String str=nodearr.get(i).getName();
            Node_mat mat = create_mat(nodearr.get(i), nodearr, given, Qwry, del_later);
            if (mat != null) {
                mats.add(mat);
            }
        }
        // deleting columns that contains nodes i dont need;
        //for(int i=0;i<del_later.size();i++) {
        while (del_later.size() >= 1) {
            int size = del_later.size() - 1;
            for (int j = 0; j < mats.size(); j++) {
                for (int k = 0; k < mats.get(j).getMat()[0].length; k++) {
                    if (mats.get(j).getMat()[0][k].equals(del_later.get(size))) {
                        Node_mat mat1 = get_rid_of_cols(mats.get(j).getMat(), del_later.get(size));
                        mats.remove(j);
                        if (mat1 != null) {
                            mats.add(mat1);
                        }
                    }
                }
            }
            del_later.remove(size);
        }
        System.out.println("printing all the mats");
        for (int i = 0; i < mats.size(); i++) {
            for (int j = 0; j < mats.get(i).mat.length; j++) {
                System.out.println(Arrays.toString(mats.get(i).mat[j]));
            }

            System.out.println();
        }
// if there is only 1 mat then we need to find the correct value and return it
        if (mats.size() == 1) {
            count1 = s.length();
            for (int i = s.length() - 1; i >= 0; i--) {
                count1 = count1 - 1;
                if (s.charAt(i) == '=') {
                    break;
                }
            }
            double last = 0;
            Object[][] finale_mat = mats.get(0).getMat();
            String val = s.substring(count1 + 1);
            for (int i = 1; i < finale_mat.length; i++) {
                if (finale_mat[i][0].equals(val)) {
                    last = (Double) finale_mat[i][finale_mat[0].length - 1];
                }
            }
            String ans = last + "," + 0 + "," + 0;
            return ans;
        }



		/*
		System.out.println("printing the mats in the arraylist");
		for (int i=0;i<mats.size();i++) {
			System.out.println(mats.get(i));
		}
		 */


        // after we have all the tables we need in the ArrayList,
        // we will start to eliminate variables in order

        ArrayList<Node_mat> hidden_mats = new ArrayList<Node_mat>();
        // going through mats and putting all the mats that contain a hidden together in a ArrayList
        // so that i can start the joining.
        Node_mat.Node_mat_Comparator comp = new Node_mat.Node_mat_Comparator();
        int count = 0;
        for (int k = 0; k < hidden.length; k++) {
            hidden_mats.clear();
            count = 0;
            for (int i = 0; i < mats.size(); i++) {
                for (int l = 0; l < mats.get(i).mat[0].length; l++) {
                    if (mats.get(i).mat[0][l].equals(hidden[k])) {
                        hidden_mats.add(mats.get(i));
                        count = count + 1;
                    }
                }
            }
            boolean removed = false;

            for (int i = mats.size() - 1; i >= 0; i--) {
                removed = false;
                for (int l = 0; !removed && l < mats.get(i).mat[0].length; l++) {
                    if (mats.get(i).mat[0][l].equals(hidden[k])) {
                        mats.remove(mats.get(i));
                        removed = true;
                    }
                }
            }

            while (hidden_mats.size() > 1) {
                Collections.sort(hidden_mats, comp);
                Object[][] mat_new = join_matrix(hidden_mats.get(0).getMat(), hidden_mats.get(1).getMat(), hidden[k], nodearr);
                Node_mat n = new Node_mat(mat_new);
                mult = mult + (n.getRows() - 1);
                hidden_mats.remove(1);
                hidden_mats.remove(0);
                hidden_mats.add(n);

            }
            if (hidden_mats.size() > 0) {
                for (int i = 0; i < nodearr.size(); i++) {
                    if (nodearr.get(i).getName().equals(hidden[k])) {
                        div = nodearr.get(i).outcomes.size();
                    }
                }
                Object[][] m = eliminate_matrix(hidden_mats.get(0).getMat(), hidden[k], div, nodearr);
                Node_mat n = new Node_mat(m);
                mats.add(n);
                add = add + (((hidden_mats.get(0).getRows()) / div) * (div - 1));
            }
        }
        //System.out.println("mult=" + mult + " add= " +add +"\n"+ mats);

        String v = "";
        count = 0;
        for (int i = 0; i < s.length(); i++) {
            count = count + 1;
            if (s.charAt(i) == '=') {
                break;
            }
        }
        v = s.substring(0, count - 1);

        while (mats.size() > 1) {
            Collections.sort(mats, comp);
            Object[][] new_mat = join_matrix(mats.get(0).getMat(), mats.get(1).getMat(), v, nodearr);
            mult = mult + mats.get(1).getRows() - 1;
            mats.remove(1);
            mats.remove(0);
            Node_mat new_mat1 = new Node_mat(new_mat);
            mats.add(new_mat1);
        }

        Object[][] finale_mat = mats.get(0).getMat();

        double last = 0;
        for (int i = 1; i < finale_mat.length; i++) {
            last = last + (Double) finale_mat[i][finale_mat[0].length - 1];
            add = add + 1;
        }
        add = add - 1;
        last = 1.0 / last;

        count1 = s.length();
        for (int i = s.length() - 1; i >= 0; i--) {
            count1 = count1 - 1;
            if (s.charAt(i) == '=') {
                break;
            }
        }
        String val = s.substring(count1 + 1);
        //	System.out.println("val: " + val);

        for (int i = 1; i < finale_mat.length; i++) {
            if (finale_mat[i][0].equals(val)) {
                last = last * (Double) finale_mat[i][finale_mat[0].length - 1];
            }
        }
        last = (double) Math.round(last * 100000d) / 100000d;
        //System.out.println(last);
        System.out.println("mult=" + mult + " add= " + add + " final ans: " + last);
        System.out.println();
        String ans = last + "," + add + "," + mult;
        return ans;
    }


    public static Node_mat create_mat(Nodes node, ArrayList<Nodes> nodearr, String[] given, String Qwry, ArrayList<String> del_later) {

        int count1 = 0;
        String s1;
        String s2;

        ArrayList<String> givs = new ArrayList<String>();
        if (given[0] != " ") {
            for (int i = 0; i < given.length; i++) {
                count1 = 0;
                for (int j = 0; j < given[i].length(); j++) {
                    if (given[i].charAt(j) == '=') {
                        break;
                    }
                    count1 = count1 + 1;
                }
                s1 = given[i].substring(0, count1);
                s2 = given[i].substring(count1 + 1);
                givs.add(s1);
                givs.add(s2);
            }
        }
        ArrayList<Nodes> given_nodes = new ArrayList<Nodes>();
        for (int j = 0; j < nodearr.size(); j++) {
            for (int k = 0; k < givs.size(); k = k + 2) {
                if (nodearr.get(j).getName().equals(givs.get(k))) {
                    given_nodes.add(nodearr.get(j));
                }
            }
        }

        boolean is_Q_or_g = false;
        if (node.getName().equals(Qwry)) {
            is_Q_or_g = true;
        }
        for (int i = 0; i < givs.size(); i = i + 2) {
            if (node.getName().equals(givs.get(i))) {
                is_Q_or_g = true;
            }
        }

        Nodes n1;
        ArrayList<Boolean> ancestory = new ArrayList<Boolean>();
        String isIndependent = "";
        boolean isAncestor = false;
        if (!is_Q_or_g) {
            for (int j = 0; j < nodearr.size(); j++) {
                if (nodearr.get(j).getName().equals(Qwry)) {
                    n1 = nodearr.get(j);
                    // is the node an ancestor of qwry
                    isAncestor = ancestor(n1, node, nodearr);
                    //System.out.println(node.name+ " is a ancetor of "+ Qwry+" : " +isAncestor );
                    ancestory.add(isAncestor);
                    isIndependent = bayesBall(node, n1, given_nodes, nodearr);
                    //System.out.println("is " +Qwry + " isIndependent: form "+node.getName()+ "? "+isIndependent);
                } else {
                    // is node an ancestor of given
                    for (int k = 0; k < givs.size(); k = k + 2) {
                        if (nodearr.get(j).getName().equals(givs.get(k))) {
                            n1 = nodearr.get(j);
                            isAncestor = ancestor(n1, node, nodearr);
                            //System.out.println(node.name+ " is a ancetor of "+ givs.get(k)+" : " +isAncestor );
                            ancestory.add(isAncestor);
                        }
                    }
                }

                //isIndependent=bayesBall(n1,node,arr,nodearr);
                //System.out.println("is " +Qwry + " isIndependent: form "+node.getName()+ "? "+isIndependent);
            }
        }
        isAncestor = false;
        for (int i = 0; i < ancestory.size(); i++) {
            if (ancestory.get(i).equals(true)) {
                isAncestor = true;
            }
        }

        if (is_Q_or_g || (isAncestor && isIndependent.equals("no"))) {

            int outcomes_spot = 0;
            int num = 1;
            //int count1=0;
            int rows = node.percents.size() + 1;
            int columns = 2 + node.parents.size();
            int fill_in_rows = rows - 1;
            boolean redo = true;


            Object[][] mat = new Object[rows][columns];
            mat[0][columns - 2] = node.name;
            mat[0][columns - 1] = "p(...)";

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
            //filling in the truth values
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
            // getting rid if the rows and columns of given var

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
        } else {
            del_later.add(node.name);
            return null;
        }
    }

/*
    public static boolean need_to_create_new_mat(Object[][] mat) {
        boolean redo = false;
        int columns = mat[0].length;
        for (int i = 0; i < columns; i++) {
            if (mat[0][i] == null) {
                redo = true;
            }
        }
        return redo;
    }
*/

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

/*
    public static Object[][] get_rid_of_rows(Object[][] mat, String s1, String s2) {
        int rows = mat.length;
        int columns = mat[0].length;
        int i = 0;
        int count = 0;
        for (i = 0; i < columns; i++) {
            if (mat[0][i] == s1) {
                break;
            }
        }
        for (int j = 1; j < rows; j++) {
            if (mat[i][j] == s2) {
                count = count + 1;
            }
        }
        int rows1 = rows - count;
        int columns1 = columns - 1;
        Object[][] mat1 = new Object[rows1][columns1];

        return null;
    }
*/

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
        //System.out.println("printing s drom join_matrix " +s);
        ArrayList<String> col = new ArrayList<String>();
        //ArrayList<Double> outs=new ArrayList<Double>();
        //int rows_small=mat_smaller.length;
        //int rows_big=mat_bigger.length;
        int rows = 1;


        // adding column names to col ArrayList
        for (int i = 0; i < mat_bigger[0].length - 1; i++) {
            if (!col.contains(mat_bigger[0][i])) {
                col.add((String) mat_bigger[0][i]);
            }
        }
        // adding column names to col ArrayList if arent there
        for (int i = 0; i < mat_smaller[0].length; i++) {
            if (!col.contains(mat_smaller[0][i])) {
                col.add((String) mat_smaller[0][i]);
            }
        }
        // finding the amount of rows needed;
        for (int i = 0; i < col.size() - 1; i++) {
            for (int j = 0; j < nodearr.size(); j++) {
                if (nodearr.get(j).name.equals(col.get(i))) {
                    rows = rows * nodearr.get(j).outcomes.size();
                }
            }
        }
        rows = rows + 1;

        // putting column names in new mat
        Object[][] new_mat = new Object[rows][col.size()];
        for (int i = 0; i < col.size(); i++) {
            new_mat[0][i] = col.get(i);
        }
        // putting truth value in new mat
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
                        //if(count1<((rows-1)/Math.pow(num, i+1))) {
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
        ArrayList<Integer> col_small_in_new = new ArrayList<Integer>();
        ArrayList<Integer> col_big = new ArrayList<Integer>();
        ArrayList<Integer> col_small = new ArrayList<Integer>();

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


        ArrayList<String> truth_val_row = new ArrayList<String>();
        boolean added = false;
        boolean wrong = false;
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
        // if added cols in different order we get a problem
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
        int rows = mat_a.length;
        int rows1 = ((rows - 1) / div) + 1;
        int col = mat_a[0].length;
        int col1 = mat_a[0].length - 1;
        boolean skip = false;
        int skipping = 0;
        Object[][] new_mat = new Object[rows1][col1];
        // filling in the columns
        for (int i = 0; i < col1; i++) {
            if (!skip) {
                if (mat_a[0][i].equals(s)) {
                    skip = true;
                    skipping = i;
                    new_mat[0][i] = mat_a[0][i + 1];
                } else {
                    new_mat[0][i] = mat_a[0][i];
                }
            } else {
                new_mat[0][i] = mat_a[0][i + 1];
            }
        }
        //filling in the truth values
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
                            ;
                        }
                    }
                }
            }
        }
        double sum = 0;
        //	int count=0;
        String v = "";
        double percent = 0;
        ArrayList<String> val1 = new ArrayList<String>();
        ArrayList<String> val2 = new ArrayList<String>();
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