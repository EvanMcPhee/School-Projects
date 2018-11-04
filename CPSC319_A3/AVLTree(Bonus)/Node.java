import java.io.FileWriter;
import java.io.IOException;

public class Node {

    private int studentnum, year, height;

    private String studentname, department, program;

    private Node left, right;


    public Node(String [] substrings){
        studentnum = Integer.parseInt(substrings[1]);
        studentname = substrings[2];
        department = substrings[3];
        program = substrings[4];
        year = Integer.parseInt(substrings[5]);
        height = 0;
    }

    public void write(String outfile) throws IOException {
         FileWriter output = new FileWriter(outfile, true);
         output.write(studentnum + "           " + studentname + " " + department + "           " + program + "       " + year );
         output.write("\n");
         output.close();
    }

    public String getStudentname() {
        return studentname;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
            this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }
}
