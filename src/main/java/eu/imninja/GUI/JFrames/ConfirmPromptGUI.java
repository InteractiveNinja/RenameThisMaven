package eu.imninja.GUI.JFrames;

import javax.swing.*;

public class ConfirmPromptGUI extends JOptionPane {

    public static boolean ShowConfirmMessage(String text) {
        int value = showConfirmDialog(null,text,"Möchtest du das tun?",JOptionPane.YES_NO_OPTION);

        return value == 0;

    }
}
