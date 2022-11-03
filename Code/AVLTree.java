
/**
 * An implementation of an AVL Tree <br>
 * has a pointer to an AVLNode which is the root of the tree <br>
 * implements search, insert, delete, in O(logn) by keeping the tree balanced
 * (each AVLNode has Balance Factor smaller the 2 and greater the -2)
 */
public class AVLTree {
	/**
	 * will contain a pointer to an AVLNode which is the root of the tree,
	 * enables access to other nodes by that
	 */
	private AVLNode root;
	/**
	 * will point to the AVLNode with the minimum key in the tree
	 */
	private AVLNode minNode;
	/**
	 * will point to the AVLNode with the maximum key in the tree
	 */
	private AVLNode maxNode;

	/**
	 * default constructor- initialize a tree with default fields (root = null)
	 */
	public AVLTree() { }

  /**
   * returns true if and only if the tree is empty (down't contain any node)
   * @return
   * true- if the tree is empty, else- false
   */
  public boolean empty() {
    return this.root == null;
  }

 /**
   * returns the info of an item with key k if it exists in the tree
   * @param k key of item being searched.
   * @return
   * node with key k
   * otherwise, returns null
   */
  public String search(int k)
  {
	  AVLTree.AVLNode node = getNodeByKey(k);
	  if(node != null) {
		return node.getValue();
	  }
	  return null;
  }

  /**
   * inserts an item with key k and info i to the AVL tree.
   * @param k key of item inserted
   * @param i value of item inserted
   * @return
   * the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * -1 if an item with key k already exists in the tree.
   * @implNote
   * 1 first finds the place to insert the node. <br>
   * 2 inserts the new node <br>
   * 3 fixes heights and sizes from the node in 1 and does rotations in the path from (1) to the root. <br>
   *     3.1 rotations will be done maximum once.
   * 4 sets the fields minNode, maxNode by using getMinNode(root), getMaxNode(root)
   */
   public int insert(int k, String i) {
	  AVLNode y = null;
	  AVLNode x = this.root;
	  while (x != null) {
	  	int Xkey = x.getKey();
	  	if (Xkey == k) { // key already exists
	  		return -1;
		}
	  	y = x;
	  	if (k < Xkey) {
	  		x = x.left;
		}
	  	else{
	  		x = x.right;
		}
	  } // node with key k were not existed
	  AVLNode newNode = new AVLNode(k, i);
	  newNode.setHeight(0);
	  newNode.setSize(1);
	  newNode.parent = y;
	  if (y == null) {
	  	this.root = newNode; // the tree was empty
	  }
	  else if (k < y.getKey()) {
	  	y.left = newNode;
	  }
	  else {
	  	y.right = newNode;
	  } // after insertion, execute rotations + fix size and length.

	  int TotalRotations = 0;
	  while (y != null) {
	  	updateSize(y);
	  	updateHeight(y);
	  	int CountRotations = checkAndCommitRotation(y); // rotations will be done maximum once.
	  	TotalRotations += CountRotations;
	  	y = y.parent;

	  }

	  this.maxNode = getMaxInSubTree(this.root);
	   this.minNode = getMinInSubTree(this.root);
	   return TotalRotations;
   }

  /**
   * deletes an item with key k from the binary tree, if it is there
   * @param k key of item requested to be deleted.
   * @return
   * the number of rebalancing operations, or 0 if no rebalancing operations were needed,
   * or -1 if an item with key k was not found in the tree.
   * @implNote
   * 1. first finds the requested node by getNodeByKey(int key). If found then: <br>
   *   1.1. sets its children and parent and gets the node to start fix the height and sizes from - by the method  setParentAndChildrenOnDelete_AndGetStartPoint(AVLNode node) <br>
   *   1.2 fixes the sizes and heights (and does rotations) from the node gotten on 1.1
   *   by the method fixHeightAndSizeOnDelete_AndGetNumOfRotations(AVLNode startFixingFrom)
   *   1.3 sets the fields minNode, maxNode by using getMinNode(root), getMaxNode(root)
   */
   public int delete(int k) {
	   AVLNode node = getNodeByKey(k);
	   if (node == null) {
		   return -1;
	   }
	   AVLNode startFixingFrom = setParentAndChildrenOnDelete_AndGetStartPoint(node);
	   int numOfRotations = fixHeightAndSizeOnDelete_AndGetNumOfRotations(startFixingFrom);

	   this.maxNode = getMaxInSubTree(this.root);
	   this.minNode = getMinInSubTree(this.root);

	   return numOfRotations;
   }

   /**
	* @return
    * Returns the info of the item with the smallest key in the tree, <br>
    * or null if the tree is empty
    */
   public String min() {
	   AVLNode min = this.minNode;
	   if (min == null) {
		   return null;
	   }
	   return min.getValue();
   }

   /**
    * @return
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max() {
	   AVLNode max = this.maxNode;
	   if (max == null) {
		   return null;
	   }
	   return max.getValue();
   }

  /**
   * @return
   * Returns a sorted array which contains all keys in the tree, <br>
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
        java.util.ArrayList<AVLNode> list = new java.util.ArrayList<>();
        inOrder(this.root, list);
        int[] keys = new int[list.size()];
        int i = 0;
        for (AVLNode node : list) {
        	keys[i] = node.getKey();
        	i++;
        }
        return keys;
  }

  /**
   * @return
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,<br>
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  java.util.ArrayList<AVLNode> list = new java.util.ArrayList<>();
	  inOrder(this.root, list);
	  String[] vals = new String[list.size()];
	  int i = 0;
	  for (AVLNode node : list) {
		  vals[i] = node.getValue();
		  i++;
	  }
	  return vals;
  }

   /**
    * @return
    * Returns the number of nodes in the tree.
    * precondition: none
    * postcondition: none
    */
   public int size() {
   	if (this.root == null) {
   		return 0;
	}
   	return this.root.getSize();
   }
   
     /**
    * @return
    * Returns the root AVL node, or null if the tree is empty
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot() {
	   return this.root;
   }


	// -------- helper funcs ---------------


	/**
	 * sets the parent and children of a node on delete <br>
	 *     and returns the node which is the first to start fixing size and height from
	 * @param node the node which has to be deleted
	 * @return
	 * node which is the first to start fixing size and height from
	 * @implNote
	 * seperates between cases: <br>
	 *     1. has to children (uses setParentAndChildrenOnDelete_AndGetStartPoint_InCaseOfTwoChildren(AVLNode node) <br>
	 *     2. has only right child <br>
	 *     3. has only left child <br>
	 *     4. doesn't have any children
	 */
	public AVLNode setParentAndChildrenOnDelete_AndGetStartPoint(AVLNode node) {
		AVLNode startFixingFrom = null;
		if (node.left != null && node.right != null) { // has two children
			startFixingFrom = setParentAndChildrenOnDelete_AndGetStartPoint_InCaseOfTwoChildren(node);
		}
		else {
			AVLNode parent = node.parent;
			if (node.right != null) { // has only right child
				AVLNode right = node.right;
				if (parent == null) { // means we want to delete the root
					root = right;
				} else { // the given node isn't the root
					if (node == parent.right) {
						parent.right = right;
					} else {
						parent.left = right;
					}
				}

				right.parent = parent;
			} else if (node.left != null) { // has only left child
				AVLNode left = node.left;
				if (parent == null) { // means we want to delete the root
					root = node.left;
				} else { // the given node isn't the root
					if (node == parent.right) {
						parent.right = left;
					} else {
						parent.left = left;
					}
				}

				left.parent = parent;
			} else { // has no children
				if (parent == null) { // deleting the root which has no children
					root = null;
				} else {
					if (node == parent.right) {
						parent.right = null;
					} else {
						parent.left = null;
					}
				}
			}

			startFixingFrom = parent;
		}

		return startFixingFrom;
	}

	/**
	 * sets the parent and children of a node with two children on delete <br>
	 * 	and returns the node which is the first to start fixing size and height from
	 * @param node node which has to be deleted. pre condition - the node has two children
	 * @return
	 * node which is the first to start fixing size and height from
	 * @implNote
	 * 	first finds the successor by getSuccessorInCaseOfTwoChildren(AVLNode node). <br>
	 * 	If the given node is successor's right child then the returned node is the successor itself. <br>
	 * 	  otherwise, returns successor's parent. <br>
	 * 	uses exchangeOriginToSuccessor(AVLNode node, AVLNode successor) to change the successor place to be instead of the given node
	 * 	 (phisically deletion of successor)
	 */
	private AVLNode setParentAndChildrenOnDelete_AndGetStartPoint_InCaseOfTwoChildren(AVLNode node) {
		AVLNode startFixingFrom = null;
		AVLNode successor = getSuccessorInCaseOfTwoChildren(node); // the successor must be under the node because it has 2 children
		if (successor == node.right) { // if the successor is the right child then start the rotations for itself
			startFixingFrom = successor;
		}
		else { // start the rotations from the father of the phisically deleted node
			startFixingFrom = successor.parent; // cannot be null
		}
		exchangeOriginToSuccessor(node, successor);
		return startFixingFrom;
	}


	/**
	 * returns the node with max key in the subtree which origin is the root.
	 * @param origin the root of sub-tree to find the node with the max key in
	 * @return
	 * the node which has the highest key in the subtree
	 */
	public AVLNode getMaxInSubTree(AVLNode origin) {
		if (origin == null) { // the tree is empty
			return null;
		}
		while (origin.getRight() != null) {
			origin = (AVLNode) origin.getRight();
		}
		return origin;
	}

	/**
	 * checks for a node if needs rotation. <br>
	 *     if does - commit rotations, updating heights and sizes of the participant nodes and count number of rotations done.
	 * @param node to check if its  avl criminal.
	 * @return
	 * returns how many rotations were made if needed.
	 */
	public int checkAndCommitRotation(AVLNode node) {
		int bf = node.getBF(node);
		if (Math.abs(bf) != 2) {
			return 0;
		}
		if (bf == 2) {
			int leftBF = node.getBF(node.left); // node.left cannot be null because BF = 2
			if (leftBF == 1 || leftBF == 0) { // equals 0 in delete
				rotateRight(node);
				return 1;
			}
			// equals -1
			rotateLeftRight(node);
			return 2;
		}

		// equals -2
		int rightBF = node.getBF(node.right);
		if (rightBF == -1 || rightBF == 0) {
			rotateLeft(node);
			return 1;
		}
		// equals 1
		rotateRightLeft(node);
		return 2;
	}


	/**
	 * get a node by it key
	 * @param key key of the node being searched
	 * @return
	 * returns the node with key of the argument inserted if it exists in the tree
	 * otherwise, returns null
	 * @implNote
	 * uses recursive function getNodeByKeyRec(AVLNode node ,int key) as helper.
	 */
	private AVLNode getNodeByKey(int key) {
		return getNodeByKeyRec(this.root, key);
	}

	/**
	 * recursive function as helper for getNodeByKey(int key)
	 * @param node root of a subtree
	 * @param key key of the node being searched
	 * @return
	 * returns the node with key of the argument inserted if it exists in the subtree
	 * otherwise, returns null
	 */
	private AVLNode getNodeByKeyRec(AVLNode node ,int key){
		if (node == null) {
			return null;
		}
		int nodeKey = node.item.getKey();
		if (nodeKey == key) {
			return node;
		}
		if (key < nodeKey) {
			return getNodeByKeyRec(node.left ,key);
		}
		return getNodeByKeyRec(node.right ,key);
	}



	/**
	 * sets the successor node to exchange origin (in case node has to children) <br>
	 *     means phisically deletion of successor and deletion of origin
	 * @param origin the node its place will be overidden
	 * @param successor the node which will be put instead of origin
	 */
	public void exchangeOriginToSuccessor(AVLNode origin, AVLNode successor) {
		AVLNode originParent = origin.parent;
		if (originParent == null) { // origin = root
			root = successor;
		}
		else if (origin == originParent.left) {
			originParent.left = successor;
		}
		else {
			originParent.right = successor;
		}

		AVLNode newNodeParent = successor.parent; // cannot be null
		if (newNodeParent.left == successor) { // successor isn't the right child of origin
			AVLNode newNodeRight = successor.right;
			newNodeParent.left = newNodeRight; // because successor doesn't have right child
			if (newNodeRight != null) {
				newNodeRight.parent = newNodeParent;
			}
			successor.right = origin.right;
			origin.right.parent = successor; // origin.right cannot be null
		}
		else { // successor is the right child of origin
			// do nothing
		}

		successor.parent = originParent;
		AVLNode originLeft = origin.left;
		successor.left = originLeft;
		if (originLeft != null) {
			originLeft.parent = successor;
		}
	}

	/**
	 * return the min node in the subtree which origin is the root
	 * @param origin root of the tree
	 * @return
	 * the min node in the subtree which origin is the root.
	 * if tree is empty, returns null
	 */
	private AVLNode getMinInSubTree(AVLNode origin) {
		if (origin == null) { // the tree is empty
			return null;
		}
		while (origin.getLeft() != null) {
			origin = (AVLNode) origin.getLeft();
		}
		return origin;
	}

	/**
	 * commits a rotation to the right
	 * @param x the node needs a rotation to the right.
	 */
   private void rotateRight(AVLNode x) {
   	AVLNode y = x.left;
   	x.left = y.right; // turn y's right subtree into x's left subtree
   	if (y.right != null) {
   		y.right.parent = x;
	}
   	y.parent = x.parent;
   	if (x.parent == null) { // means x was the root - make y to be the new root
   		this.root = y;
	}
   	else if (x == x.parent.left) {
   		x.parent.left = y;
	}
   	else {
   		x.parent.right = y;
	}
   	y.right = x;
   	x.parent = y;

   	setHeightsOnRotation(x, y);
   	setSizesOnRotation(x, y);
   }

	/**
	 * commits a rotation to the left
	 * @param x the node needs a rotation to the right.
	 */
   private void rotateLeft(AVLNode x) {
   	AVLNode y = x.right;
   	x.right = y.left; // turn y's left subtree into x's right subtree
   	if (y.left != null) {
   		y.left.parent = x;
	}
   	y.parent = x.parent;
   	if (x.parent == null) { // means x was the root - make y to be the new root
   		this.root = y;
	}
   	else if (x == x.parent.left) {
   		x.parent.left = y;
	}
   	else {
   		x.parent.right = y;
	}
   	y.left = x;
   	x.parent = y;

   	setHeightsOnRotation(x, y);
   	setSizesOnRotation(x, y);
   }


	/**
	 * commits two rotations, first to the left and then to the right.
	 * @param node the node needs rotations to the left and then to the right.
	 */
   private void rotateLeftRight(AVLNode node) {
	   AVLNode left = node.left;
	   rotateLeft(left);
	   rotateRight(node);
   }

	/**
	 * commits two rotations, first to the right and then to the left.
	 * @param node the node needs rotations to the right and then to the left.
	 */
   private void rotateRightLeft(AVLNode node) {
		AVLNode right = node.right;
		rotateRight(right);
		rotateLeft(node);
   }


	/**
	 * sets the heights of the participants nodes in a rotation.
	 * @param wasParent node which used to be the parent and now will be newParent's child
	 * @param newParent node which used to be wasParent's child and now will become its parent
	 */
   private void setHeightsOnRotation(AVLNode wasParent, AVLNode newParent) {
	   newParent.setHeight(wasParent.getHeight());
	   int leftHeight = wasParent.left == null ? -1 : wasParent.left.getHeight();
	   int rightHeight = wasParent.right == null ? -1 : wasParent.right.getHeight();
	   wasParent.setHeight(1 + Math.max(leftHeight, rightHeight));
   }

	/**
	 * sets the sizes of the participants nodes in a rotation.
	 * @param wasParent node which used to be the parent and now will be newParent's child
	 * @param newParent node which used to be wasParent's child and now will become its parent
	 */
	private void setSizesOnRotation(AVLNode wasParent, AVLNode newParent) {
		newParent.setSize(wasParent.getSize());
		int leftSize = wasParent.left == null ? 0 : wasParent.left.getSize();
		int rightSize = wasParent.right == null ? 0 : wasParent.right.getSize();
		wasParent.setSize(1 + leftSize + rightSize);
	}


	/**
	 * gets the successor of the node origin in case origin has 2 children
	 * @param origin node to find its successor. pre condition - the node has two children
	 * @return
	 * the successor of origin
	 */
	public AVLNode getSuccessorInCaseOfTwoChildren(AVLNode origin) {
		return getMinInSubTree(origin.right);
	}


	// gets start point to fix from (including itself)
	// fixes the height + size + does rotations
	// returns sum of rotations committed

	/**
	 * fixes heights, sizes, and does rotations from the given node and above in its path to the root
	 * @param startFixingFrom node to start fixing height, size and does rotation from (and include)
	 * @return
	 * number of rotations done
	 * @implNote
	 * for each node from the given in the path climbing up to the root do:
	 * 1. update height and size of the current node using updateSize(AVLNode), updateHeight(AVLNode);
	 * 2. commits rotations (checks before if needed) - both by checkAndCommitRotation(AVLNode), and sums them. <br>
	 *     Pay attention, this function fixes the heights and sizes too.
	 */
	public int fixHeightAndSizeOnDelete_AndGetNumOfRotations(AVLNode startFixingFrom) {
		int sumRotations = 0, numOfRotations;
		while (startFixingFrom != null) {
			updateSize(startFixingFrom);
			updateHeight(startFixingFrom);
			numOfRotations = checkAndCommitRotation(startFixingFrom);
			sumRotations += numOfRotations;
			startFixingFrom = startFixingFrom.parent;
		}
		return sumRotations;
	}

	/**
	 * updates the field size of node- node.
	 * @param node the node its size need update
	 */
	public void updateSize(AVLNode node) {
		int[] childrenSizes = getChildrenSizes(node);
		int leftSize = childrenSizes[0];
		int rightSize = childrenSizes[1];
		node.setSize(1 + leftSize + rightSize);
	}
	/**
	 * returns an array with the sizes of the children of node.
	 * @param node the node its children sizes needed.
	 * @return
	 * an array [leftSize, rightSize]
	 */
	private int[] getChildrenSizes(AVLNode node) {
		int leftSize = node.left == null ? 0 : node.left.getSize();
		int rightSize = node.right == null ? 0 : node.right.getSize();
		return new int[]{leftSize, rightSize};
	}

	/**
	 * updates the field height of node- node.
	 * @param node the node its height need update
	 */
	public void updateHeight(AVLNode node) {
		int[] childrenHeights = getChildrenHeights(node);
		int leftHeight = childrenHeights[0];
		int rightHeight = childrenHeights[1];
		node.setHeight(1 + Math.max(leftHeight, rightHeight));
	}

	/**
	 * returns an array with the heights of the children of node.
	 * @param node the node its children heights needed.
	 * @return
	 * an array [leftHeight, rightHeight]
	 */
	private int[] getChildrenHeights(AVLNode node) {
		int leftHeight = node.left == null ? -1 : node.left.getHeight();
		int rightHeight = node.right == null ? -1 : node.right.getHeight();
		return new int[]{leftHeight, rightHeight};
	}


	/**
	 * fills the given list with the nodes sorted by their keys from small to higher. <br>
	 * @param node the root of subtree
	 * @param list contains the so-far sorted nodes by their keys
	 * @implNote works recursively
	 */
	private void inOrder(AVLNode node, java.util.ArrayList<AVLNode> list){
		if(node != null) {
			inOrder(node.left, list);
			list.add(node);
			inOrder(node.right, list);
		}
	}

	/**
	 * sets a new root to the tree.
	 * @param newRoot the new root
	 */
	public void setRoot(AVLNode newRoot){
		this.root = newRoot;
	}


		/**
           * public interface IAVLNode
           * ! Do not delete or modify this - otherwise all tests will fail !
           */
	public interface IAVLNode{	
		public int getKey(); //returns node's key 
		public String getValue(); //returns node's value [info]
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node 
	}

   /**
   * class AVLNode implements IAVNode
   * uses to creat nodes with fields: item-key+val,parent,left,right,height,size
   * nodes uses by TreeList class to build a tree.
   */
  public class AVLNode implements IAVLNode{
  		private Item item;
  		private AVLNode parent;
		private AVLNode left;
		private AVLNode right;
		private int height;
		private int size;

	   /**
		* default constructor
		* initialize a node, with default fields.
		*/
		public AVLNode(){ }
	   /**
		* constructor initialize a node to have Item with key and info as given
		* @param key- nodes key
		* @param info- nodes info
		*/
		public AVLNode(int key, String info) {
  			this.item = new Item(key, info);
		}

	   /**
		* @return
		* Item that contain key and val of the node.
		*/
		public Item getItem() {
			return this.item;
		}

	   /**
		* @return
		* key of the node.
		*/
		public int getKey() {
			return this.item.getKey();
		}

	   /**
		* @return
		* val of the node.
		*/
		public String getValue() {
			return this.item.getInfo();
		}

	   /**
		* sets the left child of the node
		* @param node left node
		*/
		public void setLeft(IAVLNode node)	{
			this.left = (AVLNode)node;
		}
	   /**
		* gets the left child of the node
		* @return
		* left child node
		*/
		public IAVLNode getLeft()	{
			return this.left;
		}

	   /**
		* sets the right child of the node
		* @param node right node
		*/
		public void setRight(IAVLNode node)	{
			this.right = (AVLNode)node;
		}

	   /**
		* gets the right child of the node
		* @return
		* right child node
		*/
		public IAVLNode getRight() {
  			return this.right;
		}

	   /**
		* sets the parent of the node
		* @param node parent node
		*/
		public void setParent(IAVLNode node) {
			this.parent = (AVLNode)node;
		}
	   /**
		* gets the parent of the node
		* @return
		* parent of the node
		*/
		public IAVLNode getParent()	{
			return this.parent;
		}

	   /**
		* sets the height of the node
		* @param height new height to be updated
		*/
    	public void setHeight(int height) {
			this.height = height;
		}
	   /**
		* gets the height of the node
		* @return
		* height of the node
		*/
    	public int getHeight() {
			return this.height;
		}

	   /**
		* sets the size of the node
		* @param size new size to be updated
		*/
	   public void setSize(int size) {
  			this.size = size;
	   }
	   /**
		* gets the size of the node
		* @return
		* size of the node
		*/
	   public int getSize() {
		   return this.size;
	   }

	   /**
		* get the balance factor of a node
		* @param node to calculate its balance factor
		* @return
		* the Balance Factor of the node
		*/
	   private int getBF(AVLNode node) {
		   int leftHeight = node.left == null ? -1 : node.left.getHeight();
		   int rightHeight = node.right == null ? -1 : node.right.getHeight();
		   return leftHeight - rightHeight;
	   }



  }

}




