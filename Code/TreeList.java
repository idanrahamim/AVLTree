
/**
 * An implementation of a ADT List by Ranked AVL tree (not necessarily BST) which holds Items: (key-info). <br>
 *     Implements methods: retrieve(index), insert(index, key, val), delete(index) in O(logn)
 */
 public class TreeList {

    /**
     * AVLTree (not necessarily BST). Stores nodes each consists of an Item- (key, info). <br>
     *     Used for searching\inserting\deleting elements by their index (equivalent to rank - 1) in O(logn).
     *     Only the field root will be used, nor maxNode nor minNode because they not improve complexity, and requires maintaining
     */
    private AVLTree rankedAvlTree;

    /**
     * Constructor of TreeList class. <br>
     * building an avl tree for every instance of the class.
     */
    public TreeList() {
        this.rankedAvlTree = new AVLTree();
    }

    /**
     * Get Item from the list which is in place (starts from 0) of the given index.
     * @param i index of requested item in the list
     * @return
     * Item which is in the ith position if it exists in the list. <br>
     * Otherwise, returns null
     */
    public Item retrieve(int i) {
        if (! isIndexValidRetrieveAndDelete(i)) {
            return null;
        }
        AVLTree.AVLNode node = getNodeByIndex(i);
        return node.getItem();
    }

    /**
     * inserts an item to the ith position in list  with key k and  info s.
     * @param i index to be filled with the item inserted <br>
     * @param k the key of the item inserted <br>
     * @param s the value of the item inserted
     * @return
     * -1 if i smaller then 0 or i greater then n, otherwise returns 0
     * @implNote
     * (1) first checks where to insert the node: <br>
     *    (1.1) if as last - uses insertLast(AVLNode newNode)
     *    (1.2) else, finds the maximum node in the left subtree of (1) by the method AVLTree.getMaxInSubTree(AVLNode root)
     *         (1.2.1) inserts after (1.2)
     *    (1.3) update height, size and commits rotations in the path to the root
     */
    public int insert(int i, int k, String s) {
        if( !isIndexValidInsert(i)){
            return -1;
        }
        int treeSize = rankedAvlTree.size();
        AVLTree.AVLNode newNode = new AVLTree().new AVLNode(k, s);
        newNode.setSize(1);
        newNode.setHeight(0);
        if (i == treeSize) {
            insertLast(newNode);
        }else{   // i < treeSize
            AVLTree.AVLNode currIndexINode = getNodeByIndex(i);
            if(currIndexINode.getLeft() == null){
                currIndexINode.setLeft(newNode);
                newNode.setParent(currIndexINode);
            }else{
                AVLTree.AVLNode predCurrIndexINode =  rankedAvlTree.getMaxInSubTree( (AVLTree.AVLNode) currIndexINode.getLeft());
                predCurrIndexINode.setRight(newNode);
                newNode.setParent(predCurrIndexINode);
            }
        }
        AVLTree.AVLNode y = (AVLTree.AVLNode) newNode.getParent();
        while (y != null) {
            rankedAvlTree.updateSize(y);
            rankedAvlTree.updateHeight(y);
            rankedAvlTree.checkAndCommitRotation(y);
            y = (AVLTree.AVLNode) y.getParent();

        }

        return 0;

    }

    /**
     * deletes an item in the ith posittion from the list.
     * @param i index of the item requested to be deleted
     * @return
     * returns -1 if i smaller then 0 or i greater then n-1, otherwise returns 0.
     *  @implNote
     *  1. first finds the requested node by getNodeByIndex(int index). If found then: <br>
     *     1.1. sets its children and parent and gets the node to start fix the height and sizes from -
     *       by the method  AVLTree.setParentAndChildrenOnDelete_AndGetStartPoint(AVLNode node) <br>
     *     1.2 fixes the sizes and heights (and does rotations) from the node gotten on 1.1 -
     *       by the method AVLTree.fixHeightAndSizeOnDelete_AndGetNumOfRotations(AVLNode startFixingFrom)
     */
    public int delete(int i) {
        if(! isIndexValidRetrieveAndDelete(i)) {
            return -1;
        }
        AVLTree.AVLNode node = getNodeByIndex(i);
        AVLTree.AVLNode startFixingFrom = rankedAvlTree.setParentAndChildrenOnDelete_AndGetStartPoint(node);
        rankedAvlTree.fixHeightAndSizeOnDelete_AndGetNumOfRotations(startFixingFrom);
        return 0;
    }



    // ----- helper methods ----

    /**
     * Get node which is in place (starts from 0) of the given index in the tree.
     * @param i index of requested item in the list
     * @return
     * Item which is in the ith position if it exists in the Tree. <br>
     * Otherwise, returns null
     */
    private AVLTree.AVLNode getNodeByIndex(int i) {
        return select(i + 1);
    }

    /**
     * returns the node in position i (Rank) (starts from 1) in the Tree. <br>
     * uses recursion by the inner method - selectRec().
     * @param i index of requested node in the Tree
     * @return
     * node which is in the ith position if it exists in the Tree. <br>
     * Otherwise, returns null
     */
    private AVLTree.AVLNode select(int i) {
        return selectRec((AVLTree.AVLNode) rankedAvlTree.getRoot(), i);
    }

    /**
     * returns the node in position i (Rank) (starts from 1) in the Tree that node its root. <br>
     * uses recursion, inner method of select()
     * @param i index of requested node in the Tree that node its root. <br>
     * @param node - the tree's root.
     * @return
     * node which is in the ith position if it exists in the Tree. <br>
     * Otherwise, returns null
     */
    private AVLTree.AVLNode selectRec(AVLTree.AVLNode node, int i) {
        if (node == null || i <= 0) { // didn't find
            return null;
        }

        int leftSize = node.getLeft() == null ? 0 :  ((AVLTree.AVLNode)(node.getLeft())).getSize();
        if (i == leftSize + 1) {
            return node;
        }
        if (leftSize + 1 < i) { // the index i is in the right subtree
            return selectRec((AVLTree.AVLNode) node.getRight(), i - leftSize - 1); // -1 for the current node
        }
        return selectRec((AVLTree.AVLNode)node.getLeft(), i); // otherwise(leftSize + 1 > i) go left
    }

    /**
     * inserts a node to be with the hightest (most right) rank in the Tree.
     * @param newNode node to be inserted most right of the tree.
     */
    private void insertLast(AVLTree.AVLNode newNode) {
        AVLTree.AVLNode maxNode = rankedAvlTree.getMaxInSubTree((AVLTree.AVLNode)rankedAvlTree.getRoot());
        if(maxNode != null) {
            maxNode.setRight(newNode);
            newNode.setParent(maxNode);
        }else{
            rankedAvlTree.setRoot(newNode);
        }
    }

    /**
     * checks if index i is valid for retrieve and delete operations.
     * @param i index to be checked if valid.
     * @return
     * true - if valid, else (smaller the 0 or greater or equals to rankedAvlTree size)- false
     */
    private boolean isIndexValidRetrieveAndDelete(int i) {
        int size = this.rankedAvlTree.size();
        if (i < 0 || i >= size) {
            return false;
        }
        return true;
    }

    /**
     * checks if index i is valid for insert operation.
     * @param
     * i index to be checked if valid.
     * @return
     * true - if valid, else (smaller the 0 or greater then rankedAvlTree size)- false
     */
    private boolean isIndexValidInsert(int i) {
        int size = this.rankedAvlTree.size();
        if (i < 0 || i > size) {
            return false;
        }
        return true;
    }

}