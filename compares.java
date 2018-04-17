import java.util.Comparator;

public class compares implements Comparator<Edge> {

  public int compare(Edge arg0, Edge arg1) {
    if (arg0.weight == arg1.weight) {
      return 0;
    }
    if (arg0.weight > arg1.weight) {
      return 1;
    }

    else {
      return -1;
    }

  }

}
