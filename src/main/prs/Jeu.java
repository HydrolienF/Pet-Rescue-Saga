package prs;

import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class Jeu
{
    private Plateau plateau;
    private Point cell;
    private int initialBlocs, initialAnimals, initialImmoBlocs, initialBombs, initialBallons;
    private int additionalBlocs, additionalAnimals, additionalBombs, additionalBallons;
    private int random;
    private Configuration configLevel;
    private Configuration configPlayer;
    private int level;
    
    //private Joueur jouer;
    //private Compte compte;

    public Jeu()
    {
        this.configLevel = new Configuration("/home/nata/Documents/Projets/Pet-Rescue-Saga/Pet_Rescue/Pet-Rescue-Saga/config.txt");
        this.level = Integer.parseInt(configPlayer.getPlayerValue("Level"));

        this.plateau = new Plateau(Integer.parseInt(configLevel.getValue(level, "height")),
                                   Integer.parseInt(configLevel.getValue(level, "width")));

        //initialisation of start values of number of blocs, animals etc.
        initialBlocs = Integer.parseInt(configLevel.getValue(level, "initialBlocs"));
        initialAnimals = Integer.parseInt(configLevel.getValue(level, "initialAnimals"));
        initialImmoBlocs = Integer.parseInt(configLevel.getValue(level, "initialImmoBlocs"));
        if (configLevel.getValue(level, "outils") == "true")
        {
            initialBombs = Integer.parseInt(configLevel.getValue(level, "initialBombs"));
            initialBallons = Integer.parseInt(configLevel.getValue(level, "initialBallons"));
        }
        else
            {
                initialBombs = 0;
                initialBallons = 0;
            }

        //initialisation of additional values which would be added during the game according the level
        if (configLevel.getValue(level, "addBlocs") == "true")
        {
            additionalBlocs = Integer.parseInt(configLevel.getValue(level, "additionalBlocs"));
        }
        else {additionalBlocs = 0;}

        if (configLevel.getValue(level, "addAnimals") == "true")
        {
            additionalAnimals = Integer.parseInt(configLevel.getValue(level, "additionalAnimals"));
        }
        else {additionalAnimals = 0;}

        if (configLevel.getValue(level, "addBombs") == "true")
        {
            additionalBombs = Integer.parseInt(configLevel.getValue(level, "additionalBombs"));
        }
        else {additionalBombs = 0;}

        if (configLevel.getValue(level, "addBallons") == "true")
        {
            additionalBallons = Integer.parseInt(configLevel.getValue(level, "additionalBallons"));
        }
        else {additionalBallons = 0;}


    }


    private void GameSet()
    {
        /*1) ask if want play
        if no -> exit
        if yes:
          2) make un account -> create config OR insert name -> load level, points etc from configPlayer
          3) <Play>
          4) create plateau, fill it by elements
          5) cycle mouseClicked - reactions
            till:
             - level win
             - level lost
          6) save level, points etc to configPlayer
          7) start new level OR exit
         */
    }

    private void remplissage()
    {

    }

    /* function which provide reactions for clic:
     - if group of blocs, delete them all
     - if one bloc, do nothing
     - if bomb, make an explosion and deletion of 8 cells around
     - if ballon, delete all bloc of its color
     - if outil, launch it
     - if animal, do nothing for the moment
     */

    private void mouseClicked(MouseEvent e)
    {
        int x = cell.getCoordX();
        int y = cell.getCoordY();

        // player has clicked on plateau
        if (plateau.isOnPlateau(cell))
        {
            // not empty cell
            if (!plateau.isEmpty(x, y))
            {
                // if bloc
                if (plateau.getObject(x, y) instanceof Bloc)
                {
                    // only one bloc
                    if (plateau.getGroup(x, y).size() == 1)
                    {
                        //skip
                    }
                    // group of blocs
                    else
                    {
                        LinkedList<Point> gr = plateau.getGroup(x, y);
                        // delete group
                        gr.clear();
                        // TODO move down upstairs elements
                        // TODO add another blocs and elements (depends of level)
                    }
                }
                // if animal
                else if (plateau.getObject(x, y) instanceof Animal)
                {
                    //TODO get sound
                }
                // if outil
                else if (plateau.getObject(x, y) instanceof Outil)
                {
                    // if bomb
                    if (plateau.getObject(x, y) instanceof Bomb)
                    {
                        //bomb destroyed 9 cells -- its cell + 8 around
                        plateau.bombExplosion(x, y);
                    }

                    //ballon
                    else if (plateau.getObject(x, y) instanceof Ballon)
                    {
                        //ballon destroyed all blocs of his color
                        String ballonColor = ((Ballon)plateau.getObject(x, y)).getName();
                        plateau.ballonExplosion(ballonColor);
                    }
                }
            }
            //empty cell
            else
            {
                //skip
            }
        }
        // player has clicked out of plateau
        else
        {
            System.out.println("Clicked out of bounds of plateau");
        }
    }

    public static void main (String[] args)
    {
        Configuration conf = new Configuration("/home/nata/Documents/Projets/Pet-Rescue-Saga/Pet_Rescue/Pet-Rescue-Saga/config.txt");
        System.out.println(conf.getValue(1, "additionalBlocs"));
        Configuration confPlayer = new Configuration("/home/nata/Documents/Projets/Pet-Rescue-Saga/Pet_Rescue/Pet-Rescue-Saga/configPlayer.txt");
        System.out.println(confPlayer.getPlayerValue("Level"));
    }
}
