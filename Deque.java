//questions for ta:
//TODO: write more tests for remove, add, by making more examples
//is our data correctly structured
//abstracting the methods


import tester.Tester;

//class Deque that contains a single header
class Deque<T> {

  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();

    /*- TEMPLATE FOR DEQUE CLASS
     * Fields:
     * ....this.header -- Sentinel <T> ...
     * Methods:
     * ...
     * 
     * Methods on Fields:
     */
  }

  // second constructor takes in a particular Sentinel<T> to use
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // counts the number of nodes in a deque (aka list) not including header
  int size() {
    return this.header.countAllNodes(this.header);
  }

  // EFFECT: adds value T to the head of this list
  void addAtHead(T value) {
    this.header.addHead(value);
  }

  // EFFECT: add value T to the end of this list
  void addAtTail(T value) {
    this.header.addTail(value);
  }

  // removes the first node from this Deque and returns node removed
  ANode<T> removeFromHead() {
    return this.header.removeHeadNode();

  }

  // removes the last node from this Deque and returns the node removed
  ANode<T> removeFromTail() {
    return this.header.removeTailNode();
  }

  // find the first in this Deque for which the given predicate returns true
  ANode<T> find(IPred<T> pred) {
    return this.header.findHelper(pred);
  }
  
  //EFFECT: removes the given node from this Deque
  void removeNode (ANode<T> node) {
    //this.header =  this.header.removeHelp (node);
  }

}

// abstract class for nodes

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // counts the number of nodes in ANode
  public int countAllNodes(ANode<T> n) {
    if (this.next.equals(n)) {
      return 1;
    }
    else {
      return this.next.countAllNodes(n) + 1;
    }
  }

  // removes self (skips over) and returns self for the method removeHead
  ANode<T> removeNodeHelp() {
    prev.next = this.next;
    next.prev = this.prev;

    return this;

  }
//helper for the method find
  public abstract ANode<T> findHelper(IPred<T> pred);
  
  

}

// class for a Sentinel node where next and prev is this
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.next = this;
    this.prev = this;

  }

  // removes last node froma sentinel that doesnt point to self
  public ANode<T> removeTailNode() {
    if (this.pointsToSelf()) {
      throw new RuntimeException("cannot remove from empty list");
    }
    else {
      return this.prev.removeNodeHelp();
    }
  }

  // counts the number of nodes this sentinel points to not including itself
  public int countAllNodes(ANode<T> n) {
    if (this.next.equals(this)) {
      return 0;
    }
    else {
      return this.next.countAllNodes(this);
    }
  }

  // EFFECT: changes the next reference of a Sentinel
  void addHead(T value) {
    new Node<T>(value, this.next, this);
  }

  // EFFECT :changes the prev reference of a Sentinel
  void addTail(T value) {
    new Node<T>(value, this, this.prev);
  }

  // determines whether sentinel points to itself
  boolean pointsToSelf() {
    return this.next == this && this.prev == this;
  }

  // removes the first Node from a sentinel that doesnt point to itself
  ANode<T> removeHeadNode() {
    if (this.pointsToSelf()) {
      throw new RuntimeException("cannot remove from empty list");
    }
    else {
      return this.next.removeNodeHelp();
    }
  }

  // helper for find method
  public ANode<T> findHelper(IPred<T> pred) {
    if (this.pointsToSelf()) {
      return this;
    }
    else {
      return this.next.findHelper(pred);
    }
  }
  
  //helper for remove method
  public ANode<T> removeHelp (ANode<T> node) {
    if (this.pointsToSelf()) {
      return this;
    }
    else if (node.equals(this)) { ////checking for equality
       return this;                            
    }
    else {
     // return this.next.removeHelp(node); 
      return this;
    }
  }

}
// class to represent a node

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // second constructor updates the given nodes that refer back to THIS node
  // throws an exception if EITHER of the given nodes is null
  Node(T data, ANode<T> next, ANode<T> prev) {
    this.data = data;
    this.next = next;
    this.prev = prev;

    if (next == null || prev == null) {
      throw new IllegalArgumentException("Null nodes not allowed");
    }
    else {
      // now update the next node since there are no nulls with the new constructor
      prev.next = this;
      next.prev = this;

    }
  }

  // helper for find method
  public ANode<T> findHelper(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.findHelper(pred);
    }
  }

}

// Represents a boolean-valued question over values of type T
interface IPred<T> {

  // does the thing! more specifically whatever apply function for the class
  boolean apply(T value);
}

// predicate class asks if a string value is 3 char or under
class ThreeOrUnder implements IPred<String> {

  // apply method for threeOrUnder
  public boolean apply(String value) {
    return value.length() <= 3;
  }

}

  // predicate class askes if a string value is 4 char or higher
  class FourOrHigher implements IPred<String>{

  // apply method for 4OrHigher
  public boolean apply(String value) {
    return value.length() >= 4;
  }
}

// predicate class asks if a number is divisible by 5
class DivideBy5 implements IPred<Integer> {

  // apply method for divideBy5
  public boolean apply(Integer value) {
    return value % 5 == 0;
  }

}

// examples class
class ExamplesDeque {

  Sentinel<String> s1;
  Sentinel<String> s2;
  Sentinel<Integer> s3;
  Sentinel<Integer> s4;

  Sentinel<String> ATHtest;

  Node<String> n4;
  Node<String> n3;
  Node<String> n2;
  Node<String> n1;

  Node<Integer> n5;
  Node<Integer> n6;
  Node<Integer> n7;
  Node<Integer> n8;

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<Integer> deque3;
  Deque<String> deque4;
  Deque<Integer> deque5;

  IPred<String> pred1 = new ThreeOrUnder();
  IPred<Integer> pred2 = new DivideBy5();
  IPred <String> pred3 = new FourOrHigher();

  // initializes the data
  void initData() {
    this.s1 = new Sentinel<String>();
    this.s2 = new Sentinel<String>();
    this.s3 = new Sentinel<Integer>();

    this.ATHtest = new Sentinel<String>();

    this.n4 = new Node<String>("def");
    this.n3 = new Node<String>("cde");
    this.n2 = new Node<String>("bcd");
    this.n1 = new Node<String>("abc");

    this.n5 = new Node<Integer>(5);
    this.n6 = new Node<Integer>(7);
    this.n7 = new Node<Integer>(14);
    this.n8 = new Node<Integer>(20);

    this.deque1 = new Deque<String>();
    this.deque2 = new Deque<String>(s2);
    this.deque3 = new Deque<Integer>(s3);
    this.deque4 = new Deque<String>(ATHtest);
    this.deque5 = new Deque<Integer>(s4);

    this.s2.next = n1;
    this.s2.prev = n4;
    this.n1.next = n2;
    this.n1.prev = s2;
    this.n2.next = n3;
    this.n2.prev = n1;
    this.n3.next = n4;
    this.n3.prev = n2;
    this.n4.next = s2;
    this.n4.prev = n3;

    this.s3.next = n5;
    this.s3.prev = n8;
    this.n5.next = n6;
    this.n5.prev = s3;
    this.n6.next = n7;
    this.n6.prev = n5;
    this.n7.next = n8;
    this.n7.prev = n6;
    this.n8.next = s3;
    this.n8.prev = n7;

    this.deque4.addAtHead("hello");
    this.deque4.addAtTail("bye");
    
    this.deque3.removeFromTail();
    
    
    

  }

  // tests the data set
  void testData(Tester t) {
    initData();
    t.checkExpect(s2.prev.next, s2);
    t.checkExpect(n4.prev, n3);
    t.checkExpect(s2.prev, n4);
  }

  // tests the method size
  void testsize(Tester t) {
    initData();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque4.size(), 2);

  }
  
  //tests removeFromHead
  void testremoveFromHead(Tester t) {
    initData();
    
    t.checkExpect(deque2.removeFromHead(), n1);
  
  }
  
  //tests removeFromHead
  void testremoveFromTail (Tester t) {
    initData();
    
    t.checkExpect(deque3.header.prev, n7);
  }
  

  // test the find method
  void testfind(Tester t) {
    initData();
    t.checkExpect(deque1.find(pred1), s1);
    t.checkExpect(deque3.find(pred2), n5);
    t.checkExpect(deque2.find(pred1), n1);
    t.checkExpect(deque1.find(pred3), s1);

  }

}