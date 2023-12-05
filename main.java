public class main {
    public static void main(String[] args) {
        
        BTree<Integer> bTree = new BTree<>(2);
        
        bTree.insert(35);
        bTree.insert(78);
        bTree.insert(3);
        bTree.insert(22);
        bTree.insert(43);
        bTree.insert(33);
        bTree.insert(17);
        bTree.insert(71);
        bTree.insert(55);
        bTree.insert(29);
        bTree.insert(19);
        bTree.insert(84);
        
        bTree.printTree();
    }
}