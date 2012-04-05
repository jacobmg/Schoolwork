import java.util.ArrayList;

//Note: ArrayList only utilised in the printLevelOrder method

//Note: remove does not work when node has two children, balance must be fixed

/**
 * BinarySearchTree.java
 * Written by: Jacob Gold
 * Email address: jacobg@brandeis.edu
 * 
 * This class is a binary search tree. It is a set of nodes where each node contains
 * references to a key, a value, and the parent, left, and right nodes. All the nodes
 * to the left of a given node contain a key less than its key, all the nodes to the
 * right contain a greater one. This guarantees log(n) time searching for a balanced
 * tree.
 *
 */

public class BinarySearchTree<E extends Comparable<E>> implements TreeInterface<E>{
	
	/*
	 * A node representing a value in a binary tree, with pointers to the parent,
	 * left child, and right child
	 */

    private class Node{
		private String key;
    	private E value;
		private Node parent;
		private Node left;
		private Node right;
		
		public Node(String s, E data, Node p, Node l, Node r){
			key = s;
			value = data;
			parent = p;
			left = l;
			right = r;
		}
		
		public Node(String s, E data, Node p){
			key = s;
			value = data;
			parent = p;
			left = null;
			right = null;
		}
		
		public Node(String s, E data){
			key = s;
			value = data;
			parent = null;
			left= null;
			right = null;
		}
		
    }

    private Node node;

	//These are the only two constructors you should have for BinarySearchTree
    public BinarySearchTree(){
		node = null;
    }
	
	private BinarySearchTree(BinarySearchTree<E>.Node n){
		node = n;
	}

    /**
     * Throws an error if the tree has no left child
     *@return BinarySearchTree   the left child of a tree
     */
    public BinarySearchTree<E> left() throws Exception{
    	if(node.left == null){
    		throw new Exception();
    	}
    	return new BinarySearchTree<E>(node.left);
    }

    /**
     * Throws an error if the tree has no right child
     *@return BinarySearchTree   the right child of a tree
     */
    public BinarySearchTree<E> right() throws Exception{
    	if(node.right == null){
    		throw new Exception();
    	}
    	return new BinarySearchTree<E>(node.right);
    }

    /**
     * Throws an error if the tree has no parent
     *@return BinarySearchTree  the parent of the tree
     */
    public BinarySearchTree parent() throws Exception{
		if(node.parent == null){
			throw new Exception();
		}
		return new BinarySearchTree<E>(node.parent);
    }

    /**
     *check if the tree has a node to the left
     */
    public boolean hasLeft(){
    	if(node.left == null){
			return false;
		}
		return true;
    }

    /**
     * check to see if the tree has a node to the right
     */
    public boolean hasRight(){
		if(node.right == null){
			return false;
		}
		return true;
    }

    /**
     * check to see if the tree has a root
     */
    public boolean isEmpty(){
		if(node == null){
			return true;
		}
		return false;
    }

    /**
     * check to see if the node is a leaf
     */
    public boolean isLeaf(){
		if(!hasLeft() && !hasRight()){
			return true;
		}
		return false;
    }

    /**
     * Checks to see if the tree is internal (has no parent)
     *@return boolean
     */
    public boolean isInternal(){
		if(isLeaf()){
			return false;
		}
		return true;
    }

    /**
     * Checks to see if the tree is the root
     *@return boolean
     */
    public boolean isRoot(){
		if(node.parent == null){
			return true;
		}
		return false;
    }

    /**
     * Create a new node containing element e, add the new node 
     * as the root, and return the altered tree; an error occurs
     * if the tree is not empty.
     *@param e  Element to be made into new root node
     *@return BinarySearchTree  resulting Binary Tree
     */
    public BinarySearchTree addRoot(String key, E e){
    	node = new Node(key, e);
    	return this;
    }

    /**
     * Create a new node containing element e, add the new node
     * as the left child of the tree, and return the altered tree;
     * an error occurs if the tree already has a left child
     *@param e   Element to be made into new left child node
     *@return BinarySearchTree  resulting Binary Tree
     */
    public BinarySearchTree insertLeft(String key, E e) throws Exception{
		if(hasLeft()){
			throw new Exception();
		}
		node.left = new Node(key, e);
		return this;
    }

    /**
     * Create a new node containing element e, add the new node
     * as the right child of the tree, and return the altered tree;
     * an error occurs if the tree already has a right child
     *@param e  Element to be made into new right child node
     *@return BinarySearchTree   resulting Binary Tree
     */
    public BinarySearchTree insertRight(String key, E e) throws Exception{
		if(hasRight()){
			throw new Exception();
		}
		node.right = new Node(key, e);
		return this;
    }
    
    /*
     * Inserts a given key-value pair into the tree in the correct position
     */
    
    public void insert(String key, E value){
    	if(node == null){
    		addRoot(key, value);
    		return;
    	}
    	if(key.compareTo(node.key) > 0){
    		try{
    			if(hasRight()){
    				right().insert(key, value);
    			}
    			else{
    				insertRight(key, value);
    			}
    		}
    		catch(Exception e){
    			System.err.println("Has right child");
    		}
    	}
    	else{
    		if(key.compareTo(node.key) < 0){
    			try{
    				if(hasLeft()){
    					left().insert(key, value);
    				}
    				else{
    					insertLeft(key, value);
    				}
    			}
    			catch(Exception e){
    				System.err.println("Has left child");
    			}
    		}
    		else{
    			System.err.println("Key already exists");
    		}
    	}	
    }
    
    /*
     * Returns the value associated with a given key if it is present
     * in the tree; returns null otherwise.
     */
    
    public E search(String key){
    	if(node == null){
    		return null;
    	}
    	if(key.compareTo(node.key) > 0){
    		try{
    			if(hasRight()){
    				return right().search(key);
    			}
    			else{
    				return null;
    			}
    		}
    		catch(Exception e){
    			System.err.println("No right child");
    		}
    	}
    	else{
    		if(key.compareTo(node.key) < 0){
    			try{
    				if(hasLeft()){
    					return left().search(key);
    				}
    				else{
    					return null;
    				}
    			}
    			catch(Exception e){
    				System.err.println("No left child");
    			}
    		}
    		else{
    			return node.value;
    		}
    	}
    	return null;
    }

    /**
     * Remove the root node, replace it with its child, if any,
     * and return the element stored at the former root node;
     * an error occurs if the tree has two children
     *@return E   Element at the top of tree
     */
    public E remove() throws Exception{
		if(hasLeft()){
			if(hasRight()){
				throw new Exception();
			}
			else{
				Node temp = node;
				node = node.left;
				return temp.value;
			}
		}
		else{
			if(hasRight()){
				Node temp = node;
				node = node.right;
				return temp.value;
			}
			else{
				Node temp = node;
				node = null;
				return temp.value;
			}
		}
    }
    
    /*
     * Removes the key-value pair associated with a given key
     */
    
    public void remove(String key){
    	if(node == null){
    		System.err.println("Key does not exist");
    		return;
    	}
    	if(key.compareTo(node.key) > 0){
    		try{
    			if(hasRight()){
    				right().remove(key);
    			}
    			else{
    				System.err.println("Key does not exist");
    	    		return;
    			}
    		}
    		catch(Exception e){
    			System.err.println("No right child");
    		}
    	}
    	else{
    		if(key.compareTo(node.key) < 0){
    			try{
    				if(hasLeft()){
    					left().remove(key);
    				}
    				else{
    					System.err.println("Key does not exist");
    		    		return;
    				}
    			}
    			catch(Exception e){
    				System.err.println("No left child");
    			}
    		}
    		else{
    			try{
    				remove();
    			}
    			catch(Exception e){
    				System.err.println("Could not remove");
    			}
    		}
    	}
    }

    /**
     * Return the height of the tree. Your method should be recursive.
     * @return int   height of the tree
     */
    public int height(){
		if(isLeaf()){
			return 0;
		}
		int l = 0, r = 0;
		if(hasLeft()){
			try{
				l = left().height();
			}
			catch(Exception e){
				System.err.println("Error: no left node");
			}	
		}
		if(hasRight()){
			try{
				r = right().height();
			}
			catch(Exception e){
				System.err.println("Error: no right node");
			}
		}
		return Math.max(l, r) + 1;
    }
    
    /*
     * Returns the number of key-value pairs stored in the tree
     */
    
    public int size(){
    	if(node == null){
    		return 0;
    	}
    	int l = 0, r = 0;
    	if(hasLeft()){
			try{
				l = left().size();
			}
			catch(Exception e){
				System.err.println("Error: no left node");
			}	
		}
		if(hasRight()){
			try{
				r = right().size();
			}
			catch(Exception e){
				System.err.println("Error: no right node");
			}
		}
		return l + r + 1;
    }

    /**
     * return a new tree which looks like the mirror image of the
     * given tree (your method should be recursive) for example: 
     *    A                          A
     *   / \                        / \
     *  B   C   you should return  C   B
     * / \                            / \
     *D   E                          E   D
     *
     *@return BinarySearchTree   mirrored tree
     */
    public BinarySearchTree mirrorTree(){
		if(hasLeft()){
			if(hasRight()){
				Node temp = node.left;
				node.left = node.right;
				node.right = temp;
				try{
					left().mirrorTree();
					right().mirrorTree();
				}
				catch(Exception e){
					
				}
			}
			else{
				node.right = node.left;
				node.left = null;
				try{
					right().mirrorTree();
				}
				catch(Exception e){
					System.err.println("Error: no right node");
				}
			}
		}
		else{
			if(hasRight()){
				node.left = node.right;
				node.right = null;
				try{
					left().mirrorTree();
				}
				catch(Exception e){
					System.err.println("Error: no left node");
				}
			}
		}
		return this;
    }

    /**
     * Recall that a leaf is a Tree with no children.
     * Your method should be recursive.
     *@return int   count of number of leaves in the tree
     */
    public int leafCount(){
    	if(isLeaf()){
			return 1;
		}
		int l = 0, r = 0;
		if(hasLeft()){
			try{
				l = left().leafCount();
			}
			catch(Exception e){
				System.err.println("Error: no left node");
			}
		}
		if(hasRight()){
			try{
				r = right().leafCount();
			}
			catch(Exception e){
				System.err.println("Error: no right node");
			}
		}
		return l + r;
    }

    /**
     * Your method should be recursive.
     *@return int   count of the number of trees at a given level
     */
    public int levelCount(int level){
    	if(level == 0){
    		return 1;
    	}
    	int l = 0, r = 0;
    	if(hasLeft()){
    		try{
    			l = left().levelCount(level - 1);
    		}
    		catch(Exception e){
    			System.err.println("Error: no left node");
    		}
    	}
    	if(hasRight()){
    		try{
    			r = right().levelCount(level - 1);
    		}
    		catch(Exception e){
    			System.err.println("Error: no right node");
    		}
    	}
    	return l + r;
    }

    /**
     * Each Tree in a binary tree has a balance factor, which is
     * equal to the height of the its left subtree minus the height
     * of its right subtree. A binary tree is balanced if every
     * balance factor is either 0, 1 or -1. Write a method that
     * calculates the balance factor of your tree. 
     *@return int   the balance factor of the tree
     */
    public int balanceFactor(){
		int l = -1, r = -1;
    	if(hasLeft()){
			try{
				l = left().height();
			}
			catch(Exception e){
				System.err.println("Error: no left node");
			}
		}
    	if(hasRight()){
    		try{
    			r = right().height();
    		}
    		catch(Exception e){
    			System.err.println("Error: no right node");
    		}
    	}
    	return l - r;
    }

    /*
     * Rotates the tree to the right
     */
    
    public void rotateRight(){
    	if(!hasLeft()){
    		System.out.println("Error: no left child");
    	}
    	Node temp = node.left;
    	node.left = temp.right;
    	if(temp.right != null){
    		temp.right.parent = node;
    	}
    	temp.parent = node.parent;
	    if(node.parent != null){
    		if(node.parent.left == node){
	    		temp.parent.left = temp;
	    	}
	    	else{
	    		temp.parent.right = temp;
	    	}
	    }
    	node.parent = temp;
    	temp.right = node;
    	node = temp;
    }
    
    /*
     * Rotates the tree to the left
     */
    
    public void rotateLeft(){
    	if(!hasRight()){
    		System.out.println("Error: no right child");
    	}
    	Node temp = node.right;
    	node.right = temp.left;
    	if(temp.left != null){
    		temp.left.parent = node;
    	}
    	temp.parent = node.parent;
	    if(node.parent != null){	
    		if(node.parent.left == node){
	    		temp.parent.left = temp;
	    	}
	    	else{
	    		temp.parent.right = temp;
	    	}
	    }
    	node.parent = temp;
    	temp.left = node;
    	node = temp;
    }
    
    /**
     * balances the tree if it is not already balanced
     * note: this was my attempt at implementation, but it
     * is clear that i did not test soon enough and could not 
     * come up with a way to fix it
     */
    public void balance(){
    	if(isLeaf()){
    		return;
    	}
    	int bf = balanceFactor();
    	if(bf > 1){
    		try{
    			if(hasLeft()){
	    			int bfl = left().balanceFactor();
	    			if(bfl < 0){
	    				left().rotateLeft();
	    			}
    			}
    			rotateRight();
    		}
    		catch(Exception e){
    			System.err.println(1);
    		}
    	}
    	else if(bf < -1){
    		try{
    			if(hasRight()){
	    			int bfr = right().balanceFactor();
	    			if(bfr > 0){
	    				right().rotateRight();
	    			}
    			}
    			rotateLeft();
    		}
    		catch(Exception e){
    			System.err.println(2);
    		}
    	}
    	if(hasLeft()){
    		try{
    			left().balance();
    		}
    		catch(Exception e){
    			System.err.println(3);
    		}
    	}
    	if(hasRight()){
    		try{
    			right().balance();
    		}
    		catch(Exception e){
    			System.err.println(4);
    		}
    	}
    }

    /**
     * The remaining methods assume that the strings stored
     * in the binary tree are actually integers.  Your code
     * will have to convert them to integers using the
     * Integer.parseInt method.  (If you apply one of these
     * methods to a tree containing non-integer data, an
     * exception will be thrown and a message displayed)
     */

    /**
     *@return int   sum of the data values in the tree
     */
    public int treeSum(){
		if(isLeaf()){
			return Integer.parseInt(node.value.toString());
		}
		int l = 0, r = 0;
		if(hasLeft()){
			try{
				l = left().treeSum();
			}
			catch(Exception e){
				System.err.println("Error: no left node");
			}
		}
		if(hasRight()){
			try{
				r = right().treeSum();
			}
			catch(Exception e){
				System.err.println("Error: no right node");
			}
		}
		return l + r + Integer.parseInt(node.value.toString());
    }

    /**
     * Define a "path sum" in a tree to be the sum of the data
     * values in a path from the root to a leaf (including the
     * data values in both the root and the leaf).  This method
     * returns the maximum of all the path sums in the tree.
     *@return int   max sum of the paths
     */
    public int maxPathSum(){
    	if(isLeaf()){
			return Integer.parseInt(node.value.toString());
		}
		int l = 0, r = 0;
		if(hasLeft()){
			try{
				l = left().maxPathSum();
			}
			catch(Exception e){
				System.err.println("Error: no left node");
			}
		}
		if(hasRight()){
			try{
				r = right().maxPathSum();
			}
			catch(Exception e){
				System.err.println("Error: no right node");
			}
		}
		return Math.max(l, r) + Integer.parseInt(node.value.toString());
    }

    /**
     * Doubles the integer value in every Tree of the tree
     */
    public void doubles(){
    	node.value = (E) (Integer.parseInt(node.value.toString()) * 2 + "");
    	if(hasLeft()){
    		try{
    			left().doubles();
    		}
    		catch(Exception e){
    			System.err.println("Error: no left node");
    		}
    	}
    	if(hasRight()){
    		try{
    			right().doubles();
    		}
    		catch(Exception e){
    			System.err.println("Error: no right node");
    		}
    	}
    }
    
    /*
     * Returns an array of Strings containing each key in the tree
     */
    
    public String[] keys(){
    	int size = size();
    	if(size == 0){
    		return null;
    	}
    	String[] keys = new String[size];
    	BinarySearchTree[] nodes = new BinarySearchTree[size];
    	int i = 0, j = 1;
    	nodes[0] = this;
    	keys[0] = this.node.key;
    	while(i < size){
    		if(nodes[i].hasLeft()){
    			try{
    				nodes[j] = nodes[i].left();
    				keys[j] = nodes[i].left().node.key;
    				j++;
    			}
    			catch(Exception e){
    				System.err.print("No left node");
    			}
    		}
    		if(nodes[i].hasRight()){
    			try{
    				nodes[j] = nodes[i].right();
    				keys[j] = nodes[i].right().node.key;
    				j++;
    			}
    			catch(Exception e){
    				System.err.print("No right node");
    			}
    		}
    		i++;
    	}
    	return keys;
    }
    
    /*
     * Prints the tree in level-order
     */
    
    public void printLevelOrder(){
    	ArrayList<Node> printQueue = new ArrayList<Node>();
    	Node n = node;
    	while(n != null){
	    	System.out.println(n.key.toString());
	    	if(n.left != null){
	    		printQueue.add(n.left);
	    	}
	    	
	    	if(n.right != null){
	    		printQueue.add(n.right);
	    	}
	    	if(!printQueue.isEmpty()){
	    		n = printQueue.remove(0);
	    	}
	    	else{
	    		n = null;
	    	}
    	}
    }
    
    /*
     * Testing main
     */
    
    public static void main(String[] args){
    	BinarySearchTree t = new BinarySearchTree();
    	t.insert("f", 1);
    	t.insert("b", 2);
    	t.insert("j", 3);
    	t.insert("h", 4);
    	t.insert("k", 5);
    	t.insert("i", 6);
    	try{
    		t.right().rotateRight();
    	}
    	catch(Exception e){
    		
    	}
    	t.printLevelOrder();
    }

}//class BinarySearchTree
