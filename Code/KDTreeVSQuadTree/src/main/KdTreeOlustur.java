package main;

/**
 *
 * @author Okan
 */
import java.util.TreeSet;

public class KdTreeOlustur
{
  protected static KdTree root;
  
  public KdTreeOlustur() {}
  
  public KdTreeOlustur(int leftTopCornerX, int leftTopCornerY, int rightBotCornerX, int rightBotCornerY, TreeSet nodes)
  {
    root = new KdTree(leftTopCornerX, leftTopCornerY, rightBotCornerX, rightBotCornerY, nodes);
    KdTree.height = 0;
    KdTree.testSize = 0;
    root.split();
  }
}