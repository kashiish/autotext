package main.java.kashiish.autotext;

import java.util.HashMap;

/**
 * Node interface for tree nodes. 
 * @author kashish
 *
 * @param <E> Node data value type
 * @param <T> Node's children HashMap value type
 * @param <Z> Node's children HashMap key type
 */
public interface Node<E, T, Z> {
	
	E getValue();
		
	HashMap<Z, T> getChildren();
	
	Node<E, T, Z> getChild(Z key);
	
	void addChild(T value);
	
}
