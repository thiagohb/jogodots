package jogodots;

public class Ponto {

  int x, y;

  public Ponto () {
  }

  public Ponto (int x, int y) {
     this.x = x;
     this.y = y;
  }
  int getX() { return x; }
  int getY() { return y; }
  void setX (int x) { this.x = x; }
  void setY (int y) { this.y = y; }

}