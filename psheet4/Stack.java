/**
 * Stack.java
 *
 * Write an implementation of the IStack Interface, called Stack.
 * In the event that a user tried to pop() from an empty list, your class
 * should generate an appropriate exception.
 * Your stack should be backed by an array of Objects that should be resized in the event
 * that it becomes full.
 *
 * You MUST use an array of Objects. Use of an ArrayList, or any other complex data structure
 * will result in 0 marks.
 */
class Stack implements IStack{
private Object[] Array = new Object[1];
private int c = 0;
public void clear(){
Array = new Object[1];
c = 0;
}
public int size(){
return c;
}
public boolean isEmpty(){
if(c==0){
return true;
}
return false;
}
public Object pop(){
if(c==0){
throw new StackEmptyException();
}
int tmp = c;
c--;
	return Array[tmp];
}
public void push(Object o){
expand();
this.Array[c]=o;
c++;

}
private void expand(){
	Object[] tmp = new Object[Array.length + 1];
	System.arraycopy(Array, 0, tmp, 0, Array.length);
	this.Array = tmp;
}


}
