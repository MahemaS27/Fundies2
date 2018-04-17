import java.awt.Color;

import java.util.Random;

import javalib.worldimages.FrameImage;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

class ExamplesLightEmAll {

  LightEmAll game1;
  LightEmAll game2;
  LightEmAll game3;
  LightEmAll game4;
  GamePiece gp1;
  GamePiece gp2;
  Random myrand;

  WorldImage mytile = new FrameImage(
      new RectangleImage(LightEmAll.TILESIZE, LightEmAll.TILESIZE, "solid", Color.DARK_GRAY));

  void initData() {
    this.game1 = new LightEmAll(7, 7);
    this.game2 = new LightEmAll(7, 7);
    this.game3 = new LightEmAll(8, 8);
    this.game4 = new LightEmAll(2, 2);

    this.myrand = new Random();
    this.gp1 = new GamePiece();
    this.gp2 = new GamePiece();

    game2.random = myrand;
    gp1.left = true;
    gp1.top = true;

  }

  // testing assigning of the game pieces row and col
  void testAssignpieces(Tester t) {
    initData();
    t.checkExpect(game1.board.get(0).get(0).row, 0);
    t.checkExpect(game1.board.get(1).get(3).row, 3);
    t.checkExpect(game1.board.get(5).get(3).col, 5);
  }

  
  void testassignConnectors(Tester t) {
    initData();
    t.checkExpect(game2.board.get(0).get(0).bottom, true);
    t.checkExpect(game2.board.get(1).get(3).left, false);
    t.checkExpect(game2.board.get(1).get(2).right, true);
    game1.height = 1;
    game1.width = 1;
    t.checkExpect(game1.board.get(0).get(0).bottom, true);
    t.checkExpect(game1.board.get(0).get(0).right, false);
    game1.height = 2;
    game1.width = 1;
    t.checkExpect(game1.board.get(0).get(0).top, false);

  }

  void testassignNeigbors(Tester t) {
    initData();
    t.checkExpect(game1.board.get(0).get(0).neighbors.size(), 2);
    t.checkExpect(game1.board.get(3).get(3).neighbors.size(), 4);
    t.checkExpect(game1.board.get(0).get(1).neighbors.size(), 3);

  }

  void testRotatePiece(Tester t) {;
    initData();
    game2.board.get(0).get(0).rotatePiece();
    t.checkExpect(game2.board.get(0).get(0).left, true);
    t.checkExpect(game2.board.get(0).get(0).bottom, false);
    t.checkExpect(game2.board.get(0).get(0).right, false);
  }

  void testaddNodes(Tester t) {
    initData();
    t.checkExpect(game1.nodes.size(), 49);
    t.checkExpect(game3.nodes.size(), 64);

  }

  void testWithinRadius(Tester t) {
    initData();
    t.checkExpect(game1.withinRadius(game1.board.get(0).get(0)), true);
    t.checkExpect(game3.withinRadius(game3.board.get(7).get(7)), false);
  }

  void testallLitUp(Tester t) {
    initData();
    t.checkExpect(game1.allLitUp(), false);
    t.checkExpect(game2.allLitUp(), false);
  }

  void testlightUp(Tester t) {
    initData();
    game2.board.get(3).get(3).lightUp();
    t.checkExpect(game2.board.get(3).get(3).connected, false);
    t.checkExpect(game2.board.get(3).get(3).neighbors.get(0).connected, false);
    t.checkExpect(game1.board.get(5).get(5).connected, false);
  }
  
  void testcreateEdges (Tester t) {
    initData();
    t.checkExpect(game1.board.get(0).get(0).createEdges().size(), 2);
    t.checkExpect(game1.board.get(4).get(4).createEdges().size(), 4);
    t.checkExpect(game1.mst.size(), 168);
    t.checkExpect(game4.mst.size(), 8);
  }
  
  
  // main
  void testMain(Tester t) {
    initData();
    game3.bigBang(700, 700, 1.0);

  }

}
