package app;

import app.ui.MainFrame;

import java.awt.*;
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // 主界面
            MainFrame mainFrame = new MainFrame();
        });
    };
}
