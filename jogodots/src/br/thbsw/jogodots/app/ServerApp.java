package br.thbsw.jogodots.app;

import br.thbsw.jogodots.view.FrameDotsServer;

import javax.swing.*;

public class ServerApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new FrameDotsServer();
    }
}
