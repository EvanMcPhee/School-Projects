public class Digraph {
    private int[][] adjmat;
    private int rows;
    private int cols;
    private boolean[] visited;
    private boolean pathfoundflag;

    public Digraph(int r, int c) {
        adjmat = new int[r][c];
        rows = r;
        cols = c;
        visited = new boolean[r];
        pathfoundflag = false;
    }

    public void add(int row, int col, int weight) {

        adjmat[row][col] = weight;
    }

    public void resize(int row, int col) {
        int[][] temp = new int[row * 2][col];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                temp[i][j] = adjmat[i][j];
            }
        }
        cols = col;
        adjmat = temp;
    }

    public void print() {
        System.out.println("Rows = " + rows + " Cols = " + cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(adjmat[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void depthFirstTraversal(int[][] querylist) {
        for (int i = 0; i < querylist.length; i++) {
            visited = new boolean[rows];
            pathfoundflag = false;
            MyStack stack = new MyStack(rows);
            int start = querylist[i][0];
            System.out.println();
            System.out.println("\n\nDepth first from : " + start + " to " + querylist[i][1]);
            DFS(start, querylist[i][0], querylist[i][1], stack);
        }
    }

    public void DFS(int start, int v, int end, MyStack stack) {

        stack.push(v);
        visited[v] = true;
        if (v == end) {
            stack.print();
            pathfoundflag = true;
        }
        for (int i = 0; i < cols; i++) {
            if (adjmat[v][i] == 1 && !visited[i]) {
                DFS(start, i, end, stack);
            }
        }
        visited[v] = false;
        stack.pop();
        if (start == v && pathfoundflag == false) {
            System.out.println(v + " -1 " + end);
        }

    }

    /*public void readComplete(int rows){
        Node [][] temp = new Node[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                temp[i][j] = adjmat[i][j];
            }
        }
        adjmat = temp;
    } */
    public void djikstra(int[][] querylist) {
        for (int i = 0; i < querylist.length; i++) {
            djikstraSearch(querylist[i][0], querylist[i][1]);
        }

    }

    public void djikstraSearch(int start, int end) {
        visited = new boolean[rows];
        int[] distance = new int[rows];
        MyStack stack = new MyStack(rows);
        MyQueue queue = new MyQueue(rows);
        int[] pred = new int[rows];

        for (int i = 0; i < rows; i++) {
            distance[i] = Integer.MAX_VALUE;
            queue.enqueue(i);
            pred[i] = -1;
        }
        distance[start] = 0;

        while (!queue.isEmpty()) {
            int v = findShortest(distance);
            visited[v] = true;
            for (int i = 0; i < rows; i++) {
                if (adjmat[v][i] != 0 && !visited[i]) {
                    if (distance[i] > distance[v] + adjmat[v][i]) {
                        distance[i] = distance[v] + adjmat[v][i];
                        pred[i] = v;
                    }
                }
            }
            queue.dequeue();
        }
        int i = end;
        if (pred[i] != -1) {
            while (i != -1) {
                stack.push(i);
                i = pred[i];
            }
            while (!stack.isEmpty()) {
                System.out.print(stack.pop() + " ");
            }
            System.out.println();
        } else {
            System.out.println(start + " -1 " + end);
        }
    }

    public int findShortest(int[] distance) {
        int shortindex = 0;
        for (int i = 0; i < visited.length; i++) {
            if (visited[i] == false) {
                shortindex = i;
                //shortest = distance[i];
            }
        }
        for (int i = 0; i < distance.length; i++) {
            if (distance[i] < distance[shortindex] && visited[i] == false) {
                shortindex = i;
            }
        }
        return shortindex;
    }

    public void breadthFirst(int[][] querylist) {
        for (int i = 0; i < querylist.length; i++) {
            System.out.println("NEW SEARCH");
            BFT(querylist[i][0], querylist[i][1]);
        }
    }

    public void BFT(int start, int end) {
        visited = new boolean[rows];
        MyQueue queue = new MyQueue(rows);
        MyStack stack = new MyStack(rows);
        pathfoundflag = false;

        visited[start] = true;
        queue.enqueue(start);

        stack.push(start);
        while(!queue.isEmpty()){
            int u = queue.dequeue();

            for(int j = 0; j < rows; j++){
                if(adjmat[u][j] == 1 && visited[j] == false){
                    visited[j] = true;
                    queue.enqueue(j);
                    stack.push(j);
                    if(j == end){
                        break;
                    }
                }
            }
        }
    }
}