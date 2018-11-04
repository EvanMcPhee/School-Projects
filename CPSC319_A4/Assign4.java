import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Assign4 {
    public static void main(String[] args) {
        //Checking arguments
        /*if(args.length != 4){
            System.out.println("Incorrect format: use 'Assign4    inputfile    queryfile    depthfirstOutput    breadthfirstOutput'");
            System.exit(0);
        }
        String inputfile = args[0];
        String outputfile = args[1];
        String DFoutput = args[2];
        String BFoutput = args[3];
        */

        //Setting up graph structure from file
        Digraph mygraph = null;
        try {
            FileInputStream input = new FileInputStream("input.txt");
            Scanner insize = new Scanner(input);
            //Finding size of array needed
            int cols = 0;
            int rows = 1;
            String firstrow = insize.nextLine();
            for(int i = 0; i < firstrow.length(); i++){
                if(firstrow.charAt(i) != '\t' || firstrow.charAt(i) == '0'){
                    cols++;
                }
            }
            while(insize.hasNext()){
                insize.nextLine();
                rows++;
            }
            mygraph = new Digraph(rows,cols);
            input = new FileInputStream("input.txt");
            Scanner in = new Scanner(input);
            System.out.println(rows + " " + cols);


            //Filling array
            int count = 0;
            while (in.hasNext()) {
                String str = in.nextLine();
                int j = 0;
                for(int i = 0; i < str.length(); i++){
                    if(str.charAt(i) != '0' && str.charAt(i) != '\t' ){
                        int weight = Character.getNumericValue(str.charAt(i));
                        mygraph.add(count,j++, weight);
                        /*
                        Used to be if (str.CharAt(i) == 1){
                        mygraph.add(count,j++,1);
                        }
                         */
                    } else if(str.charAt(i) == '0'){
                        mygraph.add(count,j++,0);
                    }
                }
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Now reading in the query file
        int[][] querylist = null;
        try {
            FileInputStream qinput = new FileInputStream("query.txt");
            Scanner qinsize = new Scanner(qinput);
            int rows = 0;
            while (qinsize.hasNext()){
                qinsize.nextLine();
                rows++;
            }
            querylist = new int[rows][2];
            qinput = new FileInputStream("query.txt");
            Scanner qin = new Scanner(qinput);
            int i = 0;
            while (qin.hasNextInt()){
                int temp = qin.nextInt();
                querylist[i][0] = temp;
                temp = qin.nextInt();
                querylist[i][1] = temp;
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Now doing traversals
        mygraph.depthFirstTraversal(querylist);
        System.out.println("\n Djikstras \n");
        mygraph.djikstra(querylist);
        mygraph.breadthFirst(querylist);

    }
}
