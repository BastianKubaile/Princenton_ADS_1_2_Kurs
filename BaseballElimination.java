

import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private String[] teams;
    private Stack<String> teams2 = new Stack<>(); 
    private Map<String, Integer> ids; 
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;//Games against another team
    
    public BaseballElimination(String filename){
	In in = new In(filename);
	int numOfTeams = Integer.parseInt(in.readLine());
	teams = new String[numOfTeams];
	ids = new HashMap<>(numOfTeams);
	wins = new int[numOfTeams];
	losses = new int[numOfTeams];
	remaining = new int[numOfTeams];
	against = new int[numOfTeams][numOfTeams];
	for(int i = 0; i < teams.length && in.hasNextLine(); i++){
	    teams[i] = in.readString();
	    teams2.push(teams[i]);
	    ids.put(teams[i], i);
	    wins[i] = in.readInt();
	    losses[i] = in.readInt();
	    remaining[i] = in.readInt();
	    for(int j = 0; j < against.length; j++){
		against[i][j] = in.readInt();
	    }
	}
    }
    
    private String team(int id){
	return teams[id];
    }
    
    private int id(String team){
	return ids.get(team);
    }
    
    public Iterable<String> teams(){
	return teams2;
    }
    
    public int numberOfTeams(){
	return teams.length;
    }
    
    public int wins(String team){
	return wins[id(team)];
    }
    
    public int losses(String team){
	return losses[id(team)];
    }

    public int remaining(String team){
	return remaining[id(team)];
    }
    
    public int against(String str1, String str2){
	return against[id(str1)][id(str2)];
    }
    
    public boolean isEliminated(String team){
 	for(int i = 0; i < wins.length; i++){
 	    if(id(team) == i ) continue;
 	    if(wins[i] > wins(team) + remaining(team)) return true;
 	}
 	double maxFlow = 0;
 	OUTER_LOOP: for(int i = 0; i < against.length; i++){
 	    for(int j = i+1; j < against[i].length; j++){
 		if(j == id(team)) continue;
 		if(i == id(team)) { j--; continue OUTER_LOOP;}
 		maxFlow += against[i][j];
 	    }
 	}
	FordFulkerson ff = this.fordFulkerson(team);
	double flow = ff.value();
	if(flow < maxFlow) return true;
	return false;
    }
    
    public Iterable<String> certificateOfElimination(String team){
	if(!isEliminated(team)) return null;
	Stack<String> eliminators = new Stack<>();
	boolean isEasilyEliminated = false;
 	for(int i = 0; i < wins.length; i++){
 	    if(id(team) == i ) continue;
 	    if(wins[i] > wins(team) + remaining(team)){
 		isEasilyEliminated = true; 
 		eliminators.push(teams[i]);
 	    }
 	}
	
	if(isEasilyEliminated) return eliminators;
	FordFulkerson ff = fordFulkerson(team);
	int nT =  teams.length - 1;
	int numOfGames = (nT*nT+nT)/2-nT;
	int v = nT + 2 +numOfGames;//Number of Vertices
	for(int i = v - nT -1, j = 0; i < v-1;i++,j++){
	    if(id(team) == j) j++;
	    if(ff.inCut(i)){
		eliminators.push(team(j));
	    }
	}
	return eliminators;
    }
    
    private FordFulkerson fordFulkerson(String team){
	int nT = teams.length-1;
	int numOfGames = (nT*nT+nT)/2-nT;
	FlowNetwork network = new FlowNetwork(nT+2+numOfGames);
	//StdOut.println(network.toString());
	
	//Initializes the game edges
	OUTER_LOOP: for(int i = 1,k = 0; i < numOfGames+1 && k < against.length; k++){
	    for(int j = k+1; j < against[k].length; j++,i++){
		if(j == this.id(team)){ i--; continue ;}
		if(k == this.id(team)){ continue OUTER_LOOP;}
		//StdOut.println("Fight between: " + j + "/" + k + "; games left: " + against[k][j]);
		FlowEdge edge = new FlowEdge(0, i, against[k][j]);
		network.addEdge(edge);
	    }
	}
	//StdOut.println(network.toString());
	
	//Initializes the team edges
	for(int i = network.V()-nT-1, j = 0; i < network.V()-1 && j < teams.length;i++,j++){
	    if(j == id(team)){ i--;continue;}
	    int maxWins = wins(team) + remaining(team);
	    //StdOut.println("Team: " + team(j));
	    FlowEdge edge = new FlowEdge(i, network.V()-1, maxWins - wins[j]);
	    network.addEdge(edge);
	}
	
	//StdOut.println("With team edges!");
	//StdOut.println(network.toString());
	int addition = 1 + numOfGames;
	for(int w1 = 0, gameIndex = 1; w1 < teams.length-2 ;w1++){
	    for(int w2 = w1+1; w2 < teams.length-1;w2++, gameIndex++){
		FlowEdge edge1 = new FlowEdge(gameIndex, w1 + addition, Double.POSITIVE_INFINITY);
		FlowEdge edge2 = new FlowEdge(gameIndex, w2 + addition, Double.POSITIVE_INFINITY);
		network.addEdge(edge1);
		network.addEdge(edge2);
	    }
	}
	//StdOut.println(network.toString());
	FordFulkerson ff = new FordFulkerson(network, 0, network.V()-1);
	return ff;
	
    }
    


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("C:\\Java\\Programme\\PrincetonCourse\\testing\\baseball\\teams4a.txt");
        StdOut.println(division.isEliminated("Bin_Ladin"));
        /*for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }*/
    }


}


