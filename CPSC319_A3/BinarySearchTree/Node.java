import java.io.FileWriter;
import java.io.IOException;

public class Node {

    private int studentnum, year;

    private String studentname, department, program;

    private Node left, right, parent;

    public Node(){
        studentnum = year = 0;
        studentname = department = program = null;
        left = right = parent = null;

    }

    public Node(String [] substrings){
        studentnum = Integer.parseInt(substrings[1]);
        studentname = substrings[2];
        department = substrings[3];
        program = substrings[4];
        year = Integer.parseInt(substrings[5]);
    }

    public Node(Node x){
        studentnum = x.studentnum;
        year = x.year;
        studentname = x.studentname;
        department = x.department;
        program = x.program;
        left = null;
        right = null;
        parent = null;

    }

    public String toLower(){
        char [] xchar = studentname.toCharArray();
        for(int i = 0; i < studentname.length(); i++){
            xchar[i] = Character.toLowerCase(xchar[i]);
        }
        String lower = new String(xchar);
        return lower;
    }

    public void write(String outfile) throws IOException {
         FileWriter output = new FileWriter(outfile, true);
         output.write(studentnum + "           " + studentname + " " + department + "           " + program + "       " + year );
         output.write("\n");
         output.close();
    }

    public int getStudentnum() {
        return studentnum;
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

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
