package dom_parser;
import java.util.ArrayList;
import java.util.Arrays;
public class Nodes {
	
	public String name;
	public Boolean seen;
	public int state;
	public ArrayList<String> outcomes=new ArrayList<String>();
	public ArrayList<Double> percents=new ArrayList<Double>();
	public ArrayList<Nodes> parents=new ArrayList<Nodes>();
	public ArrayList<Nodes> kids=new ArrayList<Nodes>();
	
	
	public Nodes(String name){
		this.name=name;	
		seen=false;
		state=0;
}
	
	
	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public ArrayList getParents() {
		return parents;
	}



	public void setParents(ArrayList parents) {
		this.parents = parents;
	}



	public ArrayList getKids() {
		return kids;
	}



	public void setKids(ArrayList kids) {
		this.kids = kids;
	}


	
	public String getName() {
		return name;
	}


	private void setName(String name) {
		this.name = name;
	}


	public Boolean getSeen() {
		return seen;
	}


	public void setSeen(Boolean seen) {
		this.seen = seen;
	}


	public ArrayList getOutcomes() {
		return outcomes;
	}


	private void setOutcomes(ArrayList outcomes) {
		this.outcomes = outcomes;
	}


	public ArrayList getPercents() {
		return percents;
	}


	private void setPercents(ArrayList percents) {
		this.percents = percents;
	}

	public String toString() {
		String p="[";
		for (int i=0; i< parents.size();i++) {
			p=p+((Nodes) parents.get(i)).getName()+" ";
		}
		p=p+"]";
		String k="[";
		for (int i=0; i<kids.size();i++) {
			k=k+((Nodes) kids.get(i)).getName()+" ";
		}
		k=k+"]";
	String s= "name: " + this.name +" seen: " + this.seen +" state: "+ this.state + " parents: " + p+" kids: "+ k+ " outcomes: "+ this.outcomes+ " percents: "+ this.percents ;
		return s;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
}
