public class ListNode {
    private Node node;
    private ListNode next;

    public ListNode(Node node){
        this.node = node;
        this.next = null;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public Node getNode() {
        return node;
    }
}
