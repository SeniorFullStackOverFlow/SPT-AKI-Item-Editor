package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Writemodules {
    //Для возвращения значения из диалогового окна
    private static int rtnDialogVlue;


    public final FileRecovery Recovery = new FileRecovery();
    public final Settings Settings = new Settings();
    public final Language Language = new Language();
    public final AppWindow AppWindow = new AppWindow();
    public final Find Find = new Find();
    public final Write Write = new Write();


    public class FileRecovery {

        public void writeSettings() {
            try {
                File Settings = new File(System.getProperty("user.dir") + "\\Settings.txt");
                String[] lmWrtStngsArr = new String[17];
                FileWriter FW = new FileWriter(Settings);
                BufferedWriter BW = new BufferedWriter(FW);


                if (lmWrtStngsArr.length > 0) {
                    lmWrtStngsArr[0] = "Setting file save = 0";
                    lmWrtStngsArr[1] = "                   Global settings";
                    lmWrtStngsArr[2] = "Selected language = English";
                    lmWrtStngsArr[3] = "Selected file to change =";
                    lmWrtStngsArr[4] = "Selected globalfile to change =";
                    lmWrtStngsArr[5] = "Menu animation = true";
                    lmWrtStngsArr[6] = "Items editor panel animation = true";
                    lmWrtStngsArr[7] = "Global value panel animation = true";
                    lmWrtStngsArr[8] = "Items stats panel animation = true";
                    lmWrtStngsArr[9] = "Path to game directory =";
                    lmWrtStngsArr[10] = "Path to items file =";
                    lmWrtStngsArr[11] = "Path to global values file =";
                    lmWrtStngsArr[12] = "Path to save/backup directory =";
                    lmWrtStngsArr[13] = "               Main programm settings";
                    lmWrtStngsArr[14] = "Selected tags =";
                    lmWrtStngsArr[15] = "Selected type of item =";
                    lmWrtStngsArr[16] = "Selected item =";
                }
                for (int i = 0; i < lmWrtStngsArr.length; i++) {
                    BW.write(lmWrtStngsArr[i]);
                    BW.newLine();
                }
                BW.close();
                FW.close();
            } catch (IOException v) {
                System.out.println("Ошибка записи начального файла настроек: " + v);
            }
        }
        //End write()
    }

    public class Settings {

        public String[] readSettings() {
            String[] mRtrnLineBufr = new String[13];
            try {
                File Settings = new File(System.getProperty("user.dir") + "\\Settings.txt");

                FileReader FR = new FileReader(Settings);
                BufferedReader BR = new BufferedReader(FR);
                LineNumberReader LNR = new LineNumberReader(BR);

                char chr;

                String lmReaded = new String();

                //Начало работы модуля
                mRtrnLineBufr[0] = LNR.readLine();
                do {
                    lmReaded = LNR.readLine();
                    if (lmReaded.contains("Global settings")) {
                        for (int i = 1; i < 13; i++) {
                            lmReaded = LNR.readLine();
                            for (int o = 0; o < lmReaded.length(); o ++){
                                chr = lmReaded.charAt(o);
                                if (chr == '='){
                                    if (lmReaded.length() > (o + 2)) {
                                        lmReaded = lmReaded.substring(o + 2);
                                    } else {
                                        lmReaded = "";
                                    }
                                    o = lmReaded.length() + 1;
                                }
                            }
                            mRtrnLineBufr[i] = lmReaded;
                        }
                    }
                } while (LNR.getLineNumber() < 14);
                FR.close();
                BR.close();
                LNR.close();
            } catch (IOException v) {
                System.out.println("Ошибка чтения настроек: " + v);
            }
            return mRtrnLineBufr;
        }

        public String[][] analyzeStngsChngs(String settingbuffer[], String settingbuffer1[]) {
            int cnt1 = 0;
            String[][] mRtrnSmplArrStrng = new String[1][1];

            int[] lmChrsBufr;
            String[] lmStrngBufr;

            String[] lmChrs;

            if (cnt1 < 1) {
                for (int i = 0; i < settingbuffer1.length; i++) {
                    if (settingbuffer[i] != settingbuffer1[i]) {
                        cnt1++;
                    }
                }

                lmChrsBufr =  new int[cnt1];
                lmStrngBufr = new String[cnt1];

                cnt1 = 0;
                for (int m = 0; m < settingbuffer1.length; m++) {
                    if (settingbuffer[m] != settingbuffer1[m]) {
                        if (m < 1) {
                            lmChrsBufr[cnt1] = m;
                        } else if (m > 0) {
                            lmChrsBufr[cnt1] = m + 1;
                        }
                        lmStrngBufr[cnt1] = settingbuffer1[m];
                        if (cnt1 < lmChrsBufr.length) {
                            cnt1++;
                        }
                    }
                }

                lmChrs = new String[lmChrsBufr.length];

                for (int b = 0; b <lmChrsBufr.length; b++){
                    lmChrs[b] = String.valueOf(lmChrsBufr[b]);
                }
                if (cnt1 > 0) {
                    mRtrnSmplArrStrng = new String[2][];
                    mRtrnSmplArrStrng[0] = new String[lmChrs.length];
                    mRtrnSmplArrStrng[0] = lmChrs;
                    mRtrnSmplArrStrng[1] = new String[lmStrngBufr.length];
                    mRtrnSmplArrStrng[1] = lmStrngBufr;
                }
            }
            return mRtrnSmplArrStrng;
        }


        public void writeChngsInStngs(int[] changesbuffer, String[] lmSvdChrsbuffer){
            try{
                File stt = new File (System.getProperty("user.dir") + "\\Settings.txt");
                if (stt.canRead()) {

                    int lmChk = 0, i = 0;
                    String lmStrng = "";
                    String lmSvdChrs = "";

                    File n = new File(System.getProperty("user.dir") + "\\Settings.txt");
                    File copy = new File (System.getProperty("user.dir") + "\\Settings1.txt");

                    FileWriter FW = new FileWriter(copy);
                    BufferedWriter BW = new BufferedWriter(FW);
                    Scanner scanner = new Scanner(stt);

                    for (i = 0; i < changesbuffer.length; i++) {
                        if (changesbuffer[i] > lmChk) {
                            lmChk = changesbuffer[i];
                        }
                    }
                    if (lmChk > 0){
                        lmChk += 1;
                    } else if (lmChk > 12){
                        lmChk += 2;
                    }
                    while (i < lmChk) {
                        int cnt = 0;
                        i = 0;
                        for (i = 0; i < lmChk; i++) {
                            lmStrng = scanner.nextLine();
                            if ((cnt < changesbuffer.length) && (i == changesbuffer[cnt])) {
                                for (int o = 0; o < lmStrng.length(); o++){
                                    if (lmStrng.charAt(o) == '='){
                                        lmSvdChrs = (lmStrng.substring(0, (o + 1)) + " ");
                                        o = lmStrng.length() + 1;
                                    }
                                }

                                BW.write(lmSvdChrs + lmSvdChrsbuffer[cnt]);
                                BW.newLine();
                                cnt++;
                            } else {
                                BW.write(lmStrng);
                                BW.newLine();
                            }
                        }
                    }
                    i = 0;
                    while (scanner.hasNextLine()) {
                        BW.write(scanner.nextLine());
                        BW.newLine();
                        i++;
                    }
                    FW.flush();
                    BW.close();
                    scanner.close();
                    FW.close();

                    stt.delete();
                    copy.renameTo(n);
                }
            } catch (IOException v) {System.out.println("Ошибка перезаписи данных в файле настроек: " + v);}
        }

        public void searchgmvrsandwrtsstngs(){
            //Считывает версию игры
        }
    }

    public class Language{
        //считывание языковых настроек
        public  ArrayList<ArrayList<ArrayList<String>>> findLangFile(String language, String filepath, int[][] lnsloadarr) {
            String path = filepath + language + ".txt";
            File LanguageFile = new File(path);

            ArrayList<ArrayList<ArrayList<String>>> mRtrnLangStngs = new ArrayList<ArrayList<ArrayList<String>>>();

            if (LanguageFile.canRead()) {
                try {
                    ArrayList<ArrayList<String>> lmLangStng = new ArrayList<ArrayList<String>>();
                    ArrayList<ArrayList<String>> lmLangStngErr = new ArrayList<ArrayList<String>>();
                    //ArrayList<ArrayList<String>> lmLangStngHnts = new ArrayList<ArrayList<String>>(); - Их пока нет

                    ArrayList<ArrayList<String>> lmObjLnk = lmLangStng;

                    int lmChk = 0;
                    int[][] lmLnsIndxs = lnsloadarr;
                    String strng;

                    FileReader FR = new FileReader(LanguageFile);
                    Scanner scanner = new Scanner(FR);

                    for (int i = 0; i < lmLnsIndxs[0][0]; i++){
                        scanner.nextLine();
                    }

                    for (int i = 0; i < lmLnsIndxs.length; i++){
                        switch (i){
                            case (0):
                                lmObjLnk = lmLangStng;
                                break;
                            case (1):
                                lmObjLnk = lmLangStngErr;
                                break;
                            /*case (2):
                                lmObjLnk = lmLangStngHnts;
                                break;
                                */
                        }

                        for (int u = 1; u < lmLnsIndxs[i].length; u++){
                            ArrayList<String>  ArList = new ArrayList<String>();
                            for (int y = 0; y < lmLnsIndxs[i][u]; y++){
                                strng = scanner.nextLine();
                                String bfr = "";
                                for (int c = 0; c < strng.length(); c++){
                                    if (strng.charAt(c) != '*'){
                                        bfr += strng.charAt(c);
                                    } else{
                                        c = strng.length() + 1;
                                    }
                                }
                                ArList.add(bfr);
                            }
                            lmObjLnk.add(ArList);
                        }
                    }
                    mRtrnLangStngs.add(lmLangStng);
                    mRtrnLangStngs.add(lmLangStngErr);
                }catch (IOException v) {System.out.println("Ошибка чтения языкового файла: " + v);}
            } else {
                //Тут ошибка
                //
                //
                //
                //
                //
            }
            return mRtrnLangStngs;
        }
    }

    public class AppWindow {
        int lmChk = 0;
        public void errorWindow(String errortext) {
            lmChk = errortext.length();

            //Автоматический расчет ширины
            lmChk = ((lmChk * 7) + 30);
            if (lmChk < 200) {
                lmChk = 200;
            }

            if (lmChk > 0) {
                Stage window = new Stage(StageStyle.TRANSPARENT);
                window.initModality(Modality.APPLICATION_MODAL);

                VBox vBoxModal = new VBox();

                Label lblWindw = new Label(errortext);
                Button btnWindw = new Button("Close");

                Scene windwscene = new Scene(vBoxModal, lmChk, 100, Color.TRANSPARENT);



                vBoxModal.setStyle("-fx-background-color: derive(snow, 25%) ; -fx-border-color: deepskyblue;"
                        + "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 1px;");
                vBoxModal.setAlignment(Pos.CENTER);
                vBoxModal.setPadding(new Insets(20, 0, 0, 0));
                vBoxModal.setSpacing(15);


                btnWindw.setOnAction(event -> {
                    window.close();
                });

                vBoxModal.getChildren().addAll(lblWindw, btnWindw);

                window.setScene(windwscene);
                window.setTitle("Error");
                window.showAndWait();
            }
        }

        public int dialogWindow(String dialogtext) {
            lmChk = dialogtext.length();

            lmChk = ((lmChk * 7) + 30);
            if (lmChk < 200) {
                lmChk = 200;
            }

            if (lmChk > 0) {
                Stage window = new Stage(StageStyle.TRANSPARENT);
                window.initModality(Modality.APPLICATION_MODAL);

                VBox vBoxModal = new VBox();
                HBox hBoxModal = new HBox();

                Label lblWindw = new Label(dialogtext);

                Button btnOk = new Button("Yes");
                Button btnClose = new Button("No");

                Scene windwscene = new Scene(vBoxModal, lmChk, 100, Color.TRANSPARENT);


                vBoxModal.setStyle("-fx-background-color: derive(snow, 25%) ; -fx-border-color: deepskyblue;"
                        + "-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-width: 1px;");
                vBoxModal.setAlignment(Pos.CENTER);
                vBoxModal.setPadding(new Insets(5, 0, 0, 0));
                vBoxModal.setSpacing(15);


                hBoxModal.setAlignment(Pos.CENTER);
                hBoxModal.setSpacing(40);


                btnOk.setOnAction(event -> {
                    rtnDialogVlue = 1;
                    window.close();
                });


                btnClose.setOnAction(event -> {
                    rtnDialogVlue = 0;
                    window.close();
                });

                hBoxModal.getChildren().addAll(btnOk, btnClose);

                vBoxModal.getChildren().addAll(lblWindw, hBoxModal);

                window.setScene(windwscene);
                window.setTitle("Warning");
                window.showAndWait();
            }
            return rtnDialogVlue;
        }
    }

    public class Find {
        public String findFileOrDrctry(String title, Stage stage, int workmode) {
            String path = new String();
            try {
                if (workmode == 0) {
                    FileChooser choser = new FileChooser();
                    choser.setTitle(title);
                    path = choser.showOpenDialog(stage).getPath();
                } else if (workmode == 1) {
                    DirectoryChooser choser = new DirectoryChooser();
                    choser.setTitle(title);
                    path = choser.showDialog(stage).getPath();
                }
            } catch (RuntimeException v) {
                System.out.println("Ошибка поиска файла/папки: " + v);
            }
            return path;
        }

        public void findGameLocation(Stage primaryStage, String warning) {
            Writemodules Wm = new Writemodules();
            String lmBufPathToFile = "";
            String mTitleFileChoser = "Select the location of SPT-AKI Launcher.exe";
            int lmChk = 0;

            String settingbuffer[] = new String[12];
            String settingbuffer1[] = new String[12];
            int modelsearching = 0;

            String pathtoitems = "";
            String pathtoglobals = "";

            String pathtocopy;

            int cncl = 0;

            if (lmChk == 0) {
                String exception = "";
                do {
                    lmChk = 0;
                    try {
                        lmBufPathToFile = Wm.Find.findFileOrDrctry(mTitleFileChoser, primaryStage, 0);
                        if (!lmBufPathToFile.contains("Launcher.exe") ){
                            if (Wm.AppWindow.dialogWindow(warning) == 1) {
                                cncl = 1;
                            }
                        }
                        lmChk = 1;
                        throw new Exception(lmBufPathToFile);
                    } catch (Exception b) {
                        exception = b.toString();
                    }
                } while ((cncl == 0) && (!lmBufPathToFile.contains("Launcher.exe")));
            }

            if ((cncl == 0) & (lmChk == 1)) {

                for(int i = 0; i < lmBufPathToFile.length(); i++){
                    if (lmBufPathToFile.charAt(i) == '\\'){
                        lmChk = i;
                    }
                }

                File dir = new File(lmBufPathToFile.substring(0,lmChk));
                File[] listOfFiles = dir.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].getName().contains("EscapeFromTarkov.exe")) {
                        lmBufPathToFile = listOfFiles[i].getPath()
                                .replace("\\EscapeFromTarkov.exe", "");
                    }
                }

                if (modelsearching == 0) {
                    settingbuffer = Wm.Settings.readSettings();
                    settingbuffer1 = settingbuffer.clone();
                }
                if (Wm.Find.findFileDrctry(lmBufPathToFile, "Aki_Data", 0) == "1") {
                    modelsearching = 2;
                } else if (Wm.Find.findFileDrctry(lmBufPathToFile, "packages", 0) == "1") {
                    modelsearching = 1;
                }
                if (modelsearching == 0) {
                    modelsearching = -1;
                }

                if (modelsearching == 2) {
                    if (Wm.Find.findFileDrctry(lmBufPathToFile + "\\Aki_Data\\Server\\database", "globals.json", 0) == "1") {
                        settingbuffer1[8] = lmBufPathToFile;
                        lmBufPathToFile = lmBufPathToFile + "\\Aki_Data\\Server\\database";
                        settingbuffer1[9] = lmBufPathToFile + "\\templates\\items.json";
                        pathtoitems = lmBufPathToFile + "\\templates\\items.json";
                        settingbuffer1[10] = lmBufPathToFile + "\\globals.json";
                        pathtoglobals = lmBufPathToFile + "\\globals.json";
                    }
                }
                if (modelsearching == 1) {
                    if (Wm.Find.findFileDrctry(lmBufPathToFile +
                            "\\packages\\eft-database\\db", "globals.json", 0) == "1") {
                        settingbuffer1[8] = lmBufPathToFile;
                        lmBufPathToFile = lmBufPathToFile + "\\packages";
                        settingbuffer1[9] = lmBufPathToFile +
                                "\\eft-database\\db\\templates\\items.json";
                        pathtoitems = lmBufPathToFile + "\\eft-database\\db\\templates\\items.json";
                        settingbuffer1[10] = lmBufPathToFile +
                                "\\packages\\eft-database\\db\\globals.json";
                        pathtoglobals = lmBufPathToFile + "\\packages\\eft-database\\db\\globals.json";

                    }
                }
                if (modelsearching > 0) {
                    settingbuffer1[2] = System.getProperty("user.dir") +
                            "\\Save\\FileChanges\\Gameitems\\items.json";
                    settingbuffer1[3] = System.getProperty("user.dir") +
                            "\\Save\\FileChanges\\Gameglobals\\globals.json";
                    settingbuffer1[11] = System.getProperty("user.dir") +
                            "\\Save\\*";
                    String[][] doublemassive = Wm.Settings.analyzeStngsChngs(settingbuffer, settingbuffer1);
                    int[] changesbuffer = new int[doublemassive[0].length];
                    String[] lmSvdChrsbuffer = new String[doublemassive[1].length];
                    for (int i = 0; i < doublemassive[0].length; i++) {
                        changesbuffer[i] = Integer.valueOf(doublemassive[0][i]);
                    }
                    lmSvdChrsbuffer = doublemassive[1];
                    Wm.Settings.writeChngsInStngs(changesbuffer, lmSvdChrsbuffer);

                    //Считывания исходного файла игры в директорию приложения для востановления

                    pathtocopy = System.getProperty("user.dir") + "\\Save\\BackupFiles\\Backupglobals";
                    Wm.Write.writeFile(pathtocopy, pathtoglobals, 0, false);
                    pathtocopy = System.getProperty("user.dir") + "\\Save\\BackupFiles\\Backupitems";
                    Wm.Write.writeFile(pathtocopy, pathtoitems, 0, false);

                    //Считывания файлов для замены с иходников
                    pathtocopy = System.getProperty("user.dir") + "\\Save\\FileChanges\\Gameglobals";
                    Wm.Write.writeFile(pathtocopy, pathtoglobals, 0, false);
                    pathtocopy = System.getProperty("user.dir") + "\\Save\\FileChanges\\Gameitems";
                    Wm.Write.writeFile(pathtocopy, pathtoitems, 0, false);
                }
            }
        }

        public String findFileDrctry(String path, String filename, int workmode) {
            File dir = new File(path);
            File[] listOfFiles = dir.listFiles();
            String mRtrnPathToDrctry = "";

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].getName().contains(filename)) {
                    mRtrnPathToDrctry = listOfFiles[i].getPath();
                }
            }

            if (workmode == 0 & mRtrnPathToDrctry.length() > 0) {
                mRtrnPathToDrctry = "1";
            } else if (workmode == 0 & mRtrnPathToDrctry.length() <= 0) {
                mRtrnPathToDrctry = "0";
            }
            return mRtrnPathToDrctry;
        }

        public ObservableList<String> findFilesInDrctry(String filepath, String pathtobackupfile, int workmode) {
            File filetochange = new File(filepath);

            String dir = new String(filepath);

            File[] listfiles = (filetochange.listFiles());
            ObservableList<String> mRtrnObsrvblList = FXCollections.observableArrayList();

            //TODO Установить востановление файла по копированию из директории игры + бахнуть возможность востановления по логу
            if (!filetochange.canRead()) {
                Writemodules Wm = new Writemodules();
                Wm.Write.writeFile(filetochange.getPath(), pathtobackupfile, 1, false);
            }

            filetochange = new File(dir);

            if (listfiles.length > 0) {
                for (int i = 0; listfiles.length > i; i++) {
                    mRtrnObsrvblList.add(listfiles[i].getName());
                }
            }
            return mRtrnObsrvblList;
        }
    }

    public class Write {
        public void writeFile(String pathwrite, String scanfilepath, int writemode, boolean rewrtFile) {
            try {
                File itemscopy;
                File scan = new File(scanfilepath);

                //Выбор прописываемой директории itemscopy
                if (writemode == 0) {
                    itemscopy = new File(pathwrite + "\\" + scan.getName());
                } else {
                    itemscopy = new File(pathwrite);
                }

                FileWriter FW = new FileWriter(itemscopy);
                BufferedWriter BW = new BufferedWriter(FW);
                Scanner sc = new Scanner(scan);

                if (rewrtFile) {
                    itemscopy = new File(pathwrite + '1');
                }

                if (!itemscopy.canWrite()) {
                    itemscopy.createNewFile();
                }

                while (sc.hasNextLine()) {
                        BW.write(sc.nextLine());
                        BW.newLine();
                }

                BW.close();
                FW.close();
                sc.close();

                if (rewrtFile) {
                    scan.delete();
                    itemscopy.renameTo(scan);
                }
            } catch (IOException M) {
                System.out.println("Ошибка при записи файла: " + M);
            }
        }
    }
}
