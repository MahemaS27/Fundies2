import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//NOTE THAT SCRAMBLE IS COMMENTED OUT FOR NOW TO RUN TESTS

class LightEmAll extends World {
  // a list of columns of GamePieces,
  // i.e., represents the board in column-major order
  ArrayList<ArrayList<GamePiece>> board;
  // a list of all nodes
  ArrayList<GamePiece> nodes;
  // a list of edges of the minimum spanning tree
  ArrayList<Edge> mst;
  // the width and height of the board
  int width;
  int height;
  // the current location of the power station,
  // as well as its effective radius
  int powerRow;
  int powerCol;
  int radius;
  Random random;
  int counter;
  Posn powersource;

  LightEmAll(int width, int height) {
    this.width = width;
    this.height = height;
    this.radius = (int) (Math.sqrt((this.width * this.width))) / 2 + 1;
    this.random = new Random();
    this.board = this.buildgrid();
    this.nodes = new ArrayList<GamePiece>();
    this.mst = this.initialListofEdges();
    this.powerRow = 0;
    this.powerCol = 0;
    this.counter = 0;
    this.powersource = new Posn(this.powerCol, this.powerRow);

  }

  // Constant variables for drawing
  public static int TILESIZE = 50;
  public static int WIDTH = 1000;
  public static int HEIGHT = 1000;
  public static WorldImage CONNECT = new RectangleImage(2, 25, "solid", Color.GRAY);
  public static WorldImage LIGHTUPCONNECT = new RectangleImage(2, 25, "solid", Color.YELLOW);
  public static WorldImage POWERSTATION = new StarImage(20, 5, OutlineMode.SOLID, Color.CYAN);

  // draws the 2 dimensional grid for the game
  public WorldScene makeScene() {
    WorldScene s = new WorldScene(WIDTH, HEIGHT);
    WorldImage score = new TextImage("Time:" + Integer.toString(this.counter), 25, Color.CYAN);
    WorldImage title = new TextImage("Light Em All!", 50, Color.CYAN);
    WorldImage icon = new OverlayImage(new StarImage(10, 5, OutlineMode.SOLID, Color.cyan),
        new RectangleImage(30, 30, "solid", Color.DARK_GRAY));
    WorldImage fulllogo = new BesideImage(title, icon);
    WorldImage ending = new TextImage("You Win!", 30, Color.GREEN);

    this.board.get(powerCol).get(powerRow).powerStation = true;
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece currentpiece = this.board.get(i).get(n);
        if (this.withinRadius(currentpiece)) {
          currentpiece.lightUp();
        }
        s.placeImageXY(currentpiece.drawGamePiece(), i * TILESIZE + 60, n * TILESIZE + 60);

      }
      s.placeImageXY(score, 60, 500);
      s.placeImageXY(fulllogo, 200, 550);

    }

    if (this.allLitUp()) {
      s.placeImageXY(ending, 250, 250);
    }

    return s;

  }

  // heapsort - sorts the edges in the mst
  public ArrayList<Edge> heapsort() {
    for (int i = 1; i < this.mst.size(); i++) {
      this.upheap(i);
    }
    this.buildnewmst(this.mst);
    return mst;
  }

  // upheap
  public void upheap(int thisIdx) {
    ArrayUtils<Edge> au = new ArrayUtils<Edge>();
    int parentIdx = /* Math.floorDiv */(thisIdx - 1) / 2;
    if (this.mst.get(thisIdx).weight < this.mst.get(parentIdx).weight) {
      // Swap items at indices idx and parentIdx
      au.swap(this.mst, thisIdx, parentIdx);
      this.upheap(parentIdx);
    }
  }

  // builds the sorted list of edges
  public void buildnewmst(ArrayList<Edge> basemst) {
    ArrayUtils<Edge> au = new ArrayUtils<Edge>();
    ArrayList<Edge> sortededges = new ArrayList<Edge>();
    au.swap(this.mst, 0, this.mst.size() - 1);

    while (this.mst.size() > 0) {

      sortededges.add(this.mst.remove(this.mst.size() - 1));
      this.downheap(0);
    }
  }

  // downheap - called after the items are removed
  public void downheap(int index) {
    ArrayUtils<Edge> au = new ArrayUtils<Edge>();
    int leftIndx = 2 * index + 1;
    int rightIdx = 2 * index + 2;

    if (rightIdx < this.mst.size() || leftIndx < this.mst.size()) {

      if (this.mst.get(index).weight < this.mst.get(leftIndx).weight
          || this.mst.get(index).weight < this.mst.get(rightIdx).weight) {
        int biggestIdx = Math.max(leftIndx, rightIdx);
        au.swap(this.mst, index, biggestIdx);
        this.downheap(biggestIdx);
      }
    }

  }

  // creates the indices for all of the game pieces
  public ArrayList<ArrayList<GamePiece>> buildgrid() {
    ArrayList<ArrayList<GamePiece>> a1 = new ArrayList<ArrayList<GamePiece>>();
    this.assignPieces(a1);
    this.addNodes(a1);
    System.out.println(this.nodes.size());
    System.out.println(this.nodes);
    this.assignConnectors(a1, 0, 0, width, height);
    this.assignKruzkalConnectors(a1);
    return a1;

  }

  // EFFECT: assigns game pieces to the board and assigns row & col for each game
  // piece
  public void assignPieces(ArrayList<ArrayList<GamePiece>> arr) {

    for (int i = 0; i < this.width; i++) {
      ArrayList<GamePiece> column = new ArrayList<GamePiece>();
      for (int n = 0; n < this.height; n++) {
        GamePiece g1 = new GamePiece();
        g1.col = i;
        g1.row = n;
        column.add(g1);

      }
      arr.add(column);
    }

    /// assigns the neighbors for all the indiv pieces
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece workingpiece = arr.get(i).get(n);

        workingpiece.assignNeighbors(arr);
      }
    }

    // MESS EVERYTHING UP

    // for (int i = 0; i < this.width; i++) {
    // for (int n = 0; n < this.height; n++) {
    // GamePiece workingpiece = arr.get(i).get(n);
    // workingpiece.scrambleBoard(this.random);
    //
    // }
    //
    // }

  }

  // EFFECt: For Kruzkal board generation assign all the connecters of the cells
  public void assignKruzkalConnectors(ArrayList<ArrayList<GamePiece>> arr) {
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece workingpiece = arr.get(i).get(n);

        workingpiece.scrambleBoard(this.random);
      }
    }

  }

  // overriding equals

  // EFFECT: finds the minimum spaning tree
  public void findMST() {
    HashMap<GamePiece, GamePiece> representatives = new HashMap<GamePiece, GamePiece>();
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
    // couldnt get heapsort to work but that implementaion is in line 86-kept
    // getting index errors
    ArrayList<Edge> worklist = this.mst;

    for (GamePiece g : this.nodes) {

      representatives.put(g, g);

    }

    while (worklist.size() > 1) {
      Edge current = worklist.get(0);
      if (representatives.get(current.fromNode).equals(representatives.get(current.toNode))){
        worklist.remove(0);
      }
      else {
        edgesInTree.add(worklist.remove(0));

      }
    }

  }

  // find the parent of this node in kruzkal's
  public GamePiece find(HashMap<GamePiece, GamePiece> representative, GamePiece piece) {
    if (representative.get(piece) == piece) {
      return piece;
    }
    else {
      return this.find(representative, representative.get(piece));
    }
  }

  // EFFECT: updates the counter for time score
  public void onTick() {
    this.counter = this.counter + 1;
  }

  // EFFECT: updates the list of nodes to include all the pieces in the game board
  public void addNodes(ArrayList<ArrayList<GamePiece>> arr) {
    ArrayList<GamePiece> workingarr = new ArrayList<GamePiece>();
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece currentpiece = arr.get(i).get(n);
        workingarr.add(currentpiece);
      }
    }
    this.nodes = workingarr;
  }

  // EFFECT: For fractal generation, assigns all the connectors by changing the
  // boolean fields of the default cells
  public void assignConnectors(ArrayList<ArrayList<GamePiece>> arr, int left, int top, int width,
      int height) {

    // for odd number width and height boards
    if (width <= 1 || height <= 1) {
      arr.get(left).get(height).bottom = true;
      arr.get(left).get(height).right = true;
      return;
    }

    if (width <= 1 && height <= 2) {
      arr.get(left).get(height).top = true;
      arr.get(left).get(height).right = true;
      arr.get(left).get(height - 1).bottom = true;
    }

    if (width <= 2 && height <= 1) {
      arr.get(left + 1).get(height).top = true;
      arr.get(left + 1).get(height).left = true;

      arr.get(left).get(height).right = true;
      arr.get(left).get(height).top = true;
    }

    // divide the grid up into 4 different quadrants
    this.assignConnectors(arr, left, top, width / 2, height / 2);
    this.assignConnectors(arr, left, top + (height / 2), width / 2, (int) Math.ceil(height / 2.0));
    this.assignConnectors(arr, width / 2 + left, top, (int) Math.ceil(width / 2.0), height / 2);
    this.assignConnectors(arr, width / 2 + left, top + (height / 2), (int) Math.ceil(width / 2.0),
        (int) Math.ceil(height / 2.0));

    arr.get(left).get(height / 2 + top).top = true;
    arr.get(left).get(height / 2 - 1 + top).bottom = true;

    arr.get(width / 2 + left).get(height - 1 + top).left = true;
    arr.get(width / 2 - 1 + left).get(height - 1 + top).right = true;

    arr.get(width - 1 + left).get(height / 2 + top).top = true;
    arr.get(width - 1 + left).get(height / 2 - 1 + top).bottom = true;

  }

  // determines if the currentpiece is within the powerstation radius
  boolean withinRadius(GamePiece current) {

    int distfrompower = (int) Math
        .sqrt(Math.abs((current.col - this.powerCol) * (current.col - this.powerCol))
            + Math.abs((current.row - this.powerRow) * (current.row - this.powerRow)));

    return distfrompower < this.radius;

  }

  // breadth first search
  public ArrayList<GamePiece> breadthFirstSearch() {
    ArrayList<GamePiece> edgesInTree = new ArrayList<GamePiece>();
    ArrayList<GamePiece> worklist = this.nodes;
    GamePiece g = worklist.get(0);

    while (this.nodes.size() > 0) {
      if (g.neighbors.contains(g)) {
        this.nodes.remove(0);

      }
      else {
        edgesInTree.add(g);

      }

    }
    // Explanation for BFS-what we wanted it to do
    // Pick the next cheapest edge of the graph: suppose it connects X and Y.
    // If this node has itself in its connection -aka cycle
    // discard this node // they're already connected
    // Else:
    // Record this node in edgesInTree
    // and recur on the neighbors
    // Return the edgesInTree

    return edgesInTree;

  }

  // EFFECT: updates this games mst to have a sorted by weight list of edges that
  // connects all game
  // pieces
  public ArrayList<Edge> initialListofEdges() {
    ArrayList<Edge> newmst = new ArrayList<Edge>();
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece currentpiece = this.board.get(i).get(n);
        newmst.addAll(0, currentpiece.createEdges());
      }

    }
    this.assignWeights(newmst);
    newmst.sort(new compares());
    return newmst;

  }

  // EFFECT: updates a list of edges with weights for the minimum spanning tree
  public void assignWeights(ArrayList<Edge> unweightededges) {
    for (Edge e : unweightededges) {
      int r = this.random.nextInt();
      e.weight = r;
    }

  }

  // EFFECT: rotates the piece that the user clicks on
  public void onMouseClicked(Posn pos, String buttonName) {
    int xindex = (pos.x - 40) / TILESIZE;
    int yindex = (pos.y - 40) / TILESIZE;

    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece currentpiece = this.board.get(i).get(n);

        if (this.withinRadius(currentpiece)) {
          currentpiece.lightUp();
        }
      }
    }

    this.board.get(xindex).get(yindex).rotatePiece();

    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {

        // so that when player unclicks a piece, it doesnt stay a power up
        this.board.get(i).get(n).connected = false;
      }
    }

  }

  // checks to make sure that the powerstation is in bounds
  int changePowerCol(int change) {
    if (this.powerCol + change >= 0 && this.powerCol + change < this.width) {
      return this.powerCol + change;
    }
    else {
      return this.powerCol;
    }
  }

  int changePowerRow(int change) {
    if (this.powerRow + change >= 0 && this.powerRow + change < this.height) {
      return this.powerRow + change;
    }
    else {
      return this.powerRow;
    }

  }

  // EFFECT: Changes the world states based on the clicking
  public void onKeyEvent(String key) {

    GamePiece powerpiece = this.board.get(powerCol).get(powerRow);
    GamePiece piecetotheleft = this.board.get(this.changePowerCol(-1)).get(powerRow);
    GamePiece piecetotheright = this.board.get(this.changePowerCol(1)).get(powerRow);
    GamePiece piecetothetop = this.board.get(powerCol).get(this.changePowerRow(-1));
    GamePiece piecetothebottom = this.board.get(powerCol).get(this.changePowerRow(1));

    // ensures the the powerstation can only travel on a wire path
    if (key.equals("left") && piecetotheleft.right && powerpiece.left) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.board.get(powerCol).get(powerRow).connected = false;
      this.powerCol = this.changePowerCol(-1);
    }
    if (key.equals("right") && piecetotheright.left && powerpiece.right) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.board.get(powerCol).get(powerRow).connected = false;

      this.powerCol = this.changePowerCol(1);
    }
    if (key.equals("up") && piecetothetop.bottom && powerpiece.top) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.board.get(powerCol).get(powerRow).connected = false;

      this.powerRow = this.changePowerRow(-1);
    }
    if (key.equals("down") && piecetothebottom.top && powerpiece.bottom) {
      this.board.get(powerCol).get(powerRow).powerStation = false;
      this.board.get(powerCol).get(powerRow).connected = false;

      this.powerRow = this.changePowerRow(1);
    }

    if (key.contentEquals("r") && this.allLitUp()) {
      this.counter = 0;
      // this.scrambleBoard(this.board); and somehow reset
    }

  }

  // determines if all the wires in this game are connected
  public boolean allLitUp() {
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        if (!this.board.get(i).get(n).connected) {
          return false;
        }

      }
    }
    return true;

  }
}

// class for array utilites
class ArrayUtils<T> {
  // In ArrayUtils
  void swap(ArrayList<T> arr, int index1, int index2) {
    T oldValueAtIndex1 = arr.get(index1);
    T oldValueAtIndex2 = arr.get(index2);

    arr.set(index2, oldValueAtIndex1);
    arr.set(index1, oldValueAtIndex2);
  }
}
