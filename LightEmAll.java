import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

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
    this.random = new Random();
    this.board = this.buildgrid();
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
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece currentpiece = this.board.get(i).get(n);
        this.lightUp(powersource, currentpiece);
        s.placeImageXY(currentpiece.drawGamePiece(), i * TILESIZE + 60, n * TILESIZE + 60);
      }
      s.placeImageXY(score, 60, 400);
      s.placeImageXY(fulllogo, 200, 450);
    }
    // starting place for the powerstation when the game is intially started
    this.board.get(0).get(1).powerStation = true;
    return s;
  }

  // creates the indices for all of the game pieces
  public ArrayList<ArrayList<GamePiece>> buildgrid() {
    ArrayList<ArrayList<GamePiece>> a1 = new ArrayList<ArrayList<GamePiece>>();
    this.assignPieces(a1);
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
    // assign all the connectors for game play
    this.assignConnectors(arr);
    // MESS EVERYTHING UP
    this.scrambleBoard(arr);

  }

  public void scrambleBoard(ArrayList<ArrayList<GamePiece>> arr) {

    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        // THIS POINT IS WHERE WE HAVE AN INDIV. TILE

        if (this.random == null) {
          throw new RuntimeException("null");
        }

        int r = this.random.nextInt();
        for (int t = 0; t < r; t++) {
          arr.get(i).get(n).rotatePiece();
        }
      }
    }

  }

  // EFFECT: updates the counter for time score
  public void onTick() {
    this.counter = this.counter + 1;
  }

  // EFFECT: For manual generation, assigns all the connectors by changing the
  // boolean fields of the default cells
  public void assignConnectors(ArrayList<ArrayList<GamePiece>> arr) {
    // Manual Generation
    // for all tiles with row 0 make bottom be true
    for (int i = 0; i < this.width; i++) {
      arr.get(i).get(0).bottom = true;
    }
    // for all tiles with row [height] make top be true
    for (int i = 0; i < this.width; i++) {
      arr.get(i).get(this.height - 1).top = true;
    }
    // for the tile with row/2 and col = 0 make top, right, and bottom true
    int h = this.height / 2;
    GamePiece leftcorner = arr.get(0).get(h);
    leftcorner.top = true;
    leftcorner.right = true;
    leftcorner.bottom = true;

    // for the tile with row/2 and col = width make top left and bottom true
    GamePiece rightcorner = arr.get(this.width - 1).get(h);
    rightcorner.top = true;
    rightcorner.left = true;
    rightcorner.bottom = true;

    // for the other tiles with row/2 make all 4 fields true
    for (int i = 1; i < this.width - 1; i++) {
      GamePiece middle = arr.get(i).get(h);
      middle.top = true;
      middle.right = true;
      middle.bottom = true;
      middle.left = true;

    }
    // all other tiles have top and bottom be true
    for (int i = 0; i < this.width; i++) {
      for (int n = 1; n < h; n++) {
        GamePiece beforebar = arr.get(i).get(n);
        beforebar.top = true;
        beforebar.bottom = true;
      }

    }

    for (int i = 0; i < this.width; i++) {
      for (int n = h + 1; n < this.height - 1; n++) {
        GamePiece afterbar = arr.get(i).get(n);
        afterbar.top = true;
        afterbar.bottom = true;
      }
    }
  }

  // EFFECT: rotates the piece that the user clicks on
  public void onMouseClicked(Posn pos, String buttonName) {
    int xindex = (pos.x - 40) / TILESIZE;
    int yindex = (pos.y - 40) / TILESIZE;

    this.board.get(xindex).get(yindex).rotatePiece();

    if (this.allLitUp()) {
      this.endOfWorld("You Win!");
    }

  }
  
  // EFFECT: resets the game if the player wants to start over
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.board = this.buildgrid();
    }
  }

  // EFFECT: lights up the cells that are connected the powerstation
  public void lightUp(Posn start, GamePiece workingpiece) {
    if (!workingpiece.isConnected(this.board.get(start.x).get(start.y))) {
      workingpiece.connected = false;
    }

    if (workingpiece.isConnected(this.board.get(start.x).get(start.y))) {
      workingpiece.connected = true;
      // this.lightUp(new Posn(workingpiece.col, workingpiece.row), workingpiece);

    }
  }

  // determines if all the wires in this game are connected
  public boolean allLitUp() {
    boolean tracker = false;
    for (int i = 0; i < this.width; i++) {
      for (int n = 0; n < this.height; n++) {
        GamePiece currentpiece = this.board.get(i).get(n);
        if (!currentpiece.connected) {
          return false;
        }
        else {
          tracker = true;
        }
        
      }

    }
    return tracker;

  }

}
