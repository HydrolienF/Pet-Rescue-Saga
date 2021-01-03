package prs.map;

import java.io.*;


public class Joueur implements Serializable{
  private Compte compte;
  private String pseudo;

  // CONSTRUCTEUR ---------------------------------------------------------------
  public Joueur(){
    this.compte = new Compte();
    this.pseudo = "Anonyme";
  }
  // GET SET --------------------------------------------------------------------
  public Compte getCompte(){return compte;}
  public String getPseudo() { return pseudo;}
  public void setPseudo(String p) {this.pseudo = p; }

//  // Fonctions propre -----------------------------------------------------------
//  public String toString(){return "Player: " + pseudo + " " + compte.toString();}
//  public boolean unlockLevel(int i){ return compte.unlockLevel(i);}
//  public boolean isLevelUnlock(int i){return compte.isLevelUnlock(i);}
//
  public void buyBallon()
  {
      if(compte.getGold() >= 10){
          this.compte.setGold(this.compte.getGold() - Compte.ballonPrix);
          this.compte.setBallon(this.compte.getBallon() + 1);
      }else{
          System.out.println("You don't have enoth gold for buying a new ballon.");
      }
  }


  public void activateBallon()                                          // make -1 ballon in Compte
  {
      this.getCompte().setBallon(this.getCompte().getBallon() - 1);
  }

  public String toString1()
  {
      return "Player: " + this.pseudo + " " + this.compte.toString1();
  }
}
