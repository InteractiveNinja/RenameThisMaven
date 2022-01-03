package eu.imninja.GUI.Controller;

import eu.imninja.GUI.JFrames.ConfirmPromptGUI;
import eu.imninja.GUI.JFrames.MessageGUI;
import eu.imninja.Model.Model;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

public class Controller {

    private Model model;

    /**
     * Creates a new Controller and creates also new Model
     */
    public Controller() {
        model = new Model();
    }



    /**
     * Is Connected to the formatting TextField in the Main Window
     */
    @FXML
    TextField formatting;
    /**
     * Is Connected to the List ListView in the Main Window
     */
    @FXML
    ListView FileList;

    /**
     * Is Connected to the Checkbox in the Main Window
     */
    @FXML
    CheckBox openFolder;

    /**
     * actionEvent for the Button.
     * onClick this Event will be Triggered
     */
    public void renameFiles() {
        String format = formatting.getText();
        String renamePreview = model.fileRenamePreview(format);
        if(ConfirmPromptGUI.ShowConfirmMessage(renamePreview)) {
            if(model.renameAllFiles(formatting.getText(),openFolder.isSelected())) {
                new MessageGUI("Dateien wurden unbenannt!");
                formatting.clear();
                fillList();
            }
        } else {
            new MessageGUI("Es wurden keine Datein umbennant!");
        }


    }

    /**
     * Load Files from the Sytem and add them to the Model and show them in the List
     */
    public void loadFiles() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        List<File> f = fileChooser.showOpenMultipleDialog(new Stage());
        if(f != null) f.forEach(file -> model.addToList(file));
        fillList();
    }

    /**
     * Fills the List, uses the Model for generating the List
     */
    private void fillList() {
        FileList.setItems(model.getModel());
    }

    /**
     * Keypressed Event will be triggerd in keys are Pressed in the List
     */
    public void keyPressed(){
        FileList.setOnKeyPressed((e) ->{
            String keypress = e.getCode().toString();
            String selected = (FileList.getSelectionModel().getSelectedItem() != null)? FileList.getSelectionModel().getSelectedItem().toString() : "";
            if(keypress.equals("UP") || keypress.equals("DOWN") ) {
                //Swaping Logic for the List
                int index = model.move(selected,e.getCode().toString());
                FileList.getSelectionModel().select(index);
            }
            if(keypress.equals("DELETE")) {
                    if(!selected.equals(""))model.deleteSelected(selected);
            }

            if(keypress.equals("L")) {
                ClipboardContent c = new ClipboardContent();
                c.putString(selected);
                Clipboard.getSystemClipboard().setContent(c);
            }

            if(keypress.equals("X")) {
                model.clearList();
            }
            fillList();
        });

    }






}
