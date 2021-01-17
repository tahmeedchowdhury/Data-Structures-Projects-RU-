package lse;

import java.io.*;
import java.util.*;
 
public class Driver {
public static void main(String args[]) throws IOException {
	LittleSearchEngine lse = new LittleSearchEngine(); // builds lse
	ArrayList<String> documents = new ArrayList<String>(); //creates list of documents
	lse.makeIndex("docs2.txt", "noisewords.txt"); //makes lse and hashtables
	System.out.println(lse.top5search("wierd", "orange"));
	}
} 