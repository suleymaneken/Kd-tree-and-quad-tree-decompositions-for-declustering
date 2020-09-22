package main;

/**
 *
 * @author Okan
 */
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class KdTree
{
  private int leftTopCornerX;
  private int rightBotCornerX;
  private int leftTopCornerY;
  private int rightBotCornerY;
  private boolean leaf;
  protected static int height;
  static int testSize;
  private TreeSet list;
  protected static HashSet searchResults = new HashSet();
  protected static HashSet lines = new HashSet();
  public static HashSet rectangles = new HashSet();
  private KdTree leftChild;
  private KdTree rightChild;
  SegmentKesismesi seg = new SegmentKesismesi();
  Oryantasyon orient = new Oryantasyon();
  
  public KdTree()
  {
    this.leftTopCornerX = 0;
    this.rightBotCornerX = 0;
    this.leftTopCornerY = 0;
    this.rightBotCornerY = 0;
    this.leaf = true;
    this.list = new TreeSet();
    this.leftChild = null;
    this.rightChild = null;
  }
  
  public KdTree(int leftTopCornerX, int leftTopCornerY, int rightBotCornerX, int rightBotCornerY, TreeSet nodes)
  {
    this.leftTopCornerX = leftTopCornerX;
    this.leftTopCornerY = leftTopCornerY;
    this.rightBotCornerX = rightBotCornerX;
    this.rightBotCornerY = rightBotCornerY;
    this.list = nodes;
  }
  
  public void split()
  {
    slowDown();
    if (this.list.size() > 1)
    {
      TreeSet leftList = new TreeSet();
      TreeSet rightList = new TreeSet();
      
      height += 1;
      
      Iterator iter = this.list.iterator();
      for (int i = 0; i < Math.ceil(this.list.size() / 2.0D) - 1.0D; i++) {
        leftList.add(iter.next());
      }
      Dugum median = (Dugum)iter.next();
      leftList.add(median);
      while (iter.hasNext()) {
        rightList.add(iter.next());
      }
      if (this.list.size() != leftList.size() + rightList.size()) {
        System.out.println("height: " + height + " \nerror in list size\n before: " + this.list.size() + " \n after : " + (leftList.size() + rightList.size()));
      }
      height -= 1;
     
      Color c = Main.gc.getColor();
      Main.gc.setColor(Color.red);
      if (isHeightEven())
      {
        lines.add(new Cizgi(median.x, this.leftTopCornerY, median.x, this.rightBotCornerY));
        Main.gc.drawLine(median.x, this.leftTopCornerY, median.x, this.rightBotCornerY);
        this.leftChild = new KdTree(this.leftTopCornerX, this.leftTopCornerY, median.x, this.rightBotCornerY, leftList);
        this.rightChild = new KdTree(median.x , this.leftTopCornerY, this.rightBotCornerX, this.rightBotCornerY, rightList);
      }
      else
      {
        lines.add(new Cizgi(this.leftTopCornerX, median.y, this.rightBotCornerX, median.y));
        Main.gc.drawLine(this.leftTopCornerX, median.y, this.rightBotCornerX, median.y);
        this.leftChild = new KdTree(this.leftTopCornerX, this.leftTopCornerY, this.rightBotCornerX, median.y, leftList);
        this.rightChild = new KdTree(this.leftTopCornerX, median.y , this.rightBotCornerX, this.rightBotCornerY, rightList);
      }
      Main.gc.setColor(c);
      
      height += 1;
      
      this.list = null;
      this.leftChild.split();
      this.rightChild.split();
      height -= 1;
    }
  }
  
  private int getSlowFactor()
  {
    try
    {
      String slow="0";
      Integer slowInteger = new Integer(slow);
      return slowInteger.intValue();
    }
    catch (NumberFormatException ex) {}
    return 0;
  }
  
  private void slowDown()
  {
    for (int i = 0; i < getSlowFactor(); i++) {
      for (int j = 0; j < 1000000; j++) {}
    }
  }
  
  public static boolean isHeightEven()
  {
    return height % 2 == 0;
  }
  
  public void walkTree(KdTree tree)
  {
    System.out.println(tree.leftTopCornerX + ", " + tree.leftTopCornerY + 
      " --  " + tree.rightBotCornerX + ", " + tree.rightBotCornerY);
    if (tree.leftChild != null) {
      tree.walkTree(tree.leftChild);
    }
    if (tree.rightChild != null) {
      tree.walkTree(tree.rightChild);
    }
  }
  
  public void reportSubTree(KdTree tree)
  {
    rectangles.add(new KaynakDikdortgen(tree.leftTopCornerX, tree.leftTopCornerY, tree.rightBotCornerX, tree.rightBotCornerY, Color.ORANGE));
    if (tree.list != null) {
      for (Iterator iter = tree.list.iterator(); iter.hasNext();) {
        searchResults.add(iter.next());
      }
    }
//    if (tree.leftChild != null) {
//      tree.reportSubTree(tree.leftChild);
//    }
//    if (tree.rightChild != null) {
//      tree.reportSubTree(tree.rightChild);
//    }
     
  }
  
  public void searchkdTree(KdTree tree, int regionLeftTopX, int regionLeftTopY, int regionRightBotX, int regionRightBotY)
  {
    if ((tree.leftChild == null) && (tree.rightChild == null))
    {
      rectangles.add(new KaynakDikdortgen(tree.leftTopCornerX, tree.leftTopCornerY, tree.rightBotCornerX, tree.rightBotCornerY, Color.ORANGE));
      if (tree.list != null) {
        for (Iterator iter = tree.list.iterator(); iter.hasNext();)
        {
          Dugum node;
          if ((node = (Dugum)iter.next()).isInRegion(regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY)) {
            searchResults.add(node);
          }
        }
      }
    }
    else
    {
      colorRectangle(tree);
      if (tree.leftChild.isFullyContained(tree.leftChild.leftTopCornerX, tree.leftChild.leftTopCornerY, tree.leftChild.rightBotCornerX, tree.leftChild.rightBotCornerY, 
        regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY)) {
        tree.reportSubTree(tree.leftChild);
      } else if (tree.leftChild.isIntersecting(regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY)) {
        tree.searchkdTree(tree.leftChild, regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY);
      }
      if (tree.rightChild.isFullyContained(tree.rightChild.leftTopCornerX, tree.rightChild.leftTopCornerY, tree.rightChild.rightBotCornerX, tree.rightChild.rightBotCornerY, 
        regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY)) {
        tree.reportSubTree(tree.rightChild);
      } else if (tree.rightChild.isIntersecting(regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY)) {
        tree.searchkdTree(tree.rightChild, regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY);
      }
    }
  }
  
  public boolean isInRegion(int pointX, int pointY, int regionLeftTopX, int regionLeftTopY, int regionRightBotX, int regionRightBotY)
  {
    if ((pointX >= regionLeftTopX) && (pointX <= regionRightBotX) && 
      (pointY >= regionLeftTopY) && (pointY <= regionRightBotY)) {
      return true;
    }
    return false;
  }
  
  public boolean isFullyContained(int leftTopCornerX, int leftTopCornerY, int rightBotCornerX, int rightBotCornerY, int regionLeftTopX, int regionLeftTopY, int regionRightBotX, int regionRightBotY)
  {
    if ((isInRegion(leftTopCornerX, leftTopCornerY, regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY)) && 
      (isInRegion(rightBotCornerX, rightBotCornerY, regionLeftTopX, regionLeftTopY, regionRightBotX, regionRightBotY))) {
      return true;
    }
    return false;
  }
  
  public boolean isIntersecting(int regionLeftTopX, int regionLeftTopY, int regionRightBotX, int regionRightBotY)
  {
    Dugum point1 = new Dugum(this.leftTopCornerX, this.leftTopCornerY, 0);
    Dugum point2 = new Dugum(this.leftTopCornerX, this.rightBotCornerY, 0);
    Dugum point3 = new Dugum(this.rightBotCornerX, this.rightBotCornerY, 0);
    Dugum point4 = new Dugum(this.rightBotCornerX, this.leftTopCornerY, 0);
    Dugum region1 = new Dugum(regionLeftTopX, regionLeftTopY, 0);
    Dugum region2 = new Dugum(regionLeftTopX, regionRightBotY, 0);
    Dugum region3 = new Dugum(regionRightBotX, regionRightBotY, 0);
    Dugum region4 = new Dugum(regionRightBotX, regionLeftTopY, 0);
    Dugum[] pointArray = { point1, point2, point3, point4 };
    Dugum[] regionArray = { region1, region2, region3, region4 };
    int count = 0;
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (this.seg.doIntersect(pointArray[(i % 4)], pointArray[((i + 1) % 4)], regionArray[(j % 4)], regionArray[((j + 1) % 4)])) {
          count++;
        }
      }
    }
    if (count >= 1) {
      return true;
    }
    if (isFullyContained(region1.x, region1.y, region3.x, region3.y, point1.x, point1.y, point3.x, point3.y)) {
      return true;
    }
    return false;
  }
  
  public void testTree(KdTree tree)
  {
    if (tree.list != null) {
      testSize += tree.list.size();
    }
    if (tree.leftChild != null) {
      tree.testTree(tree.leftChild);
    }
    if (tree.rightChild != null) {
      tree.testTree(tree.rightChild);
    }
  }
  
  public static void colorNodes()
  {
    Color c = Main.gc.getColor();
    Main.gc.setColor(Color.RED);
    for (Iterator iter = searchResults.iterator(); iter.hasNext();)
    {
      Dugum node = (Dugum)iter.next();
      Main.gc.fillOval(node.x - 2, node.y - 2, 5, 5);
    }
    Main.gc.setColor(c);
  }
  
  public void drawSearchLine(KdTree tree)
  {
    Color c = Main.gc.getColor();
    Main.gc.setColor(Color.red);
    slowDown();
    if ((tree.leftTopCornerX == tree.leftChild.leftTopCornerX) && 
      (tree.rightBotCornerY == tree.leftChild.rightBotCornerY)) {
      Main.gc.drawLine(tree.leftChild.rightBotCornerX, tree.leftChild.leftTopCornerY, tree.leftChild.rightBotCornerX, tree.leftChild.rightBotCornerY);
    } else {
      Main.gc.drawLine(tree.leftChild.leftTopCornerX, tree.leftChild.rightBotCornerY, tree.leftChild.rightBotCornerX, tree.leftChild.rightBotCornerY);
    }
    Main.gc.setColor(c);
  }
  
  public void colorRectangle(KdTree tree)
  {
    Color c = Main.gc.getColor();
    Main.gc.setColor(Color.LIGHT_GRAY);
    Main.gc.fillRect(tree.leftTopCornerX, tree.leftTopCornerY, tree.rightBotCornerX - tree.leftTopCornerX, tree.rightBotCornerY - tree.leftTopCornerY);
    drawRect();
    Main.drawNode();
    drawLines();
    Main.drawSearchRect();
    Main.gc.setColor(Color.WHITE);
    slowDown();
    Main.gc.fillRect(0, 0, Main.width, Main.height);
    Main.gc.setColor(c);
  }
  
  public static void clearRect()
  {
    rectangles.clear();
  }
  
  public static void drawRect()
  {
    Color c = Main.gc.getColor();
    for (Iterator iter = rectangles.iterator(); iter.hasNext();)
    {
      KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
      Main.gc.setColor(rec.color);
      Main.gc.fillRect(rec.x1, rec.y1, rec.width, rec.height);
    }
    Main.gc.setColor(c);
  }
  
  public static void drawLines()
  {
    Color c = Main.gc.getColor();
    Main.gc.setColor(Color.red);
    for (Iterator iter = lines.iterator(); iter.hasNext();)
    {
      Cizgi l = (Cizgi)iter.next();
      Main.gc.drawLine(l.x1, l.y1, l.x2, l.y2);
    }
    Main.gc.setColor(c);
  }
}
