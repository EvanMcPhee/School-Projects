public class CQueue {
    private ListNode head;
    private ListNode tail;


    public CQueue(){
        head = null;
        tail = null;
    }

    public boolean isEmpty(){
        if(head == null)
            return true;
        else
            return false;
    }

    public void enqueue(Node s){
        if(isEmpty()){
            head = new ListNode(s);
            tail = head;
        } else {
            ListNode temp = new ListNode(s);
            tail.setNext(temp);
            tail = temp;
            }
    }

    public Node dequeue(){
        Node temp = head.getNode();
        head = head.getNext();
        return temp;

    }


}
