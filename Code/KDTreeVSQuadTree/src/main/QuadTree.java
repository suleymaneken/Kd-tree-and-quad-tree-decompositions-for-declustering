package main;

/**
 *
 * @author Okan
 */
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class QuadTree {
	
    private Dugum p;
    public static HashSet rectangles = new HashSet();
    public static HashSet colorRectangles = new HashSet();
    private TreeSet NW, NE, SW, SE;
    private int top, right, bottom, left;
    public QuadTree s1, s2, s3, s4;
    
    public QuadTree(TreeSet list, int top, int right, int bottom, int left) {
        
        NE = new TreeSet();
        NW = new TreeSet();
        SE = new TreeSet();
        SW = new TreeSet();
        p = null;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        if(list.isEmpty()) return;
        if(list.size() == 1)
        {
            p = (Dugum) list.first();
            return;
        } else {
            int xmid = (left + right) / 2;
            int ymid = (top + bottom) / 2;
           
            divide(list, xmid, ymid);
            s1 = new QuadTree(NE, top, right, ymid, xmid);
            s2 = new QuadTree(NW, top, xmid, ymid, left);
            s3 = new QuadTree(SE, ymid, right, bottom, xmid);
            s4 = new QuadTree(SW, ymid, xmid, bottom, left);
        }
         rectangles.clear();
         drawTree(this);
    }

    private void divide(TreeSet p, int x, int y) {
        for(Iterator<Dugum> i = p.iterator(); i.hasNext();) {
            Dugum b = i.next();
            if(b.x <= (double)x && b.y <= (double)y) NW.add(b);
            if(b.x >  (double)x && b.y <= (double)y) NE.add(b);
            if(b.x <= (double)x && b.y >  (double)y) SW.add(b);
            if(b.x >  (double)x && b.y >  (double)y) SE.add(b);
        }
    }

    public TreeSet pointsInside( QuadTree tree, int top, int right, int bottom, int left) {
        TreeSet insideList = new TreeSet();
        if(tree == null) return insideList;
        insideList.addAll(pointsInside(tree.s1, top, right, bottom, left));
        insideList.addAll(pointsInside(tree.s2, top, right, bottom, left));
        insideList.addAll(pointsInside(tree.s3, top, right, bottom, left));
        insideList.addAll(pointsInside(tree.s4, top, right, bottom, left));
        if (isInside(tree.p, top, right, bottom, left)) insideList.add(tree.p);
        return insideList;
    }

    private boolean isInside(Dugum p, int top, int right, int bottom,  int left) {
        return ( p != null && p.x <= (double)right && p.x >= (double)left && p.y <= (double)bottom && p.y >= (double)top );
    }

    public void drawTree(QuadTree tree) {
        if(tree == null) return;
        Main.gc.setColor(Color.red);
        removeUpRec(tree.getLeft(), tree.getTop(),tree.getRight(),tree.getBottom());
        Main.gc.drawRect(tree.getLeft(), tree.getTop(), tree.getRight() - tree.getLeft(), tree.getBottom() - tree.getTop());
        if(!(tree.getLeft() == Main.width+20 && tree.getTop() == 0 && tree.getRight() == Main.width*2+20 && tree.getBottom() == Main.height))
            rectangles.add(new KaynakDikdortgen(tree.getLeft(), tree.getTop(),tree.getRight(),tree.getBottom(), Color.ORANGE));
        drawTree(tree.s1);
        drawTree(tree.s2);
        drawTree(tree.s3);
        drawTree(tree.s4);
    }
    
    public void removeUpRec(int x1,int y1, int x2, int y2){
        for (Iterator iter = rectangles.iterator(); iter.hasNext();)
        {
           KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
           if(!(y2-1 < rec.y1 || y1+1 > rec.y2 || x2-1 < rec.x1 || x1+1 > rec.x2)){
              iter.remove();
           }

         }
    }
    
    public void drawRect(int x1,int y1,int x2, int y2){
       Color c = Main.gc.getColor();
       colorRectangles.clear();
        for (Iterator iter = rectangles.iterator(); iter.hasNext();)
        {
          KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
          if(!(y2 < rec.y1 || y1 > rec.y2 || x2 < rec.x1 || x1 > rec.x2))
          {
            Main.gc.setColor(rec.color);
            Main.gc.fillRect(rec.x1, rec.y1, rec.width, rec.height);
            colorRectangles.add(rec);
          }
        }
        Main.gc.setColor(c);
    }
    
    public void setColorRectangles(int x1,int y1,int x2, int y2){
        colorRectangles.clear();
        for (Iterator iter = rectangles.iterator(); iter.hasNext();)
        {
          KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
          if(!(y2 < rec.y1 || y1 > rec.y2 || x2 < rec.x1 || x1 > rec.x2))
          {
              colorRectangles.add(rec);
          }
        }
    }
    
    public void drawLines(){
        Color c = Main.gc.getColor();
        for (Iterator iter = rectangles.iterator(); iter.hasNext();)
        {
          KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
          Main.gc.setColor(Color.red);
          Main.gc.drawRect(rec.x1, rec.y1, rec.width, rec.height);
        } 
        Main.gc.setColor(c);
    }
    
    public void clearRect(){
        rectangles.clear();
    }
    
    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }
}