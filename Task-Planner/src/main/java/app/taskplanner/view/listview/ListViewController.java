package app.taskplanner.view.listview;

import app.taskplanner.StartApp;
import app.taskplanner.model.notes.NoteMetadata;
import app.taskplanner.view.ViewController;
import app.taskplanner.view.alerts.SelectionAlert;
import app.taskplanner.view.alerts.WrongSelectionAlert;
import app.taskplanner.viewmodel.ViewModel;
import app.taskplanner.viewmodel.listviewmodel.ListViewModel;
import javafx.scene.control.ListView;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import java.util.Comparator;
import java.util.Objects;

public class ListViewController implements ViewController {
    private ListViewModel listVM;
    static int counter;

    @FXML
    private ListView<NoteMetadata> listOfNotes = new ListView<>();

    @FXML
    private TextField newTitle;

    @FXML
    private Button plusButton;


    @FXML
    void createAndOpenNote() {
        listVM.addNoteWithTitle("Your new note " + counter++);
        listVM.openNote(listOfNotes.getItems().get(listOfNotes.getItems().size()-1).getKey());
    }

    @FXML
    void createNewNote() {
        String title = newTitle.getText();
        listVM.addNoteWithTitle(title);
        newTitle.clear();
    }

    @FXML
    void deleteSelectedNote() {
        int selectedIndex = listOfNotes.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1) {
            listVM.removeNoteAt(selectedIndex);
        }
        else {
            new SelectionAlert().show();
        }
    }

    @FXML
    void closeAllNotes() {
        listVM.closeAllNotes();
    }

    @FXML
    void openSelectedNote() {
        NoteMetadata selectedNoteInfo = listOfNotes.getSelectionModel().getSelectedItem();
        if(selectedNoteInfo != null) {
            listVM.openNote(selectedNoteInfo.getKey());
        }
        else
        {
            new SelectionAlert().show();
        }
    }

    //@Override
    public void init(ViewModel listVM) {
        this.listVM = (ListViewModel) listVM;
        listOfNotes.itemsProperty().bindBidirectional(this.listVM.notesProperty());
        listOfNotes.setOnMouseClicked(event -> {
            if (listOfNotes.getSelectionModel().getSelectedItems() != null) {
                showOptions(listOfNotes.getSelectionModel().getSelectedIndex(), event);
            }
        });
        Image icon = new Image(Objects.requireNonNull(StartApp.class.getResourceAsStream("plus.png")));
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(50);
        iconView.setFitWidth(50);
        plusButton.setGraphic(iconView);
        listOfNotes.setCellFactory(param -> new NoteListCell());
        counter = 1;
    }

    private void showOptions(Integer item, MouseEvent event) {
        Popup popup = new Popup();
        Button openNote = new Button("Open");
        Button deleteNote = new Button("Delete");
        VBox vbox = new VBox(openNote, deleteNote);
        if(item >= listOfNotes.getItems().size() || item < 0){
            new WrongSelectionAlert().show();
            return;
        }
        int key = listOfNotes.getItems().get(item).getKey();

        openNote.setOnAction(click -> listVM.openNote(key));
        deleteNote.setOnAction(click -> {
            listVM.removeNoteAt(item);
            popup.hide();
        });
        popup.getContent().add(vbox);
        popup.show(listOfNotes.getScene().getWindow(), event.getScreenX(), event.getScreenY());
        // TODO: 03.06.2023 should disappear on dishover
        popup.hide();
    }

    public void chronoSort() {
        Comparator<NoteMetadata> comparator = Comparator.comparing(NoteMetadata::getDate, Comparator.nullsLast(Comparator.naturalOrder()));
        listOfNotes.getItems().sort(comparator);
    }
}