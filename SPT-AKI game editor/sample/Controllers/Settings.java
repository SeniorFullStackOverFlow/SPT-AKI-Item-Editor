package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sample.Writemodules;

public class Settings {

    private Stage primaryStage;
    private Scene scene;

    private final Writemodules Wm = new Writemodules();

    private int gInitReadStngs = 0;
    private int gMakeFileMode = 0;
    private int gAplyBtnChk = 0, gCanclBtnChk = 0, gWrkmd = 0, gItmsCngChk = 0;
    private String[] gStngBufr = new String[13];
    private String[] gStngBufr1 = new String[13];

    //Простой массив для считывания языковых настроек
    private static final int[][] lsLngLoadLns = new int[][]{{1,2,6,9,10,2},{0,14,7}};

    //ArrayList для динамического считывания ошибок
    private static final ArrayList<ArrayList<String>> gsErrs = new ArrayList<ArrayList<String>>();

    //Блок для работы с выбранной категорией предмета
    private String gPathToItmDrctry = "";
    private String gPathToBckupItmDrctry = "";
    private int gStngId = 0;
    private int gFileGrpIndx = 0;

    @FXML
    private void initialize() {
        if (gInitReadStngs < 1) {
            gStngBufr = Wm.Settings.readSettings();
            gStngBufr1 = gStngBufr.clone();
            gInitReadStngs = 1;
            if (gInitReadStngs > 0) {
                if (gStngBufr1[1].length() > 0) {
                    ObservableList<String> lmLangBufr = FXCollections.observableArrayList("English", "Russian");
                    String lmPathToDrctry = new String(System.getProperty("user.dir") + "\\Lib\\Images\\");
                    String flag = lmPathToDrctry;
                    if (gStngBufr1[1].contains("Eng")) {
                        languageChangerChoiceBox.setValue("English");
                        lmPathToDrctry += "16428480741900.png";
                        flag += "English.jpg";
                    }
                    if (gStngBufr1[1].contains("Rus")) {
                        languageChangerChoiceBox.setValue("Russian");
                        lmPathToDrctry += "headeyes.jpg";
                        flag += "Russian.jpeg";
                    }
                    File n = new File(lmPathToDrctry);
                    File m = new File(flag);
                    Image aaaaaaaaaaa = new Image(n.toURI().toString(), 238, 198, false, false);
                    Image languageimage = new Image(m.toURI().toString(), 52, 41, false, false);
                    languageRoflanPepegGpeg.setImage(aaaaaaaaaaa);
                    languageFlagImageView.setImage(languageimage);
                    languageChangerChoiceBox.setItems(lmLangBufr);
                }

                //Добавление категорий
                makefileCategoryChoseItemChoiceBox.getItems().addAll("Globals file", "Items File");
                makefileCategoryChoseItemChoiceBox.setValue("Items File");
                gFileGrpIndx = 1;
                gPathToItmDrctry = gStngBufr1[11].replace("*","FileChanges\\Gameitems\\");
                gPathToBckupItmDrctry = gStngBufr1[11].replace("*","BackupFiles\\Backupitems\\items.json");
                gStngId = 2;

                //Проверка и считывания настроек
                makefileItemsFileChoiseBox.setItems(Wm.Find.findFilesInDrctry(gPathToItmDrctry, gPathToBckupItmDrctry, 0));

                if (gStngBufr1[gStngId].length() > 0) {
                    if(gStngBufr1[gStngId].contains(".json")){
                        makefileItemsFileChoiseBox.setValue(gStngBufr1[gStngId].substring((gStngBufr1[gStngId].lastIndexOf('\\') + 1)));
                    } else {
                        makefileItemsFileChoiseBox.setValue(null);
                    }
                }
                if (gStngBufr1[4].contains("true")) {
                    panelanimationMenuAnimationRadioButton.setSelected(true);
                }
                if (gStngBufr1[5].contains("true")) {
                    panelanimationItemsEditorRadioButton.setSelected(true);
                }
                if (gStngBufr1[6].contains("true")) {
                    panelanimationGlobalValueRadioButton.setSelected(true);
                }
                if (gStngBufr1[7].contains("true")) {
                    panelanimationItemsStatsRadioButton.setSelected(true);
                }
                if (gStngBufr1[8].length() > 0) {
                    pathGameDirectoryTextField.setText(gStngBufr1[8]);
                }
                if (gStngBufr1[9].length() > 0) {
                    pathItemsFileTextField.setText(gStngBufr1[9]);
                }
                if (gStngBufr1[10].length() > 0) {
                    pathGlobalValuesTextField.setText(gStngBufr1[10]);
                }
                if (gStngBufr1[11].length() > 0) {
                    pathBackupTextField.setText(gStngBufr1[11]);
                }

                //Загрузка языкового пакета
                try {
                    ArrayList<ArrayList<ArrayList<String>>> lmLangBufr = Wm.Language.findLangFile(
                            languageChangerChoiceBox.getValue(), (System.getProperty("user.dir") +
                                    "\\Lib\\Languages\\"), lsLngLoadLns);

                    languageLabel.setText(lmLangBufr.get(0).get(0).get(1));

                    panelanimationLabel.setText(lmLangBufr.get(0).get(1).get(1));
                    panelanimationMenuAnimationRadioButton.setText(lmLangBufr.get(0).get(1).get(2));
                    panelanimationItemsEditorRadioButton.setText(lmLangBufr.get(0).get(1).get(3));
                    panelanimationGlobalValueRadioButton.setText(lmLangBufr.get(0).get(1).get(4));
                    panelanimationItemsStatsRadioButton.setText(lmLangBufr.get(0).get(1).get(5));

                    pathToGameLabel.setText(lmLangBufr.get(0).get(2).get(1));
                    pathToItemsLabel.setText(lmLangBufr.get(0).get(2).get(2));
                    pathToGlobalsLabel.setText(lmLangBufr.get(0).get(2).get(3));
                    pathToDirectioryLabel.setText(lmLangBufr.get(0).get(2).get(4));
                    pathGameDirectorySearchBtn.setText(lmLangBufr.get(0).get(2).get(5));
                    pathItemsFieldSearchBtn.setText(lmLangBufr.get(0).get(2).get(6));
                    pathGlobalValuesSearchBtn.setText(lmLangBufr.get(0).get(2).get(7));
                    pathBackupDirectorySearchBtn.setText(lmLangBufr.get(0).get(2).get(8));

                    makefileChoseItemLabel.setText(lmLangBufr.get(0).get(3).get(1));
                    makefileMakeNewItemsFileBtn.setText(lmLangBufr.get(0).get(3).get(2));
                    makefileDeleteItemsFileBtn.setText(lmLangBufr.get(0).get(3).get(3));
                    makefileApplyBtn.setText(lmLangBufr.get(0).get(3).get(4));
                    makefileCloseBtn.setText(lmLangBufr.get(0).get(3).get(5));
                    makefileCopyOldFileToNewRadioBtn.setText(lmLangBufr.get(0).get(3).get(6));
                    makefileCategoryItemLabel.setText(lmLangBufr.get(0).get(3).get(7));
                    makefileCategoryItemRestoreLabel.setText(lmLangBufr.get(0).get(3).get(8));
                    makefileCategoryItemRestoreButton.setText(lmLangBufr.get(0).get(3).get(9));

                    exitKey.setText(lmLangBufr.get(0).get(4).get(1));

                    //Считывания массива ошибок из базы в рабочий массив
                    for (int i = 0; i < lmLangBufr.get(1).size(); i++){
                        gsErrs.add(lmLangBufr.get(1).get(i));
                    }

                }catch (Exception b) {
                    System.out.println("При загрузке языкового пакета ошибка: " + b);
                }
            }

        }
    }

    @FXML
    private AnchorPane Globalpane;

    //Модуль смены языка
    @FXML
    private Label languageLabel;

    @FXML
    private ChoiceBox<String> languageChangerChoiceBox;

    @FXML
    private ImageView languageRoflanPepegGpeg;

    @FXML
    private ImageView languageFlagImageView;
    //Конец модуля

    //Модуль настройки анимации всплывающих панелей
    @FXML
    private Label panelanimationLabel;

    @FXML
    private RadioButton panelanimationMenuAnimationRadioButton;

    @FXML
    private RadioButton panelanimationItemsEditorRadioButton;

    @FXML
    private RadioButton panelanimationGlobalValueRadioButton;

    @FXML
    private RadioButton panelanimationItemsStatsRadioButton;
    //Конец модуля

    //Модуль путей к файлам
    @FXML
    private Label pathToGameLabel;

    @FXML
    private Label pathToItemsLabel;

    @FXML
    private Label pathToGlobalsLabel;

    @FXML
    private Label pathToDirectioryLabel;

    @FXML
    private Button pathGameDirectorySearchBtn;

    @FXML
    private Button pathItemsFieldSearchBtn;

    @FXML
    private Button pathGlobalValuesSearchBtn;

    @FXML
    private Button pathBackupDirectorySearchBtn;

    @FXML
    private TextField pathGameDirectoryTextField;

    @FXML
    private TextField pathItemsFileTextField;

    @FXML
    private TextField pathGlobalValuesTextField;

    @FXML
    private TextField pathBackupTextField;
    //Конец модуля

    //Модуль подбора файла
    @FXML
    private Label makefileCategoryItemLabel;

    @FXML
    private ChoiceBox<String> makefileCategoryChoseItemChoiceBox;

    @FXML
    private ChoiceBox<String> makefileScndCtgryChsItmChoiceBox;

    @FXML
    private Label makefileCategoryItemRestoreLabel;

    @FXML
    private Button makefileCategoryItemRestoreButton;

    @FXML
    private Label makefileChoseItemLabel;

    @FXML
    private ChoiceBox<String> makefileItemsFileChoiseBox;

    @FXML
    private Button makefileMakeNewItemsFileBtn;

    @FXML
    private Button makefileDeleteItemsFileBtn;

    @FXML
    private TextField makefileMakeNewFileTextField;

    @FXML
    private Button makefileApplyBtn;

    @FXML
    private Button makefileCloseBtn;

    @FXML
    private RadioButton makefileCopyOldFileToNewRadioBtn;

    //Модуль выхода из окна
    @FXML
    private Button exitKey;

    //Начало модуля смены языка
    @FXML
    void languageChangerChoiceBoxmousereleased(MouseEvent event) {
        languageChangerChoiceBox.setDisable(true);
        EventHandler<Event> Ehl1 = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (languageChangerChoiceBox.getValue().equals("English") & gStngBufr1[1].contains("Russian")) {
                    gStngBufr1[1] = gStngBufr1[1].replace("Russian", "English");
                }
                if (languageChangerChoiceBox.getValue().equals("Russian") & gStngBufr1[1].contains("English")) {
                    gStngBufr1[1] = gStngBufr1[1].replace("English", "Russian");
                }
                languageChangerChoiceBox.removeEventHandler(ComboBox.ON_HIDDEN, this);
                languageChangerChoiceBox.setDisable(false);
            }
        };
        languageChangerChoiceBox.addEventHandler(ComboBox.ON_HIDDEN, Ehl1);
    }
    //Конец модуля смены языка

    //Начало модуля настройки анимации всплывающих панелей
    @FXML
    void Globalmenuanimationaction(ActionEvent event) {
        changeanimation(6);
    }

    @FXML
    void Itemeditoranimationaction(ActionEvent event) {
        changeanimation(5);
    }

    @FXML
    void Itemsstatspanelanimationaction(ActionEvent event) {
        changeanimation(7);
    }

    @FXML
    void Menuanimationaction(ActionEvent event) {
        changeanimation(4);
    }
    //Конец модуля настройки анимации всплывающих панелей

    //Начало модуля путей к файлам
    @FXML
    void pathBackupDirectorySearchBtnaction(ActionEvent event) {
        findLocation(gsErrs.get(0).get(2),gsErrs.get(0).get(3),gsErrs.get(0).get(4),
                gsErrs.get(0).get(1), 11, 1);
    }

    @FXML
    void pathGameDirectorySearchBtnaction(ActionEvent event) {
        findLocation(gsErrs.get(0).get(5),gsErrs.get(0).get(6),gsErrs.get(0).get(7),
                gsErrs.get(0).get(1), 8, 1);
    }

    @FXML
    void pathGlobalValuesSearchBtnaction(ActionEvent event) {
        findLocation(gsErrs.get(0).get(8),gsErrs.get(0).get(9),gsErrs.get(0).get(10),
                gsErrs.get(0).get(1), 10, 0);
    }

    @FXML
    void pathItemsFieldSearchBtnaction(ActionEvent event) {
        findLocation(gsErrs.get(0).get(11),gsErrs.get(0).get(12),gsErrs.get(0).get(13),
                gsErrs.get(0).get(1), 9, 0);
    }
    //Конец модуля путей к файлам

    //Начало модуля подбора файла
    @FXML
    void makefileCategoryChoseItemChoiceBoxClicked(MouseEvent event3) {
        makefileCategoryChoseItemChoiceBox.setDisable(true);
        EventHandler<Event> ChgCtgry = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                int lmCtgryIndx = makefileCategoryChoseItemChoiceBox.getSelectionModel().getSelectedIndex();
                if (gFileGrpIndx != lmCtgryIndx) {
                    gFileGrpIndx = lmCtgryIndx;
                    makefileScndCtgryChsItmChoiceBox.setVisible(false);
                    switch (makefileCategoryChoseItemChoiceBox.getValue()) {
                        case ("Globals file"):
                            gPathToItmDrctry = gStngBufr1[11].replace("*", "FileChanges\\Gameglobals\\");
                            gPathToBckupItmDrctry = gStngBufr1[11].replace("*", "BackupFiles\\Backupglobals\\globals.json");
                            gStngId = 3;

                            break;
                        case ("Items File"):
                            gPathToItmDrctry = gStngBufr1[11].replace("*", "FileChanges\\Gameitems\\");
                            gPathToBckupItmDrctry = gStngBufr1[11].replace("*", "BackupFiles\\Backupitems\\items.json");
                            gStngId = 2;
                            break;
                    }
                    makefileItemsFileChoiseBox.setItems(Wm.Find.findFilesInDrctry(gPathToItmDrctry, gPathToBckupItmDrctry, 0));
                    makefileItemsFileChoiseBox.setValue(gStngBufr1[gStngId].substring((gStngBufr1[gStngId].lastIndexOf('\\') + 1)));
                }
                makefileCategoryChoseItemChoiceBox.removeEventHandler(ComboBox.ON_HIDDEN, this);
                makefileCategoryChoseItemChoiceBox.setDisable(false);
            }
        };
        makefileCategoryChoseItemChoiceBox.addEventHandler(ComboBox.ON_HIDDEN, ChgCtgry);
    }

    @FXML
    void makefileScndCtgryChsItmChoiceBoxClicked(MouseEvent event3) {

    }

    @FXML
    void makefileCategoryItemRestoreButtonAction(ActionEvent event3) {
        if (Wm.AppWindow.dialogWindow(gsErrs.get(1).get(6)) == 1){
            Wm.Write.writeFile(gStngBufr1[(7 + gStngId)], gPathToBckupItmDrctry, 1, false);
        }
    }

    @FXML
    void Changefile(MouseEvent event3) {
        makefileItemsFileChoiseBox.setDisable(true);
        EventHandler<Event> Ehl = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (!gStngBufr1[gStngId].contains(makefileItemsFileChoiseBox.getValue())) {
                    gStngBufr1[gStngId] = gPathToItmDrctry + makefileItemsFileChoiseBox.getValue();
                }
                makefileItemsFileChoiseBox.removeEventHandler(ComboBox.ON_HIDDEN, this);
                makefileItemsFileChoiseBox.setDisable(false);
            }
        };
        makefileItemsFileChoiseBox.addEventHandler(ComboBox.ON_HIDDEN, Ehl);
    }

    @FXML
    void makefileDeleteItemsFileBtn1action(ActionEvent event) {
        if (makefileItemsFileChoiseBox.getValue() != null && makefileItemsFileChoiseBox.getValue().length() > 0) {
            if (Wm.AppWindow.dialogWindow(gsErrs.get(1).get(1)) == 1) {
                File deletefile = new File(gStngBufr1[gStngId]);
                deletefile.delete();
                makefileItemsFileChoiseBox.setItems(Wm.Find.findFilesInDrctry(gPathToItmDrctry, gPathToBckupItmDrctry, 2));
                makefileItemsFileChoiseBox.setValue(null);
                gStngBufr1[gStngId] = "";
            }
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(2));
        }
    }

    @FXML
    void Applypressed(MouseEvent event) {
        gAplyBtnChk = 1;
    }

    @FXML
    void Cancelpressed(MouseEvent event) {
        gCanclBtnChk = 1;
    }

    @FXML
    void makefileMakeNewItemsFileBtnaction(ActionEvent event) {
        if (gMakeFileMode == 0) {
            gMakeFileMode = 1;
            makefileItemsFileChoiseBox.setVisible(false);
            makefileItemsFileChoiseBox.setDisable(true);
            makefileMakeNewFileTextField.setVisible(true);
            makefileMakeNewFileTextField.setDisable(false);
            makefileMakeNewItemsFileBtn.setVisible(false);
            makefileMakeNewItemsFileBtn.setDisable(true);
            makefileDeleteItemsFileBtn.setVisible(false);
            makefileDeleteItemsFileBtn.setDisable(true);
            makefileApplyBtn.setVisible(true);
            makefileApplyBtn.setDisable(false);
            makefileCloseBtn.setVisible(true);
            makefileCloseBtn.setDisable(false);
            makefileCopyOldFileToNewRadioBtn.setVisible(true);
            makefileCopyOldFileToNewRadioBtn.setDisable(false);
            EventHandler<ActionEvent> aey = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event1) {
                    if(gMakeFileMode == 2){
                        if (gAplyBtnChk == 1) {
                            if (makefileMakeNewFileTextField.getLength() > 0) {
                                boolean lmCopyFile = false;
                                String lmScanFile;
                                if (makefileCopyOldFileToNewRadioBtn.isSelected()){
                                    lmScanFile = gStngBufr1[gStngId];
                                    gWrkmd = 1;
                                } else {
                                    lmScanFile = gPathToBckupItmDrctry;
                                    gWrkmd = 0;
                                }
                                if (Wm.Find.findFileDrctry(gPathToItmDrctry,(makefileMakeNewFileTextField.getText() + ".json"), 0) == "0"){
                                    Wm.Write.writeFile((gPathToItmDrctry + makefileMakeNewFileTextField.getText() + ".json"), lmScanFile, 1, false);
                                    gStngBufr1[gStngId] = (gPathToItmDrctry + makefileMakeNewFileTextField.getText() + ".json");
                                    gItmsCngChk = 1;
                                    gMakeFileMode = 3;
                                    gAplyBtnChk = 0;
                                } else {
                                    if (Wm.AppWindow.dialogWindow(gsErrs.get(1).get(3)) == 1) {
                                        if (makefileMakeNewFileTextField.getLength() > 0) {
                                            if (makefileCopyOldFileToNewRadioBtn.isSelected()) {
                                                lmScanFile = gStngBufr1[gStngId];
                                                gWrkmd = 1;
                                            } else {
                                                lmScanFile = gStngBufr1[(7 + gStngId)];
                                                gWrkmd = 0;
                                            }
                                            Wm.Write.writeFile((gPathToItmDrctry + makefileMakeNewFileTextField.getText() + ".json"), lmScanFile, 1, false);
                                            gStngBufr1[gStngId] = (gPathToItmDrctry + makefileMakeNewFileTextField.getText() + ".json");
                                            gItmsCngChk = 1;
                                            gMakeFileMode = 3;
                                            gAplyBtnChk = 0;
                                        } else {
                                            Wm.AppWindow.errorWindow(gsErrs.get(1).get(5));
                                        }
                                    } else {
                                        gMakeFileMode = 2;
                                        gAplyBtnChk = 0;
                                    }
                                }
                            } else {
                                Wm.AppWindow.errorWindow(gsErrs.get(1).get(4));
                            }
                        }
                        if (gCanclBtnChk == 1) {
                            gMakeFileMode = 3;
                            gCanclBtnChk = 0;
                        }
                    }

                    if (gMakeFileMode == 3) {
                        makefileItemsFileChoiseBox.setVisible(true);
                        makefileItemsFileChoiseBox.setDisable(false);
                        makefileMakeNewFileTextField.setVisible(false);
                        makefileMakeNewFileTextField.setDisable(true);
                        makefileMakeNewItemsFileBtn.setVisible(true);
                        makefileMakeNewItemsFileBtn.setDisable(false);
                        makefileDeleteItemsFileBtn.setVisible(true);
                        makefileDeleteItemsFileBtn.setDisable(false);
                        makefileApplyBtn.setVisible(false);
                        makefileApplyBtn.setDisable(true);
                        makefileCloseBtn.setVisible(false);
                        makefileCloseBtn.setDisable(true);
                        makefileCopyOldFileToNewRadioBtn.setVisible(false);
                        makefileCopyOldFileToNewRadioBtn.setDisable(true);
                        makefileCopyOldFileToNewRadioBtn.setSelected(false);
                        if (gItmsCngChk == 1){
                            makefileItemsFileChoiseBox.setItems(Wm.Find.findFilesInDrctry(gPathToItmDrctry, gPathToBckupItmDrctry, gWrkmd));
                            makefileItemsFileChoiseBox.setValue((makefileMakeNewFileTextField.getText() + ".json"));
                        }
                        makefileMakeNewFileTextField.setText(null);
                        gMakeFileMode = 0; gWrkmd = 0; gItmsCngChk = 0;
                        makefileApplyBtn.removeEventHandler(ActionEvent.ACTION, this);
                        makefileCloseBtn.removeEventHandler(ActionEvent.ACTION, this);
                    }
                }
            };
            if (gMakeFileMode == 1) {
                makefileApplyBtn.addEventHandler(ActionEvent.ACTION, aey);
                makefileCloseBtn.addEventHandler(ActionEvent.ACTION, aey);
                gMakeFileMode = 2;
            }
        }
    }
    //Конец модуля подбора файла

    //Начало модуля выхода из окна
    @FXML
    void exitKey(ActionEvent event) throws IOException {
        try {
            int write = 0;
            for (int i = 0; i < gStngBufr.length; i++) {
                if (gStngBufr[i] != gStngBufr1[i]) {
                    write = 1;
                }
            }
            if (write == 1) {
                String[][] lmDblArr = Wm.Settings.analyzeStngsChngs(gStngBufr, gStngBufr1);
                int[] lmChngsBufr = new int[lmDblArr[0].length];

                String[] charsbuffer = lmDblArr[1];


                for (int i = 0; i < lmDblArr[0].length; i++) {
                    lmChngsBufr[i] = Integer.valueOf(lmDblArr[0][i]);
                }

                Wm.Settings.writeChngsInStngs(lmChngsBufr, charsbuffer);
            }
            Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
            scene = new Scene(root);
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e) {
            System.out.println("Ошибка загрузки сцены sample: " + e);
        }
    }
    //Конец модуля

    //Начало фабрик и универсальных модулей

    private void changeanimation(int settinsbufferindex){
        if (gStngBufr1[settinsbufferindex].contains("true")) {
            gStngBufr1[settinsbufferindex] = gStngBufr1[settinsbufferindex].replace("true", "false");
        } else {
            gStngBufr1[settinsbufferindex] = gStngBufr1[settinsbufferindex].replace("false", "true");
        }
    }

    private void findLocation(String titlename, String password, String mdlerrtxt, String cmnerrtxt, int bfrindx, int gWrkmd){
        String pathtofile = Wm.Find.findFileOrDrctry(titlename, primaryStage, gWrkmd);
        if (pathtofile.length() > 0){
            if (pathtofile.contains(password)){
                pathBackupTextField.setText(pathtofile);
                gStngBufr1[bfrindx] = gStngBufr[bfrindx].substring(0, gStngBufr[bfrindx].indexOf('=')) + " " + pathtofile;
            } else {
                Wm.AppWindow.errorWindow(mdlerrtxt);
            }
        } else {
            Wm.AppWindow.errorWindow(cmnerrtxt);
        }
    }
}
