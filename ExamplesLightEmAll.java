import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.worldimages.FrameImage;
import javalib.worldimages.OverlayOffsetImage;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

class ExamplesLightEmAll {

  LightEmAll game1;
  LightEmAll game2;
  GamePiece gp1;
  Random myrand;

  WorldImage mytile = new FrameImage(
      new RectangleImage(LightEmAll.TILESIZE, LightEmAll.TILESIZE, "solid", Color.DARK_GRAY));

  void initData() {
    this.game1 = new LightEmAll(7, 7);
    this.game2 = new LightEmAll (7, 7);
    
    
    this.myrand = new Random();
    this.gp1 = new GamePiece();
    
    
    game2.random = myrand;
    gp1.left = true;
    
    

  }

  // testing assigning of the game pieces row and col
  void testAssignpieces(Tester t) {
    initData();
    t.checkExpect(game1.board.get(0).get(0).row, 0);
    t.checkExpect(game1.board.get(1).get(3).row, 3);
    t.checkExpect(game1.board.get(5).get(3).col, 5);
  }

 void testDrawConnector (Tester t) {
   initData();
   t.checkExpect(gp1.drawGamePiece(), new OverlayOffsetImage(GamePiece.FLIPCONNECT, 12.5, 0, mytile ));
 
 }
 
 void testassignConnectors (Tester t) {
   initData();
   t.checkExpect(game2.board.get(0).get(0).bottom, true);
   t.checkExpect(game2.board.get(1).get(3).left, true);
   t.checkExpect(game2.board.get(1).get(2).right, false);
 }
 
 void testallLitUp (Tester t) {
   initData();
   t.checkExpect(game1.allLitUp(), false);
 }
 
 
 //main
  void testMain(Tester t) {
    initData();
    game2.bigBang(700, 700, 1.0);

  }

}
