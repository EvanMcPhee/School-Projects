public class Node {
    private int weight;
    private Node pred;

    public Node(int w){
        weight = w;
    }

    public int getWeight(){
        return weight;
    }


    public Node getPred() {
        return pred;
    }

    public void setPred(Node pred) {
        this.pred = pred;
    }
}
