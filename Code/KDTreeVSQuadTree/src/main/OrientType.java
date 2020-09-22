/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Okan
 */
public class OrientType
{
  private int type;
  
  private OrientType(int typeName)
  {
    this.type = typeName;
  }
  
  public String toString()
  {
    String temp = "";
    return String.valueOf(this.type);
  }
  
  public static final OrientType LEFT_TURN = new OrientType(1);
  public static final OrientType RIGHT_TURN = new OrientType(-1);
  public static final OrientType COLLINEAR_P_BETWEEN_QR = new OrientType(-2);
  public static final OrientType COLLINEAR_Q_BETWEEN_PR = new OrientType(0);
  public static final OrientType COLLINEAR_R_BETWEEN_PQ = new OrientType(2);
  public static final OrientType DEGENERATE = new OrientType(3);
}
