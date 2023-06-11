package app.taskplanner.view.boardView;

import app.taskplanner.model.notes.NoteMetadata;
import app.taskplanner.viewmodel.boardviewmodel.BoardViewModel;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SimpleNoteController {

    private NoteMetadata selfNote;
    private BoardViewModel boardVM;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private TextArea noteContent;

    @FXML
    private TextField noteTitle;

    SimpleNoteController(NoteMetadata noteMetadata) {}
    public SimpleNoteController(){};

    public void init(NoteMetadata noteMetadata,BoardViewModel boardVM,int i){
        this.boardVM = boardVM;
        selfNote = noteMetadata;
        noteTitle.setText(selfNote.getTitle());
        //noteTitle.textProperty().bindBidirectional((Property<String>) boardVM.notesProperty().get(i));
        noteContent.setText(boardVM.getNote(selfNote.getKey()).getNoteBody().getContent());
        contentPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        contentPane.setBackground(new Background(new BackgroundFill(Color.LAVENDER,CornerRadii.EMPTY, Insets.EMPTY)));

        noteContent.setWrapText(true);
    }

    @FXML
    void openInSeparateWindow(ActionEvent event) {
        boardVM.openInSeparateWindow(selfNote.getKey());
    }

}

