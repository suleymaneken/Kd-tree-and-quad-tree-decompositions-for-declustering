package main;

/**
 *
 * @author Okan
 */
public class Dugum implements Comparable{
  protected int x;
  protected int y;
  protected int id;
  
  public Dugum()
  {
    this.x = 0;
    this.y = 0;
    this.id = 0;
  }
  
  public Dugum(int x, int y, int id)
  {
    this.x = x;
    this.y = y;
    this.id = id;
  }
  
  public int compareTo(Object other)
  {
    Dugum otherNode = (Dugum)other;
    if (equals(other)) {
      return 0;
    }
    if (KdTree.isHeightEven()) {
      return this.x - otherNode.x == 0 ? -1 : this.x - otherNode.x;
    }
    return this.y - otherNode.y == 0 ? -1 : this.y - otherNode.y;
  }
  
  public boolean equals(Object other)
  {
    Dugum otherNode = (Dugum)other;
    if ((this.x == otherNode.x) && (this.y == otherNode.y)) {
      return true;
    }
    return false;
  }
  
  public boolean isInRegion(int regionLeftTopX, int regionLeftTopY, int regionRightBotX, int regionRightBotY)
  {
    if ((this.x >= regionLeftTopX) && (this.x <= regionRightBotX) && 
      (this.y >= regionLeftTopY) && (this.y <= regionRightBotY)) {
      return true;
    }
    return false;
  }
  
  public double slope(Dugum other)
  {
    double distX = this.x - other.x;
    double distY = this.y - other.y;
    if (distX == 0.0D) {
      return (1.0D / 0.0D);
    }
    return distY / distX;
  }
}
