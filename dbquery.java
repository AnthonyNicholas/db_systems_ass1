/*
 * Task 3B: Implement Heap Query File in Java
 *
 *
 * Write a program to perform text query search operations on the field BN NAME heap file (without an index) produced by 
 * your dbload program in Section 5.
 *
 * The executable name of your program to build a heap file must be dbquery and should be executed using the command: 
 * java dbquery text pagesize 
 * Your program should read in the file, one page at a time. For example, if the pagesize parameter is 4096, your program 
 * should read in the records in the first page in heap.4096 from disk. These can then be scanned, in-memory, for a match (the 
 * string in text parameter is contained in the field BN NAME). If a match is found, print the matching record to stdout, 
 * there may be multiple answers. Then read in the next page of records from the file. The process should continue until there are no 
 * more records in the file to process.
 *
 * In addition, the program must always output the total time taken to do all the search opera- tions in milliseconds to stdout.
 *
*/

import java.io.*;
import java.util.*;
import java.lang.*;

public class dbquery{

    private int pagesize, pageNum, matchNum;
    private String search_text, heap_file_name; 
    private byte [] search_bytes;
    InputStream in;
    ByteArrayInputStream byte_in;
    byte [] buf;
    
    /*   
    * Constructor - populates pagesize, output_file_name, byte_os (used for buffer) and os (used to write to file). 
    */

    public dbquery(String[] args){
 
        this.search_text = args[0];
        
        try{
            
            this.pagesize = Integer.parseInt(args[1]);
            this.heap_file_name = "heap." + Integer.toString(this.pagesize);
            this.in = new FileInputStream(heap_file_name);
            this.buf = new byte [pagesize];
            this.search_bytes = this.search_text.getBytes();

       } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }   
    
   /*   
    * Function searches heap file for search_text from DATA_FILE.  Search is conducted one buffer at a time.
    */

    private void searchHeap(){

        String line;
        byte[] match_bytes, search_buf;
        String match_string;
        int foundIndex, nextDelimIndex, lastDelimIndex;
        this.matchNum = 0;
        

        try{
            System.out.println(this.heap_file_name);
            
            while(in.read(buf) != -1){ //in.read(buf) returns -1 if it hits the EOF. 
                //search in buf
                
                foundIndex = indexOf(buf, search_bytes, 0);
                while(foundIndex != -1){
                    //System.out.println("found match at index: " + foundIndex);
                    nextDelimIndex = findNextDelimiter(foundIndex, buf);
                    lastDelimIndex = findLastDelimiter(foundIndex, buf);
                    match_bytes = Arrays.copyOfRange(buf, lastDelimIndex, nextDelimIndex); 
                    match_string = new String(match_bytes);
                    System.out.println(match_string);    
                    this.matchNum += 1;
                    foundIndex = indexOf(buf, search_bytes, foundIndex + 1);
                }      
                //keep count of number of pages
                //System.out.println(buf.length);
                this.pageNum += 1;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

/*
 * Function that searches buffer for search_bytes pattern and returns index if found
 * Acknowledgment: https://stackoverflow.com/questions/21341027/find-indexof-a-byte-array-within-another-byte-array
 */

    public int indexOf(byte[] outerArray, byte[] smallerArray, int startIndex) {
        for (int i = startIndex; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;  
    }  

    public int findNextDelimiter(int matchPosition, byte[] buf){
    
        byte[] arr = Arrays.copyOfRange(buf, matchPosition, buf.length);        
        return matchPosition + this.indexOf(arr, "|".getBytes(), 0);
    }

    public int findLastDelimiter(int matchPosition, byte[] buf){
    
        byte[] arr = Arrays.copyOfRange(buf, matchPosition - 50, matchPosition);        
        return matchPosition - 50 + this.indexOf(arr, "|".getBytes(), 0);
    }







    public static void main(String[] args){
        
        dbquery q = new dbquery(args);

        long startTime = System.currentTimeMillis();

        q.searchHeap();
        
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);

        System.out.println("Time to finish: " + duration + " milliseconds");
        System.out.println("Number of pages used: " + q.pageNum);
        System.out.println("Number of Matches: " + q.matchNum);

    }  

    //Your dbload program must also output the following to stdout, the number of records
    //loaded, number of pages used and the number of milliseconds to create the heap file.
 

}



