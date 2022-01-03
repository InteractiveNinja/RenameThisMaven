package eu.imninja.Model;

import eu.imninja.GUI.JFrames.MessageGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Model {

    private List<File> files = new ArrayList<>();
    private ObservableList<String> data;

    /**
     * Adds File to the Filelist which corresponds to the ListView
     * @param f
     */
    public void addToList(File f) {
        if(!files.contains(f)) {
            files.add(f);
        }
    }

    /**
     * Clears all Entry in FileList, this corresponds o the ListView
     */
    public void clearList() {
        files.clear();
    }

    /**
     * Swaps Places the an Item in the List
     * @param item Name of the Selected Item
     * @param code KeyCode Value that got Pressed
     * @return Index for SelectSystem for a Dynamic Experience
     */
    public int move(String item,String code) {
        Optional<File> f = files.stream().filter(file -> file.getName().equals(item)).findFirst();
        boolean type = code.equals("UP");
        int oldIndex = files.indexOf(f.get());
        int newindex;
        if(type ){
         newindex = Math.max(oldIndex - 1, 0);

        } else {

        newindex = Math.min(oldIndex + 1, files.size() - 1);
        }
        File oldFile = files.get(oldIndex);
        File newFile = files.get(newindex);
        files.set(newindex,oldFile);
        files.set(oldIndex,newFile);
        return newindex;
    }

    /**
     * Creates the Model for the ListView
     * @return
     */
    public ObservableList<String> getModel() {
        data = FXCollections.observableArrayList();
        files.forEach(file -> data.add(file.getName()));
        return  data;
    }

    /**
     * Renames all Filesentry with the Format as Example
     * @param format The Formating Rule for all Files
     * @return boolean = true if all files got renamed
     */
    public boolean renameAllFiles(String format,boolean openFolder) {
        String formatting = (!format.equals(""))? format: "NoName $ep";
        AtomicReference<String> folderPath = new AtomicReference<>("");
        if(files.size() == 0) return false;
        try {
            files.forEach(file -> {
                folderPath.set(file.getPath().replace(file.getName(), ""));
                String path = file.getPath();
                String name = file.getName();
                String episodeNumber = String.format("%02d",(files.indexOf(file)+1));
                String newname = formatting.replace("$ep",""+episodeNumber);
                String newPath = path.replace(name,newname);
                file.renameTo(new File(newPath));
            });
            clearList();
            if(openFolder)Desktop.getDesktop().open(new File(folderPath.get()));
        } catch (Exception e){
            new MessageGUI(e.getLocalizedMessage());
            return false;
        }
        return true;


    }
    public String fileRenamePreview(String format) {
        String formatting = (!format.equals(""))? format: "NoName $ep";

        StringBuilder renames = new StringBuilder();

        files.forEach(file -> {
            String name = file.getName();
            String episodeNumber = String.format("%02d",(files.indexOf(file)+1));
            String newname = formatting.replace("$ep",""+episodeNumber);
            renames.append(name + " -> " + newname + "\n");
        });

        return renames.toString();
    }

    /**
     * Deletes File out of the List with the Name of the File
     * @param item Name of the File that gets deleted
     */
    public void deleteSelected(String item) {
        Optional<File> f = files.stream().filter(file -> file.getName().equals(item)).findFirst();
        files.remove(f.get());
    }




}
