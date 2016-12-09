/**
 * IStack.java
 *
 * This file should remain unchanged.
 */

public interface IStack {
    public Object pop() throws StackEmptyException;
    public void push(Object o);
    public boolean isEmpty();
    public int size();
    public void clear();
}
