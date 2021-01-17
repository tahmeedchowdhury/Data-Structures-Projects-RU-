package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;
 
public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<String> ans = new ArrayList<String>();
		Queue<String> tobeseen = new Queue<String>();
		ArrayList<String> seen = new ArrayList<String>();
		ArrayList<String> seenby = new ArrayList<String>();
		boolean found = false;
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		if(g.map.containsKey(p1) == false || g.map.containsKey(p2) == false) {
			return ans;
		}
		Person per1 = g.members[g.map.get(p1)];
		if(per1.first == null) {
			return ans;
		}
		//come here if previous cases did not work
		tobeseen.enqueue(p1);
		while(tobeseen.isEmpty() == false) {
			String person = tobeseen.dequeue();
			Friend friend = g.members[g.map.get(person)].first; //starting point, already checked that it exists, so this will not be null
			if(seen.contains(g.members[g.map.get(person)].name) == false) {
					seen.add(g.members[g.map.get(person)].name);
					seenby.add("null");
			}
			//start BFS search until reaching person 2
			
			while(friend != null) {  //while a path is still open
				if(seen.contains(g.members[friend.fnum].name) == false) { //this friend has not been seen so far
					seen.add(g.members[friend.fnum]. name); //add current friend to seen
					seenby.add(person);
					if(g.members[friend.fnum].name.equals(p2)) { // person found. should only need the first one where p2 shows up
						found = true;
						break;
					}
					else {
						tobeseen.enqueue(g.members[friend.fnum].name);
					}
				}
				friend = friend.next; //next friend
			}
			if(found == true) {
				break;
			}
		}
		 //comes to here if found equal true
		if (found == true) { //backtrack to get shortest path
			ans = backtracktogetsol(seen,seenby,p1,p2);
		} //if found isn't true then path just doesn't exist, so ans is empty
		
		return ans;
		
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		ArrayList<ArrayList<String>> ans = new ArrayList<ArrayList<String>>(); //the answer list
		ArrayList<String> seen = new ArrayList<String>(); //list of seen people
		for(int i = 0; i < g.members.length;i++) {
			if(g.members[i].school != null) {
			if(g.members[i].school.equals(school) && seen.contains(g.members[i].name) == false ) {
				seen.add(g.members[i].name);
				ans.add(getClique(g,school,seen, g.members[i].name));
			}
		}
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return ans;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> ans = new ArrayList<String>();
		ArrayList<String> seen = new ArrayList<String>();
		int[] dfsnums = new int[g.members.length];
		int[] backnums = new int[g.members.length];
		for(int i =0; i < g.members.length; i++) {
			if(seen.contains(g.members[i].name) == false) { //DFS needs main calling method
				connectordfs(g,ans,seen,i,dfsnums,backnums,1);
				removesource(g, ans, i, dfsnums, backnums);
			}
		}
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return ans;
		
	}
	
	
	//helper methods
	
	//methods for shortestpath
	
	private static ArrayList<String> backtracktogetsol(ArrayList<String> seen, ArrayList<String> seenby, String p1, String p2) {
		ArrayList<String> ans = new ArrayList<String>();
		ans.add(p2); // adds the person that chain ends with
		String next = seenby.get(seenby.size()-1); //person who found p2
		while(next.equals("null") == false) { // while next hasn't reached source
			ans.add(0,next); // add to beginning since this is backtracking
			next = seenby.get(seen.indexOf(next)); //set next to who saw the previous person
		}
		return ans; //this should be answer
	}
	
	
	
	//methods for cliques
	private static ArrayList<String> getClique(Graph g, String school, ArrayList<String> seen, String name) {
		ArrayList<String> ans = new ArrayList<String>();
		ans.add(name); //adds the first person who starts the clique from the main clique method
		Queue<String> tobeseen = new Queue<String>();
		tobeseen.enqueue(name);
		while (tobeseen.isEmpty() == false) {
			String person = tobeseen.dequeue();
			Friend friend = g.members[g.map.get(person)].first;
			while(friend != null) {
				if(seen.contains(g.members[friend.fnum].name) == false) {
					seen.add(g.members[friend.fnum].name);
				if(g.members[friend.fnum].school != null) {
					if(g.members[friend.fnum].school.equals(school)) {
						ans.add(g.members[friend.fnum].name);
						tobeseen.enqueue(g.members[friend.fnum].name);
					}
				}
			}
				friend = friend.next;
			}
		}
		//Note to self: the process is the same as with shortest path just this time look at school name for friends before continuing search and
		//also remember to add found people with school name to seen so that they don't show up again
		
		return ans;
	}
	
	
	
	//methods for connectors
	private static void connectordfs(Graph g, ArrayList<String> ans, ArrayList<String> seen, int i, int[] dfsnums, int[] backnums, int value ) {
		seen.add(g.members[i].name);
		dfsnums[i] = value;
		backnums[i] = value; //everything matches index of memebers for convienience
		Friend friend = g.members[i].first; //to start dfs
		
		while(friend != null) {
			if(seen.contains(g.members[friend.fnum].name) == false) {
				value++;
				connectordfs(g,ans,seen,friend.fnum,dfsnums,backnums,value); //note to self: backstep occurs here when done, so changes to back and additions to answer happen after this line
				if(dfsnums[i] > backnums[friend.fnum]) {
					backnums[i] = Math.min(backnums[i],backnums[friend.fnum]);
				} //changes occuring due to backstep
				if(dfsnums[i] <= backnums[friend.fnum]) { //condition to add to answers
					if(ans.contains(g.members[i].name) == false) { //dupe control 
						ans.add(g.members[i].name);
					}
				}
			}
			else {
				backnums[i] = Math.min(backnums[i], dfsnums[friend.fnum]);
			}
			friend = friend.next;
		}	
		
		
	}
	
	//source removal
	private static void removesource(Graph g, ArrayList<String> ans, int i, int[] dfsnums, int[] backnums) {
		boolean remains = true;
		Friend f = g.members[i].first;
		ArrayList<Friend> friends = new ArrayList<Friend>();
		while(f != null) {
			friends.add(f);
			f = f.next;
		}
		if(friends.size() < 2) {
			remains = false;
		}
		else {
			int size = friends.size();
			for(Friend x : friends) {
				for(int j = 0; j < size; j++) {
					if(Math.abs(dfsnums[x.fnum]-dfsnums[friends.get(j).fnum]) >= size) {
						remains = false;
					}
				}
			}
			for(Friend x : friends) {
				for(int q = 0; q < size; q++) {
					if(backnums[x.fnum] != backnums[friends.get(q).fnum]) {
						remains = false;
					}
				}
			}
		}
		if(ans.contains(g.members[i].name) && remains == false) {
			ans.remove(ans.indexOf(g.members[i].name));
		}
	}

}