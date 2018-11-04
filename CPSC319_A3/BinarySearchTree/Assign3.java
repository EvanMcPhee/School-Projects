import java.io.*;

public class Assign3 {

    public static void main(String [] args){
        BinarySearchTree tree = new BinarySearchTree();

        //checking input arguments
        if(args.length != 3){
            System.out.println("Invalid input, run using the line 'java Assign3 inputfilename outputfilename1 outputfilename2'");
            System.exit(0);
        }
        String input = args[0];
        String output1 = args[1];
        String output2 = args[2];
        try{
            File file = new File(input);
            FileInputStream fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String in;
            Node temp;

            // Reading input and creating tree
            while((in = br.readLine()) != null){
                    String [] substrings = new String [6];
                    for(int i = 0; i < 6; i++){
                        if(i == 0){
                            substrings[0] = (in.substring(0,1));
                        } else if(i == 1){
                            substrings[1] = (in.substring(1,8));
                        } else if(i == 2){
                            substrings[2] = (in.substring(8,33));
                        } else if(i == 3){
                            substrings[3] = (in.substring(33,37));
                        } else if(i == 4){
                            substrings[4] = (in.substring(37,41));
                        } else{
                            substrings[5] = (in.substring(41,42));
                        }
                    }
                    temp = new Node(substrings);

                    if(substrings[0].charAt(0) == 'I'){
                        tree.addNode(temp);
                    } else {
                        tree.delNode(temp);
                    }
            }
            br.close();
            fstream.close();
        }catch(Exception e){
            System.out.println("Error Reading file: "+ input + " Error was: " + e);
            System.exit(0);
        }

        // depth first inorder traversal, outputs to output1
        tree.depthFirstStart(output1);

        // breadth first traversal, outputs to output 2
        tree.breadthFirst(output2);
    }
}
