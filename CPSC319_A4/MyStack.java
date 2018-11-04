public class MyStack {
    private int[] stack;
    private int top;

    public MyStack(int size){
        stack = new int[size+2];
        top = -1;
    }

    public void push(int x){
        stack[++top] = x;
    }

    public int pop(){
            return stack[top--];
    }

    public void print(){
        for(int i = 0; i <= top; i++){
            System.out.print(stack[i] + " ");
        }
        System.out.println();
    }

    public boolean isEmpty(){
        if(top == -1){
            return true;
        } else
            return false;
    }
}
