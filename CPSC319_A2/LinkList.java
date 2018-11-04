public class LinkList {
    private Node head;
    private int size;

    public LinkList(String s){
        head = new Node(s);
        size = 1;
    }

    public int getSize(){
        return size;
    }

    // returns the node at a specified position
    public Node getNode(int pos){
        if(pos > size || pos < 0){
            System.out.println("Invalid position to get node from, size is: " + size + " And position was: " + pos);
            System.exit(0);
        }
        Node target = head;
        while(pos != 0){
            target = target.getNext();
            pos--;
        }
        return target;
    }

    // checks if pos is valid and then calls insert node
    public void addNode(String s, int pos){
        if(pos > size || pos < 0){
            System.out.println("Invalid position, size of this list is: " + size );
            return;
        } else {
            insertNode(s, pos);
        }
    }

    // inserts a node at specified position with String s as its word
    public void insertNode(String s, int pos){
        if(pos == 0){
            Node temp = new Node(s);
            temp.setNext(head);
            head = temp;
        } else {
            Node before = null;
            Node after = head;
            while(pos != 0){
                before = after;
                after = after.getNext();
                pos--;
            }
             Node temp = new Node(s);
            temp.setNext(after);
            before.setNext(temp);
        }
        size++;
    }

    // Compares Strings in the head node,
    // used by the RefArray quicksort method to alphabetically order linked lists in the array of references
    public int compareTo(LinkList x){
        char [] xchar = x.head.getWord().toCharArray();
        char [] test = head.getWord().toCharArray();
        int length = 0;
        int i;
        if(xchar.length >= test.length) {
            length = test.length;
        }
        else {
            length = xchar.length;
        }
        for(i = 0; i < length; i++){
            if(xchar[i] < test[i]){
                return 1;
            }
            if(xchar [i] > test[i]){
                return -1;
            }
        }
         return xchar.length - test.length;

    }

}
