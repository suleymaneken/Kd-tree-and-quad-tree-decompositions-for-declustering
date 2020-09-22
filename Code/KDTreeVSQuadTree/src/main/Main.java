package main;

/**
 *
 * @author Okan
 */
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Applet
{
  BorderLayout borderLayout1 = new BorderLayout();
  Panel panel1 = new Panel();
  Button randomDotButton = new Button();
  Button randomRecButton = new Button();
  GridLayout gridLayout1 = new GridLayout();
  TextField textField1 = new TextField();
  static Label  label1 = new Label("KD-Tree total = 0"+" KD-Tree avrg = 0");
  static Label  label2 = new Label("   Quad-Tree total = 0"+" Quad-Tree avrg = 0");
  Button createButton = new Button();
  Button clearButton = new Button();
  Button test = new Button();
  static Graphics gc;
  Graphics gcTemp;
  static TreeSet list;
  static TreeSet list2;
  static int width;
  static int height;
  static int mouseX;
  static int mouseY;
  static int lastX;
  static int lastY;
  static int lastClick;
  static boolean lines = true;
  static boolean rects = true;
  static QuadTree quadTree;
  
  @Override
  public void paint(Graphics g)
  {
    gc = getGraphics();
    Color c = g.getColor();
    g.setColor(Color.DARK_GRAY);
    g.drawRect(0,0, width, height);
    g.drawRect(width+20,0,width,height);
    
    g.setColor(Color.DARK_GRAY);
    g.fillRect(0, 400, 601, 20);
    g.setColor(Color.WHITE);
    g.drawString("KD-Tree", 280, 415);
    
    g.setColor(Color.DARK_GRAY);
    g.fillRect(620, 400, 601, 20);
    g.setColor(Color.WHITE);
    g.drawString("QUAD-Tree", 900, 415);
    g.setColor(c);
  }
  
  @Override
  public void init()
  {
    width = 600;
    height = 400;
    
    setSize(width , height);
    setLayout(this.borderLayout1);
    
    this.panel1.setLocale(Locale.getDefault());
    this.panel1.setLayout(this.gridLayout1);
    this.randomDotButton.setLabel("random node");
    this.randomRecButton.setLabel("random query range");
    this.gridLayout1.setRows(4);
    this.gridLayout1.setColumns(0);
    this.textField1.setText("50");
    this.clearButton.setLabel("clear");
    this.createButton.setLabel("create");
    this.test.setLabel("test");
    
    setBackground(Color.WHITE);
    setForeground(Color.DARK_GRAY);
    add(this.panel1, "South");
    this.panel1.add(this.label1);
    this.panel1.add(this.label2);
    
    this.panel1.add(this.createButton, null);
    this.panel1.add(this.randomRecButton,null);
    this.panel1.add(this.randomDotButton, null);
    this.panel1.add(this.textField1, null);
    
    
    this.panel1.add(this.clearButton, null);
    this.panel1.add(this.test, null);
  
    list = new TreeSet();
    list2 = new TreeSet();
    gc = getGraphics();
    this.randomDotButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
          randomDot(Main.width,Main.height, new Integer(Main.this.textField1.getText()).intValue(),true);
          Main.drawNode();
      }
    });
    this.randomRecButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) 
        {
          if(KdTreeOlustur.root != null || quadTree != null){
            Main.gc.clearRect(0, 0, Main.width, Main.height);
            Main.gc.clearRect(Main.width+21, 0, Main.width-1, Main.height);
            
            KdTree.clearRect();
            KdTree.searchResults.clear();
           
            Main.drawNode();
            
            int x1 = (int)(Math.random() * Main.width);
            int y1 = (int)(Math.random() * Main.height);
            int x2 = (int)(Math.random() * Main.width);
            int y2 = (int)(Math.random() * Main.height);
            
            if(x1>x2){
                int temp = x1;
                x1=x2;
                x2=temp;
            }
            if(y1>y2){
                int temp = y1;
                y1=y2;
                y2=temp;
            }

            
            KdTreeOlustur.root.searchkdTree(KdTreeOlustur.root, x1, 
            y1, x2, y2);

            KdTree.drawRect();
            Color c = Main.gc.getColor();
            Main.gc.setColor(Color.BLUE);
            Main.gc.drawRect(x1, y1, Math.abs(x2 - x1), 
              Math.abs(y2 - y1));
            quadTree.drawRect(x1+Main.width+20,y1,x2+Main.width+20,y2);
            Main.gc.drawRect(x1+Main.width+20, y1, Math.abs(x2 - x1),
              Math.abs(y2 - y1));
            
            
            Main.gc.setColor(c);
            Main.drawNode();
            KdTree.drawLines();
            quadTree.drawLines();
            KdTree.colorNodes();
          
          
            int total_space_kd = totalSpace(KdTree.rectangles);
            int total_space_quad = totalSpace(QuadTree.colorRectangles);
            
            
            Main.label1.setText("KD-Tree total = "+total_space_kd+" KD-Tree avrg = "+(total_space_kd/KdTree.rectangles.size()));
            Main.label2.setText("   Quad-Tree total = "+total_space_quad+" Quad-Tree avrg = "+(total_space_quad/QuadTree.colorRectangles.size()));
        }else{
             javax.swing.JOptionPane.showMessageDialog(null,"önce oluştur butonuna basın!","Uyarı!",0);
        }
       }
    });
    this.createButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        create();
      }
    });
    this.clearButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        clear();
      }
    });
    this.test.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
          
//        Main.gc.clearRect(0, 0, Main.width, Main.height);
//        if (Main.rects) {
//          KdTree.drawRect();
//        }
//        if (Main.lines)
//        {
//          Main.lines = false;
//        }
//        else
//        {
//          Main.lines = true;
//          KdTree.drawLines();
//        }
//        Main.drawSearchRect();
//        Main.drawNode();
//        KdTree.colorNodes();
              
              //test2();
              // test3();
              
              ArrayList lt = new ArrayList();
              ArrayList lng = new ArrayList();
              
              File  a  = new File("C:\\a.txt");
              try {
                  Scanner sc = new Scanner(a);
                  while(sc.hasNextLine()){
                      String word = sc.nextLine();
                      if(word.split("\\s+")[0].split("\\.").length > 2
                              && word.split("\\s+")[1].split("\\.").length > 2){
                          lt.add(word.split("\\s+")[0].split("\\.")[0]+"."+word.split("\\s+")[0].split("\\.")[1]+word.split("\\s+")[0].split("\\.")[2]);
                          lng.add(word.split("\\s+")[1].split("\\.")[0]+"."+word.split("\\s+")[1].split("\\.")[1]+word.split("\\s+")[1].split("\\.")[2]);
                      }
                  }
                  sc.close();
              } catch (FileNotFoundException ex) {
                  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              }
              
              for(int i=0 ;i<lt.size();i++){
                  
                  double lat =Double.parseDouble((String)lt.get(i));
                  double lon =Double.parseDouble((String)lng.get(i));
                  
                  
                  
                  //double x = MercatorProjection.longitudeToPixelX(lat,new Byte("1"));
                  //double y = MercatorProjection.latitudeToPixelY(lon,new Byte("1"));
                  
                  double x =((lat*600)/22) - 650;
                  double y = 1000 - ((lon*400)/19);
                  
                  
                  
                  //x = 35; y = 38;
                  
                  
                  
                  //x = (x*6) / 19;
                  // y = 402 - ((y*400) / 6);
                  
                   //System.out.println(x+", "+y);
                  //x = ((x-25)/19)*(600-0)+0;
                  //y = ((y-35)/6)*(400-0)+0;
                  
                  
                 // clear();
                  boolean flg = true;
                   for (Iterator iter = Main.list.iterator(); iter.hasNext();)
                        {
                          Dugum d = (Dugum)iter.next();
                          if(Math.abs(d.x-x) < 1 && Math.abs (d.y-y) < 1){
                               flg = false;
                          }
                          
                        }
                  
                  if ((x < Main.width) && (y < Main.height) && (x>0 && y>0) && flg)
                  {
                      Color c = Main.gc.getColor();
                      Main.gc.setColor(Color.BLACK);
                      Main.gc.fillOval((int)x - 2, (int)y - 2, 5, 5);
                      Main.gc.fillOval(Main.width+20+(int)x - 2, (int)y - 2, 5, 5);
                      Main.gc.setColor(c);
                      Main.list.add(new Dugum((int)x, (int)y, 1));
                      Main.list2.add(new Dugum(Main.width+20+(int)x, (int)y, 1));
                      
                  }
              }
              
              
//              create();
//              System.out.println("Query range göreceli küçük...");
//              int[][] results = new int[2][10];
//              for (int i = 3; i <=30; i+=3) {
//                  int kdtree_cost=0,quadtree_cost=0;
//                 
//                  for (int j = 0; j < 50; j++) {
//                      
//                      randomRec(i);
//                      kdtree_cost+=totalSpace(KdTree.rectangles) - i;
//                      quadtree_cost+=totalSpace(QuadTree.colorRectangles)-i;
//                      
//                  }
//                  results[0][i/3-1]=kdtree_cost/50;
//                  results[1][i/3-1]=quadtree_cost/50;
//              }
//              
//              for (int i = 0; i < 2; i++) {
//                  System.out.println(i+1);
//                  System.out.print("[");
//                  for (int j = 0; j < 10; j++) {
//                      System.out.print(results[i][j]+", ");
//                  }
//                  System.out.println("]");
//              }
//
//              System.out.println("Query range göreceli büyük...");
//              int[][] results2 = new int[2][14];
//              for (int i = 40; i <=300; i+=20) {
//                  int kdtree_cost=0,quadtree_cost=0;
//                 
//                  for (int j = 0; j < 50; j++) {
//                      
//                      randomRec(i);
//                      kdtree_cost+=totalSpace(KdTree.rectangles) - i;
//                      quadtree_cost+=totalSpace(QuadTree.colorRectangles) - i;
//                      
//                  }
//                  results2[0][i/20-2]=kdtree_cost / 50;
//                  results2[1][i/20-2]=quadtree_cost / 50;
//              }
//              
//              for (int i = 0; i < 2; i++) {
//                  System.out.println(i+1);
//                  System.out.print("[");
//                  for (int j = 0; j < 14; j++) {
//                      System.out.print(results2[i][j]+", ");
//                  }
//                  System.out.println("]");
//              }
      
      }
    });
    addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent e)
      {
        int x = e.getX();
        int y = e.getY();
        
          if(x<=0)
              x=1;
          if(y<=0)
              y=1;
        if ((x <= Main.width) && (y <= Main.height))
        {
          if (e.getButton() == 1)
          {
            Main.lastClick = 1;
            Color c = Main.gc.getColor();
            Main.gc.setColor(Color.BLACK);
            Main.gc.fillOval(x - 2, y - 2, 5, 5);
            Main.gc.fillOval(Main.width+20+x - 2, y - 2, 5, 5);
            Main.gc.setColor(c);
            Main.list.add(new Dugum(x, y, 1));
            Main.list2.add(new Dugum(Main.width+20+x, y, 1));
          }
          if (e.getButton() == 3)
          {
            Main.lastClick = 3;
            Main.mouseX = x;
            Main.mouseY = y;
            Main.gc.clearRect(0, 0, Main.width, Main.height);
            Main.gc.clearRect(Main.width+21, 0, Main.width-1, Main.height);
            KdTree.clearRect();
            KdTree.searchResults.clear();
            Main.drawNode();
          }
        }
      }
      
      public void mouseReleased(MouseEvent e)
      {
        int x = e.getX();
        int y = e.getY();
          if(x>=Main.width)
              x=Main.width;
          if(y>=Main.height)
              y=Main.height;
          
        if (e.getButton() == 3)
        {
          Main.lastX = x;
          Main.lastY = y;
          KdTreeOlustur.root.searchkdTree(KdTreeOlustur.root, Main.mouseX, 
          Main.mouseY, x, y);
          
          KdTree.drawRect();
          Color c = Main.gc.getColor();
          Main.gc.setColor(Color.BLUE);
          Main.gc.drawRect(Main.mouseX, Main.mouseY, Math.abs(x - Main.mouseX), 
            Math.abs(y - Main.mouseY));
          quadTree.drawRect(Main.mouseX+Main.width+20,Main.mouseY,x+Main.width+20,y);
          Main.gc.drawRect(Main.mouseX+Main.width+20, Main.mouseY, Math.abs(x - Main.mouseX), 
            Math.abs(y - Main.mouseY));
          Main.gc.setColor(c);
          
          
          Main.drawNode();
          KdTree.drawLines();
          quadTree.drawLines();
          KdTree.colorNodes();
          
          
            int total_space_kd = 0,total_space_quad = 0;
            for (Iterator iter = KdTree.rectangles.iterator(); iter.hasNext();)
            {
              KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
              total_space_kd += rec.space;
            }
            for (Iterator iter = QuadTree.colorRectangles.iterator(); iter.hasNext();)
            {
              KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
              total_space_quad += rec.space;
            }
            
            Main.label1.setText("KD-Tree total = "+total_space_kd+" KD-Tree avrg = "+(total_space_kd/KdTree.rectangles.size()));
            Main.label2.setText("   Quad-Tree total = "+total_space_quad+" Quad-Tree avrg = "+(total_space_quad/QuadTree.colorRectangles.size()));
            
            
            
        }
      }
    });
    addMouseMotionListener(new MouseMotionAdapter()
    {
      public void mouseDragged(MouseEvent e)
      {
        int x = e.getX();
        int y = e.getY();
        if(x>=Main.width)
              x=Main.width;
          if(y>=Main.height)
              y=Main.height;
        if ((Main.lastClick == 3) && (Main.list.size() <= 1000))
        {
          Main.gc.clearRect(0, 0, Main.width, Main.height);
          Main.gc.clearRect(Main.width+21, 0, Main.width-1, Main.height);
          Main.drawNode();
          
          Color c = Main.gc.getColor();
          Main.gc.setColor(Color.YELLOW);
          Main.gc.drawRect(Main.mouseX, Main.mouseY, Math.abs(x - Main.mouseX), Math.abs(y - 
            Main.mouseY));
          Main.gc.drawRect(Main.mouseX+Main.width+20, Main.mouseY, Math.abs(x - Main.mouseX), Math.abs(y - 
            Main.mouseY));
          Main.gc.setColor(c);
        }
      }
    });
  }
  
  public static void drawNode(){
    Color c = gc.getColor();
    gc.setColor(Color.BLACK);
    for (Iterator iter = list.iterator(); iter.hasNext();)
    {
      Dugum node = (Dugum)iter.next();
      gc.fillOval(node.x - 2, node.y - 2, 5, 5);
    }
    
    for (Iterator iter = list2.iterator(); iter.hasNext();)
    {
      Dugum node = (Dugum)iter.next();
      gc.fillOval(node.x - 2, node.y - 2, 5, 5);
    }
    gc.setColor(c);
  }
  
  public static void drawSearchRect(){
    Color c = gc.getColor();
    gc.setColor(Color.GREEN);
    gc.drawRect(mouseX, mouseY, lastX - mouseX, lastY - mouseY);
    gc.setColor(c);
  }
  
  public static void clear(){
        Main.gc.clearRect(0, 0, Main.width, Main.height);
        Main.gc.clearRect(Main.width,0,Main.width+21,Main.height);
        Color c = Main.gc.getColor();
        Main.gc.setColor(Color.darkGray);
        Main.gc.drawRect(0, 0, Main.width, Main.height);
        Main.gc.drawRect(Main.width+20,0,Main.width,Main.height);
        Main.gc.setColor(c);
        Main.list = new TreeSet();
        Main.list2 = new TreeSet();
  }
  
  public static void create(){
      KdTree.lines.clear();
      KdTree.searchResults.clear();
      KdTree.rectangles.clear();
      KdTreeOlustur kdtree = new KdTreeOlustur(0,0, Main.width, Main.height, Main.list);
      quadTree = new QuadTree(Main.list2,0,Main.width*2+20,Main.height,Main.width+20);
      
     // System.out.println("Agaçlar oluşturuluyor....");
  }
  
  public static void randomDot(int width,int height,int size,boolean isUniform){
    for (int i = 0; i < size; i++)
    {
        int xcoor,ycoor;
        
        if(isUniform){
            xcoor = (int)(Math.random() * width);
            ycoor = (int)(Math.random() * height);
        }else{
            int xd = new Random().nextInt(width);
            if(xd <= width/2)
                xcoor = new Random().nextInt(Math.abs(width - xd)) + xd ;
            else
                xcoor = Math.abs(new Random().nextInt(Math.abs(width - xd)) - xd );

            int yd  = new Random().nextInt(height);
            if(yd<= height/2)
                ycoor = new Random().nextInt(Math.abs(height - yd)) + yd;
            else
                ycoor = Math.abs(new Random().nextInt(Math.abs(height - yd)) - yd);
        }
        
        
      Dugum newNode = new Dugum(xcoor, ycoor, 1);
      Dugum newNode2 = new Dugum(xcoor+Main.width+20, ycoor, 1);

      Main.list.add(newNode);
      Main.list2.add(newNode2);
    }
  }

  public static void randomRec(int size){
    KdTree.clearRect();
    KdTree.searchResults.clear();
    
    
    int x1,x2,y1,y2;
    
    while(true){
     x1 = (int)(Math.random() * Main.width);
     y1 = (int)(Math.random() * Main.height);

     x2 = (int)(Math.random() * Main.width);
     y2 = (int)(Math.random() * Main.height);

     if(x1>x2){
        int temp = x1;
        x1=x2;
        x2=temp;
     }
     if(y1>y2){
        int temp = y1;
        y1=y2;
        y2=temp;
     }

     
         if(Math.abs(x1-x2)* Math.abs(y1-y2)==size)
             break;
    }
    
    KdTreeOlustur.root.searchkdTree(KdTreeOlustur.root, x1, 
    y1, x2, y2);
    
    quadTree.drawRect(x1+Main.width+20,y1,x2+Main.width+20,y2);
    
     // System.out.println("-Query Alanı = "+(Math.abs(x1-x2)* Math.abs(y1-y2)));
  }

  public static int totalSpace(HashSet set){
    int total_space = 0;
    for (Iterator iter = set.iterator(); iter.hasNext();)
    {
      KaynakDikdortgen rec = (KaynakDikdortgen)iter.next();
      total_space += rec.space;
    }
   // System.out.println("-Toplam Alan = "+total_space+"-Ortalama Alan = "+(double)(total_space/set.size()));
    return total_space;
  }

  public static void test(){
      System.out.println("Test Başlıyor.....");
      for (int z = 0; z < 100; z++) {
          System.out.println("");
          System.out.println("Adim "+(z+1)+" ---------------------Başladı");
          
          int a = new Random().nextInt(2);
          
          if(a==1){
            System.out.println("Veriler Uniform...");
            randomDot(Main.width, Main.height, 100, true);
          }else{
            System.out.println("Veriler Uniform Değil...");
            randomDot(1+new Random().nextInt(400), 1+new  Random().nextInt(400), 50, false);
          }
         
          
          create();
          
          int b = new Random().nextInt(2);
          
//          if(b == 1){
//              System.out.println("Query range göreceli büyük...");
//              randomRec(true, 30);
//          }else{
//              System.out.println("Query range göreceli küçük...");
//              randomRec(false, 30);
//          }
          
          System.out.println("Kd-Tree için");
          totalSpace(KdTree.rectangles);
          System.out.println("Quad-Tree için");
          totalSpace(QuadTree.colorRectangles);
          System.out.println("Adim "+(z+1)+" ---------------------Bitti");
          System.out.println("");
          clear();
      }
      System.out.println("Test Bitti.....");
  }
  
  public static void test2(){
      
          System.out.println("Veriler Uniform...");
          System.out.println("Query range göreceli küçük...");
          int[][] results = new int[4][10];
          for (int i = 3; i <=30; i+=3) {
              int kdtree_top=0,kdtree_ort=0,quadtree_top=0,quadtree_ort=0;
              for (int j = 0; j < 5; j++) {
                    randomDot(Main.width, Main.height, 100, true);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    kdtree_top+=totalSpace(KdTree.rectangles);
                    kdtree_ort+=totalSpace(KdTree.rectangles)/KdTree.rectangles.size();
                    quadtree_top+=totalSpace(QuadTree.colorRectangles);
                    quadtree_ort+=totalSpace(QuadTree.colorRectangles)/QuadTree.colorRectangles.size();
                    clear();
              }
              results[0][i/3-1]=kdtree_top;
              results[1][i/3-1]=kdtree_ort;
              results[2][i/3-1]=quadtree_top;
              results[3][i/3-1]=quadtree_ort;
          }
          
          for (int i = 0; i < 4; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 10; j++) {
                  System.out.print(results[i][j]+", ");
              }
              System.out.println("]");
          }
          
          System.out.println("Query range göreceli büyük...");
          int[][] results2 = new int[4][14];
          for (int i = 40; i <=300; i+=20) {
              int kdtree_top=0,kdtree_ort=0,quadtree_top=0,quadtree_ort=0;
              for (int j = 0; j < 5; j++) {
                    randomDot(Main.width, Main.height, 100, true);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    kdtree_top+=totalSpace(KdTree.rectangles);
                    kdtree_ort+=totalSpace(KdTree.rectangles)/KdTree.rectangles.size();
                    quadtree_top+=totalSpace(QuadTree.colorRectangles);
                    quadtree_ort+=totalSpace(QuadTree.colorRectangles)/QuadTree.colorRectangles.size();
                    clear();
              }
              results2[0][i/20-2]=kdtree_top;
              results2[1][i/20-2]=kdtree_ort;
              results2[2][i/20-2]=quadtree_top;
              results2[3][i/20-2]=quadtree_ort;
          }
          
          for (int i = 0; i < 4; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 14; j++) {
                  System.out.print(results2[i][j]+", ");
              }
              System.out.println("]");
          }
          
          
           System.out.println("Veriler Uniform Değil...");
          System.out.println("Query range göreceli küçük...");
          int[][] results3 = new int[4][10];
          for (int i = 3; i <=30; i+=3) {
              int kdtree_top=0,kdtree_ort=0,quadtree_top=0,quadtree_ort=0;
              for (int j = 0; j < 5; j++) {
                    randomDot(Main.width, Main.height, 50, false);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    kdtree_top+=totalSpace(KdTree.rectangles);
                    kdtree_ort+=totalSpace(KdTree.rectangles)/KdTree.rectangles.size();
                    quadtree_top+=totalSpace(QuadTree.colorRectangles);
                    quadtree_ort+=totalSpace(QuadTree.colorRectangles)/QuadTree.colorRectangles.size();
                    clear();
              }
              results3[0][i/3-1]=kdtree_top;
              results3[1][i/3-1]=kdtree_ort;
              results3[2][i/3-1]=quadtree_top;
              results3[3][i/3-1]=quadtree_ort;
          }
          
          for (int i = 0; i < 4; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 10; j++) {
                  System.out.print(results3[i][j]+", ");
              }
              System.out.println("]");
          }
          
          System.out.println("Query range göreceli büyük...");
          int[][] results4 = new int[4][14];
          for (int i = 40; i <=300; i+=20) {
              int kdtree_top=0,kdtree_ort=0,quadtree_top=0,quadtree_ort=0;
              for (int j = 0; j < 5; j++) {
                    randomDot(Main.width, Main.height, 50, false);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    kdtree_top+=totalSpace(KdTree.rectangles);
                    kdtree_ort+=totalSpace(KdTree.rectangles)/KdTree.rectangles.size();
                    quadtree_top+=totalSpace(QuadTree.colorRectangles);
                    quadtree_ort+=totalSpace(QuadTree.colorRectangles)/QuadTree.colorRectangles.size();
                    clear();
              }
              results4[0][i/20-2]=kdtree_top;
              results4[1][i/20-2]=kdtree_ort;
              results4[2][i/20-2]=quadtree_top;
              results4[3][i/20-2]=quadtree_ort;
          }
          
          for (int i = 0; i < 4; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 14; j++) {
                  System.out.print(results4[i][j]+", ");
              }
              System.out.println("]");
          }
       

  }
  
 

  
  public static void test3(){
      System.out.println("Veriler Uniform...");
          System.out.println("Query range göreceli küçük...");
          int[][] results = new int[2][10];
          for (int i = 3; i <=30; i+=3) {
              int kdtree_cost=0,quadtree_cost=0;
              for (int j = 0; j < 50; j++) {
                    randomDot(Main.width, Main.height, 100, true);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    
                    kdtree_cost+=totalSpace(KdTree.rectangles) - i;
                    quadtree_cost+=totalSpace(QuadTree.colorRectangles)-i;
                    
                    clear();
              }
              results[0][i/3-1]=kdtree_cost/50;
              results[1][i/3-1]=quadtree_cost/50;
          }
          
          for (int i = 0; i < 2; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 10; j++) {
                  System.out.print(results[i][j]+", ");
              }
              System.out.println("]");
          }
          
          System.out.println("Query range göreceli büyük...");
          int[][] results2 = new int[2][14];
          for (int i = 40; i <=300; i+=20) {
              int kdtree_cost=0,quadtree_cost=0;
              for (int j = 0; j < 50; j++) {
                    randomDot(Main.width, Main.height, 100, true);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    kdtree_cost+=totalSpace(KdTree.rectangles) - i;
                    
                    quadtree_cost+=totalSpace(QuadTree.colorRectangles) - i;
                   
                    clear();
              }
              results2[0][i/20-2]=kdtree_cost / 50;
              results2[1][i/20-2]=quadtree_cost / 50;
              
          }
          
          for (int i = 0; i < 2; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 14; j++) {
                  System.out.print(results2[i][j]+", ");
              }
              System.out.println("]");
          }
          
          
           System.out.println("Veriler Uniform Değil...");
          System.out.println("Query range göreceli küçük...");
          int[][] results3 = new int[2][10];
          for (int i = 3; i <=30; i+=3) {
              int kdtree_cost=0,quadtree_cost=0;
              for (int j = 0; j < 50; j++) {
                    randomDot(Main.width, Main.height, 50, false);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    kdtree_cost+=totalSpace(KdTree.rectangles) - i;
                   
                    quadtree_cost+=totalSpace(QuadTree.colorRectangles) - i;
                   
                    clear();
              }
              results3[0][i/3-1]=kdtree_cost / 50;
              results3[1][i/3-1]=quadtree_cost / 50;
        
          }
          
          for (int i = 0; i < 2; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 10; j++) {
                  System.out.print(results3[i][j]+", ");
              }
              System.out.println("]");
          }
          
          System.out.println("Query range göreceli büyük...");
          int[][] results4 = new int[2][14];
          for (int i = 40; i <=300; i+=20) {
              int kdtree_cost=0,quadtree_cost=0;
              for (int j = 0; j < 50; j++) {
                    randomDot(Main.width, Main.height, 50, false);
                    create();
                    //System.out.println("Query range göreceli küçük...");
                    randomRec(i);
                    kdtree_cost+=totalSpace(KdTree.rectangles) - i;
                   
                    quadtree_cost+=totalSpace(QuadTree.colorRectangles) - i;
                    
                    clear();
              }
              results4[0][i/20-2]=kdtree_cost / 50;
              results4[1][i/20-2]=quadtree_cost / 50;
              
          }
          
          for (int i = 0; i < 2; i++) {
              System.out.println(i+1);
              System.out.print("[");
              for (int j = 0; j < 14; j++) {
                  System.out.print(results4[i][j]+", ");
              }
              System.out.println("]");
          }
  }
}