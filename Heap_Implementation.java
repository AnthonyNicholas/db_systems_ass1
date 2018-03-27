/*
 * Task 3: Implement Heap File in Java
 *
 * Set up a git repository for your code, and complete the following programming tasks using Java on the AWS linux instance assigned to you.
 * A program to load a database relation writing a heap file. The source records are variable-length. Your heap file may hold fixed-length 
 * records (you will need to choose appropriate maximum lengths for each field). However, you may choose to implement variable lengths 
 * for some fields, especially if you run out of disc space or secondary memory!
 *
 * Set up a git repository for your code, and complete the following programming tasks using Java on the AWS linux instance assigned to you. 
 * A program to load a database relation writing a heap file
 * The source records are variable-length. Your heap file may hold fixed-length records (you will need to choose appropriate maximum 
 * lengths for each field). However, you may choose to implement variable lengths for some fields, especially if you run out of 
 * disc space or secondary memory!
 *
 * All attributes with Int type must be stored in 4 bytes of binary, e.g. if the value of ID is equal to 70, it must be stored as 70 
 * (in decimal) or 46 (in hexadecimal; in Java: 0x46). It must not be stored as the string ?~@~\70?~@~], occupying two bytes. 
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
 * ?~@~\page?~@~] of the file at a time. For example, with a pagesize of 4096, you would write out a page of 4096 bytes possibly 
 * containing multiple records of data to disk at a time. You are not required to implement spanning of records 
 * across multiple pages.
 * 
 * Your dbload program must also output the following to stdout, the number of records loaded, number of pages used and the number 
 * of milliseconds to create the heap file.  A program that performs a text search using your heap file. 
 *
 * Write a program to perform text query search operations on the field “BN NAME” heap file (without an index) produced by 
 * your dbload program in Section 5.
 *
 * The executable name of your program to build a heap file must be dbquery and should be executed using the command: 
 * java dbquery text pagesize 
 * Your program should read in the file, one “page” at a time. For example, if the pagesize parameter is 4096, your program 
 * should read in the records in the first page in heap.4096 from disk. These can then be scanned, in-memory, for a match (the 
 * string in text parameter is contained in the field “BN NAME”). If a match is found, print the matching record to stdout, 
 * there may be multiple answers. Then read in the next page of records from the file. The process should continue until there are no 
 * more records in the file to process.
 *
 * In addition, the program must always output the total time taken to do all the search opera- tions in milliseconds to stdout.
 *
*/

/*
 * Suggested Approach: First write some java code to read the csv file line by line and print it out to console (to prove it is 
 * actually being read in correctly)
 *
 * Next tokenise each line using the delimiter, again printing out the tokens
 *
 * Next define your data structure/class for each record and parse each token into the relevant data format (eg int, float, string, etc) and then print it out to ensure the information is being recorded correctly
 *
 * Next write out the binary variable tokens to a file in binary format
 *
 * for example usingi int field = 2; 
 *
 * DataOutputStream os = new DataOutputStream(new FileOutputStream("binout.dat"));
 *
 * os.writeInt(field);
 *
 * os.close();
 *
 * if you want to view the content in your binary heap, you can use the xxd command in linux to view the vinary content
 *
 * ie.
 *
 * xxd binout.dat
 *
 *to make it easier to stick your pageformat size, you can use a byte array (set to your page size) to store a page of records at a time, and write out the page when it is full (with padding at the end of the record)

Hope that helps get you started.
 *
 *
 *
 */

