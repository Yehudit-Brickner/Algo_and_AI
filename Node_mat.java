package dom_parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Node_mat  {



	public Object[][] mat;
	public int rows;
	public ArrayList<Integer>ascii_values=new ArrayList<Integer>();

	public Node_mat(Object[][] mat){
		this.mat=mat;
		this.rows=mat.length;
		int x=0;
		for (int i=0; i<mat[0].length;i++) {
			x=0;
			String s=(String) mat[0][i];
			for (int j=0; j<s.length();j++) {
				x=x+(int) s.charAt(j);
			}
			ascii_values.add(x);
		}
	}

	public String toString() {

		String s="rows: " + rows + " ascii_values: "+ ascii_values+ " mat: "+"\n" ;
		for (int i=0; i<mat.length;i++) {
			s=s+Arrays.toString(mat[i])+"\n";
		}
		return s;
	}

	public Object[][] getMat() {
		return mat;
	}

	public int getRows() {
		return rows;
	}

	public ArrayList<Integer> getAscii_values() {
		return ascii_values;
	}
	

	public static class Node_mat_Comparator implements Comparator<Node_mat> {

		@Override
		public int compare(Node_mat a, Node_mat b) {
			if (a.rows==b.rows) {
				int i=a.ascii_values.size();
				int j=a.ascii_values.size();
				int count=0;
				int x=0;
				while(x==0) {
					if (count<i && count<j) {
						if(a.ascii_values.get(count)>b.ascii_values.get(count)) {
							x=1;
							return 1;
						}
						else if(b.ascii_values.get(count)>a.ascii_values.get(count)) {
							x=-1;
							return -1;
						}
						count=count+1;
					}
					else {
						if(i<j) {
							x=1;
							return 1;
						}
						else {
							x=-1;
							return -1;
						}
					}
				}
			}
			else if(b.rows>a.rows) {
				return -1;
			}
			else {
				return 1;
			}
			
			return 0;
		
		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		Object[][ ] a= new Object[][ ]{{"A","P(...)"},{"T",0.63},{"F",0.0005}};
		Node_mat A=new Node_mat(a);
		//System.out.println(A);
		
		
		Object[][ ] b= new Object[][]{{"E","B","A","P(...)"},{"T","T","T",0.95},{"T","T","F",0.05},{"T","F","T",0.29},
		{"T","F","F",0.71},{"F","T","T",0.94},{"F","T","F",0.06},{"F","F","T",0.001},{"F","F","F",0.999}};
		Node_mat B=new Node_mat(b);
		//System.out.println(B.toString());

		
		Object[][ ] c= new Object[][ ]{{"A","B","P(...)"},{"T","T",0.63},{"T","F",0.0005},{"F","T",0.63},{"F","F",0.0005}};
		Node_mat C=new Node_mat(c);
		//System.out.println(C);
		
		
		Object[][ ] d= new Object[][ ]{{"A","D","P(...)"},{"T","T",0.63},{"T","F",0.0005},{"F","T",0.63},{"F","F",0.0005}};
		Node_mat D=new Node_mat(d);
		//System.out.println(C);
		
		
		
		ArrayList<Node_mat> mats=new ArrayList<Node_mat>();
		mats.add(A);
		mats.add(D);
		mats.add(B);
		mats.add(C);
		System.out.println(mats);
		
		
		System.out.println();
		System.out.println();
		Node_mat_Comparator x= new Node_mat_Comparator();
		
	
		Collections.sort(mats, x);
		
	
		System.out.println(mats);
	}

	


}
