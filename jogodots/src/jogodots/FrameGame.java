package jogodots;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrameGame extends JFrame {

  int rows, columns;
  Controller controlador;

  JPanel contentPane;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jpSul = new JPanel();
  JPanel jpMensagens = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JLabel jlMensagens = new JLabel();
  JPanel jpBotoes = new JPanel();
  JButton jbSair = new JButton();
  JButton jbAjuda = new JButton();
  JPanel jpPontos = new JPanel(){
    public void paint(Graphics g){
      super.paint(g);
      desenha(g);
    }
  };

  //Construct the frame
  public FrameGame(int rows, int columns, Controller controller) {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.rows = rows;
      this.columns = columns;
      this.controlador = controller;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {

    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(400, 400));

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


    this.setTitle("Jogo Dots!");
    jpSul.setLayout(gridLayout1);
    gridLayout1.setRows(2);
    jlMensagens.setText("");
    jbSair.setText("Finalizar");
    jbSair.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSair_actionPerformed(e);
      }
    });
    jbAjuda.setText("Ajuda");
    contentPane.add(jpSul, BorderLayout.SOUTH);
    jpSul.add(jpMensagens, null);
    jpMensagens.add(jlMensagens, null);
    jpSul.add(jpBotoes, null);
    jpBotoes.add(jbSair, null);
    jpBotoes.add(jbAjuda, null);
    contentPane.add(jpPontos, BorderLayout.CENTER);

    jpPontos.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jpPontos_mouseClicked(e);
      }
    });

    jpPontos.repaint();
    show();

  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }


  //Eventos do MOUSE
  void jpPontos_mouseClicked(MouseEvent e) {

   if (controlador.isMyTurn()){

    Dot p = avaliaCoordenadas(e.getX(), e.getY());

    Dimension d = jpPontos.getSize();
    double altura = d.getHeight()/(rows +2);
    double largura = d.getWidth()/(columns +2);

    //Testa se a posição clicada está dentro dos limites do jogo
    if ((e.getX() < largura) || (e.getX() > (d.getWidth()-largura)) ||
        (e.getY() < altura) || (e.getY() > (d.getHeight()-altura))){}

    else{

      //Analisando a diferença + importante
      int deltaX = e.getX()-p.getX();
      int deltaY = e.getY()-p.getY();
      int linhaTabela = e.getY()/((int)altura);
      int colunaTabela = e.getX()/((int)largura);

      //Significa que a linha deve ser desenhada com o ponto
      //a esquerda, acima ou abaixo
      if (deltaX < 0){

        //Significa que a linha deve ser desenhada com o ponto
        //a esquerda ou acima
        if (deltaY < 0){

          if ((-deltaX) > (-deltaY)) {//Linha H a esq
            controlador.markHorizontalDash(linhaTabela, colunaTabela - 1);
            controlador.setOutput(linhaTabela, colunaTabela - 1, 'H');
          }
          else{ //Linha V acima
            controlador.markVerticalDash(linhaTabela - 1, colunaTabela);
            controlador.setOutput(linhaTabela - 1, colunaTabela, 'V');
          }
        }
        //Significa que a linha deve ser desenhada com o ponto
        //a esquerda ou abaixo
        else {
          if ((-deltaX) > deltaY) {//Linha H esq
            controlador.markHorizontalDash(linhaTabela - 1, colunaTabela - 1);
            controlador.setOutput(linhaTabela - 1, colunaTabela - 1, 'H');
          }
          else {//Linha V abaixo
            controlador.markVerticalDash(linhaTabela - 1, colunaTabela);
            controlador.setOutput(linhaTabela - 1, colunaTabela, 'V');
          }
        }
      }

      //Significa que a linha deve ser desenhada com o ponto
      //a direita, acima ou abaixo -- deltaX positivo
      else{

        //Significa que a linha deve ser desenhada com o ponto
        //a direita ou acima
        if (deltaY < 0){
          if (deltaX > (-deltaY)){// Linha H a dir
            controlador.markHorizontalDash(linhaTabela, colunaTabela - 1);
            controlador.setOutput(linhaTabela, colunaTabela - 1, 'H');
          }
          else { //Linha V acima
            controlador.markVerticalDash(linhaTabela - 1, colunaTabela - 1);
            controlador.setOutput(linhaTabela - 1, colunaTabela - 1, 'V');

          }
        }
        //Significa que a linha deve ser desenhada com o ponto
        //a direita ou abaixo
        else {
          if (deltaX > deltaY) {// Linha H a dir
            controlador.markHorizontalDash(linhaTabela - 1, colunaTabela - 1);
            controlador.setOutput(linhaTabela - 1, colunaTabela - 1, 'H');
          }
          else {//Linha V abaixo
            controlador.markVerticalDash(linhaTabela - 1, colunaTabela-1);
            controlador.setOutput(linhaTabela - 1, colunaTabela - 1, 'V');
          }
        }
      }

      jpPontos.repaint();
    }
   }
  }



  void desenha(Graphics g){

    Dimension d = jpPontos.getSize();
    double altura = d.getHeight()/(rows +2);
    double largura = d.getWidth()/(columns +2);
    int espVert = (int)altura/5;
    int espHoriz = (int)largura/5;


    //Desenha os pontos
    for (double i = largura; i <= ((columns +1)*largura) + (largura/10); i += largura)
      for (double j = altura; j <= ((rows +1)*altura) + (altura/10); j += altura)
        g.fillOval((int)i, (int)j, 5, 5);

    //Desenha as rows horizontais
    for (double i = largura; i <= columns *largura; i += largura)
      for (double j = altura; j <= (rows +1)*altura; j += altura)
        if (controlador.isHorizontalDash(((int)j/((int)altura))-1, ((int)i/((int)largura))-1))
          g.drawLine((int)i, (int)(j+2), (int)(i+largura), (int)(j+2));


    //Desenha as rows verticais
    for (double i = largura; i <= (columns +1)*largura; i += largura)
      for (double j = altura; j <= rows *altura; j += altura)
        if (controlador.isVerticalDash(((int)j/((int)altura))-1, ((int)i/((int)largura))-1))
          g.drawLine((int)i+2, (int)j, (int)(i+2), (int)(j+altura));


    //Desenha os marcadores
    for (double i = largura; i <= columns *largura; i += largura)
      for (double j = altura; j <= rows *altura; j += altura)
        if (controlador.getMarkers(((int)j/((int)altura))-1, ((int)i/((int)largura))-1) == 1){
          g.setColor(Color.red);
          g.drawOval((int)(i + espHoriz), (int)(j + espVert), ((int)largura) - (espHoriz + espHoriz/3), ((int)altura) - (espVert + espVert/3));
        }
        else if (controlador.getMarkers(((int)j/((int)altura))-1, ((int)i/((int)largura))-1) == 2){
          g.setColor(Color.blue);
          g.drawOval((int)(i + espHoriz), (int)(j + espVert), ((int)largura) - (espHoriz + espHoriz/3), ((int)altura) - (espVert + espVert/3));
        }
  }

  Dot avaliaCoordenadas(int x, int y){

    Dimension d = jpPontos.getSize();
    double altura = d.getHeight()/(rows +2);
    double largura = d.getWidth()/(columns +2);
    double rx, ry;  //restos da divisão
    int dx, dy;     //parte inteira da divisão
    Dot p = new Dot();

    rx = ((double) x) % largura;
    ry = ((double) y) % altura;

    dx = (int)((double) x / largura);
    dy = (int)((double)y / altura);

    if ((rx >= largura / 2.0) && (dx <= rows)) p.setX((dx+1)*((int)largura));
    else  p.setX(dx*((int)largura));

    if ((ry >= altura / 2.0) && (dy <= columns)) p.setY((dy+1)*((int)altura));
    else p.setY(dy*((int)altura));

    return p;
  }

  void jbSair_actionPerformed(ActionEvent e) {
       controlador.setOutput(-1,-1,'F');
  }

  public void updateLabel(String s){
    jlMensagens.setText(s);
  }

  public void updateScreen(){
    jpPontos.repaint();
  }
  public void setBoardSize(int boardSize){
    rows = columns = boardSize;
    updateScreen();
  }
}