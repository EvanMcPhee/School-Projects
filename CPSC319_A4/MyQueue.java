public class MyQueue {
    private int [] queue;
    private int size;
    private int front;
    private int tail;

    public  MyQueue(int size){
        queue = new int [size];
        this.size = size;
        front = tail = 0;
    }

    public void enqueue(int x){
        if(tail != size) {
            queue[tail++] = x;
        } else
            System.out.println("ERROR QUEUE IS FULL");
    }

    public int dequeue(){
        return queue[front++];
    }

    public boolean isEmpty(){
        if(front == tail)
            return true;
        else
            return false;
    }

    public void print(){
        for (int i = tail; i > front; i--){
            System.out.print(queue[i]);
        }
        System.out.println();
    }
}
