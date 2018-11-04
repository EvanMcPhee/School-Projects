import java.io.*;


public class Assign2 {
    public static void main(String[] args) throws IOException {
        RefArray arr = new RefArray(10000);

        //checking input arguments
        if(args.length != 2){
            System.out.println("Invalid input, run using the line 'java Assign2 inputfilename outputfilename'");
            System.exit(0);
        }
        String input = args[0];
        String output = args[1];

        //reading input
        double start = System.nanoTime();
        double endinput = 0;
        try{
            File file = new File(input);
            FileInputStream fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strline;
            while((strline = br.readLine()) != null){
                arr.addString(strline);
            }
            arr.fillComplete();
            endinput = System.nanoTime() - start;
            br.close();
            fstream.close();
        }catch(Exception e){
            System.out.println("Error Reading file" + e);
            System.exit(0);
        }
        // finished input
        System.out.println("File processed in: " + (endinput/1000000) + " milliseconds");

        // sorting the reference array
        double startquick = System.nanoTime();
        arr.quickSort(0,arr.getArrsize()-1);
        double endquick = System.nanoTime() - startquick;

        // outputting sorted array and lists
        arr.write(output);
        FileWriter outfile = new FileWriter(output, true);
        outfile.write("Time taken to process input into Linked Lists in order: " + endinput/1000000 + " milliseconds");
        outfile.write("\n" + "Time taken to sort the array of Linked Lists using quick sort " + endquick/1000000 + " milliseconds");
        outfile.write("\nTotal Time taken to process, sort, and output the file " + (System.nanoTime() - start)/1000000 + " milliseconds");
        outfile.close();
        System.out.println("Total Time taken to process, sort, and output the file " + (System.nanoTime() - start)/1000000 + " milliseconds");
    }
}
