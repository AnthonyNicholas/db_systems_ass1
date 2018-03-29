/*
 * Task 3A: Implement loading Heap File in Java
 *
 * A program to load a database relation writing a heap file. The source records are variable-length. Your heap file may hold fixed-length 
 * records (you will need to choose appropriate maximum lengths for each field). However, you may choose to implement variable lengths 
 * for some fields, especially if you run out of disc space or secondary memory!
 *
 * All attributes with Int type must be stored in 4 bytes of binary, e.g. if the value of ID is equal to 70, it must be stored as 70 
 * (in decimal) or 46 (in hexadecimal; in Java: 0x46). It must not be stored as the string 70, occupying two bytes. 
 * Your heap file is therefore a binary file.
 *
 * For simplicity, the heap file does not need a header (containing things like the number of records in the file or a free space list), 
 * though you might need to keep a count of records in each page. The file should be packed, i.e. there is no gap between records, 
 * but there will need to be gaps at the end of each page.
 * 
 * The executable name of your program to build a heap file must be dbload and should be executed using the command:
 * 	java dbload -p pagesize datafile
 *
 * The output file will be heap.pagesize where your converted binary data is written as a heap. Your program should write out one 
 * page] of the file at a time. For example, with a pagesize of 4096, you would write out a page of 4096 bytes possibly 
 * containing multiple records of data to disk at a time. You are not required to implement spanning of records 
 * across multiple pages.
 * 
 * Your dbload program must also output the following to stdout, the number of records loaded, number of pages used and the number 
 * of milliseconds to create the heap file.  A program that performs a text search using your heap file. 
 *
 */

import java.io.*;
import java.util.*;
import java.lang.*;

public class dbload{

    private int pagesize, pageNum, recordNum;
    private String output_file_name; 
    private String datafile;
    DataOutputStream os;
    ByteArrayOutputStream byte_os;

    /*   
    * Constructor - populates pagesize, output_file_name, byte_os (used for buffer) and os (used to write to file). 
    */

    public dbload(String[] args){
        
        this.pageNum = 0;
        this.recordNum = 0;
        this.handleArguments(args);
        try{
            this.output_file_name = "heap." + Integer.toString(this.pagesize);
            this.os = new DataOutputStream(new FileOutputStream(this.output_file_name));
            this.byte_os = new ByteArrayOutputStream(this.pagesize);
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }   
    
    /*   
    * Function handles command line arguments (pagesize and name) 
    */

    private void handleArguments(String[] args){
        if (args[0].equals("-p")){
            this.pagesize = Integer.parseInt(args[1]);
            this.datafile = args[2];
        } else {
            this.datafile = args[0];
            this.pagesize = Integer.parseInt(args[2]);
        }
    }

    /*   
    * Function reads from DATA_FILE, fills byte_os buffer.  When buffer is full, writes to output file.
    */

    private void readFile(){

        String line;

        try{

            BufferedReader bReader = new BufferedReader(new FileReader(this.datafile));

            //Ignore first line which contains headings
            bReader.readLine();

            while((line = bReader.readLine()) != null){

                String[] values = line.split("\t");
                 
                if (values.length != 9){
                    continue;
                }                

                //Record is defined is seperate class; has properties name, status, reg_dt, canc_dt, renew_dt, state_num, state & abn              
                Record r = new Record(values); 
                //r.print();
                this.recordNum += 1;
               
                //If the buffer is not yet full, add to buffer 
                if (this.byte_os.size() + r.byteLength < this.pagesize){
                    this.byte_os.write(r.getByteArray());//add one more record to buffer
                }
                else{ //If the buffer is full, write, empty it and then add to buffer
                    this.pageNum += 1;
                    this.byte_os.writeTo(this.os);
                    this.byte_os.reset();
                    this.byte_os.write(r.getByteArray());
                }
           }
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    //Main - reads from datafile and prints to heap file using specified buffer

    public static void main(String[] args){
        
        dbload db = new dbload(args);

        long startTime = System.currentTimeMillis();

        db.readFile();
        
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);

        System.out.println("Time to insert: " + duration + " milliseconds");
        System.out.println("Number of records loaded: " + db.recordNum);
        System.out.println("Number of pages used: " + db.pageNum);

    }  

 
}



