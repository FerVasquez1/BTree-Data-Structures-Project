import java.util.ArrayList;

public class BTree <Integer> {
    private BNode<Integer> root;
    private int degree;
    int max;

    public BTree(int degree) {
        this.root = new BNode<>(true);
        this.degree = degree;

        max = (2 * degree) - 1;
    }

    public pair search(Integer key) {
        return search(root, key);
    }

    public pair search(BNode<Integer> father, Integer key) {
        int i = 0;

        while(i < father.getKeys().size() && (int) key > (int) father.getKeys().get(i))
            i++;

        if(i < father.getKeys().size() && (int) key == (int) father.getKeys().get(i))
            return new pair(father, i);
        else if(father.isLeaf())    
            return null;
        else
            return search(father.getChild(i), key);
    }

    public void insert(Integer k) {

        if(root.getKeys().size() == max) {
            BNode<Integer> newRoot = new BNode<>(false);
            newRoot.getChildren().add(0, root);
            root = newRoot;
            splitChild(newRoot, 0);
            insertNonFull(newRoot, k);
        }
        else
            insertNonFull(root, k);
    }

    private void splitChild(BNode<Integer> fatherNode, int i) {
        BNode<Integer> splitNode1 = fatherNode.getChild(i);

        BNode<Integer> splitNode2 = new BNode<>(splitNode1.isLeaf());
        fatherNode.getChildren().add(i+1, splitNode2);

        fatherNode.getKeys().add(i, splitNode1.getKeys().get(degree-1));

        splitNode2.setKeys(new ArrayList<>(splitNode1.getKeys().subList(degree, max)));
        splitNode1.setKeys(new ArrayList<>(splitNode1.getKeys().subList(0, degree - 1)));
        
        if(!splitNode1.isLeaf()) {
            splitNode2.setChildren(new ArrayList<>(splitNode1.getChildren().subList(degree,  max + 1)));
            splitNode1.setChildren(new ArrayList<>(splitNode1.getChildren().subList(0, degree)));
        }
    }

    private void insertNonFull(BNode<Integer> node, Integer keys) {
        int i = node.getKeys().size() - 1;

       if(node.isLeaf()) {
            node.getKeys().add(null);
            while (i >= 0 && (int) keys < (int) node.getKeys().get(i)) {
                node.getKeys().set(i + 1, node.getKeys().get(i));
                i--;
            }
            node.getKeys().set(i + 1, keys);
        }
        else {
            while (i >= 0 && (int) keys < (int) node.getKeys().get(i) ) 
                i--;
            i++;
            if(node.getChildren().get(i).getKeys().size() == max) {
                splitChild(node, i);
                
                if ((int) keys > (int) node.getKeys().get(i)) {
                    i++;
                }
            }
            insertNonFull(node.getChild(i), keys);
        }
    }

    public void delete(Integer key) {
        if (root == null) {
            System.out.println("tree is empty.");
            return;
        }

        delete(root, key);

        // adjust root
        if (root.getKeys().isEmpty() && !root.isLeaf()) {
            root = root.getChildren().get(0);
        }
    }

    private void delete(BNode<Integer> node, Integer key) {
        
        int i = 0;
        while (i < node.getKeys().size() && (int) key > (int) node.getKeys().get(i)) {
            i++;
        }

        if (i < node.getKeys().size() && key.equals(node.getKeys().get(i))) {
            // case 1: key is in node
            deleteKeyFromNode(node, i);
        } else if (node.isLeaf()) {
            System.out.println("not in tree");
        } else {
            //case 2: key is not in node (not a leaf)
            BNode<Integer> child = node.getChildren().get(i);
            if (child.getKeys().size() < degree) {
                // case 3: fewere keys than degree
                borrowOrMerge(node, i);
            }
            delete(child, key);
        }
    }

    private void deleteKeyFromNode(BNode<Integer> node, int i) {
        // case 1a: leaf
        if (node.isLeaf()) {
            node.getKeys().remove(i);
        } else {
            // case 2a: key in internal node
            BNode<Integer> child = node.getChild(i);
            BNode<Integer> predecessorNode = getPredecessor(child);

            Integer predecessor = predecessorNode.getKeys().get(predecessorNode.getKeys().size() - 1);
            node.getKeys().set(i, predecessor);

            delete(child, predecessor);
        }
    }

    private BNode<Integer> getPredecessor(BNode<Integer> node) {
        while (!node.isLeaf()) {
            node = node.getChildren().get(node.getChildren().size() - 1);
        }
        return node;
    }

    private void borrowOrMerge(BNode<Integer> parentNode, int i) {
        if (i > 0 && parentNode.getChildren().get(i - 1).getKeys().size() >= degree) {
            // case 1b: borrow from left
            borrowFromLeftSibling(parentNode, i);
        } else if (i < parentNode.getChildren().size() - 1 &&
                   parentNode.getChildren().get(i + 1).getKeys().size() >= degree) {
            // case 2b: borrow from right
            borrowFromRightSibling(parentNode, i);
        } else {
            // case 3a: merge!!
            mergeWithSibling(parentNode, i);
        }
    }

    private void borrowFromLeftSibling(BNode<Integer> node, int i) {
        BNode<Integer> child = node.getChild(i);
        BNode<Integer> left = node.getChild(i-1);

        // move key from left sibling
        Integer borrowedKey = left.getKeys().remove(left.getKeys().size() - 1);
        child.getKeys().add(0, node.getKeys().get(i - 1));
        node.getKeys().set(i - 1, borrowedKey);

        // move child from left
        if (!child.isLeaf()) {
            child.getChildren().add(0, left.getChildren().remove(left.getChildren().size() - 1));
        }
    }

    private void borrowFromRightSibling(BNode<Integer> node, int i) {
        BNode<Integer> childNode = node.getChild(i);
        BNode<Integer> rightSibling = node.getChild(i+1);


        Integer borrowedKey = rightSibling.getKeys().remove(0);
        childNode.getKeys().add(node.getKeys().get(i));
        node.getKeys().set(i, borrowedKey);


        if (!childNode.isLeaf()) {
            childNode.getChildren().add(rightSibling.getChildren().remove(0));
        }
    }

    private void mergeWithSibling(BNode<Integer> node, int i) {
        BNode<Integer> left = (i > 0) ? node.getChildren().get(i - 1) : null;
        BNode<Integer> right = (i < node.getChildren().size() - 1) ?
                                       node.getChildren().get(i + 1) : null;

        if (left != null) {
            BNode<Integer> childNode = node.getChildren().get(i);
            Integer parentkey = node.getKeys().remove(i - 1);

            left.getKeys().add(parentkey);
            left.getKeys().addAll(childNode.getKeys());

            left.getChildren().addAll(childNode.getChildren());
            node.getChildren().remove(i);
        } else if (right != null) {
            BNode<Integer> childNode = node.getChild(i);
            Integer parentkey = node.getKeys().remove(i);

            childNode.getKeys().add(parentkey);
            childNode.getKeys().addAll(right.getKeys());

            childNode.getChildren().addAll(right.getChildren());
            node.getChildren().remove(i + 1);
        }
    }


    public void printTree() {
        if (root != null) {
            printNode(root, 0);
        }
    }

    private void printNode(BNode<Integer> node, int level) {
        System.out.print("Level " + level + ": ");
        for (Integer key : node.getKeys()) {
            System.out.print(key + " ");
        }
        System.out.println();

        if (!node.isLeaf()) {
            for (BNode<Integer> child : node.getChildren()) {
                printNode(child, level + 1);
            }
        }
    }
}
