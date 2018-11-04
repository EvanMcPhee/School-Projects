import java.io.FileWriter;
import java.io.IOException;

/*
 * Code and ideas implemented in this class were adapted from https://www.geeksforgeeks.org/avl-tree-set-1-insertion/
 */

public class AVLTree {

    private Node root;

    public void addNode(Node temp){
        root = insertNode(root,temp);
    }

    public Node insertNode(Node current, Node Inserting){

        if(current == null){                                                               // Base Case
            return Inserting;
        }
        if(Inserting.getStudentname().compareTo((current.getStudentname())) < 0){          // Going left
            current.setLeft(insertNode(current.getLeft(), Inserting));
        } else {                                                                       // Going right
            current.setRight(insertNode(current.getRight(),Inserting));
        }
        current.setHeight(max(height(current.getLeft()),height(current.getRight())) + 1);       //change height of ancestor node

        int balance = nodeBalance(current);

        current = checkBalance(balance, current, Inserting);                                 // Call to determine which case and take care of the rotations if necessary

        return current;
    }

    public Node checkBalance(int balance, Node current, Node insert){                     // Determines what case we are in and does the appropriate rotation

        if(balance < -1 && insert.getStudentname().compareTo(current.getStudentname()) > 0 ){
            // do a left rotation
            return lRotate(current);

        } else if ( balance < -1 && insert.getStudentname().compareTo(current.getStudentname()) < 0 ){
            // do a right left rotation
            current.setRight(rRotate(current.getRight()));
            return lRotate(current);
        }

        if(balance > 1 && insert.getStudentname().compareTo(current.getStudentname()) < 0){
            // do a right rotation
            return rRotate(current);

        } else if(balance > 1 && insert.getStudentname().compareTo(current.getStudentname()) > 0){
            // do a left right rotation
            current.setLeft(lRotate(current.getLeft()));
            return rRotate(current);
        }

        return current;
    }

    public Node rRotate(Node x){
        Node y = x.getLeft();
        Node z = y.getRight();

        // right rotate
        y.setRight(x);
        x.setLeft(z);

        // adjust heights
        x.setHeight(max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(max(height(y.getLeft()), height(y.getRight())) + 1);

        return y;
    }

    public Node lRotate(Node x){
        Node y = x.getRight();
        Node z = y.getLeft();

        // left rotate
        y.setLeft(x);
        x.setRight(z);

        // adjust heights
        x.setHeight(max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(max(height(y.getLeft()), height(y.getRight())) + 1);

        return y;
    }

    public int height(Node x) {
        if (x == null)
            return 0;
        else
            return x.getHeight();
    }

    public int nodeBalance(Node x){
        return height(x.getLeft()) - height(x.getRight());
    }

    public int compare(String current, String test){
        return test.compareTo(current);
    }

    public int max(int x, int y){
        if(x > y)
            return x;
        else
            return y;
    }



    public void depthFirstStart(String outfile){
        try{
            FileWriter output = new FileWriter(outfile);
            output.write("Depth-First, In-Order traversal \n");
            output.close();
            writeHeader(outfile);
        } catch (IOException e){
            e.printStackTrace();
        }
        depthFirst(outfile, root);
    }

    // Depth First Traversal of the tree
    public void depthFirst(String outfile, Node Current){
        if(Current!=null){
            depthFirst(outfile, Current.getLeft());
            try{
                Current.write(outfile);
            } catch (IOException e){
                e.printStackTrace();
            }
            depthFirst(outfile, Current.getRight());
        }
    }

    // Breadth First Traversal of the tree
    public void breadthFirst(String outfile){
        try {
            FileWriter output = new FileWriter(outfile);
            output.write("Breadth-First Traversal \n");
            output.close();
            writeHeader(outfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node p = root;
        CQueue queue = new CQueue();

        if(p != null){
            queue.enqueue(p);
            while (!queue.isEmpty()){
                p = queue.dequeue();
                try {
                    p.write(outfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(p.getLeft()!= null){
                    queue.enqueue(p.getLeft());
                }
                if(p.getRight() != null){
                    queue.enqueue(p.getRight());
                }
            }
        }

    }

    public void writeHeader(String outfile) throws IOException {
        FileWriter output = new FileWriter(outfile, true);
        output.write('\n');
        output.write("Student number    Student Name            Department      Program     year");
        output.write("\n");
        output.write("---------------------------------------------------------------------------");
        output.write("\n");
        output.close();
    }
}
