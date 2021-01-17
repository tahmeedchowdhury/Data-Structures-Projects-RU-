package trie;

import java.util.ArrayList;

import org.w3c.dom.Node;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		//System.out.println("this is result"); //debugging code
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		TrieNode root = new TrieNode(null, null, null);
		if(allWords.length == 0) {
			return root;
		} // if given array is completely empty
		root.firstChild = new TrieNode(new Indexes(0,(short)0,(short)(allWords[0].length()-1)), null,null);
		for(int i = 1; i < allWords.length; i++) { //i is the index
			place(root.firstChild, null, i, allWords);
		}
		return root; //change return type for debugging
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		//start by searching for the given prefix
		ArrayList<TrieNode> res = new ArrayList<TrieNode>();
		if(root.firstChild == null) {
			return null;
		} // case for no children and empty tree
		
		TrieNode current = root.firstChild;
		boolean fromhere = false;
		int endex = 0;
		int compdex = 0;
		int numchars = prefix.length();
		int charsmatched = 0;
		while(fromhere == false && current != null) {
			endex = 0;
			String nodeword = allWords[current.substr.wordIndex];
			for(int i = current.substr.startIndex; i < current.substr.endIndex + 1; i++) {
				if(i > prefix.length()-1) {
					break;
				}
				if(nodeword.charAt(i) == prefix.charAt(i)) {
					endex++;
					charsmatched++;
				}
				else {
					break;
				}
			}
			endex--;
			compdex = endex + current.substr.startIndex;
			//checks that entire prefix is present or not
			if(charsmatched == numchars) {
				fromhere = true;
			}
			
			if(endex == -1) {
				current= current.sibling;
			}
			else if(charsmatched != numchars) {
				current = current.firstChild;
			}
		}
		// comes here once current equals first node of the process that contains prefix 
		//Insert helper method here to fill up the arraylist with answers
		if(current == null) {
			return null;
		}
		
		else if(current.firstChild == null) {
			res.add(current);
		}
		else {
			fill(res, current.firstChild);
		}
		return res;
	}
	
	
	
	
	//printing stuff don't touch
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
	
	
	
	
	
	
	// printing stuff don't touch
	
	
	
	
	
	//helper methods
	//array and word need to be carried throughout, and node being compared to per run
private static void place(TrieNode current, TrieNode prev, int index, String[] allWords) { 
	int endex = 0;
	int compdex = 0;
	String word = allWords[index];
	if(current == null) {
		prev.sibling = new TrieNode(new Indexes(index,prev.substr.startIndex,(short)(word.length()-1)), null,null);
	}
	else {
		String nodeword = allWords[current.substr.wordIndex];
		if(current.substr.startIndex > word.length()) {
			place(current.sibling,current,index, allWords);
		}
		else {
			 endex = 0;
			for(int i = current.substr.startIndex; i < current.substr.endIndex + 1; i++) {
				if(word.charAt(i) == nodeword.charAt(i)) {
					endex++;
				}
				else {
					break;
				}
			} //end of loop for coming up with where ending sameness
			endex--; //takes where end is since starting at 0
			compdex = endex + current.substr.startIndex; // use for comparison purposes
		}
		if(endex == -1) { //no match at all
			place(current.sibling, current, index, allWords);
		}
		else if(compdex == current.substr.endIndex) { //complete match
			place(current.firstChild, current, index, allWords);
		}
		else if(compdex < current.substr.endIndex) {
			TrieNode backupcurrent = current;
			if(current != null) {
				Indexes tempcurrindexes = current.substr; //temporary copy of current's current indexes
				TrieNode tempfirstChild = current.firstChild; //temporary copy of current's first child
				 Indexes newcurrindexes = new Indexes(current.substr.wordIndex,current.substr.startIndex,(short)(compdex));
				 Indexes findex = new Indexes(tempcurrindexes.wordIndex,(short)(compdex + 1),(short)(tempcurrindexes.endIndex));
				 Indexes sindex = new Indexes(index,(short)(compdex + 1),(short)(word.length()-1));
				 current.substr = newcurrindexes;
				 current.firstChild = new TrieNode(findex,null,null);
				 current.firstChild.firstChild = tempfirstChild;
				 current.firstChild.sibling = new TrieNode(sindex,null,null);
			}
		}
	}
}


//helper method to fill up arraylist with answer to completionList
private static void fill(ArrayList<TrieNode> res, TrieNode current) {
	if(current.firstChild == null) {
		res.add(current);
	}
	else {
		fill(res, current.firstChild);
	}
	if(current.sibling != null) {
		fill(res,current.sibling);
	}
}
	
	
 }
