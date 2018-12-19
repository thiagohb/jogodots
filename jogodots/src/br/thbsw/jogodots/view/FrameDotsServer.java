package br.thbsw.jogodots.view;

import br.thbsw.jogodots.controller.Player;
import br.thbsw.jogodots.model.Board;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.io.*;

public class FrameDotsServer extends JFrame implements Runnable{

   private Player players[];
   private ServerSocket server;
   private int currentPlayer;
   private Board board;
   private int row, column;

   private Thread thread;

  private JPanel jpContent;
  private TitledBorder titledBorder1;
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel jpSouth = new JPanel();
  private JButton jbDisconnect = new JButton();
  private JButton jbConnect = new JButton();
  private JPanel jpNorth = new JPanel();
  private BorderLayout borderLayout2 = new BorderLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private TitledBorder titledBorder2;
  private JPanel jpCenter = new JPanel();
  private TitledBorder titledBorder3;
  private BorderLayout borderLayout3 = new BorderLayout();
  private JPanel jpNames = new JPanel();
  private JPanel jpStatus = new JPanel();
  private GridLayout gridLayout1 = new GridLayout();
  private JLabel jlEmpty1 = new JLabel();
  private JLabel jlEmpty2 = new JLabel();
  private JLabel jlPlayer2 = new JLabel();
  private JLabel jlPlayer1 = new JLabel();
  private JLabel jlServer = new JLabel();
  private GridLayout gridLayout2 = new GridLayout();
  private JLabel jlEmpty3 = new JLabel();
  private JLabel jlEmpty4 = new JLabel();
  private JLabel jlStatus2 = new JLabel();
  private JLabel jlStatus1 = new JLabel();
  private JLabel jlStatus = new JLabel();
  private JButton jbExit = new JButton();

  //Construct the frame
  public FrameDotsServer() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {

    players = new Player[2];
    currentPlayer = 0;

    jpContent = (JPanel) this.getContentPane();
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    jpContent.setLayout(borderLayout1);
    this.setSize(new Dimension(310, 200));
    this.setTitle(Messages.TITLE);

     //Centraliza a janela
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    this.setVisible(true);

    jpContent.setBorder(titledBorder1);
    jpContent.setPreferredSize(new Dimension(200, 61));
    jbDisconnect.setText("Disconnect");
    jbDisconnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbDisconnect_actionPerformed(e);
      }
    });
    jbDisconnect.setEnabled(false);
    jbConnect.setText("Connect");
    jbConnect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbConnect_actionPerformed(e);
      }
    });
    jpSouth.setLayout(flowLayout1);
    jpNorth.setLayout(borderLayout2);
    jpCenter.setBorder(titledBorder3);
    jpCenter.setLayout(borderLayout3);
    jpSouth.setBorder(titledBorder1);
    jpNames.setLayout(gridLayout1);
    gridLayout1.setRows(5);
    jlPlayer2.setFont(new java.awt.Font("Dialog", 1, 12));
    jlPlayer2.setText("Player 2: ");
    jlPlayer1.setFont(new java.awt.Font("Dialog", 1, 12));
    jlPlayer1.setText("Player 1: ");
    jlServer.setFont(new java.awt.Font("Dialog", 1, 12));
    jlServer.setText("Server:        ");
    jpStatus.setLayout(gridLayout2);
    gridLayout2.setRows(5);
    jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
    jlStatus2.setForeground(Color.red);
    jlStatus2.setText("disconnected.");
    jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
    jlStatus1.setForeground(Color.red);
    jlStatus1.setText("disconnected.");
    jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
    jlStatus.setForeground(Color.red);
    jlStatus.setText("disconnected.");
    jbExit.setText("Exit");
    jbExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbExit_actionPerformed(e);
      }
    });
    jpCenter.add(jpNames,  BorderLayout.WEST);
    jpCenter.add(jpStatus, BorderLayout.CENTER);
    jpSouth.add(jbConnect, null);
    jpContent.add(jpSouth, BorderLayout.SOUTH);
    jpSouth.add(jbDisconnect, null);
    jpSouth.add(jbExit, null);
    jpContent.add(jpNorth, BorderLayout.NORTH);
    jpContent.add(jpCenter, BorderLayout.CENTER);
    jpNames.add(jlEmpty1, null);
    jpNames.add(jlServer, null);
    jpNames.add(jlPlayer1, null);
    jpNames.add(jlPlayer2, null);
    jpNames.add(jlEmpty2, null);
    jpStatus.add(jlEmpty3, null);
    jpStatus.add(jlStatus, null);
    jpStatus.add(jlStatus1, null);
    jpStatus.add(jlStatus2, null);
    jpStatus.add(jlEmpty4, null);
    show();

  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void jbConnect_actionPerformed(ActionEvent e) {

      // ServerSocket
      try{
        server = new ServerSocket(5000, 2);
      }
      catch (IOException e1){
         JOptionPane.showMessageDialog(null, Messages.MSG_CONNECTION_ERROR, Messages.TITLE,
	                            JOptionPane.INFORMATION_MESSAGE);
         System.exit(1);
      }

      jlStatus.setText("connected, waiting for connections...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jbDisconnect.setEnabled(true);
      jbConnect.setEnabled(false);

      jbDisconnect.requestFocus();

       thread = new Thread(this);
       thread.start();

  }

   public void run(){

    for(int i = 0; i < players.length; i++){

      try{

        players[i] = new Player(server.accept(), this, i);
        players[i].start();

        if (i == 0){
          column = row = players[0].getBoardSize();
          board = new Board(row, column);
        }

        else{

          players[1].setBoardSize(row);
        }

        display(i);
      }

      catch (Exception e){

      }
    }

    // player 1 is suspended until the player 2 get connected
    try{

      synchronized (players[0]){
        players[0].threadSuspended = false;
        players[0].notify();

      }
    }
    catch (Exception e){

    }

  }

  public void restartServer(){

    // Disconnect ServerSocket
      try{
        server.close();
        currentPlayer = 0;
        board.initBoard();
      }
      catch (Exception e){

      }

       // ServerSocket
      try{
        server = new ServerSocket(5000, 2);
      }
      catch (IOException e1){
         JOptionPane.showMessageDialog(null, Messages.MSG_CONNECTION_ERROR, Messages.TITLE,
	                            JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }


      jlStatus.setText("connected, waiting for connections...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.red);
      jlStatus1.setText("disconnected.");

      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.red);
      jlStatus2.setText("disconnected.");

      jbDisconnect.setEnabled(true);
      jbConnect.setEnabled(false);

      jbDisconnect.requestFocus();

      thread = new Thread(this);
      thread.start();

  }

   public void display(int numJogador){

    if (numJogador == 0){
      jlStatus.setText("connected, waiting for connections...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jlStatus1.setText("connected, waiting for opponent...");
      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.blue);

      jlStatus2.setText("disconnected.");
      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.red);
    }

    else{

      jlStatus.setText("connected, playing...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jlStatus1.setText("connected.");
      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.blue);

      jlStatus2.setText("connected.");
      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.blue);
    }

  }

  // Is valid move
  public synchronized boolean isValidMove(int row, int column, int player,
                                              char direction){

    while (player != currentPlayer){

      try{
        wait();
      }
      catch(InterruptedException e){
        e.printStackTrace();
      }

    }

    if (!(isDashed(row, column, direction))){

      if (direction == 'H') {

        board.setHorizontalDash(row, column);

        if(!(board.markDash(row, column, currentPlayer,'H'))){
          currentPlayer = (currentPlayer + 1) % 2;
          players[currentPlayer].opponentMove(row, column, direction);
          notify();
        }

        else{
            int opponentPlayer = (currentPlayer + 1) % 2;
            players[opponentPlayer].opponentMove(row, column, direction);
        }
      }

      else {

        board.setVerticalDash(row, column);

        if(!(board.markDash(row, column, currentPlayer,'V'))){
          currentPlayer = (currentPlayer + 1) % 2;
          players[currentPlayer].opponentMove(row, column, direction);
          notify();
       }

       else{
           int opponentPlayer = (currentPlayer + 1) % 2;
           players[opponentPlayer].opponentMove(row, column, direction);
       }

      }
      return true;
    }

    else  return false;

  }

  public boolean isDashed(int row, int column, char direction){
    if (direction == 'H') return board.getHorizontalDash(row, column);
    else return board.getVerticalDash(row, column);
  }

   public boolean markAsClosed(int row, int column, char direction){
      return board.isClosed(row, column, direction);
  }

  public boolean gameOver(){

      if (board.isBoardCompleted()){
        for (int i = 0; i < 2; i++)
           players[i].sendGameOver();

        restartServer();
        return true;
      }
      return false;

  }

  public void finish(){

    for (int i = 0; i < 2; i++){

      try{
        players[i].sendGameHasFinished();
      }
      catch(Exception e){
      }

    }
    restartServer();

  }


  void jbDisconnect_actionPerformed(ActionEvent e) {

    // Close ServerSocket
      try{
          finish();
          server.close();
      }

      catch (IOException e1){

      }

      jlStatus.setText("disconnected.");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.red);

      jlStatus1.setText("disconnected.");
      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.red);

      jlStatus2.setText("disconnected.");
      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.red);

      jbDisconnect.setEnabled(false);
      jbConnect.setEnabled(true);
      jbConnect.requestFocus();

  }

  void jbExit_actionPerformed(ActionEvent e) {

      this.dispose();
      System.exit(0);
  }

}