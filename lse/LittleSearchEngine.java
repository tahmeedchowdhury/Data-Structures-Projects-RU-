package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		  if(docFile == null) { //null case
			  return null;
		  }
		  HashMap<String,Occurrence> words = new HashMap<String,Occurrence>(); 
		  Scanner sc = new Scanner(new File(docFile)); //reads the file to get word by word
		  String word = ""; // String to hold word to getKeyWords for
		  String currword = ""; //stores current word
		  while(sc.hasNext()) {
			  currword = sc.next();
			  word = getKeyword(currword);
			  
			  if(word == null) {
				  continue;
			  }
			  else {
				  if(words.containsKey(word)) {
					  words.get(word).frequency = words.get(word).frequency + 1;
				  }
				  else {
					  words.put(word, new Occurrence(docFile,1));
				  }
			  }
		  }
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return words;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		Set<String> keys = new HashSet<String>();
		keys = kws.keySet();
		
		for(String s: keys) {
			if(keywordsIndex.containsKey(s) == false) { //master keywordsIndex does not have this key
				keywordsIndex.put(s, new ArrayList<Occurrence>());
				keywordsIndex.get(s).add(kws.get(s));
			}
			else { //master keywordsIndex does have the key
				keywordsIndex.get(s).add(kws.get(s));
				insertLastOccurrence(keywordsIndex.get(s));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if(word == null) { //checks null case
			return null;
		}
		int stoppos = 0;
		boolean midpunc = false;
		boolean foundpunc = false;
		word = word.toLowerCase(); //converts all to lower case
		for(int i = 0; i < word.length(); i++) { //will take care of trailing punctuation
			if(Character.isLetter(word.charAt(i))) {
				stoppos++;
			}
			else {
				foundpunc = true;
				break;
			}
		}
		if(foundpunc == true) {
		String tempword = word.substring(stoppos,word.length()-1); //the rest of word after the first punctuation
		for(int i = 0; i < tempword.length(); i++) { // this checks whether punctuation is in the middle or end of word
			if(Character.isLetter(tempword.charAt(i)) == true) {
				midpunc = true;
				break;
			}
		}
		}
		if(midpunc == true) { //word had middle punctuation and therefore null is returned
			return null;
		}
		else { //punctuation was at the end and so word is cut to just the word
			word = word.substring(0,stoppos); 
		}
		if(noiseWords.contains(word)) { //is a noise word
			return null;
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return word; //after all tests word is returned
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Integer> locs = new ArrayList<Integer>();
		Occurrence tempocc = occs.get(occs.size()-1); //storing last element
		occs.remove(occs.size()-1); // removing last element to do binary with right indexes
		int low = 0;
		int high = occs.size()-1;
		int mid = 0;
		while(high >= low) {
			mid = (low + high)/2;
			locs.add(mid);
		if(tempocc.frequency == occs.get(mid).frequency) {
			break;
		}
		else if(occs.get(mid).frequency > tempocc.frequency) {
			low = mid + 1;
		}
		else if(occs.get(mid).frequency < tempocc.frequency) {
			high = mid - 1;
		}
			
		} //end of searching for position loop
		
		if(occs.get(mid).frequency <= tempocc.frequency) {
			occs.add(mid, tempocc);
		}
		else if (occs.get(mid).frequency > tempocc.frequency) {
			if(mid == occs.size()-1) {
				occs.add(tempocc);
			}
			else {
				occs.add(mid+1,tempocc);
			}
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return locs;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
			
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> res = new ArrayList<String>();
		ArrayList<Occurrence> tempres = new ArrayList<Occurrence>();
		if(keywordsIndex.containsKey(kw1)) { //adds the occurrences of documents that keyword 1 occurs in
			for(int i = 0; i < keywordsIndex.get(kw1).size(); i++) {
				tempres.add(keywordsIndex.get(kw1).get(i));
			} //adds the elements already in order to tempres which will move to second keyword
		}// if none then tempres will stay empty
		if(keywordsIndex.containsKey(kw2)) { //starts to go to second word and make any changes
			for(int i = 0; i < keywordsIndex.get(kw2).size(); i++) {
				addtores(tempres,keywordsIndex.get(kw2).get(i));
			}
		} //after this all documents are in order and set.
	
		//Take top five documents and get their strings to get final res
		for(int i = 0; i < tempres.size(); i++) {
			if(res.size() == 5) { //top 5 are in and done
				break;
			}
			else { //add the tempres document and keep going
				res.add(tempres.get(i).document);
			}
		}
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return res;
	
	}
	
	
	//helper methods
	
	public static void addtores(ArrayList<Occurrence> tempres, Occurrence o) {
		if(tempres.size() == 0) {
			tempres.add(o);
			return;
		}
		if(find(tempres, o) != -1) {
			int temploc = find(tempres, o);
			if (tempres.get(temploc).frequency < o.frequency) { //the frequency of kw2 is higher so remove kw1 and then move on to find loc of kw2
				tempres.remove(temploc);
			}
			else { //kw1 has hihger frequency so just end it here
				return;
			}
		}
		for(int i = 0; i < tempres.size(); i++) { //considers greater than and equal to
			
			 if (tempres.get(i).frequency == o.frequency) {
				addAfterDupes(tempres,o,i);
				return;
			}
			 else if(tempres.get(i).frequency < o.frequency) {
				 tempres.add(i,o);
				 return;
			 }
		} 
		//now consider less than as the last position
		tempres.add(o);
	}
	
	
	
	
	//helper method to find the location of duplicate document from kw1
	
	public static int find(ArrayList<Occurrence> tempres, Occurrence o) {
		for(int i = 0 ; i < tempres.size(); i++) {
			if(tempres.get(i).document.equals(o.document)) {
				return i;
			}
		}
		return -1;
	}
	
	public static void addAfterDupes(ArrayList<Occurrence> tempres, Occurrence o, int startdex) {
		while(tempres.get(startdex).frequency == o.frequency && startdex < tempres.size()-1) {
			startdex++;
		}
		if(startdex == tempres.size()-1) {
			tempres.add(o);
		}
		else {
		tempres.add(startdex,o);
		}
	}
}
