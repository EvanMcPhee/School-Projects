import java.io.FileWriter;
import java.io.IOException;

public class BinarySearchTree {

    private Node root;

    public void addNode(Node temp){
        if(root == null){
            root = new Node(temp);
        } else {
            Node cursor = root;
            Node nextcursor = root;
            char dir = '\0';
            String Tlower = temp.toLower();     // string of lowercase only for student name
            while(nextcursor != null){
                cursor = nextcursor;
                String Clower = cursor.toLower();   // string of lowercase only for student name

                if(compare(Clower, Tlower) <= 0){       // going left
                    nextcursor = cursor.getLeft();
                    dir = 'L';
                } else {                                   // going right
                    nextcursor = cursor.getRight();
                    dir = 'R';
                }
            }
            if(dir == 'L'){
                cursor.setLeft(temp);
                temp.setParent(cursor);
            } else {
                cursor.setRight(temp);
                temp.setParent(cursor);
            }
        }
    }

    public int compare(String current, String test){
        return test.compareTo(current);
    }

    public void delNode(Node temp){
        if(root == null){
            System.out.println("Couldn't Delete Because Tree is empty");
        } else {
            Node cursor = root;
            Node nextcursor = root;
            char dir = '\0';
            String Tlower = temp.toLower();

            while(nextcursor != null && temp.getStudentnum()!= nextcursor.getStudentnum()){
                cursor = nextcursor;
                String Clower = cursor.toLower();
                if(compare(Clower, Tlower) <= 0){       // going left
                    nextcursor = cursor.getLeft();
                    dir = 'L';
                } else {                                // going right
                    nextcursor = cursor.getRight();
                    dir = 'R';
                }
            }

            // Finished searching for node
            if(nextcursor == null){
                System.out.println("Couldn't Find the Specified Node");
            }

            // The case of no children Nodes
            else if(nextcursor.getLeft() == null && nextcursor.getRight() == null){
                if(dir == 'L')
                    cursor.setLeft(null);
                else
                    cursor.setRight(null);
            }

            // Only a right child Node
            else if(nextcursor.getLeft() == null){
                if(dir == 'L')
                    cursor.setLeft(nextcursor.getRight());
                else
                    cursor.setRight(nextcursor.getRight());

            }

            // Only a left child Node
            else if(nextcursor.getRight() == null){
                if(dir == 'L')
                    cursor.setLeft(nextcursor.getLeft());
                else
                    cursor.setRight(nextcursor.getLeft());
            }

            // Two children Nodes
            else {

                Node small = nextcursor.getRight();
                while(small.getLeft() != null) {
                    small = small.getLeft();
                }
                    // found smallest node in right subtree

                if(small.getParent().getLeft() == small && small.getRight() != null){
                    small.getParent().setLeft(small.getRight());
                }
                    // takes care of if smallest node has a child node


                if(nextcursor.getParent() == null) {
                    root = small;
                } else if(nextcursor.getParent().getLeft() == nextcursor) {
                    nextcursor.getParent().setLeft(small);
                } else {
                    nextcursor.getParent().setRight(small);
                }
                    // sets the parent of removed nodes appropriate child pointer to small

                small.setParent(nextcursor.getParent());
                small.setLeft(nextcursor.getLeft());
                small.setRight(nextcursor.getRight());
                    // places small in the spot where the removed node was
            }
        }
        }


        // Writes the header and then starts the traversal
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
                Current.write(outfile);             // Visiting Node
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
                    p.write(outfile);               // Visiting Node
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
