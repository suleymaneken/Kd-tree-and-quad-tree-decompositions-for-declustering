/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Okan
 */
public class SegmentKesismesi{
  Oryantasyon orient = new Oryantasyon();
  
  public boolean doIntersect(Dugum a, Dugum b, Dugum c, Dugum d)
  {
    if (testCommonEdge(a, b, c, d)) {
      return true;
    }
    OrientType test1 = this.orient.testOrientation(a, c, d);
    OrientType test2 = this.orient.testOrientation(b, c, d);
    if (((test1 == OrientType.COLLINEAR_P_BETWEEN_QR) || (test1 == OrientType.COLLINEAR_Q_BETWEEN_PR) || 
      (test1 == OrientType.COLLINEAR_R_BETWEEN_PQ)) && ((test2 == OrientType.COLLINEAR_P_BETWEEN_QR) || 
      (test2 == OrientType.COLLINEAR_Q_BETWEEN_PR) || (test2 == OrientType.COLLINEAR_R_BETWEEN_PQ)))
    {
      if ((a.x == b.x) && (b.x == c.x))
      {
        if (((Math.min(c.y, d.y) > a.y) && (Math.min(c.y, d.y) > b.y)) || (
          (Math.max(c.y, d.y) < a.y) && (Math.max(c.y, d.y) < b.y))) {
          return false;
        }
        return true;
      }
      if (((Math.min(c.x, d.x) > a.x) && (Math.min(c.x, d.x) > b.x)) || (
        (Math.max(c.x, d.x) < a.x) && (Math.max(c.x, d.x) < b.x))) {
        return false;
      }
      return true;
    }
    if (((test1 == OrientType.LEFT_TURN) && (test2 == OrientType.RIGHT_TURN)) || (
      (test1 == OrientType.RIGHT_TURN) && 
      (test2 == OrientType.LEFT_TURN)))
    {
      OrientType test3 = this.orient.testOrientation(c, a, b);
      OrientType test4 = this.orient.testOrientation(d, a, b);
      if (((test3 == OrientType.LEFT_TURN) && 
        (test4 == OrientType.RIGHT_TURN)) || (
        (test3 == OrientType.RIGHT_TURN) && 
        (test4 == OrientType.LEFT_TURN))) {
        return true;
      }
      return false;
    }
    return false;
  }
  
  private static boolean testCommonEdge(Dugum a, Dugum b, Dugum c, Dugum d)
  {
    if ((a.equals(c)) || (a.equals(d)) || (b.equals(c)) || (b.equals(d))) {
      return true;
    }
    return false;
  }
}
