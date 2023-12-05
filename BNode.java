import java.util.ArrayList;

public class BNode <Integer>{
    
    private ArrayList<Integer> keys;
    private ArrayList<BNode<Integer>> children;
    private boolean isLeaf;

    public BNode(boolean b) {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.isLeaf = b;
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }

    public ArrayList<BNode<Integer>> getChildren() {
        return children;
    }

    public BNode<Integer> getChild(int i) {
        return children.get(i);
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setKeys(ArrayList<Integer> keys) {
        this.keys = keys;
    }

    public void setChildren(ArrayList<BNode<Integer>> children) {
        this.children = children;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Override
    public String toString() {
        String str = "[";

        for(Integer i : keys)
            str+= "" + i + " ";

        return str + "]";
    }
}
