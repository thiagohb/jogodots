package jogodots;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Controlador implements Runnable{

  private Tabuleiro tabuleiro;
  private FrameJogo jogo;

  private Socket conexao;
  private DataInputStream input;
  private DataOutputStream output;
  private Thread outputThread;
  private int jogando;
  private boolean minhaVez;

  int linhas;
  int colunas;

  public Controlador() {

    boolean errado = true;

    while (errado){

        try {

            String servidor = JOptionPane.showInputDialog(null, "Digite o nome ou o IP do servidor: ",
	      	              "Jogo Dots!",JOptionPane.QUESTION_MESSAGE);
            if (servidor != null){

                conexao = new Socket(InetAddress.getByName(servidor), 5000 );
                input = new DataInputStream(conexao.getInputStream() );
                output = new DataOutputStream(conexao.getOutputStream() );
                errado = false;

                jogo = new FrameJogo(0, 0, this);
            }

            else System.exit(0);

        }

        catch ( IOException e ) {

              if (e.toString().equals("java.net.ConnectException: Connection refused: connect"))
                    JOptionPane.showMessageDialog(null, "Servidor Desconectado!","Jogo Dots!",
	                                JOptionPane.INFORMATION_MESSAGE);

             else JOptionPane.showMessageDialog(null, "Endereço de servidor incorreto!","Jogo Dots!",
	                                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    outputThread = new Thread( this );
    outputThread.start();
  }

  //Retorna a situação de uma linha horizontal
  public boolean getHorizontais(int linha, int coluna){
    return tabuleiro.getHorizontais(linha, coluna);
  }

  //Retorna a situação de uma linha vertical
  public boolean getVerticais(int linha, int coluna){
    return tabuleiro.getVerticais(linha, coluna);
  }

  //Retorna a situação de um marcador
  public int getMarcadores(int linha, int coluna){
    return tabuleiro.getMarcadores(linha, coluna);
  }

  //Atualiza a situação de uma linha horizontal
  public void setHorizontais(int linha, int coluna){

    if(!(tabuleiro.getHorizontais(linha, coluna))){
       tabuleiro.setHorizontais(linha, coluna);
       tabuleiro.fechouQuadrado(linha, coluna, jogando,'H');
    }
  }

  //Atualiza a situação de uma linha vertical
  public void setVerticais(int linha, int coluna){
    if (!(tabuleiro.getVerticais(linha, coluna))){
      tabuleiro.setVerticais(linha, coluna);
      tabuleiro.fechouQuadrado(linha, coluna, jogando,'V');
    }
  }

  //Atualiza a situação de um marcador
  public void setMarcadores(int linha, int coluna, int jogador){
    tabuleiro.setMarcadores(linha, coluna, jogador);
  }

  //Retorna se a matriz marcadores está completa
  public boolean completo(){
     return tabuleiro.completo();
  }

  //Retorna quantos pontos tem o jogador
  public int getPontos(int jogador){
     return tabuleiro.getPontos(jogador);
  }

  //Método run
  public void run(){
    String s;
    boolean sair = true;

    try{
        s = input.readUTF();
        processaMensagem(s);

       jogando = input.readInt();
       jogo.setTitle("Jogo Dots! - Jogador " + (jogando + 1));
       minhaVez = (jogando == 0 ? true : false);
       jogo.atualizaLabel("Você é o jogador " + jogando);


    }

    catch(IOException e){
        JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
        System.exit(1);
    }

    //Recebe mensagens enviadas para o cliente
    while(sair){

      try{
        s = input.readUTF();

        if(s.equals("Game Over")) sair = false;

        processaMensagem(s);
      }

      catch(IOException e){
       JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
       System.exit(1);
      }

    }
  }

  //Processa as mensagens recebidas pelo cliente
  public void processaMensagem(String s){

    if (s.equals("Tamanho do tabuleiro")){

      boolean errado = true;

      while(errado){

        try{

          String tam = JOptionPane.showInputDialog(null, "Tamanho do Tabuleiro (2 a 15): ",
	  	       "Jogo Dots",JOptionPane.QUESTION_MESSAGE);

          //Tamanho escolhido pelo jogador
          if (tam != null) linhas = colunas = Integer.parseInt(tam);

          //Se o jogador não escolher um tamanho, este terá um tamanho padrão igual a 8
          else linhas = colunas = 5;

          if ((linhas < 2) || (linhas > 15)){
           errado = true;
           JOptionPane.showMessageDialog(null, "Tamanho inválido!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);

          }
          else {
            output.writeInt(linhas);
            tabuleiro = new Tabuleiro(linhas, colunas);
            jogo.setTamanho(linhas);
            errado = false;
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

          errado = true;
        }

      }
    }

    if (s.equals("Pegar tamanho do tabuleiro")){

      boolean flag = true;

      while (flag){
        try{
          linhas = colunas = input.readInt();

          tabuleiro = new Tabuleiro(linhas, colunas);
          jogo.setTamanho(linhas);
          flag = false;
        }
        catch(IOException e){

        }
      }

    }

    if (s.equals("Movimento Válido")){

      try{
        boolean fechou = input.readBoolean();

        if (fechou){
         minhaVez = true;
         jogo.atualizaLabel("Fechou quadrado, jogue outra vez!");
        }

        else{
           jogo.atualizaLabel("Movimento válido, por favor espere!");
           minhaVez = false;
        }

      }

      catch(IOException e){
       JOptionPane.showMessageDialog(null, "Ocorreu um erro durante a conexão!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
       System.exit(1);
      }

      jogo.atualizaTela();
    }

    else if (s.equals("Movimento Inválido")){
            minhaVez = true;
         }

         else if (s.equals("Jogada do Adversário")){

                 try{

                   int adversario = (jogando == 0 ? 1 : 0);
                   char matriz = input.readChar();
                   int linha = input.readInt();
                   int coluna = input.readInt();

                   if (matriz == 'H'){
                      tabuleiro.setHorizontais(linha, coluna);

                      if (tabuleiro.fechouQuadrado(linha, coluna, adversario, 'H')){
                        minhaVez = false;
                        jogo.atualizaLabel("Jogada do Adversário!");
                      }

                      else{
                       minhaVez = true;
                       jogo.atualizaLabel("Sua Vez!");
                      }
                   }

                   else{
                      tabuleiro.setVerticais(linha, coluna);

                      if (tabuleiro.fechouQuadrado(linha, coluna, adversario, 'V')){
                        minhaVez = false;
                        jogo.atualizaLabel("Jogada do Adversário!");
                      }

                      else{
                        minhaVez = true;
                        jogo.atualizaLabel("Sua Vez!");
                      }
                   }

                   jogo.atualizaTela();
                }

                catch(IOException e){
                  e.printStackTrace();
                }
         }

         else if (s.equals("Game Over")){

            int pontos1 = tabuleiro.getPontos(1);
            int pontos2 = tabuleiro.getPontos(2);
            String vencedor = "", output = "";

            if(pontos1 > pontos2) vencedor = "Vencedor: Jogador 1";
            else if(pontos1 < pontos2) vencedor = "Vencedor: Jogador 2";
            else vencedor = "Empate";

            output += " Placar Final:\n\n Jogador 1: " + pontos1 + " pontos\n" +
                      " Jogador 2: " + pontos2 + " pontos\n\n " + vencedor;

            JOptionPane.showMessageDialog(null, output,"Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);

            jogo.dispose();
            System.exit(0);

         }
         else if (s.equals("Esperando por outro jogador")){
            minhaVez = false;
            jogo.atualizaLabel(s);
         }
         else if (s.equals("Finalizou")){
            JOptionPane.showMessageDialog(null, "O jogo será finalizado!","Jogo Dots!",
	                            JOptionPane.INFORMATION_MESSAGE);
            jogo.dispose();
            System.exit(0);
         }
         else if (s.equals("Outro jogador conectado. Sua Vez")){
            minhaVez = true;
            jogo.atualizaLabel(s);
         }
         else jogo.atualizaLabel(s);

  }

  //Manda para o jogador a jogada atual
  public void setOutput(int linha, int coluna, char matriz){

    if (minhaVez){

      try{
        output.writeChar(matriz);
        output.writeInt(linha);
        output.writeInt(coluna);
        output.flush();
        minhaVez = false;
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
    return minhaVez;
  }

  public static void main(String[] args) {

    Controlador c = new Controlador();
  }
}