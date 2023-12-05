public class pair<Integer> {
    private BNode<Integer> Node;
    private int location;

    public pair(BNode<Integer> father, int location) {
        this.Node = father;
        this.location = location;
    }

    public String toString() {
        return  Node.toString() + ", <" + location + ">"; 
    }
}
