package main;

/**
 *
 * @author Okan
 */
public class Oryantasyon
{
  public OrientType testOrientation(Dugum p, Dugum q, Dugum r)
  {
    if (testDegenerate(p, q, r)) {
      return OrientType.DEGENERATE;
    }
    if (testCollinear(p, q, r))
    {
      Dugum middle = testMiddle(p, q, r);
      if (p.equals(middle)) {
        return OrientType.COLLINEAR_P_BETWEEN_QR;
      }
      if (q.equals(middle)) {
        return OrientType.COLLINEAR_Q_BETWEEN_PR;
      }
      return OrientType.COLLINEAR_R_BETWEEN_PQ;
    }
    double det = determinant(p, q, r);
    if (det > 0.0D) {
      return OrientType.RIGHT_TURN;
    }
    return OrientType.LEFT_TURN;
  }
  
  private double determinant(Dugum p, Dugum q, Dugum r)
  {
    return (q.x - p.x) * (r.y - p.y) - (r.x - p.x) * (q.y - p.y);
  }
  
  private boolean testDegenerate(Dugum p, Dugum q, Dugum r)
  {
    return (p.equals(q)) || (p.equals(r)) || (q.equals(r));
  }
  
  private boolean testCollinear(Dugum p, Dugum q, Dugum r)
  {
    double slopePQ = p.slope(q);
    double slopePR = p.slope(r);
    if (slopePQ == slopePR) {
      return true;
    }
    return false;
  }
  
  private Dugum testMiddle(Dugum p, Dugum q, Dugum r)
  {
    if ((p.x == q.x) && (p.x == r.x))
    {
      double max = Math.max(Math.max(p.y, q.y), r.y);
      double min = Math.min(Math.min(p.y, q.y), r.y);
      if ((p.y != max) && (p.y != min)) {
        return p;
      }
      if ((q.y != max) && (q.y != min)) {
        return q;
      }
      return r;
    }
    if ((p.y == q.y) && (p.y == r.y))
    {
      double max = Math.max(Math.max(p.x, q.x), r.x);
      double min = Math.min(Math.min(p.x, q.x), r.x);
      if ((p.x != max) && (p.x != min)) {
        return p;
      }
      if ((q.x != max) && (q.x != min)) {
        return q;
      }
      return r;
    }
    double max = Math.max(Math.max(p.x, q.x), r.x);
    double min = Math.min(Math.min(p.x, q.x), r.x);
    if ((p.x != max) && (p.x != min)) {
      return p;
    }
    if ((q.x != max) && (q.x != min)) {
      return q;
    }
    return r;
  }
}
