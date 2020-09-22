package main;

import java.awt.Color;

/**
 *
 * @author Okan
 */
public class KaynakDikdortgen
{
  int x1;
  int y1;
  int x2;
  int y2;
  
  int width;
  int height;
  int space;
  Color color;
  
  public KaynakDikdortgen(int p1, int p2, int p3, int p4, Color c)
  {
    this.x1 = p1;
    this.y1 = p2;
    this.x2 = p3;
    this.y2 = p4;
    
    this.width = (p3 - p1);
    this.height = (p4 - p2);
    this.color = c;
    this.space = this.height*this.width;
  }
  
}