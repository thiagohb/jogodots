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
       frameGame.atualizaLabel("You are the player " + player);


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

  //Processa as mensagens recebidas pelo cliente
  public void doMessage(String message){

    if (message.equals("Board size")){

      boolean running = true;

      while(running){

        try{

          String size = JOptionPane.showInputDialog(null, "Board size (2 to 15): ",
	  	       "Jogo Dots!",JOptionPane.QUESTION_MESSAGE);

          // Size that the player has chosen
          if (size != null) rows = columns = Integer.parseInt(size);

          //Se o jogador não escolher um tamanho, este terá um tamanho padrão igual a 8
          else rows = columns = 5;

          if ((rows < 2) || (rows > 15)){
           running = true;
           JOptionPane.showMessageDialog(null, "Tamanho inválido!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);

          }
          else {
            output.writeInt(rows);
            board = new Board(rows, columns);
            frameGame.setTamanho(rows);
            running = false;
          }
        }

        catch(IOException e){
          JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                              JOptionPane.INFORMATION_MESSAGE);
          System.exit(1);
        }

        catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null, "Formato de número inválido!","Jogo Dots!",
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
          frameGame.setTamanho(rows);
          flag = false;
        }
        catch(IOException e){

        }
      }

    }

    if (message.equals("Valid move")){

      try{
        boolean fechou = input.readBoolean();

        if (fechou){
         myTurn = true;
         frameGame.atualizaLabel("Fechou quadrado, jogue outra vez!");
        }

        else{
           frameGame.atualizaLabel("Movimento válido, por favor espere!");
           myTurn = false;
        }

      }

      catch(IOException e){
       JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
       System.exit(1);
      }

      frameGame.atualizaTela();
    }

    else if (message.equals("Invalid move")){
            myTurn = true;
         }

         else if (message.equals("Jogada do Adversário")){

                 try{

                   int adversario = (player == 0 ? 1 : 0);
                   char matriz = input.readChar();
                   int linha = input.readInt();
                   int coluna = input.readInt();

                   if (matriz == 'H'){
                      board.setHorizontalDash(linha, coluna);

                      if (board.markDash(linha, coluna, adversario, 'H')){
                        myTurn = false;
                        frameGame.atualizaLabel("Jogada do Adversário!");
                      }

                      else{
                       myTurn = true;
                       frameGame.atualizaLabel("Sua Vez!");
                      }
                   }

                   else{
                      board.setVerticalDash(linha, coluna);

                      if (board.markDash(linha, coluna, adversario, 'V')){
                        myTurn = false;
                        frameGame.atualizaLabel("Jogada do Adversário!");
                      }

                      else{
                        myTurn = true;
                        frameGame.atualizaLabel("Sua Vez!");
                      }
                   }

                   frameGame.atualizaTela();
                }

                catch(IOException e){
                  e.printStackTrace();
                }
         }

         else if (message.equals("Game Over")){

            int pontos1 = board.getNumberOfPoints(1);
            int pontos2 = board.getNumberOfPoints(2);
            String vencedor = "", output = "";

            if(pontos1 > pontos2) vencedor = "Vencedor: Jogador 1";
            else if(pontos1 < pontos2) vencedor = "Vencedor: Jogador 2";
            else vencedor = "Empate";

            output += " Placar Final:\n\n Jogador 1: " + pontos1 + " pontos\n" +
                      " Jogador 2: " + pontos2 + " pontos\n\n " + vencedor;

            JOptionPane.showMessageDialog(null, output,"Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);

            frameGame.dispose();
            System.exit(0);

         }
         else if (message.equals("Waiting for the next player")){
            myTurn = false;
            frameGame.atualizaLabel(message);
         }
         else if (message.equals("Finished")){
            JOptionPane.showMessageDialog(null, "O jogo será finalizado!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
            frameGame.dispose();
            System.exit(0);
         }
         else if (message.equals("Other player connected. Your turn.")){
            myTurn = true;
            frameGame.atualizaLabel(message);
         }
         else frameGame.atualizaLabel(message);

  }

  //Manda para o jogador a jogada atual
  public void setOutput(int linha, int coluna, char matriz){

    if (myTurn){

      try{
        output.writeChar(matriz);
        output.writeInt(linha);
        output.writeInt(coluna);
        output.flush();
        myTurn = false;
      }

      catch(IOException e){
       JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a leitura!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
       System.exit(1);
      }

    }
  }


  //Retorna se é a vez do jogador
  public boolean getMinhaVez(){
    return myTurn;
  }

  public static void main(String[] args) {

    Controller c = new Controller();
  }
}