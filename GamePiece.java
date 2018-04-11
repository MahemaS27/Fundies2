import java.awt.Color;

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
  //whether a cell has a connection
  boolean connected;
  
 

  public static WorldImage FLIPCONNECT = new RotateImage(LightEmAll.CONNECT, 90.0);
  public static WorldImage FLIPCONNECTLIGHT = new RotateImage(LightEmAll.LIGHTUPCONNECT, 90.0);

  // draws this gamepiece
  WorldImage drawGamePiece() {
    WorldImage mytile = new FrameImage(
        new RectangleImage(LightEmAll.TILESIZE, LightEmAll.TILESIZE, "solid", Color.DARK_GRAY));

    if (this.powerStation) {
      return new OverlayImage(LightEmAll.POWERSTATION, this.drawConnector(LightEmAll.LIGHTUPCONNECT, FLIPCONNECTLIGHT ,mytile));
    }
    
    if (this.powerStation && !this.connected) {
      return new OverlayImage(LightEmAll.POWERSTATION, this.drawConnector(LightEmAll.CONNECT, GamePiece.FLIPCONNECT ,mytile));
    }
    
    if (this.connected) {
      return this.drawConnector(LightEmAll.LIGHTUPCONNECT, FLIPCONNECTLIGHT ,mytile);
    }
    
    else {
      return this.drawConnector(LightEmAll.CONNECT, FLIPCONNECT ,mytile);
    }
  }

  //draws the connectors for this GamePiece
  WorldImage drawConnector(WorldImage regularconnector, WorldImage flipconnector,WorldImage base) {

    if (this.bottom ) {
      base = new OverlayOffsetImage(regularconnector, 0, -12.5, new FrameImage(
          new RectangleImage(LightEmAll.TILESIZE, LightEmAll.TILESIZE, "solid", Color.DARK_GRAY)));
     
    }  
    if (this.top ) {
      base = new OverlayOffsetImage(regularconnector, 0, 12.5, base);
     
    }
    
    if (this.left ) {
      base = new OverlayOffsetImage(flipconnector, 12.5, 0, base);
    }
    
    if (this.right ) {
      base = new OverlayOffsetImage(flipconnector, -12.5, 0, base);
      
    }
   
    return base; 

  }
  
//determines if two tiles are connected
boolean isConnected (GamePiece other) {
  if (this.top && other.bottom) {
    return true;
  }
  if (this.right && other.left ) {
    return true;
  }
  if (this.bottom && other.top ) {
    return true;
  }
  else {
   return this.left && this.right;
  }
}


 

  //EFFECT: rotates a game piece image-placeholder for boolean switching method
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
  
 

 

}
