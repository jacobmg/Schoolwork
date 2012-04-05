                                                                     
                                                                     
                                                                     
                                             
/**
 * TreeInterface.java
 */

public interface TreeInterface<E>{

    public abstract BinarySearchTree left() throws Exception;

    public abstract BinarySearchTree right() throws Exception;

    public abstract BinarySearchTree parent() throws Exception;

    public abstract boolean hasLeft();

    public abstract boolean hasRight();

    public abstract boolean isEmpty();

    public abstract boolean isLeaf();

    public abstract boolean isInternal();

    public abstract boolean isRoot();

    public abstract BinarySearchTree addRoot(String key, E e);

    public abstract BinarySearchTree insertLeft(String key, E e) throws Exception;

    public abstract BinarySearchTree insertRight(String key, E e) throws Exception;

    public abstract E remove() throws Exception;

    public abstract int height();

    public abstract BinarySearchTree mirrorTree();

    public abstract int leafCount();

    public abstract int levelCount(int level);

    public abstract int balanceFactor();

    public abstract void balance();

}