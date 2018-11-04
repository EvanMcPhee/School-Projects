import java.io.FileWriter;
import java.io.IOException;

public class RefArray {

    private LinkList[] list;
    private int arrsize;

    public RefArray(int x){
        list = new LinkList[x];
        arrsize = 0;
    }


    public int getArrsize(){
        return arrsize;
    }

    // reduces the size of the array to exact capacity needed (arrsize)
    public void fillComplete(){
        LinkList[] temp = new LinkList[arrsize];
        for(int i = 0; i < arrsize; i++){
            temp[i] = list[i];
        }
        list = temp;
    }

    // resizes the array to arrsize * 10
    public void addRoom(){
        LinkList[] temp = new LinkList[arrsize * 10];
        for(int i = 0; i < arrsize; i++){
            temp[i] = list[i];
        }
        list = temp;
    }


    // adds a node to a line of anagrams if its string is an anagram or allocates a new link list for that node
    public void addString(String s){
       if(arrsize == 0){
           list[0] = new LinkList(s);
           arrsize++;
       } else {
           for (int i = 0; i < arrsize; i++) {
               if (checkAnagram(list[i], s)) {
                   int pos = checkPos(list[i],s);
                   list[i].addNode(s,pos);
                   return;
               }
           }
           if((arrsize + 1) % 100 == 0){
               addRoom();
           }
           list[arrsize] = new LinkList(s);
           arrsize++;
       }
    }

    // calls sortString to check if strings are anagrams
    public boolean checkAnagram(LinkList x, String check){
        String src = sortString(x.getNode(0).getWord());
        String test = sortString(check);
        if(test.equals(src)){
            return true;
        } else {
            return false;
        }
    }

    //insertion sort to put strings into alphabetical order to help check for anagrams
    public String sortString(String x){
        char [] xchar = x.toCharArray();
        for(int i = 1, j; i < x.length(); i++){
            char temp = xchar[i];
            for(j = i; j > 0 && temp < xchar[j-1];j--){
                xchar[j] = xchar[j-1];
            }
            xchar[j] = temp;
        }
        String sorted = new String(xchar);
        return sorted;
    }

    // linear insertion sort used to place nodes in linked list
    public int checkPos(LinkList list, String s){
            char [] test = s.toCharArray();
            int pos = 0;
        for(int i = 0; i < list.getSize(); i++ ){
            char[] temp = list.getNode(i).getWord().toCharArray();
            for(int j = 0; j < s.length(); j++){
                if(test[j] < temp[j]){
                    return i;
                }
                if(test[j] > temp[j]) {
                    pos = i+1;
                    break;
                }
            }
        }
        return pos;
    }

    /*
    quick sort to sort the array into alphabetical order
    code adapted from lectures
     */
    void quickSort(int lowerIndex, int higherIndex) {
        int low = lowerIndex;
        int high = higherIndex;
        LinkList pivot = list[lowerIndex + (higherIndex - lowerIndex) / 2];

        while (low <= high) {
            while (list[low].compareTo(pivot) < 0) {
                low++;
            }

            while (list[high].compareTo(pivot) > 0) {
                high--;
            }

            if (low <= high) {
                swap(low++, high--);
            }
        }
        //calling quickSort recursively
        if (lowerIndex < high) {
            quickSort(lowerIndex, high);
        }
        if (low < higherIndex) {
            quickSort(low, higherIndex);
        }
    }

    // swaps items at index start and end
    public void swap(int start, int end){
        LinkList temp = list[start];
        list[start] = list[end];
        list[end] = temp;
    }


    // outputs array of linked lists to given file
    public void write(String outfile) throws IOException {
        FileWriter output = new FileWriter(outfile);
        for(int i = 0; i < arrsize; i++){
            for(int j = 0; j < list[i].getSize(); j++){
                output.write(list[i].getNode(j).getWord() + " ");
            }
            output.write("\n");
        }
        output.close();
    }

}
