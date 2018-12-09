package jogodots;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Controller implements Runnable{

  private Board board;
  private FrameGame frameGame;

  private Socket connection;
  private DataInputStream input;
  private DataOutputStream output;
  private Thread outputThread;
  private int player;
  private boolean myTurn;

  int rows;
  int columns;

  public Controller() {

    boolean running = true;

    while (running){

        try {

            String serverIP = JOptionPane.showInputDialog(null, "Server IP: ",
	      	              "Jogo Dots!",JOptionPane.QUESTION_MESSAGE);
            if (serverIP != null){

                connection = new Socket(InetAddress.getByName(serverIP), 5000 );
                input = new DataInputStream(connection.getInputStream() );
                output = new DataOutputStream(connection.getOutputStream() );
                running = false;

                frameGame = new FrameGame(0, 0, this);
            }

            else System.exit(0);

        }

        catch ( IOException e ) {

              if (e.toString().equals("java.net.ConnectException: Connection refused: connect"))
                    JOptionPane.showMessageDialog(null, "Server disconnected!","Jogo Dots!",
	                                JOptionPane.INFORMATION_MESSAGE);

             else JOptionPane.showMessageDialog(null, "Server IP is not correct!","Jogo Dots!",
	                                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    outputThread = new Thread( this );
    outputThread.start();
  }

  // Return if it is a horizontal dash
  public boolean isHorizontalDash(int row, int column){
    return board.getHorizontalDash(row, column);
  }

  // Return if it is a vertical dash
  public boolean isVerticalDash(int row, int column){
    return board.getVerticalDash(row, column);
  }

  // Return the marker of a cell
  public int getMarkers(int row, int columns){
    return board.getCellPlayer(row, columns);
  }

  // Mark a horizontal dash
  public void markHorizontalDash(int row, int column){

    if(!(board.getHorizontalDash(row, column))){
       board.setHorizontalDash(row, column);
       board.markDash(row, column, player,'H');
    }
  }

  // Mark a vertical dash
  public void markVerticalDash(int row, int column){
    if (!(board.getVerticalDash(row, column))){
      board.setVerticalDash(row, column);
      board.markDash(row, column, player,'V');
    }
  }

  // Update cell board
  public void setCellPlayer(int row, int column, int player){
    board.setCellPlayer(row, column, player);
  }

  // Return if the booad is completed
  public boolean isBoardCompleted(){
     return board.isBoardCompleted();
  }

  // Return the number of points of the player
  public int getNumberOfPoints(int player){
     return board.getNumberOfPoints(player);
  }

  // run method
  public void run(){
    String message;
    boolean running = true;

    try{
        message = input.readUTF();
        doMessage(message);

       player = input.readInt();
       frameGame.setTitle("Jogo Dots! - Player " + (player + 1));
       myTurn = (player == 0 ? true : false);
       frameGame.updateLabel("You are the player " + player);


    }

    catch(IOException e){
        JOptionPane.showMessageDialog(null, "Connection error!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
        System.exit(1);
    }

    // Receive messages sent by the clients
    while(running){

      try{
        message = input.readUTF();

        if(message.equals("Game Over")) running = false;

        doMessage(message);
      }

      catch(IOException e){
       JOptionPane.showMessageDialog(null, "Connection error!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
       System.exit(1);
      }

    }
  }

  // Process the messages recived by the clients.
  public void doMessage(String message){

    if (message.equals("Board size")){

      boolean running = true;

      while(running){

        try{

          String size = JOptionPane.showInputDialog(null, "Board size (2 to 15): ",
	  	       "Jogo Dots!",JOptionPane.QUESTION_MESSAGE);

          // Size that the player has chosen
          if (size != null) rows = columns = Integer.parseInt(size);

          // The size is 5 if the player does not type the size of the board
          else rows = columns = 5;

          if ((rows < 2) || (rows > 15)){
           running = true;
           JOptionPane.showMessageDialog(null, "Invalid board size!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);

          }
          else {
            output.writeInt(rows);
            board = new Board(rows, columns);
            frameGame.setBoardSize(rows);
            running = false;
          }
        }

        catch(IOException e){
          JOptionPane.showMessageDialog(null, "Connection error!","Jogo Dots!",
	                              JOptionPane.INFORMATION_MESSAGE);
          System.exit(1);
        }

        catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null, "Invalid size number!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);

          running = true;
        }

      }
    }

    if (message.equals("Get board size")){

      boolean flag = true;

      while (flag){
        try{
          rows = columns = input.readInt();

          board = new Board(rows, columns);
          frameGame.setBoardSize(rows);
          flag = false;
        }
        catch(IOException e){

        }
      }

    }

    if (message.equals("Valid move")){

      try{
        boolean closedNewCell = input.readBoolean();

        if (closedNewCell){
         myTurn = true;
         frameGame.updateLabel("Closed new square. Play again!");
        }

        else{
           frameGame.updateLabel("Valid move, please wait!");
           myTurn = false;
        }

      }

      catch(IOException e){
       JOptionPane.showMessageDialog(null, "Connection error!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
       System.exit(1);
      }

      frameGame.updateScreen();
    }

    else if (message.equals("Invalid move")){
            myTurn = true;
         }

         else if (message.equals("Opponent move")){

                 try{

                   int opponent = (player == 0 ? 1 : 0);
                   char direction = input.readChar();
                   int row = input.readInt();
                   int column = input.readInt();

                   if (direction == 'H'){
                      board.setHorizontalDash(row, column);

                      if (board.markDash(row, column, opponent, 'H')){
                        myTurn = false;
                        frameGame.updateLabel("Opponent turn!");
                      }

                      else{
                       myTurn = true;
                       frameGame.updateLabel("Your turn!");
                      }
                   }

                   else{
                      board.setVerticalDash(row, column);

                      if (board.markDash(row, column, opponent, 'V')){
                        myTurn = false;
                        frameGame.updateLabel("Opponent turn!");
                      }

                      else{
                        myTurn = true;
                        frameGame.updateLabel("Your turn!");
                      }
                   }

                   frameGame.updateScreen();
                }

                catch(IOException e){
                  e.printStackTrace();
                }
         }

         else if (message.equals("Game Over")){

            int player1NumberOfPoints = board.getNumberOfPoints(1);
            int player2NumberOfPoints = board.getNumberOfPoints(2);
            String winner = "", output = "";

            if(player1NumberOfPoints > player2NumberOfPoints) winner = "Winner: Player 1";
            else if(player1NumberOfPoints < player2NumberOfPoints) winner = "Winner: Player 2";
            else winner = "Draw";

            output += " Final:\n\n Player 1: " + player1NumberOfPoints + " points\n" +
                      " Player 2: " + player2NumberOfPoints + " points\n\n " + winner;

            JOptionPane.showMessageDialog(null, output,"Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);

            frameGame.dispose();
            System.exit(0);

         }
         else if (message.equals("Waiting for the next player")){
            myTurn = false;
            frameGame.updateLabel(message);
         }
         else if (message.equals("Finished")){
            JOptionPane.showMessageDialog(null, "The game has finished!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
            frameGame.dispose();
            System.exit(0);
         }
         else if (message.equals("Other player has connected. Your turn.")){
            myTurn = true;
            frameGame.updateLabel(message);
         }
         else frameGame.updateLabel(message);

  }

  // Send message to the other player
  public void setOutput(int row, int column, char direction){

    if (myTurn){

      try{
        output.writeChar(direction);
        output.writeInt(row);
        output.writeInt(column);
        output.flush();
        myTurn = false;
      }

      catch(IOException e){
       JOptionPane.showMessageDialog(null, "Connection error!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
       System.exit(1);
      }

    }
  }


  // Return if it is my turn
  public boolean isMyTurn(){
    return myTurn;
  }

  public static void main(String[] args) {

    Controller c = new Controller();
  }
}