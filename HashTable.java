
/**
 * Jacob Gold
 *
 * jacobg@brandeis.edu
 */

/*
 * This class is a hash table: it distributes key-value pairs evenly through an
 * array based on a function involving the key, which makes it likely that
 * searching can occur in constant time, because the same funtion can be used to
 * find where a given key would end up.
 * 
 * When multiple keys end up being put into the same slot in the array, they are
 * stored in a binary search tree, so the search time becomes log(n) assuming
 * multiple key-value pairs are in a single index
 */

public class HashTable<E extends Comparable>{
	
	private BinarySearchTree[] table;
	
	/*
	 * Constructs a hash table of a given size
	 */
	
	public HashTable(int size){
		table = new BinarySearchTree[size];
		for(int i = 0; i < table.length; i++){
			table[i] = new BinarySearchTree();
		}
	}
	
	/*
	 * While it is possible that two Strings could have the same value before the
	 * mod operation, the fact that changing a letter by one step (e.g. b -> c)
	 * changes the value by a different number for each step means that the hash
	 * function will work with a table of any size
	 * 
	 * NOTE: after testing what seemed logically to be a successful hash function,
	 * I came to the conclusion that it does generate many collisions when similar
	 * Strings are used.
	 */
	
	public int hash(String s){
		int x = 0;
		for(int i = 0; i < s.length(); i++){
			x += Math.pow(s.charAt(i), i + 1);
		}
		return x % table.length;
	}
	
	/*
	 * Assuming the strings are only made up of uppercase and lowercase letters
	 * (and the characters with unicode values in between 'Z' and 'a') this
	 * hash function will never result in the same number for any two strings
	 * before the mod operation
	 * 
	 * This function should not be used with tables of size 59 or multiples thereof
	 * Ideally, the hash table should have a size which is a prime number
	 * 
	 * Theoretically, this function could work for any characters, but the number of
	 * possible characters is 65,536, and raising that to any power greater than 2 
	 * would quickly create a number too large to deal with
	 */
	
	public int hash2(String s){
		int x = 0;
		for(int i = 0; i < s.length(); i++){
			x += (s.charAt(i) - 64) * Math.pow(59, i);
		}
		return x % table.length;
	}
	
	/*
	 * Inserts a given key-value pair into the correct slot in the table
	 */
	
	public void put(String key, E value){
		int hash = hash2(key);
		table[hash].insert(key, value);
		table[hash].balance();
	}
	
	/*
	 * Returns the value associated with a given key if that key exists;
	 * returns null if it does not
	 */
	
	public E get(String key){
		return (E) table[hash2(key)].search(key);
	}
	
	/*
	 * Returns true if a given key is in the hash table, false otherwise
	 */
	
	public boolean hasKey(String key){
		Comparable temp = table[hash2(key)].search(key);
		if(temp == null){
			return false;
		}
		return true;
	}
	
	/*
	 * Removes the key-value pair associated with a given key from the hash table
	 */
	
	public void remove(String key){
		int hash = hash2(key);
		table[hash].remove(key);
		table[hash].balance();
	}
	
	
	/*
	 * Returns the number of key-value pairs stored in the table
	 */
	
	public int size(){
		int size = 0;
		for(BinarySearchTree t: table){
			size += t.size();
		}
		return size;
	}
	
	/*
	 * Returns an array of ints corresponding to the number of
	 * key-value pairs in each bucket
	 */
	
	public int[] distribution(){
		int[] dist = new int[table.length];
		for(int i = 0; i < table.length; i++){
			dist[i] = table[i].size();
		}
		return dist;
	}
	
	/*
	 * Returns an array of Strings containing all of the keys present
	 * in the table
	 */
	
	public String[] keys(){
		String[] keys = new String[size()];
		int i = 0;
		for(int j = 0; j < table.length; j++){
			String[] sub = table[j].keys();
			for(int k = 0; k < table[j].size(); k++){
				keys[i] = sub[k];
				i++;
			}
		}
		return keys;
	}
	
	/*
	 * Testing main
	 */
	
	public static void main(String[] args){
		HashTable h = new HashTable(19);
		String[] strings = {"tall", "talk", "tarp", "tern", "team", "term", "tram", "trap", "trams", "tapes", "trips", "teams", "tames", "tomes"};
		for(int i = 0; i < strings.length; i++){
			h.put(strings[i], i);
		}
		strings = h.keys();
		for(String s: strings){
			System.out.println(s);
		}
		int[] dist = h.distribution();
		for(int i: dist){
			System.out.println(i);
		}
	}
	

}
