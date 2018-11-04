public class Node {
    private String word;
    private Node next;

    public Node(String s){
        word = s;
        next = null;
    }

    public void setNext(Node x){
        next = x;
    }

    public Node getNext(){
        return next;
    }

    public String getWord(){
        return word;
    }

}
