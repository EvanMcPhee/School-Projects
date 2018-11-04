import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Assign1 {

    private int data[];
    private int size;
    private int tempdata[];

    public Assign1(int size)
    {
        data = new int[size];
        this.size = size;
    }

    public void setData(int data, int index) {
        this.data[index] = data;
    }

    public int randGen(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 100000 + 1);
        return randomNum;
    }



    public void selectionSort(int [] data) {       // Adapted from code provided in lectures
        for(int i = 0; i < data.length - 1; i++){
            int x = i;
            for(int j = i + 1;j < data.length;j++) {
                if(data[j]< data[x])
                    x = j;
            }
            int small = data[x];
            data[x] = data[i];
            data[i] = small;
        }
    }

    public void insertionSort(int[] data){
        for(int i = 1,j; i < data.length; i++){
            int tmp = data[i];
            for(j = i; j > 0 && tmp < data[j-1]; j--)
                data[j] = data[j-1];
            data[j] = tmp;
        }
    }

   public void mSort(int [] data){      // All merge sort code adapted from code provided in lectures
        tempdata = new int [size];
        doMergeSort(0,size - 1);
   }

   private void doMergeSort(int low, int high){
        if(low < high){
            int mid = low + (high - low) / 2;
            doMergeSort(low, mid);
            doMergeSort(mid + 1, high);
            mergeParts(low,mid,high);
        }
   }

   private void mergeParts(int low, int mid, int high){
        for(int i = low; i<=high;i++)
            tempdata[i]=data[i];
        int i = low;
        int j = mid + 1;
        int k = low;
        while(i <= mid && j <= high){
            if(tempdata[i] <= tempdata[j]){
                data[k] = tempdata[i];
                i++;
            } else {
                data[k] = tempdata[j];
                j++;
            }
            k++;
        }
        while(i <= mid){
            data[k] = tempdata[i];
            k++;
            i++;
        }
   }

    public void quickSort(int low, int high){      // All quick sort code adapted from code provided in lectures

        int i = low;
        int j = high;
        int pivot = data[low + (high-low) / 2];

        while(i <= j){
            while(data[i] < pivot)
                i++;
            while(data[j] > pivot)
                j--;
            if(i <= j){
                int temp = data[i];
                data[i] = data[j];
                data[j] = temp;
                i++;
                j--;
            }
        }
        if(low < j)
            quickSort(low,j);
        if(i < high)
            quickSort(i,high);
    }



    public static void main(String [] args) throws IOException {
        if(args.length < 4 || args.length > 4){
            System.out.println("Invalid number of arguments, Try 'order' 'size' 'algorithm' 'outputfilename' ");
            System.exit(0);
        }
        String order = args[0];
        int size = Integer.parseInt(args[1]);
        String algorithm = args [2];
        String outputfile = args [3];
        Assign1 arrayobj = new Assign1(size);
        double elapsedT = 0;
        FileWriter output = null;
        output = new FileWriter(outputfile);

        // Check if size is a valid input
        if(size <= 0){
            System.out.println("Please enter a positive number for size, program terminated");
            System.exit(0);
        }

        // Check order input and fill the array
        if(order.equals("ascending") || order.equals("Ascending")) {
            int number = 1;
            for(int i = 0; i < size; i++)
            {
                arrayobj.setData(number,i);
                number++;
            }
        }
        else if(order.equals("random") || order.equals("Random")) {
            for (int i = 0; i < size; i++) {
                arrayobj.setData(arrayobj.randGen(), i);
            }
        }
        else if(order.equals("descending") || order.equals("Descending")){
            int number = 1;
            for(int i = size-1; i > 0; i--){
                arrayobj.setData(number,i);
                number++;
            }
        }
        else{
            System.out.print("Could not understand order (random, descending, ascending) to fill array, program terminated");
            System.exit(0);
        }

        // Check algorithm input and run algorithm with timer, prints sorted data
        if(algorithm.equals("selection") || algorithm.equals("Selection")){
            double start = System.nanoTime();
            arrayobj.selectionSort(arrayobj.data);
            elapsedT = System.nanoTime() - start;

            for(int z = 0; z < size; z++)
                output.write(arrayobj.data[z] + "\n");
            output.write("Array sorted " + size + " integers from " + order +" order Using " + algorithm);
        }
        else if(algorithm.equals("insertion") || algorithm.equals("Insertion")){
            double start = System.nanoTime();
            arrayobj.insertionSort(arrayobj.data);
            elapsedT = System.nanoTime() - start;

            for(int z = 0; z < size; z++)
                output.write(arrayobj.data[z] + "\n");
            output.write("Array sorted " + size + " integers from " + order +" order Using " + algorithm);
        }
        else if(algorithm.equals("merge") || algorithm.equals("Merge")){
            double start = System.nanoTime();
            arrayobj.mSort(arrayobj.data);
            elapsedT = System.nanoTime() - start;

            for(int z = 0; z < size; z++)
                output.write(arrayobj.data[z] + "\n");
            output.write("Array sorted " + size + " integers from " + order +" order Using " + algorithm);
        }
        else if(algorithm.equals("quick") || algorithm.equals("Quick")){
            double start = System.nanoTime();
            arrayobj.quickSort(0,size - 1);
            elapsedT = System.nanoTime() - start;

            for(int z = 0; z < size; z++)
                output.write(arrayobj.data[z] + "\n");
            output.write("Array sorted " + size + " integers from " + order +" order Using " + algorithm);
        }
        else{
            System.out.print("Could not understand algorithm (selection, insertion, merge, quick) to sort array, program terminated");
            System.exit(0);
        }

        // printout time elapsed
        output.write("\n Elapsed time in nanoseconds is: ");
        output.write(Double.toString(elapsedT));
        output.write("\n Elapsed time in milliseconds is: ");
        output.write(Double.toString((elapsedT/1000000)));
        output.close();

    }
}
