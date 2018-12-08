package jogodots;

public class Dot {

  int x, y;

  public Dot () {
  }

  public Dot (int x, int y) {
     this.x = x;
     this.y = y;
  }
  int getX() { return x; }
  int getY() { return y; }
  void setX (int x) { this.x = x; }
  void setY (int y) { this.y = y; }

}