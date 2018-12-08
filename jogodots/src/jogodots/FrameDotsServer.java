package jogodots;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.io.*;

public class FrameDotsServer extends JFrame implements Runnable{

   private Player jogadores[];
   private ServerSocket servidor;
   private int jogadorCorrente;
   private Board tabuleiro;
   private int linha, coluna;

   private Thread t1;

  JPanel contentPane;
  TitledBorder titledBorder1;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jpSul = new JPanel();
  JButton jbDesconectar = new JButton();
  JButton jbConectar = new JButton();
  JPanel jpNorte = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  TitledBorder titledBorder2;
  JPanel jpCenter = new JPanel();
  TitledBorder titledBorder3;
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel jpNomes = new JPanel();
  JPanel jpStatus = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JLabel jlBranco = new JLabel();
  JLabel jlBranco2 = new JLabel();
  JLabel jlJogador2 = new JLabel();
  JLabel jlJogador1 = new JLabel();
  JLabel jlServidor = new JLabel();
  GridLayout gridLayout2 = new GridLayout();
  JLabel jlBranco3 = new JLabel();
  JLabel jlBranco4 = new JLabel();
  JLabel jlStatus2 = new JLabel();
  JLabel jlStatus1 = new JLabel();
  JLabel jlStatus = new JLabel();
  JButton jbSair = new JButton();

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

    jogadores = new Player[2];
    jogadorCorrente = 0;

    contentPane = (JPanel) this.getContentPane();
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(310, 200));
    this.setTitle("Servidor Dots!");

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

    contentPane.setBorder(titledBorder1);
    contentPane.setPreferredSize(new Dimension(200, 61));
    jbDesconectar.setText("Desconectar");
    jbDesconectar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbDesconectar_actionPerformed(e);
      }
    });
    jbDesconectar.setEnabled(false);
    jbConectar.setText("Conectar");
    jbConectar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbConectar_actionPerformed(e);
      }
    });
    jpSul.setLayout(flowLayout1);
    jpNorte.setLayout(borderLayout2);
    jpCenter.setBorder(titledBorder3);
    jpCenter.setLayout(borderLayout3);
    jpSul.setBorder(titledBorder1);
    jpNomes.setLayout(gridLayout1);
    gridLayout1.setRows(5);
    jlJogador2.setFont(new java.awt.Font("Dialog", 1, 12));
    jlJogador2.setText("Jogador 2: ");
    jlJogador1.setFont(new java.awt.Font("Dialog", 1, 12));
    jlJogador1.setText("Jogador 1: ");
    jlServidor.setFont(new java.awt.Font("Dialog", 1, 12));
    jlServidor.setText("Servidor:        ");
    jpStatus.setLayout(gridLayout2);
    gridLayout2.setRows(5);
    jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
    jlStatus2.setForeground(Color.red);
    jlStatus2.setText("desconectado...");
    jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
    jlStatus1.setForeground(Color.red);
    jlStatus1.setText("desconectado...");
    jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
    jlStatus.setForeground(Color.red);
    jlStatus.setText("desconectado...");
    jbSair.setText("Sair");
    jbSair.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSair_actionPerformed(e);
      }
    });
    jpCenter.add(jpNomes,  BorderLayout.WEST);
    jpCenter.add(jpStatus, BorderLayout.CENTER);
    jpSul.add(jbConectar, null);
    contentPane.add(jpSul, BorderLayout.SOUTH);
    jpSul.add(jbDesconectar, null);
    jpSul.add(jbSair, null);
    contentPane.add(jpNorte, BorderLayout.NORTH);
    contentPane.add(jpCenter, BorderLayout.CENTER);
    jpNomes.add(jlBranco, null);
    jpNomes.add(jlServidor, null);
    jpNomes.add(jlJogador1, null);
    jpNomes.add(jlJogador2, null);
    jpNomes.add(jlBranco2, null);
    jpStatus.add(jlBranco3, null);
    jpStatus.add(jlStatus, null);
    jpStatus.add(jlStatus1, null);
    jpStatus.add(jlStatus2, null);
    jpStatus.add(jlBranco4, null);
    show();

  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void jbConectar_actionPerformed(ActionEvent e) {

      //configura ServerSocket
      try{
        servidor = new ServerSocket(5000, 2);
      }
      catch (IOException e1){
         JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
         System.exit(1);
      }

      jlStatus.setText("conectado, esperando conexões...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jbDesconectar.setEnabled(true);
      jbConectar.setEnabled(false);

      jbDesconectar.requestFocus();

       t1 = new Thread(this);
       t1.start();

  }

   public void run(){

    for(int i = 0; i < jogadores.length; i++){

      try{

        jogadores[i] = new Player(servidor.accept(), this, i);
        jogadores[i].start();

        if (i == 0){
          coluna = linha = jogadores[0].getBoardSize();
          tabuleiro = new Board(linha, coluna);
        }

        else{

          jogadores[1].setBoardSize(linha);
        }

        display(i);
      }

      catch (Exception e){

      }
    }

    // o jogador 1 será suspenso até que o jogador 2 conecte
    //O jodagor 1 reassume agora.
    try{

      synchronized (jogadores[0]){
        jogadores[0].threadSuspended = false;
        jogadores[0].notify();

      }
    }
    catch (Exception e){

    }

  }

  public void restartServidor(){

    //desconecta ServerSocket
      try{
        servidor.close();
        jogadorCorrente = 0;
        tabuleiro.initBoard();
      }
      catch (Exception e){

      }

       //configura ServerSocket
      try{
        servidor = new ServerSocket(5000, 2);
      }
      catch (IOException e1){
         JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
         System.exit(1);
      }


      jlStatus.setText("conectado, esperando conexões...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.red);
      jlStatus1.setText("desconectado...");

      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.red);
      jlStatus2.setText("desconectado...");

      jbDesconectar.setEnabled(true);
      jbConectar.setEnabled(false);

      jbDesconectar.requestFocus();

      t1 = new Thread(this);
      t1.start();

  }

   public void display(int numJogador){

    if (numJogador == 0){
      jlStatus.setText("conectado, esperando conexão...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jlStatus1.setText("conectado, esperando adversário ...");
      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.blue);

      jlStatus2.setText("desconectado ...");
      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.red);
    }

    else{

      jlStatus.setText("conectado, jogo em execução...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.blue);

      jlStatus1.setText("conectado ...");
      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.blue);

      jlStatus2.setText("conectado ...");
      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.blue);
    }

  }

  //Determina se um movimento é válido.
  public synchronized boolean isValidMove(int linha, int coluna, int jogador,
                                              char matriz){

    while (jogador != jogadorCorrente){

      try{
        wait();
      }
      catch(InterruptedException e){
        e.printStackTrace();
      }

    }

    if (!(ocupado(linha, coluna, matriz))){

      if (matriz == 'H') {

        tabuleiro.setHorizontalDash(linha, coluna);

        if(!(tabuleiro.markDash(linha, coluna, jogadorCorrente,'H'))){
          jogadorCorrente = (jogadorCorrente + 1) % 2;
          jogadores[jogadorCorrente].opponentMove(linha, coluna, matriz);
          notify();
        }

        else{
            int jogadorAdversario = (jogadorCorrente + 1) % 2;
            jogadores[jogadorAdversario].opponentMove(linha, coluna, matriz);
        }
      }

      else {

        tabuleiro.setVerticalDash(linha, coluna);

        if(!(tabuleiro.markDash(linha, coluna, jogadorCorrente,'V'))){
          jogadorCorrente = (jogadorCorrente + 1) % 2;
          jogadores[jogadorCorrente].opponentMove(linha, coluna, matriz);
          notify();
       }

       else{
           int jogadorAdversario = (jogadorCorrente + 1) % 2;
           jogadores[jogadorAdversario].opponentMove(linha, coluna, matriz);
       }

      }
      return true;
    }

    else  return false;

  }

  public boolean ocupado(int linha, int coluna, char matriz){
    if (matriz == 'H') return tabuleiro.getHorizontalDash(linha, coluna);
    else return tabuleiro.getVerticalDash(linha, coluna);
  }

   public boolean markAsClosed(int linha, int coluna, char matriz){
      return tabuleiro.isClosed(linha, coluna, matriz);
  }

  public boolean gameOver(){

      if (tabuleiro.isBoardCompleted()){
        for (int i = 0; i < 2; i++)
           jogadores[i].sendGameOver();

        restartServidor();
        return true;
      }
      return false;

  }

  public void finalizou(){

    for (int i = 0; i < 2; i++){

      try{
        jogadores[i].sendGameHasFinished();
      }
      catch(Exception e){
      }

    }
    restartServidor();

  }


  void jbDesconectar_actionPerformed(ActionEvent e) {

    //desconecta ServerSocket
      try{
          finalizou();
          servidor.close();
      }

      catch (IOException e1){

      }

      jlStatus.setText("desconectado...");
      jlStatus.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus.setForeground(Color.red);

      jlStatus1.setText("desconectado...");
      jlStatus1.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus1.setForeground(Color.red);

      jlStatus2.setText("desconectado...");
      jlStatus2.setFont(new java.awt.Font("Dialog", 1, 12));
      jlStatus2.setForeground(Color.red);

      jbDesconectar.setEnabled(false);
      jbConectar.setEnabled(true);
      jbConectar.requestFocus();

  }

  void jbSair_actionPerformed(ActionEvent e) {

      this.dispose();
      System.exit(0);
  }

   public static void main(String[] args) {

       try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       }
       catch(Exception e) {
        e.printStackTrace();
       }
       new FrameDotsServer();
  }
}