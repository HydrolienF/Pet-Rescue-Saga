package prs;

import prs.map.Compte;
import prs.map.Joueur;
import prs.map.Map;
import prs.Jeu;
import prs.graphics.*;
import prs.usuel.image;

import java.io.*;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.awt.Image;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;


public class GuiPrs
{
    private static Data data;
    private Jeu motor;
    //private Map map;
    private boolean ballonToPlace;

    public GuiPrs(){
        motor = new Jeu(true);
        ballonToPlace=false;
    }

    // GET SET -------------------------------------------------------------------
    public Data getData(){return data;}
    public static void setData(Data d){data=d;}
    public Jeu getJeu(){return motor;}

    /*============================= graphics functions ==========================*/

    /**
    *Add main window.
    */
    public boolean addFrame(){
      try {
        data.setFrame(new Frame());
        return true;
      }catch (Exception e) {
        return false;
      }
    }

    /**
    *Add a Panel to alowed player to choose a level.
    */
    public boolean addPanelMap(){
      try {
        data.setPMap(new PanelMap(this));
        data.getPMap().setSize(data.getWidthMax(),data.getHeightMax());
        data.getFrame().setContentPane(data.getPMap());
        return true;
      }catch (Exception e) {
        return false;
      }
    }

    /**
    *Add a Panel to represent a level.
    */
    public boolean addPanelPlateau(){
      try {
        data.setTailleDUneCase(data.getHeightMax()/12);
        data.setPPlateau(new PanelPlateau());
        data.getPPlateau().setPlateau(motor.getPlateau());
        data.setPInfo(new PanelInfo(motor.getCompte(),this));
        data.setPGame(new PanelGame(data.getPPlateau(),data.getPInfo()));
        data.getPGame().setJeu(this);
        data.getFrame().setContentPane(data.getPGame());
        setPanelPlateauSize();
        addActionToButton();
        return true;
      }catch (Exception e) {
        return false;
      }
    }

    public String showMessage(String request)
    {
        String message = "";

        String l1 = "LEVEL 1\nTry to eliminate the groups of blocs of the same color under animals\n" +
                "so they will go down and will be rescued";

        String l2 = "LEVEL 2\nOn this level you can explode bombs\n" +
                "to destroy the cubes surrounding. Animals will not lost in this case.";

        String l3 = "LEVEL 3\nGood luck !\n";

        String l4 = "LEVEL 4\nGo ahead!";

        if (request.equals("level1")) {message = l1;}
        else if (request.equals("level2")) {message = l2;}
        else if (request.equals("level3")) {message = l3;}
        else if (request.equals("level4")) {message = l4;}

        return message;
    }

    /**
    *Add action for the button.
    */
    public void addActionToButton(){
        //button action
        data.getPInfo().getAddBallon().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                motor.getCurrentJoueur().buyBallon();
                repaint();
            }
        });
        data.getPInfo().getPlaceBallon().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ballonToPlace=true;
            }
        });
        data.getPInfo().getBackToMap().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                motor.endLevelLost();
                JOptionPane.showMessageDialog(data.getFrame(),"The level is lost. Try again.. ");
                playOrExit(true);
            }
        });
        data.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBoutonFermer();
        data.getPInfo().getIaFinish().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                letsIaPlay();
            }
        });
    }

    public void setBoutonFermer(){
      data.getFrame().addWindowListener(new WindowAdapter() {
          @Override // indique au compilateur qu'on écrit sur la méthode windowClosing déjà défini et il est sencé vérifier qu'on a pas fait de bêtise d'écriture.
          public void windowClosing(WindowEvent e) {
              System.out.println("close widows & do a save.");
              motor.endLevelLost();
              playOrExit(false);
            //quit();
          }
      });
    }

    /**
    *set the best size for PanelPlateau & center it.
    */
    public boolean setPanelPlateauSize(){
      try {
        int dimX = 1+data.getTailleDUneCase()*motor.getPlateau().getWidth();
        int dimY = 1+data.getTailleDUneCase()*motor.getPlateau().getHeight();
        int xCenter = (data.getWidthMax()-dimX)/2;
        int yCenter = (data.getHeightMax()-dimY)/2;
        data.getPPlateau().setBounds(xCenter,yCenter,dimX,dimY);
        data.getPInfo().setBounds(data.getPPlateau().getWidth()+xCenter+10,yCenter,300,300);
        data.getPGame().revalidate();
        return true;
      }catch (Exception e) {
        return false;
      }
    }

    /**
    *{@summary Load game images.}
    *It need to have getFrame()!=null
    */
    public boolean iniImage(){
      boolean ok = true;
      Image img = image.getImage("background.jpg");
      try {
        img = img.getScaledInstance(data.getWidthMax(),data.getHeightMax() ,Image.SCALE_SMOOTH);
        if(img==null){throw new NullPointerException();}
        data.setPMapImg(img);
      }catch (Exception e) {ok=false;}
      img = image.getImage("background2.jpg");
      try {
        img = img.getScaledInstance(data.getWidthMax(),data.getHeightMax() ,Image.SCALE_SMOOTH);
        if(img==null){throw new NullPointerException();}
        data.setPPlateauImg(img);
      }catch (Exception e) {ok=false;}
      img = image.getImage("animal.jpg");
      try {
        img = img.getScaledInstance(data.getTailleDUneCase(),data.getTailleDUneCase() ,Image.SCALE_SMOOTH);
        if(img==null){throw new NullPointerException();}
        data.setAnimal(img);
      }catch (Exception e) {ok=false;}
      img = image.getImage("inmovable.jpg");
      try {
        img = img.getScaledInstance(data.getTailleDUneCase(),data.getTailleDUneCase() ,Image.SCALE_SMOOTH);
        if(img==null){throw new NullPointerException();}
        data.setInmovable(img);
      }catch (Exception e) {ok=false;}
      img = image.getImage("bomb.jpg");
      try {
        img = img.getScaledInstance(data.getTailleDUneCase(),data.getTailleDUneCase() ,Image.SCALE_SMOOTH);
        if(img==null){throw new NullPointerException();}
        data.setBomb(img);
      }catch (Exception e) {ok=false;}
      img = image.getImage("ballons.jpg");
      /*try {
        img = img.getScaledInstance(data.getTailleDUneCase(),data.getTailleDUneCase() ,Image.SCALE_SMOOTH);
        if(img==null){throw new NullPointerException();}
        data.setBallon(img);
      }catch (Exception e) {ok=false;}*/
      return ok;
    }

    public void repaint(){ data.getFrame().repaint();
      //paintAll();
    }

    /**
    * Unlock the next level
    */
    public boolean addLevel(){
      if(data.getPMap().getNbrButton() < data.getNbrLevelAviable()){
        data.getPMap().addLevel();
        return true;
      }
      return false;
    }

    public boolean paintAll(){
      try {
        data.getFrame().paintAll(data.getFrame().getGraphics());
        return true;
      }catch (Exception e) {
        //error.error("fail repaintAll");
        return false;
      }
    }

    //-------------------------------------------------------------------------
    public void iniIsGui(){
        data = new Data();
    }

    public void GUIClicAction(){
        /*rescue(); //in pressCell.
        motor.getPlateau().shiftLeft();
        rescue();*/
        repaint();
        motor.getPlateau().gameState();
        endAction();
    }

    /**
    *End game if level is win or lost.
    */
    public void endAction(){
        if (motor.getCurrentLevelStatus().equals("win")){
            endAction(true);
        }else if (motor.getCurrentLevelStatus().equals("lost")){
            endAction(false);
        }
    }

    /**
    *End game by a win or a lost.
    */
    public void endAction(boolean win){
        if (win){
            motor.endLevel();
            JOptionPane.showMessageDialog(data.getFrame(),"Congratulations, you win !");
            int answer = JOptionPane.showConfirmDialog(data.getFrame(),"do you want to go back to map ?");
            playOrExit(answer==0);
        }
        else{
            motor.endLevelLost();
            JOptionPane.showMessageDialog(data.getFrame(),"The level is lost. Try again.. ");
            int answer = JOptionPane.showConfirmDialog(data.getFrame(),"do you want to try again ?");
            playOrExit(answer==0);
        }
    }

    public void playOrExit(boolean play){
        motor.Close();
        if(play){
            data.getFrame().dispose();
            GUIGame();
        }else{
            System.out.println("See you !! ");
            data.getFrame().dispose();
        }
    }

    public void launchLevel(int i){
        if(motor.getJoueur().getCompte().isLevelUnlock(i)){
            motor.createPlateau(i);
            addPanelPlateau();
            showMessageGUI("level"+i);
        }
    }

    public void showMessageGUI(String key){
        JOptionPane.showMessageDialog(data.getFrame(),showMessage(key));
    }

    /**
     function that calculate coordinates of a click & launch pressCell
     */
    public void clicOnPlateau(int x, int y) {
        if(x<0 || y<0){return;}
        //if(x>data.getScreenDimX() || y>data.getScreenDimY()){return;}
        if(ballonToPlace){
            motor.placeBallon(x/data.getTailleDUneCase(),y/data.getTailleDUneCase());
            ballonToPlace=false;
        }else{
            motor.pressCell(x/data.getTailleDUneCase(),y/data.getTailleDUneCase());
        }
        //TODO add a sound ?
    }

    //--------------------

    /**
    * Show some windows to make player chose an existing account or create a new 1.
    */
    public void addAccountWindow()
    {
        //window with menu deroulant:
        int answer = JOptionPane.showConfirmDialog(data.getFrame(),"Do you have an account ?");
        if(answer==0){ //if yes
            Object pseudo=null;
            boolean joueurSet=false;
            while(!joueurSet){
                //make player choose on the list.
                Object[] elementsO = motor.getListOfJoueurs().toArray();
                String[] elements = new String[elementsO.length];
                int i=0;
                for (Object o : elementsO ) {
                    if(o instanceof Joueur){
                        elements[i]=((Joueur)o).getPseudo();
                        i++;
                    }else{
                        System.out.println("Error of convertion in GuiPrs.");
                    }
                }
                //(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue)
                pseudo = JOptionPane.showInputDialog(data.getFrame(),"Choose a player","Player selection",JOptionPane.QUESTION_MESSAGE,null,elements,null);
                if(pseudo instanceof String){
                    //TODO
                    motor.selectJoueur((String)pseudo);
                    joueurSet = true;
                }else{
                    System.out.println("Fail to set selectJoueur.");
                }
            }
        }else{
            //ask a pseudo while it isn't a new pseudo.
            String pseudo = "";
            do {
                pseudo = JOptionPane.showInputDialog(data.getFrame(), "Enter a new pseudo", "Anonymus");
            } while (motor.isJoueurExisting(pseudo) || pseudo.equals(""));
            //j = new Joueur();
            //j.setPseudo(pseudo);
            motor.createNewJoueur(pseudo);
        }
    }

    public void letsIaPlay(){
        int clic=0;
        while(clic < 5000){
            if(motor.getPlateau().gameState().equals("win")){
                endAction(true); //special Gui part
            }
            int x = new Random().nextInt(motor.getPlateau().getHeight() - 1);
            int y = new Random().nextInt(motor.getPlateau().getWidth() - 1);
            if(motor.pressCell(x,y)){
                clic=0;
                paintAll(); //special Gui part
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    System.out.println("can't pause in Gui.letsIaPlay");
                }
            }
            clic++;
        }
        endAction(false); //special Gui part
    }

    //--------------------

    public void GUIGame()
    {
        boolean needToLoadAccount = false;
        if(data==null){iniIsGui();needToLoadAccount=true;}
        System.out.println(addFrame());
        //faire changer ou creer un compte graphiquement.
        if(needToLoadAccount){
            addAccountWindow();
        }

        //loadPlayerInfo();
        //Map map = new Map(motor.getJoueur());
        System.out.println(iniImage());
        System.out.println(addPanelMap());
        System.out.println("addLevel "+addLevel());
        repaint();
        System.out.println("end of main of GUIGame");
    }

    //-------------------
    public static void main(String[] args) {
        GuiPrs gui = new GuiPrs();
        gui.GUIGame();
    }
}
