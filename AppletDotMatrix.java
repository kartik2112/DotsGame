/*Group Project
 Members of group:
 1411104-Raj Poladia
 1411107-Varun Rao
 1411113-Kartik Shenoy
 1411114-Yash Shinde*/
import java.applet.Applet;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class AppletDotMatrix extends Applet implements Runnable,ActionListener{

    ResultWindow w1,w2;
    
    Font f1,f2;
    int nop=2,nod=5;
    final int startX=35,startY=35,DOT_SIZE=5,DOT_GAP=30;
    int x,y,x1,x2,y1,y2,x3,y3,x4,y4,tempx,tempy,c,d,ux1,ux2,uy1,uy2;
    int boxStatus[][];
    int playerStatus=0;
    int playerBoxCount[];
    int boxCount=0;
    boolean rectFlag=false;
    boolean lineHorStatus[][];
    boolean lineVerStatus[][];
    Thread t;
    Button undo;
    boolean undoStat=false,compStatus=false,compRunStatus=false;
    @Override
    public void actionPerformed(ActionEvent e) {
       if(ux1!=0&&compStatus==false){
           undoStat=true;
           //playerChange();
           System.out.println("Player status: "+playerStatus);
           int a=(int)(ux1/35)-1;
           int b=(int)(uy1/35)-1;
           System.out.println("After Undo a: "+a+" b:"+b);
           if(ux1==ux2){
               System.out.println("\nAfter Undo:");
               undo_calc_ver(a, b);
           }
           else{
               System.out.println("\nAfter Undo:");
               undo_calc_hor(a, b);
           }
           repaint();
       }
       
    }
    public void init() {
        Frame c=(Frame)this.getParent().getParent();
        c.setTitle("Dots Games");
        do{
            nop=Integer.parseInt(JOptionPane.showInputDialog("Enter number of Players(Max 8)"));
        }while(nop>8||nop==0);
        do{
            nod=Integer.parseInt(JOptionPane.showInputDialog("Enter number of Dots(Min 5)"));
        }while(nod<5);
        
        if(nop==1){
            compStatus=true;
            nop=2;            
        }
        playerBoxCount=new int[nop];
        boxStatus=new int[nod-1][nod-1];
        lineHorStatus=new boolean[nod][nod];
        lineVerStatus=new boolean[nod][nod];
        f1=new Font("Helvetica",Font.BOLD,20);
        f2=new Font("Helvetica",Font.ITALIC,15);
        
        setSize( (nod+5)*35, (nod+8)*35); //DONE
        
        undo=new Button("Undo");
        add(undo);
        undo.addActionListener(this);
        for(int i=0;i<nod;i++){
            for(int j=0;j<nod;j++)
                lineHorStatus[i][j]=lineVerStatus[i][j]=false;
        }
        
               
        addMouseListener(new MouseAdapter(){
           public void mouseClicked(MouseEvent me){
                if(compRunStatus==false){
                     x=me.getX();
                     y=me.getY();
                     //System.out.println("Inside listener");
                     if(x>=35&&x<=35*nod+5&&y>=35&&y<=35*nod+5){

                       if(y%35<=5){

                               ux1=x1=(DOT_SIZE+DOT_GAP)*((int)x/35)+2;
                               ux2=x2=x1+35;
                               uy1=uy2=y1=y2=(DOT_SIZE+DOT_GAP)*((int)y/35)+2;
                               System.out.println(x+","+y+"Detected");
                               int a=(int)(x1/35)-1;
                               int b=(int)(y1/35)-1;
                               System.out.println("After MouseClick a: "+a+" b:"+b);
                               System.out.println("\nAfter Mouse Click");
                               calc_horiz(a, b); 
                               repaint();
                       }

                       else if(x%35<=5){                   
                           uy1=y1=(DOT_SIZE+DOT_GAP)*((int)y/35)+2;
                           uy2=y2=y1+35;
                           ux1=ux2=x1=x2=(DOT_SIZE+DOT_GAP)*((int)x/35)+2;
                           System.out.println(x+","+y+"Detected");
                           int a=(int)(x1/35)-1;
                           int b=(int)(y1/35)-1;
                           System.out.println("After MouseClick a: "+a+" b:"+b);
                           System.out.println("\nAfter Mouse Click");
                           calc_ver(a, b);
                           repaint();

                       }
                       else{
                           System.out.println(x+","+y);
                       }
                     }
                }
             
              
           }
    });
}      
        
    
    public void start(){
        
    }

//Executes only once after applet is started
    public void paint(Graphics g){
        for(int i=0;i<nod-1;i++){
            for(int j=0;j<nod-1;j++){
                boxStatus[i][j]=0;
            }
        }
        for(int i=0;i<nop;i++){
            playerBoxCount[i]=0;
        }
        for(int i=0;i<nod;i++){
            for(int j=0;j<nod;j++){
                lineHorStatus[i][j]=lineVerStatus[i][j]=false;
            }
        }
        boxCount=0;
        playerStatus=0;
        c=0;d=0;
        
        showStatus("Player 1's Turn");
        Scanner s  = new Scanner(System.in);
        
        for(int i=startY;i<startY+35*nod;i+=35){
           for(int j=startX;j<startX+35*nod;j+=35){
               g.setColor(Color.RED);
              g.fillOval(i, j, 5, 5);
           } 
        }  
        undo.setSize(40,20);
        undo.setLocation(5,5);
        g.setFont(f1);
        g.setColor(Color.BLUE); 
        for(int i=0;i<nop;i++){
            
           g.drawString("PLAYER" + (i+1) + ":", nod*35 + 40, 35+20 + i*50 );
        }
        g.setFont(null);
        g.setFont(f2);
        for(int i=0;i<nop;i++){
            //g.drawString(playerBoxCount[i]+"",nod*35+75,65+(65*i));
            g.drawString("SCORE:  0", nod*35+40, 35+20+3+15 + i*50 );
        }
        
       
    }
    //update overriden
    public void update(Graphics g){
        System.out.println("Call received in update");
        g.setFont(f2);
        if(undoStat==true){
            g.setColor(Color.WHITE);
        }
        else{
            if(playerStatus==0){
                g.setColor(Color.BLUE);            
            }
            else if(playerStatus==1){
                g.setColor(Color.GREEN);            
            }
            else if(playerStatus==2){
                g.setColor(Color.RED);            
            }
            else if(playerStatus==3){
                g.setColor(Color.PINK);            
            }
            else if(playerStatus==4){
                g.setColor(Color.ORANGE);            
            }
            else if(playerStatus==5){
                g.setColor(Color.MAGENTA);            
            }
            else if(playerStatus==6){
                g.setColor(Color.CYAN);            
            }
            else if(playerStatus==7){
                g.setColor(Color.YELLOW);            
            }
        }
        
         
        if(rectFlag){
            if(c==4){
                g.fillRect(x4+1, y4+1, 34, 34);
            }
               
            if(d==4){
                g.fillRect(x3+1, y3+1, 34, 34);
            }
               
            rectFlag=false;
            
            
            //g.setColor(Color.BLUE);
            System.out.println("Draw Rectangle"+x3+" "+y3);
            
            //System.out.println(player1BoxCount+" "+player2BoxCount+" Total:"+boxCount);
            if(undoStat==true){
                if(c==4){
                    c=0;
                    boxCount--;                
                    --playerBoxCount[playerStatus];
                    g.clearRect(nod*35+40+ 75-10,23-12 + (playerStatus+1)*50, 20, 20);
                    g.drawString(""+playerBoxCount[playerStatus],nod*35+40+75-10, 23 + (playerStatus+1)*50 );
                }

                if(d==4){
                    d=0;
                    boxCount--;                
                    --playerBoxCount[playerStatus];
                    g.clearRect(nod*35+40+ 75-10,23-12 + (playerStatus+1)*50, 20,20);
                    g.drawString(""+playerBoxCount[playerStatus],nod*35+40+75-10,23 + (playerStatus+1)*50 );
                }
            }
            else{
                if(c==4){
                    c=0;
                    boxCount++;                
                    ++playerBoxCount[playerStatus];
                    g.clearRect(nod*35+40+ 75-10,23-12 + (playerStatus+1)*50, 20,20);
                    g.drawString(""+playerBoxCount[playerStatus],nod*35+40+75-10,23 + (playerStatus+1)*50 );
                }
                if(d==4){
                    d=0;
                    boxCount++;                
                    ++playerBoxCount[playerStatus];
                    g.clearRect(nod*35+40+ 75-10,23-12 + (playerStatus+1)*50, 20,20);
                    g.drawString(""+playerBoxCount[playerStatus],nod*35+40 + 75-10,23 + (playerStatus+1)*50 );
                }
                if(playerStatus==1&&compStatus==true){
                    t=new Thread(this);
                    t.start();
                }
            }
          
        
        }
        else{
            System.out.println("Player change");
            playerChange();
            if(playerStatus==1&&compStatus==true){
                t=new Thread(this);
                t.start();
            }
        }
        if(undoStat==false){
            g.setColor(Color.BLACK);
            g.drawLine(x1,y1,x2,y2);
            System.out.println("x1"+x1+" y1"+y1+" x2"+x2+" y2"+y2);
        }
        else
        {
           g.setColor(Color.WHITE);
           g.drawLine(ux1,uy1,ux2,uy2);
           System.out.println("x1"+x1+" y1"+y1+" x2"+x2+" y2"+y2);
           undoStat=false;
           ux1=ux2=uy1=uy2=0;
           
        }
        for(int i=startY;i<startY+35*nod;i+=35){
           for(int j=startX;j<startX+35*nod;j+=35){
               g.setColor(Color.RED);
              g.fillOval(i, j, 5, 5);
           } 
        }  
        
        if(boxCount==(nod-1)*(nod-1)){
            int win=playerBoxCount[0];
            int winPos=0;
            for(int i=1;i<nop;i++){
                if(playerBoxCount[i]>win){
                    winPos=i;
                    win=playerBoxCount[i];
                }                    
            }          
            if(compStatus==true&&winPos==1){
                w2=new ResultWindow("Result","Computer wins!");
            }
            else{
                w2=new ResultWindow("Result","Player "+(winPos+1)+" wins!");
            }
            
            w2.setSize(new Dimension(500,250));
            w2.setLocation(433,134);
            w2.setVisible(true);             
            
            
        }
        System.out.println("Call ending in update");
    }
    public void playerChange(){
        if(undoStat==false){
            if(playerStatus==nop-1){
                playerStatus=0;
                showStatus("Player "+(playerStatus+1)+"'s Turn");

            }
            else{
                playerStatus++;
                if(compStatus==true&&playerStatus==1){
                    showStatus("Computer's Turn");
                    /*t=new Thread(this);
                    t.start();*/
                }
                else{
                    showStatus("Player "+(playerStatus+1)+"'s Turn");
                }
                
            }
        }
        else
        {
            if(playerStatus==0){
                playerStatus=nop-1;
                showStatus("Player "+(playerStatus+1)+"'s Turn");

            }
            else{
                --playerStatus;
                showStatus("Player "+(playerStatus+1)+"'s Turn");
            }
        }
        
    }
     
    public void run(){
       
        computer();
    }

    public void computer() {
        compRunStatus=true;
        int loopCount=0;
        int i=nod,j=nod;
        Random r1,r2;
        int a,b;
        r1=new Random();
        r2=new Random();
        try{
            for(i=0;i<nod-1;i++){
                for(j=0;j<nod-1;j++){
                    if(boxStatus[i][j]==3){
                        int no=checkLines(i,j);
                        switch(no){
                            case 1://Up line exists
                                x1=35*(i+1)+2;
                                y1=35*(j+1)+2;
                                x2=35*(i+2)+2;
                                y2=35*(j+1)+2;
                                break;
                            case 2://Down line exists
                                x1=35*(i+1)+2;
                                y1=35*(j+2)+2;
                                x2=35*(i+2)+2;
                                y2=35*(j+2)+2;
                                break;
                            case 3://Left line exists
                                x1=35*(i+1)+2;
                                y1=35*(j+1)+2;
                                x2=35*(i+1)+2;
                                y2=35*(j+2)+2;
                                break;
                            case 4://Right line exists
                                x1=35*(i+2)+2;
                                y1=35*(j+1)+2;
                                x2=35*(i+2)+2;
                                y2=35*(j+2)+2;
                                break;
                        }
                        a=(int)(x1/35)-1;
                        b=(int)(y1/35)-1;
                        if(y1==y2){
                            System.out.println("AI a: "+a+" b:"+b);

                           lineHorStatus[a][b]=true;

                           if(b>=0&&b<=nod-2)
                             c=++boxStatus[a][b];
                           if(b>0&&b<=nod)
                             d=++boxStatus[a][b-1];
                           System.out.println("BoxStatus: "+c+" "+d);

                           if(c==4){
                               rectFlag=true;
                               x4=(a+1)*35+2;
                               y4=(b+1)*35+2;
                               //repaint();
                               System.out.println("x4 "+x4+"y4 "+y4);
                               System.out.println(a+" "+b);
                               System.out.println("Inside repaint1");
                           }
                           if(d==4){
                               rectFlag=true;
                               x3=(a+1)*35+2;
                               y3=(b)*35+2;
                               //repaint();
                               System.out.println("x3 "+x3+"y3 "+y3);
                               System.out.println(a+" "+(b-1));
                               System.out.println("Inside repaint2");
                           }
                           try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {

                            }
                           repaint();
                        }
                        else
                        {
                           lineVerStatus[a][b]=true;

                           if(a>=0&&a<=nod-2)
                             c=++boxStatus[a][b];
                           if(a>0&&a<=nod)
                             d=++boxStatus[a-1][b];

                           System.out.println("BoxStatus: "+c+" "+d);
                           if(c==4){
                               rectFlag=true;
                               x4=(a+1)*35+2;
                               y4=(b+1)*35+2;
                               System.out.println("x4 "+x4+"y4 "+y4);


                              System.out.println(a+" "+b);
                              System.out.println("Inside repaint1");

                           }
                           if(d==4){
                               rectFlag=true;
                               x3=(a)*35+2;
                               y3=(b+1)*35+2;
                               System.out.println("x3 "+x3+"y3 "+y3);
                               //repaint();

                               System.out.println((a-1)+" "+b);
                               System.out.println("Inside repaint2");
                           }
                           try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {

                            }
                           repaint();                
                        }
                        return;
                    }
                }
            }
            if(i==nod-1&&j==nod-1){
                do{
                    if(loopCount<1000){
                        loopCount++;
                        a=r1.nextInt(nod-1);
                        b=r2.nextInt(nod-1);
                        if(lineHorStatus[a][b]==false){
                            x1=(35*(a+1))+2;
                            y1=(35*(b+1))+2;
                            x2=(35*(a+2))+2;
                            y2=(35*(b+1))+2;
                            calc_horiz(a, b);
                            if(c==3||d==3){
                                undo_calc_hor(a, b);
                                System.out.println("Inside future c=3 or d=3");
                                continue;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {

                            }


                            repaint();
                            return;
                        }
                        if(lineVerStatus[a][b]==false){
                            x1=(35*(a+1))+2;
                            y1=(35*(b+1))+2;
                            x2=(35*(a+1))+2;
                            y2=(35*(b+2))+2;
                            calc_ver(a, b);
                            if(c==3||d==3){
                                undo_calc_ver(a, b);
                                System.out.println("Inside future c=3 or d=3");
                                continue;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {

                            }

                               repaint();                
                            return;
                        }
                    }
                    else{
                        for(i=0;i<nod-1;i++){
                            for(j=0;j<nod-1;j++){
                               if(boxStatus[i][j]!=4) {
                                    int no=checkLines(i,j);
                                    switch(no){
                                        case 1://Up line exists
                                            x1=35*(i+1)+2;
                                            y1=35*(j+1)+2;
                                            x2=35*(i+2)+2;
                                            y2=35*(j+1)+2;
                                            break;
                                        case 2://Down line exists
                                            x1=35*(i+1)+2;
                                            y1=35*(j+2)+2;
                                            x2=35*(i+2)+2;
                                            y2=35*(j+2)+2;
                                            break;
                                        case 3://Left line exists
                                            x1=35*(i+1)+2;
                                            y1=35*(j+1)+2;
                                            x2=35*(i+1)+2;
                                            y2=35*(j+2)+2;
                                            break;
                                        case 4://Right line exists
                                            x1=35*(i+2)+2;
                                            y1=35*(j+1)+2;
                                            x2=35*(i+2)+2;
                                            y2=35*(j+2)+2;
                                            break;
                                    }
                                    a=(int)(x1/35)-1;
                                    b=(int)(y1/35)-1;
                                    if(y1==y2){
                                        System.out.println("AI a: "+a+" b:"+b);

                                       lineHorStatus[a][b]=true;

                                       if(b>=0&&b<=nod-2)
                                         c=++boxStatus[a][b];
                                       if(b>0&&b<=nod)
                                         d=++boxStatus[a][b-1];
                                       System.out.println("BoxStatus: "+c+" "+d);

                                       if(c==4){
                                           rectFlag=true;
                                           x4=(a+1)*35+2;
                                           y4=(b+1)*35+2;
                                           //repaint();
                                           System.out.println("x4 "+x4+"y4 "+y4);
                                           System.out.println(a+" "+b);
                                           System.out.println("Inside repaint1");
                                       }
                                       if(d==4){
                                           rectFlag=true;
                                           x3=(a+1)*35+2;
                                           y3=(b)*35+2;
                                           //repaint();
                                           System.out.println("x3 "+x3+"y3 "+y3);
                                           System.out.println(a+" "+(b-1));
                                           System.out.println("Inside repaint2");
                                       }
                                       try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException ex) {

                                        }
                                       repaint();
                                    }
                                    else
                                    {
                                       lineVerStatus[a][b]=true;

                                       if(a>=0&&a<=nod-2)
                                         c=++boxStatus[a][b];
                                       if(a>0&&a<=nod)
                                         d=++boxStatus[a-1][b];

                                       System.out.println("BoxStatus: "+c+" "+d);
                                       if(c==4){
                                           rectFlag=true;
                                           x4=(a+1)*35+2;
                                           y4=(b+1)*35+2;
                                           System.out.println("x4 "+x4+"y4 "+y4);
                                          


                                          System.out.println(a+" "+b);
                                          System.out.println("Inside repaint1");

                                       }
                                       if(d==4){
                                           rectFlag=true;
                                           x3=(a)*35+2;
                                           y3=(b+1)*35+2;
                                           System.out.println("x3 "+x3+"y3 "+y3);
                                           //repaint();

                                           System.out.println((a-1)+" "+b);
                                           System.out.println("Inside repaint2");
                                       }
                                       try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException ex) {

                                        }
                                       repaint();                
                                    }
                                    return;
                               }
                            }
                        }
                    }
                    
                }while(true);
            }
        }
        finally{
            compRunStatus=false; 
        }
        
        
        
    }

    private int checkLines(int i, int j) {
        if(lineHorStatus[i][j]==false)
            return 1;
        else if(lineHorStatus[i][j+1]==false)
            return 2;
        else if(lineVerStatus[i][j]==false)
            return 3;
        else
            return 4;
    }
    void calc_horiz(int a,int b){
        if(!lineHorStatus[a][b]){
               System.out.println("Drawing hor line");
               lineHorStatus[a][b]=true;

               if(b>=0&&b<=nod-2)
                 c=++boxStatus[a][b];
               if(b>0&&b<=nod)
                 d=++boxStatus[a][b-1];
               System.out.println("BoxStatus: "+c+" "+d);

               if(c==4){
                   rectFlag=true;
                   x4=(a+1)*35+2;
                   y4=(b+1)*35+2;
                   //repaint();
                   System.out.println("x4 "+x4+"y4 "+y4);


                   System.out.println(a+" "+b);
                   System.out.println("Inside repaintc");
               }
               if(d==4){
                   rectFlag=true;
                   x3=(a+1)*35+2;
                   y3=(b)*35+2;
                   //repaint();
                   System.out.println("x3 "+x3+"y3 "+y3);


                   System.out.println(a+" "+(b-1));
                   System.out.println("Inside repaintd");
               }

               
           }
    }
    void calc_ver(int a,int b){
        if(!lineVerStatus[a][b]){
               System.out.println("Drawing ver line");
               lineVerStatus[a][b]=true;

               if(a>=0&&a<=nod-2)
                 c=++boxStatus[a][b];
               if(a>0&&a<=nod)
                 d=++boxStatus[a-1][b];

               System.out.println("BoxStatus: "+c+" "+d);
               if(c==4){
                   rectFlag=true;
                   x4=(a+1)*35+2;
                   y4=(b+1)*35+2;
                   System.out.println("x4 "+x4+"y4 "+y4);


                  System.out.println(a+" "+b);
                  System.out.println("Inside repaintc");

               }
               if(d==4){
                   rectFlag=true;
                   x3=(a)*35+2;
                   y3=(b+1)*35+2;
                   System.out.println("x3 "+x3+"y3 "+y3);
                   //repaint();

                   System.out.println((a-1)+" "+b);
                   System.out.println("Inside repaintd");
               }
               
           }
    }
    void undo_calc_ver(int a,int b){
           lineVerStatus[a][b]=false;
           if(a>=0&&a<=nod-2)
             c=boxStatus[a][b]--;
           if(a>0&&a<=nod)
             d=boxStatus[a-1][b]--;
           System.out.println("After Button Click BoxStatus: "+c+" "+d);
           if(c==4){
               rectFlag=true;
               x4=(a+1)*35+2;
               y4=(b+1)*35+2;
               System.out.println("After Button Click x4 "+x4+"y4 "+y4);


              System.out.println(a+" "+b);
              System.out.println("After Button Click Inside repaint1");

           }
           if(d==4){
               rectFlag=true;
               x3=(a)*35+2;
               y3=(b+1)*35+2;
               System.out.println("After Button Click x3 "+x3+"y3 "+y3);
               //repaint();

               System.out.println((a-1)+" "+b);
               System.out.println("Inside repaint2");
           }
    }
    void undo_calc_hor(int a,int b){
           lineHorStatus[a][b]=false;
           if(b>=0&&b<=nod-2)
             c=boxStatus[a][b]--;
           if(b>0&&b<=nod)
             d=boxStatus[a][b-1]--;
           System.out.println("After Button Click BoxStatus: "+c+" "+d);
           if(c==4){
               rectFlag=true;
               x4=(a+1)*35+2;
               y4=(b+1)*35+2;
               //repaint();
               System.out.println("After Button Click x4 "+x4+"y4 "+y4);
               System.out.println(a+" "+b);
               System.out.println("After Button Click Inside repaint1");
           }
           if(d==4){
               rectFlag=true;
               x3=(a+1)*35+2;
               y3=(b)*35+2;
               //repaint();
               System.out.println("After Button Click x3 "+x3+"y3 "+y3);


               System.out.println(a+" "+(b-1));
               System.out.println("After Button Click Inside repaint2");
           }
    }
    
}
class ResultWindow extends JFrame implements Runnable
{
	String disRes;
        Thread t=new Thread(this);
        int i=0;
    ResultWindow(String title,String s)
    {
        super(title);
        disRes=s;
        setUndecorated(true);
        getContentPane().setBackground(new Color(1.0f,1.0f,1.0f,0.75f));
        setBackground(new Color(1.0f,1.0f,1.0f,0.75f));
        MyWindowAdapter adapter=new MyWindowAdapter(this);
        addWindowListener(adapter);
        t.start();
    }
  public void paint(Graphics g)
  {
      
      Font f=new Font("Arial",Font.BOLD,40);
      g.setFont(f);
        if(i%2==1){
           g.setColor(Color.RED);
           g.drawString(disRes,100,100);
            System.out.println("Exiting Paint");
        }
        else{
            g.setColor(Color.BLUE);
           g.drawString(disRes,100,100);   
        }
      }
      public void run(){
          for(i=1;i<=40;i++)
        {
          repaint();
          try {
              Thread.sleep(1000);
              System.out.println("Coming back to try-catch");
          } catch (InterruptedException ex) {
          }
        }
    }
  }
class MyWindowAdapter extends WindowAdapter{
 ResultWindow  resultwindow;
 public MyWindowAdapter(ResultWindow resultwindow)
 { 
  this.resultwindow=resultwindow;
 }
 public void windowClosing(WindowEvent we)
 {
  resultwindow.setVisible(false);
 }
}