import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javalib.worldimages.FrameImage;
import javalib.worldimages.OverlayImage;
import javalib.worldimages.OverlayOffsetImage;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.RotateImage;
import javalib.worldimages.WorldImage;

//represents the game piece tiles
// in logical coordinates, with the origin
// at the top-left corner of the screen
class GamePiece {
  int row;
  int col;
  // whether this GamePiece is connected to the
  // adjacent left, right, top, or bottom pieces
  boolean left;
  boolean right;
  boolean top;
  boolean bottom;

  // you can have a few more fields that are prevtop prev right etc for when u
  // rotate
  // whether the power station is on this piece
  boolean powerStation;
  // whether a cell has a connection
  boolean connected;
  ArrayList<GamePiece> neighbors;

  GamePiece() {
    this.neighbors = new ArrayList<GamePiece>();
  }

  public static WorldImage FLIPCONNECT = new RotateImage(LightEmAll.CONNECT, 90.0);
  public static WorldImage FLIPCONNECTLIGHT = new RotateImage(LightEmAll.LIGHTUPCONNECT, 90.0);

  // draws this gamepiece
  WorldImage drawGamePiece() {
    WorldImage mytile = new FrameImage(
        new RectangleImage(LightEmAll.TILESIZE, LightEmAll.TILESIZE, "solid", Color.DARK_GRAY));

    if (this.powerStation) {
      return new OverlayImage(LightEmAll.POWERSTATION,
          this.drawConnector(LightEmAll.LIGHTUPCONNECT, FLIPCONNECTLIGHT, mytile));
    }

    if (this.powerStation && !this.connected) {
      return new OverlayImage(LightEmAll.POWERSTATION,
          this.drawConnector(LightEmAll.CONNECT, GamePiece.FLIPCONNECT, mytile));
    }

    if (this.connected) {
      return this.drawConnector(LightEmAll.LIGHTUPCONNECT, FLIPCONNECTLIGHT, mytile);
    }

    else {
      return this.drawConnector(LightEmAll.CONNECT, FLIPCONNECT, mytile);
    }
  }

  // draws the connectors for this GamePiece
  WorldImage drawConnector(WorldImage regularconnector, WorldImage flipconnector, WorldImage base) {

    if (this.bottom) {
      base = new OverlayOffsetImage(regularconnector, 0, -12.5, new FrameImage(
          new RectangleImage(LightEmAll.TILESIZE, LightEmAll.TILESIZE, "solid", Color.DARK_GRAY)));

    }
    if (this.top) {
      base = new OverlayOffsetImage(regularconnector, 0, 12.5, base);

    }

    if (this.left) {
      base = new OverlayOffsetImage(flipconnector, 12.5, 0, base);
    }

    if (this.right) {
      base = new OverlayOffsetImage(flipconnector, -12.5, 0, base);

    }

    return base;

  }

  // EFFECT: assigns the neighbors of a tile
  public void assignNeighbors(ArrayList<ArrayList<GamePiece>> arr) {
    for (int i = 0; i < arr.size(); i++) {
      for (int n = 0; n < arr.size(); n++) {
        GamePiece workingpiece = arr.get(i).get(n);
        int totaldifference = Math.abs(this.col - workingpiece.col)
            + Math.abs(this.row - workingpiece.row);

        if (totaldifference == 1) {
          this.neighbors.add(workingpiece);
        }

      }

    }
  }

  // EFFECT: lights up the cells that are connected the powerstation
  public void lightUp() {

    if (this.powerStation) {
      this.connected = true;
    }

    if (this.connected & this.top) {

      for (GamePiece g : this.neighbors) {
        if (g.row == this.row - 1 && g.bottom && !g.connected) {
          g.connected = true;
          g.lightUp();
        }
      }
    }

    if (this.connected & this.bottom) {
      for (GamePiece g : this.neighbors) {
        if (g.row == this.row + 1 && g.top && !g.connected) {
          g.connected = true;
          g.lightUp();
        }
      }
    }

    if (this.connected & this.right) {
      for (GamePiece g : this.neighbors) {
        if (g.col == this.col + 1 && g.left && !g.connected) {
          g.connected = true;
          g.lightUp();
        }
      }
    }

    if (this.connected & this.left) {
      for (GamePiece g : this.neighbors) {
        if (g.col == this.col - 1 && g.right && !g.connected) {
          g.connected = true;
          g.lightUp();
        }
      }
    }

  }
 
  //EFFECT: rotates this piece n number of random times
  public void scrambleBoard(Random r) {
   int random = r.nextInt(20);
   for (int i = 0; i < random; i++) {
     this.rotatePiece();
   }
  

   
}

  // EFFECT: rotates a game piece image-placeholder for boolean switching method
  void rotatePiece() {
    boolean prevleft = this.left;
    boolean prevtop = this.top;
    boolean prevright = this.right;
    boolean prevbottom = this.bottom;

    this.left = prevbottom;
    this.top = prevleft;
    this.right = prevtop;
    this.bottom = prevright;

  }
  
  //Creates edges from this gamepiece
  ArrayList<Edge> createEdges (){
    ArrayList<Edge> edgelist = new ArrayList<Edge>();
    for (GamePiece g :this.neighbors ) {
      Edge e = new Edge(this, g);
      edgelist.add(e);
    }
    return edgelist;
    
  }

}
