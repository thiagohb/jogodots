package jogodots;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Player extends Thread{

  private Socket connection;
  private DataInputStream input;
  private DataOutputStream output;
  private FrameDotsServer control;
  boolean threadSuspended = true;
  private int numberOfPlayers;


  public Player(Socket s, FrameDotsServer d, int n) {

    connection = s;

    try{

      input = new DataInputStream(connection.getInputStream());
      output = new DataOutputStream(connection.getOutputStream());

    }
    catch(IOException e){
      JOptionPane.showMessageDialog(null, "Connection error!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
      System.exit(1);
    }

    control = d;
    numberOfPlayers = n;
  }

  public void opponentMove(int row, int column, char matrix){

    try{
      output.writeUTF("Opponent move");
      output.writeChar(matrix);
      output.writeInt(row);
      output.writeInt(column);

    }
    catch(IOException e){
     JOptionPane.showMessageDialog(null, "Connection error!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
    }

  }

  public void run(){

    boolean exit = false;

    try{
      control.display(numberOfPlayers);
      output.writeInt(numberOfPlayers);
      output.writeUTF("Player " + (numberOfPlayers + 1) + ", wait please.");

      //Wait for the opponent
      if(numberOfPlayers == 0){

        output.writeUTF("Waiting for the next player");

        try{
          synchronized (this){
            while(threadSuspended) wait();
          }
        }
        catch (InterruptedException e){

        }
        output.writeUTF("Other player connected. Your turn.");
      }

      //playing
      while (!exit){
        char direction = input.readChar();
        int row = input.readInt();
        int column = input.readInt();

        if (direction == 'F'){
          exit = true;
          control.finalizou();
        }

        else{

          if(control.isValidMove(row, column, numberOfPlayers, direction)){
            output.writeUTF("Valid move");
            output.writeBoolean(control.markAsClosed(row, column, direction));
          }
          else
            output.writeUTF("Invalid move");
        }

        if(control.gameOver()) exit = true;

      }

    }

    catch (IOException e){

    }

  }
  public void sendGameOver(){
    try{
      output.writeUTF("Game Over");
      connection.close();
    }
    catch (IOException e){
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void sendGameHasFinished(){
    try{
      output.writeUTF("Finished");
      connection.close();
    }
    catch (IOException e){

    }
  }

  public int getBoardSize(){
    boolean flag = true;
    int boardSize = 0;

    try{
      output.writeUTF("Board size");
    }

    catch(IOException e){
        e.printStackTrace();
    }

    while (flag){
      try{
        boardSize = input.readInt();
        flag = false;
      }
      catch(IOException e){

      }
    }

    return boardSize;
  }

  public void setBoardSize(int boardSize){
     try{
        output.writeUTF("Get board size");
        output.writeInt(boardSize);
     }
     catch(IOException e){

     }

  }

}