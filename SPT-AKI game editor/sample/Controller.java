package sample;

import java.io.*;
import java.util.*;

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
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {

    //TODO Сделать возможность написания действий в окнах
    //TODO Бахнуть добавление строк
    //TODO Сделать создание пресета через считывания записанных данных в блокнот пользователем

    //TODO При добавлении строк нужно бахнуть механизм анализа сколько нужно пробелов по предыдущей строке
    //TODO Реализовать выправление строк по кнопке, пушо ета оптимизация вотъ
    //TODO Доработать мульти выбор в javafx
    //TODO Сделать проверки при работе с листами, по типу если есть теги тогда можно удалить иначе ничего не будет
    //TODO Убрать размытие в textarea при прописывании многопотока в приложении Platform.runLater ([...])
    //TODO Сделать возможность добавления изменненой характеристики в пресет, если она уже есть

    private Stage primaryStage;
    private Scene scene;

    private final Writemodules Wm = new Writemodules();
    private final UniversalModules UniModls = new UniversalModules();
    private final readobjectarray ReadObjectArray = new readobjectarray();

    private int mgInitBufrStngsLoad = 0;
    private String[] gStngBufr = new String[13];
    private String[] gStngBufr1 = new String[13];
    private Globalarray gMainItmArr = new Globalarray();
    private final GlobalsSectionArray gGlblsSectnArr = new GlobalsSectionArray();
    private fastlinkobject gFstLnkObj = new fastlinkobject();

    private Preset gLoadPrst = new Preset();
    private AllLocations gLctnAll = new AllLocations();

    //TODO Сделать блок смещения линий
    /*Блок переменных для работы с загрузкой файлов игры типо items.json
    int linsinitms = 0, lineinglobls = 0, linsinbckupitms = 0, lineinbckupglobls = 0;
    ArrayList<String> lineshiftbackupitems = new ArrayList<String>();
    ArrayList<String> lineshiftbackupglobals = new ArrayList<String>();
    ArrayList<String> lineshiftitems = new ArrayList<String>();
    ArrayList<String> lineshiftglobals = new ArrayList<String>();
    */

    //int gSaveGlbs = 0, gSaveItms = 0;
    private int gItmVlueLnk = 0;
    int gGlblVlueLnk;
    private int gLoadItmIndx = 0;
    private int gChsdPrstIndx = 0;
    private int gPrstLoadIndx = 0;

    private int gCrtPos = 0;
    private int gErrCnt = 0;
    private int gWrkmd = 0;
    private int gScndWrkmd = 0;
    private int gPrstItmIndx = -1;
    private String gTmpVlue = "";
    private String gLnkPrstItm = "";
    private String gChsdMainType = "";
    private String gChsdScndType = "";
    private String chosedtags = "";
    private String choseditem = "";
    private String chosedpreset = "";

    //Блок переменных для управления подбором загружаемого предмета
    private int gChsTp = 0;
    private int gChsScndTp = 0;
    private int gChsTag = 0;
    private int gChsItm = 0;
    private int gChsLoadPrst = 0;
    private int gKeyChsTp = 0;
    private int gKeyChsScndTp = 0;
    private int gKeyChsTag = 0;
    private int gKeyChsItm = 0;

    //Переменные для загрузки параметров локаций
    private int gLctnChsdIndx = -1;

    //ArrayList для динамического считывания ошибок
    private static final ArrayList<ArrayList<String>> gsErrs = new ArrayList<ArrayList<String>>();

    //Массив для работы с ссылками объекта
    private final ArrayList<fastlinkobject> gFstLnkItm = new ArrayList<fastlinkobject>();
    //Массивы для востановления замены в линиях
    private final ArrayList<String> gGlblEdtrBckp = new ArrayList<String>();
    private final ArrayList<String> gItmEdtrBckp = new ArrayList<String>();
    private final ArrayList<String> gLctnEdtrBckp = new ArrayList<String>();
    //Глобальный массив пресетов в папке, загруженных и не загруженных
    private final ArrayList<Preset> gAllPrsts = new ArrayList<Preset>();
    //Уже загруженные пресеты
    private final ArrayList<String> gPrstsLoadInGame = new ArrayList<String>();
    //Массив для работы с изменениями объектов в выбранном пресете при его создании либо изменении
    private final ArrayList<PresetObject> gTmpPrstLst = new ArrayList<PresetObject>();
    //Значения для загрузки/выгрузки пресета
    private final ArrayList<String[]> gTmpPrstAddRmv = new ArrayList<String[]>();

    //Листы для подбора тегов, типов и самого предмета
    private final ObservableList gObLstScndTp = FXCollections.observableArrayList();
    private final ObservableList gObTypes = FXCollections.observableArrayList();
    private ObservableList gObChsdTypes = FXCollections.observableArrayList();
    private final ObservableList gObScndTypes = FXCollections.observableArrayList();
    private ObservableList gObChsdScndTypes = FXCollections.observableArrayList();
    private final ObservableList gObChsdTags = FXCollections.observableArrayList();
    private final ObservableList gObTmpTagsBufr = FXCollections.observableArrayList();
    private final ObservableList gObTags = FXCollections.observableArrayList();
    private final ObservableList gObItems = FXCollections.observableArrayList();
    private ObservableList gObChsdItms = FXCollections.observableArrayList();


    @FXML
    private void initialize() {

        //Проверка путей
        try {
            File Settings = new File(System.getProperty("user.dir")
                    + "\\Settings.txt");
            File BackupItems = new File(System.getProperty("user.dir")
                    + "\\Save\\BackupFiles\\Backupitems\\");
            File BackupGlobals = new File(System.getProperty("user.dir")
                    + "\\Save\\BackupFiles\\Backupglobals\\");
            File BackupLctns = new File(System.getProperty("user.dir")
                    + "\\Save\\BackupFiles\\Backuplctns\\");
            File Buffers = new File(System.getProperty("user.dir")
                    + "\\Save\\Buffers\\");
            File FileChangesGameglobals = new File(System.getProperty("user.dir")
                    + "\\Save\\FileChanges\\Gameglobals\\");
            File FileChangesGameitems = new File(System.getProperty("user.dir")
                    + "\\Save\\FileChanges\\Gameitems\\");

            File FileChangesGamelctns;

            File FileChangesMain = new File(System.getProperty("user.dir")
                    + "\\Save\\FileChangesMain\\");
            File Preset = new File(System.getProperty("user.dir")
                    + "\\Save\\Presets\\");

            ArrayList<String> LctnsAdrs = new ArrayList<String>(Arrays.asList("bigmap","factory4_day",
                    "factory4_night","interchange","laboratory", "lighthouse","rezervbase","shoreline",
                    "woods"));

            String lctnPath = System.getProperty("user.dir")
                    + "\\Save\\FileChanges\\Gamelcnts\\";

            if (!Settings.canRead()) {
                Settings.createNewFile();
            }

            if (!Buffers.exists()) {
                Buffers.mkdirs();
            }

            if (!BackupItems.exists()){
                BackupItems.mkdirs();
            }

            if (!BackupGlobals.exists()){
                BackupGlobals.mkdirs();
            }

            if (!BackupLctns.exists()) {
                BackupLctns.mkdirs();
            }

            if (!FileChangesGameglobals.exists()) {
                FileChangesGameglobals.mkdirs();
            }

            if (!FileChangesGameitems.exists()) {
                FileChangesGameitems.mkdirs();
            }

            for (int i = 0; i < LctnsAdrs.size(); i++){
                FileChangesGamelctns = new File(lctnPath + LctnsAdrs.get(i) + "\\");
                if (!FileChangesGamelctns.exists()){
                    FileChangesGamelctns.mkdirs();
                }
            }

            if (!FileChangesMain.exists()) {
                FileChangesMain.mkdirs();
            }

            if (!Preset.exists()) {
                Preset.mkdirs();
            }

            if (Settings.length() < 1) {
                Writemodules Wm = new Writemodules();
                Wm.Recovery.writeSettings();
            }
        }catch (IOException v) {System.out.println("Ошибка востановления путей: " + v);}


        //Чтение сохранненых режимов для выбора программы + проверка выбран ли путь к игре
        if (mgInitBufrStngsLoad < 1) {
            mgInitBufrStngsLoad = 1;
            gStngBufr = Wm.Settings.readSettings();
            gStngBufr1 = gStngBufr.clone();
        }

        //Загрузка языкового пакета
        try {
            /*
            Для считывания языковых настроек, первое значение это строка с которой начинается считывание

            Модуль сложный и комплексный, позволяет быстро получить данные по считыванию:
            Первый уровень lsLngLoadLns отвечает за перевод текста кроме первого значения,
            поскольку это начальная строка([0][0]), во всех уровнях  тут не будет значения.
            Второй уровень за перевод всплывающих ошибок.
            Третий уровень за перевод всплывающих подсказок которых пока нет, но они будут.
            */

            String lng;

            if (gStngBufr1[1] == null){
                lng = "English";
            } else {
                lng = gStngBufr1[1];
            }

            int[][] lsLngLoadLns = new int[][]{{54,10,4,8,16,24,7,6,6,3,3},{0,11,9,21,9,7,9,2}};

            ArrayList<ArrayList<ArrayList<String>>> languagebuffer = Wm.Language.findLangFile(
                    lng, (System.getProperty("user.dir") + "\\Lib\\Languages\\"), lsLngLoadLns);

            globalItemValues.setText(languagebuffer.get(0).get(0).get(1));
            //globalSecondEditor.setText(languagebuffer.get(0).get(0).get(2));
            globalGlobalsEditor.setText(languagebuffer.get(0).get(0).get(3));
            globalMenuTitledPane.setText(languagebuffer.get(0).get(0).get(4));
            globalSettingsButton.setText(languagebuffer.get(0).get(0).get(5));
            globalAboutProgramButton.setText(languagebuffer.get(0).get(0).get(6));
            globalChangelogButton.setText(languagebuffer.get(0).get(0).get(7));
            globalItemsEditorTitledPane.setText(languagebuffer.get(0).get(0).get(8));
            wrkwthprstPrstsTitledPane.setText(languagebuffer.get(0).get(0).get(9));

            saveprstNameLabel.setText(languagebuffer.get(0).get(1).get(1));
            saveprstOk.setText(languagebuffer.get(0).get(1).get(2));
            saveprstCancel.setText(languagebuffer.get(0).get(1).get(3));

            chngprstChoiceLabel.setText(languagebuffer.get(0).get(2).get(1));
            chngprstChngNameLabel.setText(languagebuffer.get(0).get(2).get(2));
            chngprstChngDescriptionLabel.setText(languagebuffer.get(0).get(2).get(3));
            chngprstLoadBtn.setText(languagebuffer.get(0).get(2).get(4));
            chngprstCloseBtn.setText(languagebuffer.get(0).get(2).get(5));
            chngprstRewrite.setText(languagebuffer.get(0).get(2).get(6));
            chngprstRewriteCancel.setText(languagebuffer.get(0).get(2).get(7));

            loadprstWrkMode.setText(languagebuffer.get(0).get(3).get(1));
            loadprstLinesArray.setText(languagebuffer.get(0).get(3).get(2));
            loadprstAddRemove.setText(languagebuffer.get(0).get(3).get(3));
            loadprstDeletePrst.setText(languagebuffer.get(0).get(3).get(4));
            loadprstViewPrst.setText(languagebuffer.get(0).get(3).get(5));
            loadprstLoadItms.setText(languagebuffer.get(0).get(3).get(6));
            loadprstLoadLinesInItm.setText(languagebuffer.get(0).get(3).get(7));
            loadprstLoadBtn.setText(languagebuffer.get(0).get(3).get(8));
            loadprstClosePane.setText(languagebuffer.get(0).get(3).get(9));
            loadprstAvailableLabel.setText(languagebuffer.get(0).get(3).get(10));
            loadprstAddedLabel.setText(languagebuffer.get(0).get(3).get(11));
            loadprstDescriptionLabel.setText(languagebuffer.get(0).get(3).get(12));
            loadprstLabel.setText(languagebuffer.get(0).get(3).get(13));
            loadprstItemLabel.setText(languagebuffer.get(0).get(3).get(14));
            loadprstItemLines.setText(languagebuffer.get(0).get(3).get(15));

            wrkwthprstResetSelection.setText(languagebuffer.get(0).get(4).get(1));
            wrkwthprstMakePrstBtn.setText(languagebuffer.get(0).get(4).get(2));
            wrkwthprstSavePrstBtn.setText(languagebuffer.get(0).get(4).get(3));
            wrkwthprstSrchPrstItm.setText(languagebuffer.get(0).get(4).get(4));
            wrkwthprstPrstCancel.setText(languagebuffer.get(0).get(4).get(5));
            wrkwthprstPrstOk.setText(languagebuffer.get(0).get(4).get(6));
            wrkwthprstPrstListWrkMod.setText(languagebuffer.get(0).get(4).get(7));
            wrkwthprstArrMd.setText(languagebuffer.get(0).get(4).get(8));
            wrkwthprstArrToChng.setText(languagebuffer.get(0).get(4).get(9));
            wrkwthprstAddToLst.setText(languagebuffer.get(0).get(4).get(10));
            wrkwthprstClearPrstSelection.setText(languagebuffer.get(0).get(4).get(11));
            wrkwthprstChngItmOnPrst.setText(languagebuffer.get(0).get(4).get(12));
            wrkwthprstItmsRegstrt.setText(languagebuffer.get(0).get(4).get(13));
            wrkwthprstLoadPrstBtn.setText(languagebuffer.get(0).get(4).get(14));
            wrkwthprstChngPrstBtn.setText(languagebuffer.get(0).get(4).get(15));
            wrkwthprstRmvLines.setText(languagebuffer.get(0).get(4).get(16));
            wrkwthprstAddNewItm.setText(languagebuffer.get(0).get(4).get(17));
            wrkwthprstDltItm.setText(languagebuffer.get(0).get(4).get(18));
            wrkwthprstCancelPrst.setText(languagebuffer.get(0).get(4).get(19));
            wrkwthprstMainTypeLbl.setText(languagebuffer.get(0).get(4).get(20));
            wrkwthprstScndTypeLbl.setText(languagebuffer.get(0).get(4).get(21));
            wrkwthprstTagsLbl.setText(languagebuffer.get(0).get(4).get(22));
            wrkwthprstEntrItmLbl.setText(languagebuffer.get(0).get(4).get(23));

            mainsidepaneSaveInBckp.setText(languagebuffer.get(0).get(5).get(1));
            mainsidepaneUpldToGame.setText(languagebuffer.get(0).get(5).get(2));
            mainsidepaneGlblSaveInBfr.setText(languagebuffer.get(0).get(5).get(3));
            mainsidepaneGlblRstAll.setText(languagebuffer.get(0).get(5).get(4));
            mainsidepaneRstChngs.setText(languagebuffer.get(0).get(5).get(5));
            mainsidepaneItmWrkChngWrkMode.setText(languagebuffer.get(0).get(5).get(6));

            vluespnPrviousVlue.setText(languagebuffer.get(0).get(6).get(1));
            vluespnIdLabel.setText(languagebuffer.get(0).get(6).get(2));
            vluespnNameLabel.setText(languagebuffer.get(0).get(6).get(3));
            vluespnPrntLabel.setText(languagebuffer.get(0).get(6).get(4));
            vluespnLinkLabel.setText(languagebuffer.get(0).get(6).get(5));

            rdctrpaneAddLns.setText(languagebuffer.get(0).get(7).get(1));
            rdctrpaneAddLnsArr.setText(languagebuffer.get(0).get(7).get(2));
            rdctrpaneRmvLns.setText(languagebuffer.get(0).get(7).get(3));
            rdctrpaneRstNmbrOfLns.setText(languagebuffer.get(0).get(7).get(4));
            rdctrpaneChngNmbrOfLns.setText(languagebuffer.get(0).get(7).get(5));

            arraddpaneAddArrBtn.setText(languagebuffer.get(0).get(8).get(1));
            arraddpaneCancelBtn.setText(languagebuffer.get(0).get(8).get(2));

            globalLocationsEditor.setText(languagebuffer.get(0).get(9).get(1));
            edtrSetBtsNmr.setText(languagebuffer.get(0).get(9).get(2));

            //Считывания массива ошибок из базы в рабочий массив
            for (int i = 0; i < languagebuffer.get(1).size(); i++){
                gsErrs.add(languagebuffer.get(1).get(i));
            }

        }catch (Exception b) {
            System.out.println("При загрузке языкового пакета ошибка: " + b);
        }

        //Выбор каталога
        if (!gStngBufr[9].contains("items.json")) {
            Wm.Find.findGameLocation(primaryStage, gsErrs.get(6).get(0));
            gStngBufr = Wm.Settings.readSettings();
            gStngBufr1 = gStngBufr.clone();
        }

        //Загрузка доступных пресетов
        try {
            PresetFile prstFl = new PresetFile();
            gAllPrsts.addAll(prstFl.getDesPresets());
            System.out.println("Количество доступных пресетов: " + gAllPrsts.size());
            for (int i = 0; i < gAllPrsts.size(); i++) {
                chngprstComboBox.getItems().add(gAllPrsts.get(i).getPresetName());
            }
        } catch (Exception v) {
            System.out.println("Ошибка загрузки доступных пресетов: " + v);
        }


        //Загрузка параметров локаций
        try {
            LocationChgnsStrctr lctnStrct = new LocationChgnsStrctr();
            gLctnAll.addLocations(lctnStrct.getLctnsData(gStngBufr1[10]));
            for (int i = 0; i < gLctnAll.getLcntsSize(); i++){
                edtrChsngLctn.getItems().add((gLctnAll.getLocation(i).getLocNm()));
            }
        } catch (Exception v) {
            System.out.println("Ошибка загрузки данных локации: " + v);
        }


        //Загрузка уже выбранных пресетов в строковой список
        try {
            File lmfDir = new File(gStngBufr1[11].replace("*", "FileChanges")); //path указывает на директорию
            File[] lmfListFls = lmfDir.listFiles();
            for (int i = 0; i < lmfListFls.length; i++) {
                if (lmfListFls[i].getName().contains(".obj")) {
                    gPrstsLoadInGame.add(lmfListFls[i].getName().replace(".obj", ""));
                }
            }
            System.out.println("Загружено пресетов в игру из доступных: " + gPrstsLoadInGame.size());
        } catch (Exception v) {
            System.out.println("Ошибка считывания пресетов: " + v);
        }

        //Считывание предметов
        try {
            gMainItmArr = ReadObjectArray.readarray();
        } catch (Exception v) {
            System.out.println("Ошибка считывания предметов: " + v);
        }
        wrkwthprstChsTypeOfItm.getItems().addAll("");
        wrkwthprstChsScndType.getItems().addAll("");
        for (int i = 0; i < gMainItmArr.getarraysize(); i++) {
            //Заглушка в коде, не определяет уже заданные значения
            wrkwthprstChsTypeOfItm.getItems().addAll(gMainItmArr.getglblsecondtype(i));
            for (int o = 0; o < gMainItmArr.getarraysizescnd(i); o++) {
                wrkwthprstChsScndType.getItems().addAll(gMainItmArr.getitemscndtype(i, o));
                gObChsdTags.addAll(gMainItmArr.getalltagsthrd(i, o));
                for (int p = 0; p < gMainItmArr.getarraysizethrd(i, o); p++) {
                    ArrayList<ArrayList<String>> lmArr = gMainItmArr.getobjects(i, o, p);
                    for (int l = 0; l < lmArr.get(2).size(); l++) {
                        gFstLnkObj.makeobject(lmArr.get(0), lmArr.get(1).get(l), lmArr.get(2).get(l), lmArr.get(3).get(l), lmArr.get(4).get(l), lmArr.get(5).get(l), lmArr.get(6).get(l), i, o, p, l);
                        gFstLnkItm.add(gFstLnkObj);
                        gFstLnkObj = new fastlinkobject();
                    }
                }
            }
        }
        //Загрузка смещения линий, пока неработает, модуль не эффективен, на переработку
        /*
        try {
            File glblsbckp = new File(gStngBufr1[11].replace("*", "FileChanges\\Backupglobals\\") +
                    gStngBufr1[3].substring((gStngBufr1[3].lastIndexOf("Save\\") + 5)));
            File itmsbckp = new File(gStngBufr1[11].replace("*", "FileChanges\\Backupitems\\")  +
                    gStngBufr1[2].substring((gStngBufr1[2].lastIndexOf("Save\\") + 5)));
            File glbls = new File(gStngBufr1[11].replace("*", "FileChanges\\Gameglobals\\globals.json"));
            File itms = new File(gStngBufr1[11].replace("*", "FileChanges\\Gameitems\\items.json"));

            FileReader FR;
            BufferedReader BR;
            Scanner scanner;
            String lmStrng = "";
            if (glblsbckp.canRead()){
                FR = new FileReader(glblsbckp);
                BR = new BufferedReader(FR);
                scanner = new Scanner(BR);

                do {
                    lmStrng = scanner.nextLine();
                    lineshiftbackupglobals.add(lmStrng);
                } while (scanner.hasNextLine());

            }

            if (itmsbckp.canRead()){
                FR = new FileReader(itmsbckp);
                BR = new BufferedReader(FR);
                scanner = new Scanner(BR);

                do {
                    lmStrng = scanner.nextLine();
                    lineshiftbackupitems.add(lmStrng);
                } while (scanner.hasNextLine());

            }

            if (glbls.canRead()){
                FR = new FileReader(glbls);
                BR = new BufferedReader(FR);
                scanner = new Scanner(BR);

                do {
                    lmStrng = scanner.nextLine();
                    lineshiftglobals.add(lmStrng);
                } while (scanner.hasNextLine());

            }

            if (itms.canRead()){
                FR = new FileReader(itms);
                BR = new BufferedReader(FR);
                scanner = new Scanner(BR);

                do {
                    lmStrng = scanner.nextLine();
                    lineshiftitems.add(lmStrng);
                } while (scanner.hasNextLine());

            }

        } catch (IOException v) {
            System.out.println("В загрузке смещения линий из папки ошибка: " + v);
        }
        */

        //
        wrkwthprstChsTags.getItems().addAll("view selected tags", "Match items", "Change tag", "Delete tag", "Delete all tags");
        //Прописывание значений для выбора режима поиска, пока работает только this item
        //"Items on second type", "Items in tag", пока не настроены
        wrkwthprstPrstWrkMd.getItems().addAll("", "This item");
        //Сброс значений в подборе предмета
        wrkwthprstChsTypeOfItm.setValue("");
        wrkwthprstChsScndType.setValue("");
        wrkwthprstChsTags.setValue(null);
        //TODO Переделать сериализованный объект пушо много жрет
        for (int i = 0; i < gFstLnkItm.size(); i++) {
            wrkwthprstChsItm.getItems().add(gFstLnkItm.get(i).getname());
        }

        //Модуль считывания значений в globals
        try {
            File globaltoread = new File(gStngBufr1[3]);
            FileReader FR = new FileReader(globaltoread);
            Scanner scanner = new Scanner(FR);

            String lmLine = "", lmLineBgn = "", lmLineEnd = "";
            List<String> lmListNames = Arrays.asList("Globalvalues", "Aim stats & fps", "Exp modifiers",
                    "skill stats & vault modifiers", "Skill progress", "Global health stats",
                    "Stamina", "aid & food buffs customization", "Armor class stats", "Ragfair stats", "Global relationship", "Bots");

            List<String> lmListLinesBgnName = Arrays.asList("\"AimPunchMagnitude\":", "\"match_end\":", "\"victimLevelExp\":",
                    "\"trade_level\":", "\"SessionsToShowHotKeys\":", "\"GlobalItemPriceModifier\":",
                    "\"ArmorMaterials\":", "\"LegsOverdamage\":", "\"Stimulator\":", "\"Tremor\":", "\"RagFair\":",
                    "\"Stamina\":", "\"SkillMinEffectiveness\":", "\"EventType\":", "\"SkillsSettings\":", "\"Aiming\":");

            List<String> lmListLinesEndName = Arrays.asList("\"match_end\":", "\"loot\":", "\"level\":",
                    "\"loot_attempts\":", "\"SavagePlayCooldownDevelop\":", "\"BaseCheckTime\":",
                    "\"LegsOverdamage\":", "\"Stimulator\":", "\"Tremor\":", "\"rating\":", "\"handbook\":",
                    "\"RequirementReferences\":", "\"EventType\":", "\"SkillsSettings\":", "\"AzimuthPanelShowsPlayerOrientation\":", "\"time\":");

            int lmChk = 30000;
            int lmCnt = 0;
            int lmReadIndx = 0;

            lmChk = 0;

            for (int i = 0; i < 12; i++) {
                itemgroup lmItmGrpArr = new itemgroup();
                gGlblsSectnArr.putarray(lmItmGrpArr);
                gGlblsSectnArr.getitemsarray().get(i).setname(lmListNames.get(i));
            }
            for (int i = 0; i < 12; i++) {
                gGlblsSectnArr.setname(lmListNames.get(i));
            }

            int lmTmpBgnLineIndx = 0;
            int lmTmpEndLineIndx = 0;
            do {
                itemvaluesarray lmTmpItmValues = new itemvaluesarray();
                lmTmpBgnLineIndx = 0;
                lmTmpEndLineIndx = 0;
                lmLineBgn = lmListLinesBgnName.get(lmCnt);
                lmLineEnd = lmListLinesEndName.get(lmCnt);
                if (lmLine.contains(lmLineBgn)) {
                    if (lmLine.contains(lmLineBgn)) {
                        lmTmpBgnLineIndx = lmChk;
                        lmTmpItmValues.putsting(lmLine.trim());
                    }
                } else {
                    do {
                        lmChk++;
                        lmLine = scanner.nextLine();
                        if (lmLine.contains(lmLineBgn)) {
                            lmTmpBgnLineIndx = lmChk;
                            lmTmpItmValues.putsting(lmLine.trim());
                        }
                    } while (lmTmpBgnLineIndx == 0 && scanner.hasNextLine());
                }
                do {
                    lmChk++;
                    lmLine = scanner.nextLine();
                    lmTmpItmValues.putsting(lmLine.trim());
                    if (lmLine.contains(lmLineEnd)) {
                        lmTmpEndLineIndx = lmChk;
                    }
                } while (lmTmpEndLineIndx == 0 && scanner.hasNextLine());

                lmTmpItmValues.putarraylines(lmTmpBgnLineIndx, lmTmpEndLineIndx);
                lmReadIndx = (lmTmpItmValues.getarray().size() - 1);
                switch (lmCnt) {
                    case (0):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(4).putarray(lmTmpItmValues);
                        break;
                    case (1):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(2).putarray(lmTmpItmValues);
                        break;
                    case (2):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        --lmReadIndx;
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(2).putarray(lmTmpItmValues);
                        break;
                    case (3):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        --lmReadIndx;
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(10).putarray(lmTmpItmValues);
                        break;
                    case (4):
                        gGlblsSectnArr.getitemsarray().get(11).putarray(lmTmpItmValues);
                        break;
                    case (5):
                        gGlblsSectnArr.getitemsarray().get(0).putarray(lmTmpItmValues);
                        break;
                    case (6):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(8).putarray(lmTmpItmValues);
                        break;
                    case (7):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(5).putarray(lmTmpItmValues);
                        break;
                    case (8):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(7).putarray(lmTmpItmValues);
                        break;
                    case (9):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        --lmReadIndx;
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(5).putarray(lmTmpItmValues);
                        break;
                    case (10):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(9).putarray(lmTmpItmValues);
                        break;
                    case (11):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(6).putarray(lmTmpItmValues);
                        break;
                    case (12):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(4).putarray(lmTmpItmValues);
                        break;
                    case (13):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(0).putarray(lmTmpItmValues);
                        break;
                    case (14):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(3).putarray(lmTmpItmValues);
                        break;
                    case (15):
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        --lmReadIndx;
                        lmTmpItmValues.getarray().remove(lmReadIndx);
                        gGlblsSectnArr.getitemsarray().get(1).putarray(lmTmpItmValues);
                        break;
                }
                lmCnt++;
                if (lmCnt == 16) break;
            } while (scanner.hasNextLine() & scanner.hasNextLine());

            for (int i = 0; i < gGlblsSectnArr.getgMainItmArrsize(); i++) {
                edtrChsngGlblGrp.getItems().add(gGlblsSectnArr.getitemsarray().get(i).getglobalname());
            }
        } catch (IOException v) {
            System.out.println("Ошибка создания групп глобальных характерисик: " + v);
        }

        /*
        //Переопределение Textarea
        //Textarea при сохранении пресета
        saveprstTextArea.setCache(false);
        ScrollPane svprstsp = (ScrollPane)saveprstTextArea.getChildrenUnmodifiable().get(0);
        svprstsp.setCache(false);
        for (Node n : svprstsp.getChildrenUnmodifiable()) {
            n.setCache(false);
        }
        //Textarea при замене пресета
        chngprstDescription.setCache(false);
        ScrollPane chngprstsp = (ScrollPane)chngprstDescription.getChildrenUnmodifiable().get(0);
        chngprstsp.setCache(false);
        for (Node n : chngprstsp.getChildrenUnmodifiable()) {
            n.setCache(false);
        }
        //Textarea при замене пресета
        loadprstDescription.setCache(false);
        ScrollPane ldprstsp = (ScrollPane)loadprstDescription.getChildrenUnmodifiable().get(0);
        ldprstsp.setCache(false);
        for (Node n : ldprstsp.getChildrenUnmodifiable()) {
            n.setCache(false);
        }
        */

        //Прописывание заглушки изображения выбранного предмета
        /*
        File m = new File(System.getProperty("user.dir") + "\\Lib\\Images\\16428480741900.png");
        Image aaaaaaaaaaa = new Image(m.toURI().toString(), 77, 78, false, false);
        vluespnItmImage.setImage(aaaaaaaaaaa);
        */

        //Режим выбора линий в списках, одиночный или множественный
        wrkwthprstVluesOnPrstItms.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        wrkwthprstAddedPrstVlues.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadprstAvailablePrsts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadprstAddPrsts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadprstItemsInLoadPrst.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadprstItemsLinesVievMode.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Считывание настроек анимаций
        globalMenuTitledPane.setAnimated(Boolean.valueOf(gStngBufr1[4]));
        globalItemValues.setAnimated(Boolean.valueOf(gStngBufr1[5]));
        wrkwthprstPrstsTitledPane.setAnimated(Boolean.valueOf(gStngBufr1[6]));
        globalItemsEditorTitledPane.setAnimated(Boolean.valueOf(gStngBufr1[7]));

        //Переопределение механики замены значения в ячейке Listviev
        edtrItemValuesList.setCellFactory(lv -> new ItemsValuesListCell());
        edtrGlblLstView.setCellFactory(glv -> new ItemsValuesListCell());
        wrkwthprstVluesOnPrstItms.setCellFactory(vpi -> new ItemsValuesListCell());
        edtrLctnsLstView.setCellFactory(lctn -> new ItemsValuesListCell());

        //Переопределение Combobox для того, чтобы пробел не выбирал значение
        ComboBoxListViewSkin<String> ChoiceitemtypeListViewSkin = new ComboBoxListViewSkin<String>(wrkwthprstChsTypeOfItm);
        ChoiceitemtypeListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        });
        wrkwthprstChsTypeOfItm.setSkin(ChoiceitemtypeListViewSkin);

        ComboBoxListViewSkin<String> SecondtypeListViewSkin = new ComboBoxListViewSkin<String>(wrkwthprstChsScndType);
        SecondtypeListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        });
        wrkwthprstChsScndType.setSkin(SecondtypeListViewSkin);

        ComboBoxListViewSkin<String> ChoiseitemsListViewSkin = new ComboBoxListViewSkin<String>(wrkwthprstChsTags);
        ChoiseitemsListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        });
        wrkwthprstChsTags.setSkin(ChoiseitemsListViewSkin);

        ComboBoxListViewSkin<String> EnteritemListViewSkin = new ComboBoxListViewSkin<String>(wrkwthprstChsItm);
        EnteritemListViewSkin.getPopupContent().addEventFilter(KeyEvent.ANY, (event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        });
        wrkwthprstChsItm.setSkin(EnteritemListViewSkin);

        //Переопределение списков при загрузке пресетов
        //Потом переделать потому-что работает с запозданием
        loadprstAvailablePrsts.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            if (loadprstAvailablePrsts.getSelectionModel().getSelectedIndices().size() > 0){
                loadprstAddPrsts.getSelectionModel().clearSelection();
            }
        });

        loadprstAddPrsts.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            if (loadprstAddPrsts.getSelectionModel().getSelectedIndices().size() > 0){
                loadprstAvailablePrsts.getSelectionModel().clearSelection();
            }
        });
    }

    //Глобальные модули приложения
    @FXML
    private AnchorPane globalGlobalpane;

    @FXML
    private TitledPane globalItemValues;

    @FXML
    private TitledPane globalGlobalsEditor;

    @FXML
    private TitledPane globalLocationsEditor;

    @FXML
    private TitledPane globalMenuTitledPane;

    @FXML
    private Button globalSettingsButton;

    @FXML
    private Button globalAboutProgramButton;

    @FXML
    private Button globalChangelogButton;

    @FXML
    private TitledPane globalItemsEditorTitledPane;

    @FXML
    private Pane globalPresetsMakePane;

    @FXML
    private Pane globalPresetPane;

    @FXML
    private Pane globalItemsValuePane;
    //Конец

    //Модуль редактирования предметов

    @FXML
    private ListView<String> edtrItemValuesList;

    @FXML
    private ChoiceBox<String> edtrChsngGlblGrp;

    @FXML
    private ListView<String> edtrGlblLstView;

    @FXML
    private ChoiceBox<String> edtrChsngLctn;

    @FXML
    private ChoiceBox<String> edtrChsngLctnGrp;

    @FXML
    private ListView<String> edtrLctnsLstView;

    @FXML
    private Button edtrSetBtsNmr;
    //Конец

    //Панель при нажатии кнопки wrkwthprstSavePrstBtn
    @FXML
    private Pane saveprstPresetsPane;

    @FXML
    private Label saveprstNameLabel;

    @FXML
    private TextField saveprstTextField;

    @FXML
    private TextArea saveprstTextArea;

    @FXML
    private Button saveprstOk;

    @FXML
    private Button saveprstCancel;

    //Конец

    //Панель при нажатии Change preset
    @FXML
    private Pane chngprstMainPane;

    @FXML
    private  Label chngprstChoiceLabel;

    @FXML
    private  Label chngprstChngNameLabel;

    @FXML
    private  Label chngprstChngDescriptionLabel;

    @FXML
    private ComboBox<String> chngprstComboBox;

    @FXML
    private TextField chngprstLabelPresetName;

    @FXML
    private TextArea chngprstDescription;

    @FXML
    private Button chngprstLoadBtn;

    @FXML
    private Button chngprstCloseBtn;

    @FXML
    private Button chngprstRewrite;

    @FXML
    private Button chngprstRewriteCancel;
    //Конец

    //Модуль Load Preset
    @FXML
    private AnchorPane loadprstGlobalAnchorPane;

    @FXML
    private Pane loadprstDesriptionPane;

    @FXML
    private Pane loadprstItemsPane;

    @FXML
    private RadioButton loadprstWrkMode;

    @FXML
    private RadioButton loadprstLinesArray;

    @FXML
    private Button loadprstAddRemove;

    @FXML
    private Button loadprstDeletePrst;

    @FXML
    private Button loadprstViewPrst;

    @FXML
    private Button loadprstLoadItms;

    @FXML
    private Button loadprstLoadLinesInItm;

    @FXML
    private Button loadprstLoadBtn;

    @FXML
    private Button loadprstClosePane;

    @FXML
    private ListView<String> loadprstAvailablePrsts;

    @FXML
    private ListView<String> loadprstAddPrsts;

    @FXML
    private ListView<String> loadprstItemsInLoadPrst;

    @FXML
    private ListView<String> loadprstItemsLinesVievMode;

    @FXML
    private TextArea loadprstDescription;

    @FXML
    private Label loadprstChosedItemLabel;

    @FXML
    private Label loadprstNameItemLines;

    @FXML
    private Label loadprstChosedPrstLabel;

    @FXML
    private Label loadprstAvailableLabel;

    @FXML
    private Label loadprstAddedLabel;

    @FXML
    private Label loadprstDescriptionLabel;

    @FXML
    private Label loadprstLabel;

    @FXML
    private Label loadprstItemLabel;

    @FXML
    private Label loadprstItemLines;
    //Конец

    //Модуль работы с прессетами

    @FXML
    private Button wrkwthprstResetSelection;

    @FXML
    private Button wrkwthprstMakePrstBtn;

    @FXML
    private Button wrkwthprstSavePrstBtn;

    @FXML
    private Button wrkwthprstSrchPrstItm;

    @FXML
    private Button wrkwthprstPrstCancel;

    @FXML
    private Button wrkwthprstPrstOk;

    @FXML
    private RadioButton wrkwthprstPrstListWrkMod;

    @FXML
    private RadioButton wrkwthprstArrMd;

    @FXML
    private Button wrkwthprstArrToChng;

    @FXML
    private Button wrkwthprstAddToLst;

    @FXML
    private Button wrkwthprstClearPrstSelection;

    @FXML
    private Button wrkwthprstChngItmOnPrst;

    @FXML
    private Button wrkwthprstItmsRegstrt;

    @FXML
    private ComboBox<String> wrkwthprstChsTypeOfItm;

    @FXML
    private ComboBox<String> wrkwthprstChsScndType;

    @FXML
    private ComboBox<String> wrkwthprstChsTags;

    @FXML
    private ComboBox<String> wrkwthprstChsItm;

    @FXML
    private Label wrkwthprstMainTypeLbl;

    @FXML
    private Label wrkwthprstScndTypeLbl;

    @FXML
    private Label wrkwthprstTagsLbl;

    @FXML
    private Label wrkwthprstEntrItmLbl;

    @FXML
    private TitledPane wrkwthprstPrstsTitledPane;

    @FXML
    private Button wrkwthprstLoadPrstBtn;

    @FXML
    private Button wrkwthprstChngPrstBtn;

    @FXML
    private Button wrkwthprstRmvLines;

    @FXML
    private Button wrkwthprstAddNewItm;

    @FXML
    private Button wrkwthprstDltItm;

    @FXML
    private Button wrkwthprstCancelPrst;

    @FXML
    private ListView<String> wrkwthprstAddedPrstVlues;

    @FXML
    private ListView<String> wrkwthprstVluesOnPrstItms;

    @FXML
    private ChoiceBox<String> wrkwthprstPrstWrkMd;

    //Модуль работы со строками в объекте
    @FXML
    private Button mainsidepaneSaveInBckp;

    @FXML
    private Button mainsidepaneUpldToGame;

    @FXML
    private Button mainsidepaneGlblSaveInBfr;

    @FXML
    private Button mainsidepaneGlblRstAll;

    @FXML
    private Button mainsidepaneRstChngs;

    @FXML
    private RadioButton mainsidepaneItmWrkChngWrkMode;

    @FXML
    private Pane mainsidepaneItmWrkGlblLoadVluesPane;

    @FXML
    private Pane mainsidepaneItmWrkGlblLnsAddRmvPane;

    @FXML
    private Pane mainsidepaneItmWrkArrLnsAddPane;

        //Первая панель с информацией о загруженном предмета

    @FXML
    private Label vluespnPrviousVlue;

    @FXML
    private Label vluespnIdLabel;

    @FXML
    private Label vluespnNameLabel;

    @FXML
    private Label vluespnPrntLabel;

    @FXML
    private Label vluespnLinkLabel;

    @FXML
    private Label vluespnIdLabel1;

    @FXML
    private Label vluespnNameLabel1;

    @FXML
    private ImageView vluespnItmImage;

    /*
    @FXML
    private Label vluespnPrntLabel1;
    */

    @FXML
    private Label vluespnLinkLabel1;

        //Вторая панель для работы с добавлением и уборкой линий

    @FXML
    private Button rdctrpaneAddLns;

    @FXML
    private Button rdctrpaneAddLnsArr;

    @FXML
    private Button rdctrpaneRmvLns;

    @FXML
    private Button rdctrpaneRstNmbrOfLns;

    @FXML
    private TextField rdctrpaneChngNmbrOfLns;

        //Третья панель для работы с добавлением нескольких заданных линий

    @FXML
    private TextArea arraddpaneArrLnsTxtArea;

    @FXML
    private Button arraddpaneAddArrBtn;

    @FXML
    private Button arraddpaneCancelBtn;

    // Конец модуля

    @FXML
    void Choisetypeofitemhelpselect(KeyEvent EvHFrstGrp) {
        if (!EvHFrstGrp.getCode().isArrowKey()) {
            if (EvHFrstGrp.getCode().getCode() != 8) {
                if (gKeyChsTp == 0 & !wrkwthprstChsTypeOfItm.isShowing()) {
                    gKeyChsTp = 1;
                    gChsdMainType = wrkwthprstChsTypeOfItm.getValue();
                    gObChsdTypes = wrkwthprstChsTypeOfItm.getItems();
                    gObTypes.clear();
                    wrkwthprstChsTypeOfItm.setValue(null);
                    if (wrkwthprstChsTypeOfItm.getEditor().getLength() == 0) {
                        wrkwthprstChsTypeOfItm.getEditor().insertText(0, EvHFrstGrp.getText());
                    }
                    gObTypes.addAll("");
                    if (wrkwthprstChsTypeOfItm.getEditor().getText().length() > 0) {
                        for (int i = 0; i < gMainItmArr.getarraysize(); i++) {
                            if (gMainItmArr.getglblsecondtype(i).toLowerCase().contains(wrkwthprstChsTypeOfItm.getEditor().getText().toLowerCase())) {
                                gObTypes.add(gMainItmArr.getglblsecondtype(i));
                            }
                        }
                    }
                    wrkwthprstChsTypeOfItm.setItems(gObTypes);
                    wrkwthprstChsTypeOfItm.show();
                    EventHandler<Event> EHHlpMainType = new EventHandler<Event>() {
                        @Override
                        public void handle(Event event1) {
                            gObTypes.clear();
                            if (wrkwthprstChsTypeOfItm.getEditor().getText().length() > 0) {
                                gObTypes.addAll("");
                                for (int i = 0; i < gMainItmArr.getarraysize(); i++) {
                                    if (gMainItmArr.getglblsecondtype(i).toLowerCase().contains(wrkwthprstChsTypeOfItm.getEditor().getText().toLowerCase())) {
                                        gObTypes.add(gMainItmArr.getglblsecondtype(i));
                                    }
                                }
                                wrkwthprstChsTypeOfItm.setItems(gObTypes);
                                wrkwthprstChsTypeOfItm.show();
                            }
                        }
                    };
                    EventHandler<Event> EHHlpMainTypeHide = new EventHandler<Event>() {
                        @Override
                        public void handle(Event event3) {
                            wrkwthprstChsTypeOfItm.setItems(gObChsdTypes);
                            if (wrkwthprstChsTypeOfItm.getValue() != null && wrkwthprstChsTypeOfItm.getValue() != gChsdMainType) {
                                wrkwthprstChsTypeOfItm.getEditor().setText(wrkwthprstChsTypeOfItm.getValue());
                                ListView lmTmpLstSknLnk = (ListView) ((ComboBoxListViewSkin) wrkwthprstChsTypeOfItm.getSkin()).getPopupContent();
                                lmTmpLstSknLnk.scrollTo(Math.max(0, wrkwthprstChsTypeOfItm.getSelectionModel().getSelectedIndex()));
                                wrkwthprstChsScndType.getItems().clear();
                                wrkwthprstChsScndType.setValue(null);
                                gObLstScndTp.clear();
                                gObLstScndTp.addAll("");
                                UniModls.ChsGrpItm.FindItmsWthGrps(0, null);
                                wrkwthprstChsScndType.getItems().addAll(gObLstScndTp);
                            }
                            gKeyChsTp = 0;
                            wrkwthprstChsTypeOfItm.removeEventHandler(KeyEvent.KEY_RELEASED, EHHlpMainType);
                            wrkwthprstChsTypeOfItm.removeEventHandler(ComboBox.ON_HIDDEN, this);
                        }
                    };
                    wrkwthprstChsTypeOfItm.addEventHandler(KeyEvent.KEY_RELEASED, EHHlpMainType);
                    wrkwthprstChsTypeOfItm.addEventHandler(ComboBox.ON_HIDDEN, EHHlpMainTypeHide);
                }
            }
        }
    }

    @FXML
    void Secondtypehelpselect(KeyEvent EvHScndGrp) {
        if (!EvHScndGrp.getCode().isArrowKey()) {
            if (EvHScndGrp.getCode().getCode() != 8) {
                if (gKeyChsScndTp == 0 & !wrkwthprstChsScndType.isShowing()) {
                    gKeyChsScndTp = 1;
                    gChsdScndType = wrkwthprstChsScndType.getValue();
                    gObChsdScndTypes = wrkwthprstChsScndType.getItems();
                    gObScndTypes.clear();
                    wrkwthprstChsScndType.setValue(null);
                    if (wrkwthprstChsScndType.getEditor().getLength() == 0) {
                        wrkwthprstChsScndType.getEditor().insertText(0, EvHScndGrp.getText());
                    }
                    gObScndTypes.addAll("");
                    if (wrkwthprstChsScndType.getEditor().getText().length() > 0) {
                        for (int i = 0; i < gMainItmArr.getarraysize(); i++) {
                            for (int o = 0; o < gMainItmArr.getarraysizescnd(i); o++) {
                                if (gMainItmArr.getitemscndtype(i, o).toLowerCase().contains(wrkwthprstChsScndType.getEditor().getText().toLowerCase())) {
                                    gObScndTypes.add(gMainItmArr.getitemscndtype(i, o));
                                }
                            }
                        }
                    }
                    wrkwthprstChsScndType.setItems(gObScndTypes);
                    wrkwthprstChsScndType.show();
                    EventHandler<Event> EHHlpScndType = new EventHandler<Event>() {
                        @Override
                        public void handle(Event event1) {
                            gObScndTypes.clear();
                            if (wrkwthprstChsScndType.getEditor().getText().length() > 0) {
                                gObScndTypes.addAll("");
                                for (int i = 0; i < gMainItmArr.getarraysize(); i++) {
                                    for (int o = 0; o < gMainItmArr.getarraysizescnd(i); o++) {
                                        if (gMainItmArr.getitemscndtype(i, o).toLowerCase().contains(wrkwthprstChsScndType.getEditor().getText().toLowerCase())) {
                                            gObScndTypes.add(gMainItmArr.getitemscndtype(i, o));
                                        }
                                    }
                                }
                                wrkwthprstChsScndType.setItems(gObScndTypes);
                                wrkwthprstChsScndType.show();
                            }
                        }
                    };
                    EventHandler<Event> EHHlpScndTypeHide = new EventHandler<Event>() {
                        @Override
                        public void handle(Event event3) {
                            wrkwthprstChsScndType.setItems(gObChsdScndTypes);
                            if (wrkwthprstChsScndType.getValue() != null && wrkwthprstChsScndType.getValue() != gChsdScndType) {
                                wrkwthprstChsScndType.getEditor().setText(wrkwthprstChsScndType.getValue());
                                ListView lmTmpLstSknLnk = (ListView) ((ComboBoxListViewSkin) wrkwthprstChsScndType.getSkin()).getPopupContent();
                                lmTmpLstSknLnk.scrollTo(Math.max(0, wrkwthprstChsScndType.getSelectionModel().getSelectedIndex()));
                                UniModls.ChsGrpItm.FindItmsWthGrps(1,null);
                            }
                            gKeyChsScndTp = 0;
                            wrkwthprstChsScndType.removeEventHandler(KeyEvent.KEY_RELEASED, EHHlpScndType);
                            wrkwthprstChsScndType.removeEventHandler(ComboBox.ON_HIDDEN, this);
                        }
                    };
                    wrkwthprstChsScndType.addEventHandler(KeyEvent.KEY_RELEASED, EHHlpScndType);
                    wrkwthprstChsScndType.addEventHandler(ComboBox.ON_HIDDEN, EHHlpScndTypeHide);
                }
            }
        }
    }

    @FXML
    void Choiseitemshelpselect(KeyEvent EvHTags) {
        if (EvHTags.getCode() != KeyCode.ENTER & EvHTags.getCode() != KeyCode.DELETE & EvHTags.getCode() != KeyCode.BACK_SPACE & EvHTags.getCode() != KeyCode.SPACE) {
            if (!EvHTags.getCode().isArrowKey() & !EvHTags.getCode().isModifierKey()) {
                if (EvHTags.getCode().getCode() != 8) {
                    if (gKeyChsTag == 0 & !wrkwthprstChsTags.isShowing()) {
                        gKeyChsTag = 1;
                        String lmAnlzeStrng = "";
                        int lmItmStrtIndx = 0, lmItmEndIndx = 0;

                        //Значение будет всегда в нуле

                        //Для оптимизации проверять равна ли длинна тегов с пробелами строке, быстрый поиск будет сравнение тега в базе с текущим если оно есть
                        //По факту будет анализ есть ли изменения на примере полного сравнения, так=же можно сравнить со следующим

                        //Если неравна то меняется выбранное слово, если два слова и более меняются выбрать текущее по каретке остальное востановить
                        //Быстрый анализ по количеству пробелов, долгий учитывает текущее слово по схожим буквам и позиции между тегами по возможности

                        //Проверяет есть ли вообще выбранные теги

                        chosedtags = wrkwthprstChsTags.getEditor().getText();

                        if (gObTags.size() > 0) {
                            //Возможно реализовать проверку стирание пробелов но нужно выстроить инициализацию

                            //Сравнивает полученный тег со строкой, не оптимизирован
                            //Разобраться с выбором, по факту двух и более несовпадений выбирает по каретке
                            for (int i = 0; i < gObTags.size(); i++) {
                                if (!chosedtags.contains(gObTags.get(i).toString())) {
                                    gErrCnt++;
                                }
                            }
                            System.out.println("Количество несовпадений: " + gErrCnt);

                            if (gErrCnt > 0) {
                                //Строка при несовпадении заменяет все на выбранные значение, до момента где находится каретка
                                if (wrkwthprstChsTags.getEditor().getText().length() < (gObTags.toString().replace("[", "").replace("]", "").length()))
                                    ;
                                gCrtPos = wrkwthprstChsTags.getEditor().getCaretPosition();
                                //Выбрал все теги в строку с пробелами и остановился на каретке, тут идет не совпадение
                                //Каретка может быть как в середине так и по краям, надо учесть
                                for (int i = (gCrtPos - 1); (lmItmStrtIndx == 0 && i > -1); i--) {
                                    if (wrkwthprstChsTags.getEditor().getText().charAt(i) == ' ') {
                                        lmItmStrtIndx = i;
                                    }
                                }
                                //Добавить возможность выставления каретки с учетом стираемого 1-2 = 2 изм

                                //Считывает нужное мне значение, с изменением
                                //Сделать считывание по простому, счетчик + буква добавляется от счетчика размыкается на пробеле
                                for (int i = gCrtPos; (lmItmEndIndx == 0 && (i < wrkwthprstChsTags.getEditor().getText().length())); i++) {
                                    if (wrkwthprstChsTags.getEditor().getText().charAt(i) == ' ') {
                                        lmItmEndIndx = i;
                                    }
                                }
                                if (lmItmEndIndx == 0) lmItmEndIndx = wrkwthprstChsTags.getEditor().getText().length();
                                //Остальное хуячит по быстрому не проверяя в случае если есть что хуячить, пока удалено.
                                lmAnlzeStrng = wrkwthprstChsTags.getEditor().getText(lmItmStrtIndx, lmItmEndIndx);
                            }
                        }
                        //Переработать модуль, тэги отделяет пробелом, анализ слов работает на все тэги в которых что-то меняется

                        gObTmpTagsBufr.clear();
                        //Добавлен чтобы не срабатаывал до выбора
                        wrkwthprstChsTags.setValue(null);
                        if (gErrCnt == 0) {
                            //Стирает все, добавляет букву из ивента пушто какая разница где добавлено если это не замена
                            wrkwthprstChsTags.getEditor().setText(EvHTags.getText());
                            wrkwthprstChsTags.getEditor().positionCaret(1);
                        } else {
                            wrkwthprstChsTags.getEditor().setText(lmAnlzeStrng.trim().replaceAll(",", ""));
                            if (lmItmStrtIndx > 0) {
                                lmItmStrtIndx++;
                            }
                            wrkwthprstChsTags.getEditor().positionCaret((gCrtPos - lmItmStrtIndx));
                        }

                        for (int i = 0; i < gObChsdTags.size(); i++) {
                            if (gObChsdTags.get(i).toString().toLowerCase().contains(wrkwthprstChsTags.getEditor().getText().toLowerCase())) {
                                gObTmpTagsBufr.add(gObChsdTags.get(i));
                            }
                        }
                        wrkwthprstChsTags.setItems(gObTmpTagsBufr);
                        wrkwthprstChsTags.show();
                        EventHandler<KeyEvent> EHHlpTags = new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event1) {
                                //Добавлен отсев стрелок теперь оно не закрывается
                                //Ну и собственно не выбирается а стирается когда будет выбор
                                if (event1.getCode() != KeyCode.ENTER) {
                                    if (event1.getCode() == KeyCode.DELETE) {
                                        wrkwthprstChsTags.getEditor().setText("");
                                    }
                                    if (event1.getCode().isArrowKey()) {
                                        if (event1.getCode() == KeyCode.LEFT) {
                                            wrkwthprstChsTags.getEditor().positionCaret(wrkwthprstChsTags.getEditor().getCaretPosition() - 1);
                                        }
                                        if (event1.getCode() == KeyCode.RIGHT) {
                                            if (wrkwthprstChsTags.getEditor().getCaretPosition() < wrkwthprstChsTags.getEditor().getLength()) {
                                                wrkwthprstChsTags.getEditor().positionCaret(wrkwthprstChsTags.getEditor().getCaretPosition() + 1);
                                            }
                                        }
                                    }
                                    if (!event1.getCode().isArrowKey()) {
                                        if (wrkwthprstChsTags.getEditor().getText().length() > 0) {
                                            gObTmpTagsBufr.clear();
                                            for (int i = 0; i < gObChsdTags.size(); i++) {
                                                if (gObChsdTags.get(i).toString().toLowerCase().contains(wrkwthprstChsTags.getEditor().getText().toLowerCase())) {
                                                    gObTmpTagsBufr.add(gObChsdTags.get(i).toString());
                                                }
                                            }
                                            wrkwthprstChsTags.hide();
                                            wrkwthprstChsTags.show();
                                        }
                                    }
                                }
                            }
                        };
                        EventHandler<Event> EHHlpTagsHide = new EventHandler<Event>() {
                            @Override
                            public void handle(Event event3) {
                                if (wrkwthprstChsTags.getValue() != null) {
                                    if (gWrkmd == 0) {
                                        if (gErrCnt == 0 && !gObTags.toString().contains(wrkwthprstChsTags.getValue())) {
                                            gObTags.add(wrkwthprstChsTags.getValue());
                                            gCrtPos = gObTags.toString().length() - 2;
                                            gWrkmd = 3;
                                            //Добавить возможность замены тега
                                            //Учесть возможность добавление тегов с сохранением ошибочных, отслеживание по пробелам и запятым
                                            //после выбора будет второй выбор, если ошибок больше 1, заменить n и востановить строку с учетом тегов либо оставить строку как есть
                                            //Если нет возможности востановить можно сделать добавку в виде установить нет тегов и востановить другие
                                            //Для простой версии программы с открытым кодом, замена 1 тега остальное востанавливается и строка заново собирается
                                            //Выбор удаление или замена остается
                                            //Возможно выбор режима работы будет в начале

                                        } else {
                                            wrkwthprstChsTags.getEditor().removeEventHandler(KeyEvent.KEY_RELEASED, EHHlpTags);
                                            wrkwthprstChsTags.getEditor().setEditable(false);
                                            gTmpVlue = wrkwthprstChsTags.getValue();
                                            gObTmpTagsBufr.clear();
                                            if (!gObTags.toString().contains(gTmpVlue)) {
                                                gObTmpTagsBufr.addAll("Add new tag", "Rewrite other tag");
                                            } else {
                                                wrkwthprstChsTags.getEditor().setText("This tag alredy added");
                                                gObTmpTagsBufr.add("Rewrite tag name");
                                            }
                                            gObTmpTagsBufr.addAll("Do nothing", "Delete other tag");
                                            gWrkmd = 1;
                                            wrkwthprstChsTags.setValue(null);
                                            wrkwthprstChsTags.show();
                                        }
                                    }
                                    if (gWrkmd == 1 && wrkwthprstChsTags.getValue() != null) {
                                        switch (wrkwthprstChsTags.getValue()) {
                                            case ("Rewrite other tag"):
                                                wrkwthprstChsTags.getItems().clear();
                                                wrkwthprstChsTags.getItems().addAll(gObTags);
                                                wrkwthprstChsTags.setValue(null);
                                                gWrkmd = 2;
                                                gScndWrkmd = 1;
                                                wrkwthprstChsTags.show();
                                                break;
                                            case ("Delete other tag"):
                                                wrkwthprstChsTags.getItems().clear();
                                                wrkwthprstChsTags.getItems().addAll(gObTags);
                                                wrkwthprstChsTags.setValue(null);
                                                gWrkmd = 2;
                                                gScndWrkmd = 2;
                                                wrkwthprstChsTags.show();
                                                break;
                                            case ("Rewrite tag name"):
                                                wrkwthprstChsTags.getEditor().addEventHandler(KeyEvent.KEY_RELEASED, EHHlpTags);
                                                wrkwthprstChsTags.getEditor().clear();
                                                wrkwthprstChsTags.getItems().clear();
                                                wrkwthprstChsTags.getEditor().setEditable(true);
                                                gWrkmd = 0;
                                                break;
                                            case ("Do nothing"):
                                                gWrkmd = 3;
                                                break;
                                            case ("Add new tag"):
                                                gObTags.add(gTmpVlue);
                                                gWrkmd = 3;
                                                break;
                                        }
                                        wrkwthprstChsTags.setValue(null);
                                    }
                                    if (gWrkmd == 2 && wrkwthprstChsTags.getValue() != null) {
                                        for (int i = 0; i < gObTags.size(); i++) {
                                            if (gObTags.get(i).toString().contains(wrkwthprstChsTags.getValue())) {
                                                if (gScndWrkmd == 1) {
                                                    gObTags.set(i, gTmpVlue + ';');
                                                } else if (gScndWrkmd == 2) {
                                                    gObTags.remove(i);
                                                }
                                            }
                                        }
                                        wrkwthprstChsTags.setValue(null);
                                        gWrkmd = 3;
                                    }
                                    if (gWrkmd == 3) {
                                        wrkwthprstChsTags.getEditor().clear();
                                        wrkwthprstChsTags.getItems().clear();
                                        wrkwthprstChsTags.getItems().addAll("view selected tags", "Match items",
                                                "Add new tag", "Change tag",
                                                "Delete tag", "Delete all tags");
                                        wrkwthprstChsTags.setValue(gObTags.toString().replace("[", "").
                                                replace("]", ""));
                                        wrkwthprstChsTags.getEditor().positionCaret(gCrtPos);
                                        gKeyChsTag = 0;
                                        gErrCnt = 0;
                                        gWrkmd = 0;
                                        gScndWrkmd = 0;
                                        wrkwthprstChsTags.getEditor().removeEventHandler(KeyEvent.KEY_RELEASED, EHHlpTags);
                                        wrkwthprstChsTags.removeEventHandler(ComboBox.ON_HIDDEN, this);
                                        wrkwthprstChsTags.getEditor().setEditable(true);
                                    }
                                }
                            }
                        };
                        wrkwthprstChsTags.getEditor().addEventHandler(KeyEvent.KEY_RELEASED, EHHlpTags);
                        wrkwthprstChsTags.addEventHandler(ComboBox.ON_HIDDEN, EHHlpTagsHide);
                    }
                }
            }
        }
    }

    @FXML
    void Enteritemhelpselect(KeyEvent EvHItm) {
        if (EvHItm.getCode() != KeyCode.ENTER & EvHItm.getCode() != KeyCode.DELETE & EvHItm.getCode() != KeyCode.BACK_SPACE & EvHItm.getCode() != KeyCode.SPACE) {
            if (!EvHItm.getCode().isArrowKey() & !EvHItm.getCode().isModifierKey()) {
                if (EvHItm.getCode().getCode() != 8) {
                    if (gKeyChsItm == 0 & !wrkwthprstChsItm.isShowing()) {
                        gKeyChsItm = 1;
                        choseditem = wrkwthprstChsItm.getValue();
                        gObChsdItms = wrkwthprstChsItm.getItems();
                        String lmTmpStrng = wrkwthprstChsItm.getEditor().getText();
                        int lmCrtPos = wrkwthprstChsItm.getEditor().getCaretPosition();
                        gObItems.clear();
                        wrkwthprstChsItm.setValue(null);
                        if (choseditem != null) {
                            if (lmTmpStrng.length() > choseditem.length()) {
                                wrkwthprstChsItm.getEditor().insertText(0, EvHItm.getText());
                            } else if (lmTmpStrng.length() < choseditem.length()) {
                                wrkwthprstChsItm.getEditor().setText(lmTmpStrng);
                                wrkwthprstChsItm.getEditor().positionCaret(lmCrtPos);
                            }
                        }
                        if (wrkwthprstChsItm.getEditor().getText().length() > 0) {
                            for (int i = 0; i < gFstLnkItm.size(); i++) {
                                if (gFstLnkItm.get(i).getname().toLowerCase().contains(wrkwthprstChsItm.getEditor().getText().toLowerCase())) {
                                    gObItems.add(gFstLnkItm.get(i).getname());
                                }
                            }
                        }
                        wrkwthprstChsItm.setItems(gObItems);
                        wrkwthprstChsItm.show();
                        EventHandler<KeyEvent> EHHlpItem = new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event1) {
                                //Добавлен отсев стрелок теперь оно не закрывается
                                //Ну и собственно не выбирается а стирается нахуй когда будет выбор
                                if (event1.getCode() != KeyCode.ENTER) {
                                    if (event1.getCode() == KeyCode.DELETE) {
                                        wrkwthprstChsItm.getEditor().setText("");
                                    }
                                    if (event1.getCode().isArrowKey()) {
                                        if (event1.getCode() == KeyCode.LEFT) {
                                            wrkwthprstChsItm.getEditor().positionCaret(wrkwthprstChsItm.getEditor().getCaretPosition() - 1);
                                        }
                                        if (event1.getCode() == KeyCode.RIGHT) {
                                            if (wrkwthprstChsItm.getEditor().getCaretPosition() < wrkwthprstChsItm.getEditor().getLength()) {
                                                wrkwthprstChsItm.getEditor().positionCaret(wrkwthprstChsItm.getEditor().getCaretPosition() + 1);
                                            }
                                        }
                                    }
                                    if (!event1.getCode().isArrowKey()) {
                                        gObItems.clear();
                                        if (wrkwthprstChsItm.getEditor().getText().length() > 0) {
                                            for (int i = 0; i < gFstLnkItm.size(); i++) {
                                                if (gFstLnkItm.get(i).getname().toLowerCase().contains(wrkwthprstChsItm.getEditor().getText().toLowerCase())) {
                                                    gObItems.add(gFstLnkItm.get(i).getname());
                                                }
                                            }
                                            wrkwthprstChsItm.setItems(gObItems);
                                            wrkwthprstChsItm.show();
                                        }
                                    }
                                }
                            }
                        };
                        EventHandler<Event> EHHlpItemHide = new EventHandler<Event>() {
                            @Override
                            public void handle(Event event3) {
                                wrkwthprstChsItm.setItems(gObChsdItms);
                                if ((wrkwthprstChsItm.getValue() != null) && (wrkwthprstChsItm.getValue() != choseditem)) {
                                    wrkwthprstChsItm.getEditor().setText(wrkwthprstChsItm.getValue());
                                    ListView lmTmpLstSknLnk = (ListView) ((ComboBoxListViewSkin) wrkwthprstChsItm.getSkin()).getPopupContent();
                                    lmTmpLstSknLnk.scrollTo(Math.max(0, wrkwthprstChsItm.getSelectionModel().getSelectedIndex()));
                                    UniModls.ChsGrpItm.FindItmToEdtr();
                                }
                                gKeyChsItm = 0;
                                wrkwthprstChsItm.getEditor().removeEventHandler(KeyEvent.KEY_RELEASED, EHHlpItem);
                                wrkwthprstChsItm.removeEventHandler(ComboBox.ON_HIDDEN, this);

                            }
                        };
                        wrkwthprstChsItm.getEditor().addEventHandler(KeyEvent.KEY_RELEASED, EHHlpItem);
                        wrkwthprstChsItm.addEventHandler(ComboBox.ON_HIDDEN, EHHlpItemHide);
                    }
                }
            }
        }
    }

    @FXML
    void Changepresetbuttonaction(ActionEvent event) {
        globalPresetPane.setVisible(false);
        chngprstMainPane.setVisible(true);
    }

    //Модуль подбора предмета
    @FXML
    void Choisetypeofitemaction(MouseEvent EvAMainType) {
        if (gChsTp == 0) {
            gChsTp = 1;
            String lmTmpVlue = wrkwthprstChsTypeOfItm.getValue();
            EventHandler<Event> EHActMainTypeHide = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (lmTmpVlue != wrkwthprstChsTypeOfItm.getValue()) {
                        wrkwthprstChsScndType.getItems().clear();
                        wrkwthprstChsScndType.setValue(null);
                        gObLstScndTp.clear();
                        gObLstScndTp.addAll("");
                        UniModls.ChsGrpItm.FindItmsWthGrps(0,null);
                        wrkwthprstChsScndType.getItems().addAll(gObLstScndTp);
                    }
                    wrkwthprstChsTypeOfItm.removeEventHandler(ComboBox.ON_HIDDEN, this);
                    gChsTp = 0;
                }
            };
            wrkwthprstChsTypeOfItm.addEventHandler(ComboBox.ON_HIDDEN, EHActMainTypeHide);
        }
    }

    @FXML
    void Secondtypeaction(MouseEvent EvActScndType) {
        if (gChsScndTp == 0) {
            gChsScndTp = 1;
            String lmTmpVlue = wrkwthprstChsScndType.getValue();
            EventHandler<Event> EHActScndTypeHide = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (lmTmpVlue != wrkwthprstChsScndType.getValue()) {
                        UniModls.ChsGrpItm.FindItmsWthGrps(1,null);
                    }
                    wrkwthprstChsScndType.removeEventHandler(ComboBox.ON_HIDDEN, this);
                    gChsScndTp = 0;
                }
            };
            wrkwthprstChsScndType.addEventHandler(ComboBox.ON_HIDDEN, EHActScndTypeHide);
        }
    }

    @FXML
    void Choiseitemsaction(MouseEvent EvATags) {
        if (gKeyChsTag == 0 && gChsTag == 0) {
            gChsTag = 1;
            gTmpVlue = gObTags.toString().replace("[", "").replace("]", "");
            EventHandler<Event> EHActTagsHide = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (wrkwthprstChsTags.getValue() != null) {
                        if (gChsTag == 1) {
                            switch (wrkwthprstChsTags.getValue()) {
                                case ("Do nothing"):
                                    wrkwthprstChsTags.getItems().clear();
                                    wrkwthprstChsTags.getItems().add("view selected tags");
                                    if (gObTags.size() > 0) {
                                        wrkwthprstChsTags.getItems().addAll("Match items", "Change tag", "Delete tag", "Delete all tags");
                                    }
                                    wrkwthprstChsTags.setValue(gTmpVlue);
                                    wrkwthprstChsTags.setEditable(true);
                                    gChsTag = 6;
                                    break;

                                case ("Match items"):
                                    if (gObTags.size() > -1) {
                                        wrkwthprstChsItm.getItems().clear();
                                        wrkwthprstChsItm.setValue(null);
                                        ArrayList<String> lmLnkArr;
                                        int lmChk = 0;


                                        if (gObTags.size() > 0) {
                                            UniModls.ChsGrpItm.FindItmsWthGrps(0, gObTags);
                                        } else {
                                            UniModls.ChsGrpItm.FindItmsWthGrps(0,null);
                                        }
                                        wrkwthprstChsTags.getEditor().clear();
                                        wrkwthprstChsTags.setValue(gObTags.toString().replace("[", "").replace("]", ""));
                                    } else {
                                        Wm.AppWindow.errorWindow(gsErrs.get(0).get(2));
                                        wrkwthprstChsTags.getEditor().clear();
                                        wrkwthprstChsTags.setValue(gObTags.toString().replace("[", "").replace("]", ""));
                                    }
                                    break;

                                    case ("view selected tags"):
                                        if (gObTags.size() > 0) {
                                            wrkwthprstChsTags.setEditable(false);
                                            wrkwthprstChsTags.getEditor().clear();
                                            wrkwthprstChsTags.getItems().clear();
                                            wrkwthprstChsTags.setValue(null);
                                            wrkwthprstChsTags.getItems().add("Do nothing");
                                            wrkwthprstChsTags.getItems().addAll(gObTags);
                                            wrkwthprstChsTags.show();
                                        } else {
                                            Wm.AppWindow.errorWindow(gsErrs.get(0).get(3));
                                            wrkwthprstChsTags.getItems().clear();
                                            wrkwthprstChsTags.getItems().add("view selected tags");
                                            if (gObTags.size() > 0) {
                                                wrkwthprstChsTags.getItems().addAll("Match items", "Change tag", "Delete tag", "Delete all tags");
                                            }
                                            wrkwthprstChsTags.setValue(gTmpVlue);
                                            wrkwthprstChsTags.setEditable(true);
                                            gChsTag = 6;
                                        }
                                    break;

                                    case ("Change tag"):
                                        if (gObTags.size() > 0) {
                                            wrkwthprstChsTags.setEditable(false);
                                            wrkwthprstChsTags.getItems().clear();
                                            wrkwthprstChsTags.getItems().add("Do nothing");
                                            wrkwthprstChsTags.getItems().addAll(gObTags);
                                            wrkwthprstChsTags.getEditor().setText("Chose tag to change");
                                            wrkwthprstChsTags.setValue(null);
                                            gChsTag = 2;
                                            wrkwthprstChsTags.show();
                                        } else {
                                            Wm.AppWindow.errorWindow(gsErrs.get(0).get(4));
                                            wrkwthprstChsTags.getItems().clear();
                                            wrkwthprstChsTags.getItems().add("view selected tags");
                                            if (gObTags.size() > 0) {
                                                wrkwthprstChsTags.getItems().addAll("Match items", "Change tag", "Delete tag", "Delete all tags");
                                            }
                                            wrkwthprstChsTags.setValue(gTmpVlue);
                                            wrkwthprstChsTags.setEditable(true);
                                            gChsTag = 6;
                                        }
                                    break;

                                    case ("Delete tag"):
                                        if (gObTags.size() > 0) {
                                        wrkwthprstChsTags.setEditable(false);
                                        wrkwthprstChsTags.getItems().clear();
                                        wrkwthprstChsTags.getItems().add("Do nothing");
                                        wrkwthprstChsTags.getItems().addAll(gObTags);
                                        wrkwthprstChsTags.getEditor().setText("Chose tag to delete");
                                        wrkwthprstChsTags.setValue(null);
                                        gChsTag = 3;
                                        wrkwthprstChsTags.show();
                                        } else {
                                            Wm.AppWindow.errorWindow(gsErrs.get(0).get(5));
                                            wrkwthprstChsTags.getItems().clear();
                                            wrkwthprstChsTags.getItems().add("view selected tags");
                                            if (gObTags.size() > 0) {
                                                wrkwthprstChsTags.getItems().addAll("Match items", "Change tag", "Delete tag", "Delete all tags");
                                            }
                                            wrkwthprstChsTags.setValue(gTmpVlue);
                                            wrkwthprstChsTags.setEditable(true);
                                            gChsTag = 6;
                                        }
                                    break;

                                    case ("Delete all tags"):
                                        if (gObTags.size() > 0){
                                        gObTags.clear();
                                        wrkwthprstChsTags.setValue(null);
                                        wrkwthprstChsTags.getEditor().clear();
                                        gChsTag = 6;
                                        } else {
                                            Wm.AppWindow.errorWindow(gsErrs.get(0).get(6));
                                            wrkwthprstChsTags.getItems().clear();
                                            wrkwthprstChsTags.getItems().add("view selected tags");
                                            if (gObTags.size() > 0) {
                                                wrkwthprstChsTags.getItems().addAll("Match items", "Change tag", "Delete tag", "Delete all tags");
                                            }
                                            wrkwthprstChsTags.setValue(gTmpVlue);
                                            wrkwthprstChsTags.setEditable(true);
                                            gChsTag = 6;
                                        }
                                    break;

                                    default:
                                        gChsTag = 6;
                                        wrkwthprstChsTags.show();
                                    break;
                            }
                        }
                        if (gChsTag == 2 && wrkwthprstChsTags.getValue() != null) {
                            gTmpVlue = wrkwthprstChsTags.getValue();
                            wrkwthprstChsTags.getEditor().clear();
                            wrkwthprstChsTags.setValue(null);
                            wrkwthprstChsTags.getItems().clear();
                            wrkwthprstChsTags.getItems().addAll(gObChsdTags);
                            wrkwthprstChsTags.hide();
                            wrkwthprstChsTags.show();
                            gChsTag = 4;
                        }
                        if (wrkwthprstChsTags.getValue() != null && (gChsTag > 2 & gChsTag < 6)) {
                            if (gChsTag == 4) {
                                String lmTmpStrng = wrkwthprstChsTags.getValue();
                                wrkwthprstChsTags.setValue(gTmpVlue);
                                gTmpVlue = lmTmpStrng;
                            }
                            for (int i = 0; i < gObTags.size(); i++) {
                                if (gObTags.get(i).toString().contains(wrkwthprstChsTags.getValue())) {
                                    if (gChsTag == 4) {
                                        gObTags.set(i, gTmpVlue);
                                    } else if (gChsTag == 3) {
                                        gObTags.remove(i);
                                    }
                                }
                            }
                            wrkwthprstChsTags.getItems().clear();
                            wrkwthprstChsTags.getItems().addAll("view selected tags", "Match items");
                            if (gObTags.size() > 0) {
                                wrkwthprstChsTags.getItems().addAll("Change tag", "Delete tag", "Delete all tags");
                            }
                            wrkwthprstChsTags.setValue(gObTags.toString().replace("[", "").replace("]", ""));
                            gChsTag = 6;
                            wrkwthprstChsTags.getEditor().clear();
                            wrkwthprstChsTags.setEditable(true);
                        }
                    }
                    if (gChsTag == 6) {
                        wrkwthprstChsTags.removeEventHandler(ComboBox.ON_HIDDEN, this);
                        gChsTag = 0;
                    }
                }
            };
            wrkwthprstChsTags.addEventHandler(ComboBox.ON_HIDDEN, EHActTagsHide);
        }
    }

    @FXML
    void Enteritemaction(MouseEvent EvActItm) {
        if (gChsItm == 0) {
            gChsItm = 1;
            String lmTmpStrng = wrkwthprstChsItm.getValue();
            EventHandler<Event> EHActItmHide = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (lmTmpStrng != wrkwthprstChsItm.getValue()) {
                        UniModls.ChsGrpItm.FindItmToEdtr();
                    }
                    wrkwthprstChsItm.removeEventHandler(ComboBox.ON_HIDDEN, this);
                    gChsItm = 0;
                }
            };
            wrkwthprstChsItm.addEventHandler(ComboBox.ON_HIDDEN, EHActItmHide);
        }
    }

    //Модуль редактирования переменных

    @FXML
    void edtrChsGlblGrpClk (MouseEvent EvActChsGlbCtgry) {
        edtrChsngGlblGrp.setDisable(true);

        String lmTmpVlue = edtrChsngGlblGrp.getValue();
        EventHandler<Event> EHActGlbCtgryHide = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (lmTmpVlue != edtrChsngGlblGrp.getValue()) {
                    edtrGlblLstView.getItems().clear();
                    gGlblEdtrBckp.clear();

                    String lmScnStrng = "";
                    int lmChk = 0;
                    for (int i = 0; i < gGlblsSectnArr.getitemsarray().size(); i++) {
                        if (gGlblsSectnArr.getitemsarray().get(i).getglobalname() == edtrChsngGlblGrp.getValue()) {
                            lmChk = i;
                        }
                    }
                    for (int i = 0; i < gGlblsSectnArr.getitemsarray().get(lmChk).getitemsarraysize(); i++) {
                        for (int o = 0; o < gGlblsSectnArr.getitemsarray().get(lmChk).getitemsarray().get(i).getvaluessize(); o++) {
                            lmScnStrng = gGlblsSectnArr.getitemsarray().get(lmChk).getitemsarray().get(i).getarray().get(o);
                            edtrGlblLstView.getItems().add(lmScnStrng);
                            gGlblEdtrBckp.add(lmScnStrng);
                        }
                    }
                    if (edtrGlblLstView.getItems().size() < 1) {
                        Wm.AppWindow.errorWindow(gsErrs.get(0).get(7));
                    }
                }
                edtrChsngGlblGrp.removeEventHandler(ComboBox.ON_HIDDEN, this);
                edtrChsngGlblGrp.setDisable(false);
            }
        };
        edtrChsngGlblGrp.addEventHandler(ComboBox.ON_HIDDEN, EHActGlbCtgryHide);
    }

    @FXML
    void edtrChsLctnClk (MouseEvent EvActChsLctn) {
        edtrChsngLctn.setDisable(true);
        String lmTmpVlue = edtrChsngLctn.getValue();
        EventHandler<Event> EHActLctnHide = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (lmTmpVlue != edtrChsngLctn.getValue()) {
                    edtrChsngLctnGrp.getItems().clear();

                    String lmScnStrng = "";
                    int lmChk = 0;
                    for (int i = 0; i < gLctnAll.getLcntsSize(); i++) {
                        if (gLctnAll.getLocation(i).getLocNm() == edtrChsngLctn.getValue()) {
                            lmChk = i;
                            gLctnChsdIndx = i;
                        }
                    }
                    for (int i = 0; i < gLctnAll.getLocation(lmChk).getGrpSz(); i++) {
                        edtrChsngLctnGrp.getItems().add(gLctnAll.getLocation(lmChk).getGrpNm(i));
                    }
                }
                edtrChsngLctn.removeEventHandler(ComboBox.ON_HIDDEN, this);
                edtrChsngLctn.setDisable(false);
            }
        };
        edtrChsngLctn.addEventHandler(ComboBox.ON_HIDDEN, EHActLctnHide);
    }

    @FXML
    void edtrChsLctnGrpClk(MouseEvent EvActChsLctnGrp) {
        if (gLctnChsdIndx != -1) {
            edtrChsngLctnGrp.setDisable(true);
            String lmTmpVlue = edtrChsngLctnGrp.getValue();
            EventHandler<Event> EHActLctnCtgryHide = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (lmTmpVlue != edtrChsngLctnGrp.getValue()) {
                        edtrLctnsLstView.getItems().clear();
                        gLctnEdtrBckp.clear();

                        String lmScnStrng = "";
                        int lmChk = edtrChsngLctnGrp.getSelectionModel().getSelectedIndex();

                        edtrLctnsLstView.getItems().addAll(gLctnAll.getLocation(gLctnChsdIndx).getArrInGrp(lmChk));
                        gLctnEdtrBckp.addAll(gLctnAll.getLocation(gLctnChsdIndx).getArrInGrp(lmChk));

                        if (edtrLctnsLstView.getItems().size() < 1) {
                            Wm.AppWindow.errorWindow(gsErrs.get(0).get(7));
                        }
                    }
                    edtrChsngLctnGrp.removeEventHandler(ComboBox.ON_HIDDEN, this);
                    edtrChsngLctnGrp.setDisable(false);
                }
            };
            edtrChsngLctnGrp.addEventHandler(ComboBox.ON_HIDDEN, EHActLctnCtgryHide);
        }
    }

    @FXML
    void edtrSetBtsNmrAct (ActionEvent event){

    }

    //Конец модуля редактирования переменных

    //Модуль начала создания пресета
    @FXML
    void CancelPresetAction(ActionEvent event) {
        if (Wm.AppWindow.dialogWindow(gsErrs.get(0).get(8)) == 1){
            wrkwthprstCancelPrst.setVisible(false);
            wrkwthprstSavePrstBtn.setVisible(false);
            wrkwthprstPrstCancel.setVisible(true);
            wrkwthprstPrstOk.setVisible(true);
        }
    }

    @FXML
    void Makepresetbuttonaction(ActionEvent event) {
        globalItemsValuePane.setVisible(false);
        wrkwthprstLoadPrstBtn.setVisible(false);
        wrkwthprstChngPrstBtn.setVisible(false);
        globalPresetsMakePane.setVisible(true);
        wrkwthprstSrchPrstItm.setVisible(true);
        wrkwthprstPrstWrkMd.setVisible(true);
        wrkwthprstMakePrstBtn.setVisible(false);
        globalPresetsMakePane.setVisible(true);
        wrkwthprstCancelPrst.setVisible(true);
        wrkwthprstSavePrstBtn.setVisible(true);

    }

    @FXML
    void SavePresetAction(ActionEvent event) {
        globalPresetsMakePane.setVisible(false);
        saveprstPresetsPane.setVisible(true);
        globalPresetPane.setVisible(false);
    }

    @FXML
    void SavePresetOkAction(ActionEvent event) {
        if (saveprstTextField.getText().length() > 0) {
            try {
                File PrstToSave = new File(gStngBufr1[11].replace("*", "\\Presets\\" +
                        saveprstTextField.getText() + ".obj"));
                int lmChk = 0;
                if (!PrstToSave.exists()) {
                    lmChk = 1;
                } else {
                    if(Wm.AppWindow.dialogWindow(gsErrs.get(0).get(9)) == 1){
                        lmChk = 1;
                    }
                }

                if (lmChk == 1){
                    if (!PrstToSave.canWrite()) {
                        PrstToSave.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(PrstToSave);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    Preset Prst = new Preset();
                    ArrayList<PresetObject> bufferlist = new ArrayList<PresetObject>();
                    for (int i = 0; i < gTmpPrstLst.size(); i++) {
                        bufferlist.add(gTmpPrstLst.get(i));
                    }
                    Prst.MakePreset(bufferlist, saveprstTextField.getText(), saveprstTextArea.getText());
                    gAllPrsts.add(Prst);
                    chngprstComboBox.getItems().add(saveprstTextField.getText() + ".obj");
                    oos.writeObject(Prst);
                    oos.flush();
                    oos.close();
                    fos.close();
                    saveprstPresetsPane.setVisible(false);
                    globalPresetPane.setVisible(true);
                    wrkwthprstSavePrstBtn.setVisible(false);
                    wrkwthprstCancelPrst.setVisible(false);
                    wrkwthprstMakePrstBtn.setVisible(true);
                    wrkwthprstLoadPrstBtn.setVisible(true);
                    wrkwthprstChngPrstBtn.setVisible(true);
                    wrkwthprstSrchPrstItm.setVisible(false);
                    wrkwthprstSrchPrstItm.setDisable(false);
                    wrkwthprstPrstWrkMd.setVisible(false);
                    wrkwthprstPrstWrkMd.setDisable(false);
                    saveprstTextField.clear();
                    saveprstTextArea.clear();
                    gTmpPrstLst.clear();
                    wrkwthprstVluesOnPrstItms.getItems().clear();
                    wrkwthprstAddedPrstVlues.getItems().clear();
                }
            } catch (IOException v) {
                System.out.println(v);
            }
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(0).get(10));
        }
    }

    @FXML
    void SavePresetCancelAction(ActionEvent event) {
        saveprstTextField.clear();
        saveprstTextArea.clear();
        saveprstPresetsPane.setVisible(false);
        globalPresetsMakePane.setVisible(true);
        globalPresetPane.setVisible(true);
    }

    //Конец модуля начала создания пресета

    //Модуль LoadPreset
    @FXML
    void Loadpresetbuttonaction(ActionEvent event) {
        loadprstGlobalAnchorPane.setVisible(true);
        loadprstAvailablePrsts.getItems().clear();
        loadprstAddPrsts.getItems().clear();
        File PrstsDir = new File(gStngBufr1[11].replace("*", "Presets")); //path указывает на директорию
        File[] lmfListFls = PrstsDir.listFiles();
        int lmCnt = 0;
        for (int i = 0; i < lmfListFls.length; i++) {
            if (lmfListFls[i].getName().contains(".obj")) {
                lmCnt++;
            }
        }
        if (lmCnt == gAllPrsts.size()){
            for (int o = 0; o < gAllPrsts.size(); o++) {
                loadprstAvailablePrsts.getItems().add(gAllPrsts.get(o).getPresetName());
            }
            for (int i = 0; i < gPrstsLoadInGame.size(); i++) {
                for (int p = 0; p < loadprstAvailablePrsts.getItems().size(); p++) {
                    if (gPrstsLoadInGame.get(i).equals(loadprstAvailablePrsts.getItems().get(p))) {
                        loadprstAddPrsts.getItems().add(loadprstAvailablePrsts.getItems().get(p));
                        loadprstAvailablePrsts.getItems().remove(p);
                        p = loadprstAvailablePrsts.getItems().size() + 1;
                    }
                }
            }
        } else if (lmCnt != gAllPrsts.size()) {
            try {
                for (int i = 0; i < lmfListFls.length; i++) {
                    if (lmfListFls[i].getName().contains(".obj")) {
                        try {
                            File lmfPrst = new File(lmfListFls[i].getPath());
                            FileInputStream fis = new FileInputStream(lmfPrst);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            Preset pr = (Preset) ois.readObject();
                            fis.close();
                            gAllPrsts.add(pr);
                            chngprstComboBox.getItems().add(lmfListFls[i].getName());
                        } catch (IOException b) {
                            System.out.println(b);
                        }
                    }
                }
            } catch (Exception v) {
                System.out.println(v);
            }
        }

        if (gAllPrsts.size() != lmfListFls.length) {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(1));
        }
    }

    // Далее идет все что в всплывающем AnchorPane
    @FXML
    void  LoadPresetAddRemoveAction(ActionEvent event){
        if (loadprstAvailablePrsts.getSelectionModel().getSelectedIndices().size() > 0){
            for (int i = loadprstAvailablePrsts.getSelectionModel().getSelectedIndices().size() - 1 ; i > -1 ; i--){
                int lmChk = 0;
                String[] lmPrstChk = new String[3];
                //nmbr[0] показывает что будет сделано, 0 загрузка - 1 выгрузка, при 1 доп проверка на загружен ли пресет
                lmPrstChk[0] = "0";
                lmPrstChk[1] = loadprstAvailablePrsts.getSelectionModel().getSelectedItems().get(i);
                lmPrstChk[2] = "0";
                //Тут у load preset 2 значения, первое и второе отношение к  списку
                for (int b = 0; b < gTmpPrstAddRmv.size() ; b++){
                    if (gTmpPrstAddRmv.get(b)[1].equals(lmPrstChk[1])){
                        gTmpPrstAddRmv.remove(b);
                        b = (gTmpPrstAddRmv.size() + 1);
                        lmChk++;
                    }
                }
                if (lmChk == 0){
                    gTmpPrstAddRmv.add(lmPrstChk);
                }
                loadprstAddPrsts.getItems().add(loadprstAvailablePrsts.getSelectionModel().getSelectedItems().get(i));
                loadprstAvailablePrsts.getItems().remove(loadprstAvailablePrsts.getSelectionModel().getSelectedIndices().get(i).intValue());
            }
            loadprstAvailablePrsts.getSelectionModel().clearSelection();
        } else if (loadprstAddPrsts.getSelectionModel().getSelectedIndices().size() > 0){
            for (int i = loadprstAddPrsts.getSelectionModel().getSelectedIndices().size() - 1 ; i > -1 ; i--) {
                int lmChk = 0;
                String lmTmpItmLn = loadprstAddPrsts.getSelectionModel().getSelectedItems().get(i);
                //Тут у load preset 2 значения, первое и второе отношение к  списку
                for (int b = 0; b < gTmpPrstAddRmv.size(); b++) {
                    if (gTmpPrstAddRmv.get(b)[1].equals(lmTmpItmLn)) {
                        gTmpPrstAddRmv.remove(b);
                        b = (gTmpPrstAddRmv.size() + 1);
                        lmChk++;
                    }
                }
                    if (lmChk == 0) {
                        String[] lmPrstChk = new String[3];
                        //nmbr[0] показывает что будет сделано, 0 загрузка - 1 выгрузка, при 1 доп проверка на загружен ли пресет
                        lmPrstChk[0] = "1";
                        lmPrstChk[1] = loadprstAddPrsts.getSelectionModel().getSelectedItems().get(i);
                        lmPrstChk[2] = "1";
                        gTmpPrstAddRmv.add(lmPrstChk);
                    }
                    loadprstAvailablePrsts.getItems().add(loadprstAddPrsts.getSelectionModel().getSelectedItems().get(i));
                    loadprstAddPrsts.getItems().remove(loadprstAddPrsts.getSelectionModel().getSelectedIndices().get(i).intValue());
                    loadprstAddPrsts.getSelectionModel().clearSelection();
            }
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(2));
        }
    }

    @FXML
    void  LoadPresetDeletePresetAction(ActionEvent event){
        int lmWrkmd = 0;
        ListView<String> lmLstLnk = new ListView<String>();

        if (loadprstAvailablePrsts.getSelectionModel().getSelectedIndices().size() > 0){
            lmLstLnk = loadprstAvailablePrsts;
            lmWrkmd = 1;
        } else if (loadprstAddPrsts.getSelectionModel().getSelectedIndices().size() > 0){
            lmLstLnk = loadprstAddPrsts;
            lmWrkmd = 2;
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(3));
        }

        if (lmWrkmd > 0){
            String lmPrstName;
            int lmPrstIndx;
            File fPrstToDlt;
            for (int i = 0; i < lmLstLnk.getSelectionModel().getSelectedIndices().size(); i++){
                lmPrstIndx = lmLstLnk.getSelectionModel().getSelectedIndices().get(i);
                lmPrstName = lmLstLnk.getItems().get(lmPrstIndx);
                fPrstToDlt = new File (gStngBufr1[11]
                        .replace("*", "\\Presets\\" + lmPrstName) + ".obj");
                fPrstToDlt.delete();

                if(lmWrkmd == 2) {
                    if (gTmpPrstAddRmv.size() == 0) {
                        if (Wm.AppWindow.dialogWindow(gsErrs.get(1).get(4)) == 1) {
                            String[] lmPrstChk = new String[]{"1", loadprstAddPrsts.getSelectionModel().getSelectedItems().get(i), "1"};
                            gTmpPrstAddRmv.add(lmPrstChk);
                            loadprstLoadBtn.fire();
                        }
                    } else {
                        int lmChk = 0;
                        for (int u = 0; u < gTmpPrstAddRmv.size(); u++){
                            if (gTmpPrstAddRmv.get(u).equals(lmPrstName)){
                                lmChk++;
                                u = (gTmpPrstAddRmv.size() + 1);
                            }
                        }

                        if(lmChk == 0){
                            if (Wm.AppWindow.dialogWindow(gsErrs.get(1).get(4)) == 1) {
                                String[] lmPrstChk = new String[]{"1", loadprstAddPrsts.getSelectionModel().getSelectedItems().get(i), "1"};
                                gTmpPrstAddRmv.add(lmPrstChk);
                                loadprstLoadBtn.fire();
                            }
                        }
                    }
                }

                for (int b = 0; b < gAllPrsts.size(); b++){
                    if (gAllPrsts.get(b).getPresetName().equals(lmPrstName)){
                        gAllPrsts.remove(b);
                        b = (gAllPrsts.size() + 1);
                    }
                }
                lmLstLnk.getItems().remove(lmPrstIndx);
            }
        }
    }

    @FXML
    void LoadPresetViewPresetAction(ActionEvent event){
        if (loadprstAvailablePrsts.getSelectionModel().getSelectedIndices().size() == 1){
            for (int i = 0; i < gAllPrsts.size(); i++){
                if (gAllPrsts.get(i).getPresetName().equals(loadprstAvailablePrsts.getItems().get(
                        loadprstAvailablePrsts.getSelectionModel().getSelectedIndex()))){
                    gChsdPrstIndx = i;
                }
            }
            loadprstDescription.setText(gAllPrsts.get(gChsdPrstIndx).getPresetDescription());
            loadprstChosedPrstLabel.setText(loadprstAvailablePrsts.getItems().get(loadprstAvailablePrsts.getSelectionModel().getSelectedIndex()));
        } else if (loadprstAddPrsts.getSelectionModel().getSelectedIndices().size() == 1){
            for (int i = 0; i < gAllPrsts.size(); i++){
                if (gAllPrsts.get(i).getPresetName().equals(loadprstAddPrsts.getItems().get(
                        loadprstAddPrsts.getSelectionModel().getSelectedIndex()))){
                    gChsdPrstIndx = i;
                }
            }
            loadprstDescription.setText(gAllPrsts.get(gChsdPrstIndx).getPresetDescription());
            loadprstChosedPrstLabel.setText(loadprstAddPrsts.getItems().get(loadprstAddPrsts.getSelectionModel().getSelectedIndex()));
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(5));
        }
    }

    @FXML
    void LoadPresetLoadButtonAction(ActionEvent event) {
        if(gTmpPrstAddRmv.size() > 0) {
            int lmChk = 0;
            for (int p = 0; p < gTmpPrstAddRmv.size(); p++) {
                if (gTmpPrstAddRmv.get(p)[0] == "0") {
                    for (int b = 0; b < gPrstsLoadInGame.size(); b++) {
                        if (gTmpPrstAddRmv.get(p)[1].equals(gPrstsLoadInGame.get(b))) {
                            lmChk++;
                        }
                    }
                    if (lmChk == 0) {
                        for (int b = 0; b < gAllPrsts.size(); b++) {
                            if (gAllPrsts.get(b).getPresetName().equals(gTmpPrstAddRmv.get(p)[1])) {
                                lmChk = b;
                                System.out.println("\nЗагрузка пресета с именем " + gAllPrsts.get(b).getPresetName());
                            }
                        }
                        UniModls.PrstWrk.ChangeMainItemFile(lmChk, gTmpPrstAddRmv.get(p)[1], 0);
                    }
                } else {
                    lmChk = -1;
                    for (int b = 0; b < gPrstsLoadInGame.size(); b++) {
                        if (gPrstsLoadInGame.get(b).equals(gTmpPrstAddRmv.get(p)[1])) {
                            lmChk = b;
                        }
                    }
                    if (lmChk > -1) {
                        System.out.println(gPrstsLoadInGame.get(lmChk));
                        System.out.println(gTmpPrstAddRmv.get(p)[1]);
                        UniModls.PrstWrk.ChangeMainItemFile(lmChk, gTmpPrstAddRmv.get(p)[1], 1);
                        gPrstsLoadInGame.remove(lmChk);
                    }
                }
                lmChk = 0;
            }
            gTmpPrstAddRmv.clear();
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(6));
        }
    }

    @FXML
    void  LoadPresetClosePaneAction(ActionEvent event){
        loadprstGlobalAnchorPane.setVisible(false);
    }

    @FXML
    void LoadPresetWrkModeAction(ActionEvent event){
        if (loadprstDesriptionPane.isVisible()){
            loadprstDesriptionPane.setVisible(false);
            loadprstItemsPane.setVisible(true);
        } else if (loadprstItemsPane.isVisible()){
            loadprstDesriptionPane.setVisible(true);
            loadprstItemsPane.setVisible(false);
        }
    }

    @FXML
    void LoadPresetLoadItemsAction(ActionEvent event){
        if (loadprstAvailablePrsts.getSelectionModel().getSelectedIndices().size() == 1){
            loadprstItemsInLoadPrst.getItems().clear();
            gLoadPrst = gAllPrsts.get(gChsdPrstIndx);
            for (int i = 0; i < gAllPrsts.get(loadprstAvailablePrsts.getSelectionModel().getSelectedIndex()).getPreset().size(); i++) {
                loadprstItemsInLoadPrst.getItems().addAll(gAllPrsts.get(loadprstAvailablePrsts.getSelectionModel().getSelectedIndex()).getPreset().get(i).getItemType());
            }
            loadprstChosedItemLabel.setText(loadprstAvailablePrsts.getItems().get(loadprstAvailablePrsts.getSelectionModel().getSelectedIndex()));
            loadprstItemsLinesVievMode.getItems().clear();
            loadprstNameItemLines.setText("");
            loadprstAvailablePrsts.getSelectionModel().clearSelection();
        } else if (loadprstAddPrsts.getSelectionModel().getSelectedIndices().size() == 1){
            loadprstItemsInLoadPrst.getItems().clear();
            gLoadPrst = gAllPrsts.get(gChsdPrstIndx);
            for (int i = 0; i < gAllPrsts.get(loadprstAddPrsts.getSelectionModel().getSelectedIndex()).getPreset().size(); i++) {
                loadprstItemsInLoadPrst.getItems().addAll(gAllPrsts.get(loadprstAddPrsts.getSelectionModel().getSelectedIndex()).getPreset().get(i).getItemType());
            }
            loadprstChosedItemLabel.setText(loadprstAddPrsts.getItems().get(loadprstAddPrsts.getSelectionModel().getSelectedIndex()));
            loadprstItemsLinesVievMode.getItems().clear();
            loadprstNameItemLines.setText("");
            loadprstAddPrsts.getSelectionModel().clearSelection();
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(7));
        }
    }

    @FXML
    void LoadPresetLoadLinesInItemAction(ActionEvent event) {
        if (loadprstItemsInLoadPrst.getSelectionModel().getSelectedIndices().size() > 0) {
            int lmChk = 0;
            gLoadItmIndx = loadprstItemsInLoadPrst.getSelectionModel().getSelectedIndex();
            loadprstItemsLinesVievMode.getItems().clear();
            if (!loadprstLinesArray.isSelected()) {
                lmChk = gLoadPrst.getPreset().get(gLoadItmIndx).getSizeLineNumber();
                if (lmChk > 0) {
                    for (int i = 0; i < lmChk; i++) {
                        loadprstItemsLinesVievMode.getItems().add(gLoadPrst.getPreset().get(gLoadItmIndx).getItemLineMain(i));
                    }
                }
                loadprstNameItemLines.setText(loadprstItemsInLoadPrst.getItems().get(gLoadItmIndx));
            } else {
                lmChk = gLoadPrst.getPreset().get(gLoadItmIndx).getSizeMassiveLineNumber();
                if (lmChk > 0) {
                    for (int i = 0; i < lmChk; i++) {
                        loadprstItemsLinesVievMode.getItems().add(gLoadPrst.getPreset().get(gLoadItmIndx).getMassiveItemLineMain(i).get(0));
                    }
                }
                loadprstNameItemLines.setText(loadprstItemsInLoadPrst.getItems().get(gLoadItmIndx));
            }
            loadprstItemsInLoadPrst.getSelectionModel().clearSelection();
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(1).get(8));
        }
    }

    @FXML
    void LoadPresetLinesArrayAction(ActionEvent event) {
        int lmChk = 0;
        if (!loadprstLinesArray.isSelected()) {
            lmChk = gLoadPrst.getPreset().get(gLoadItmIndx).getSizeLineNumber();
            if (lmChk > 0) {
                loadprstItemsLinesVievMode.getItems().clear();
                for (int i = 0; i < lmChk; i++) {
                    loadprstItemsLinesVievMode.getItems().add(gLoadPrst.getPreset().get(gLoadItmIndx).getItemLineMain(i));
                }
            } else {
                loadprstItemsLinesVievMode.getItems().clear();
            }
        } else {
            lmChk = gLoadPrst.getPreset().get(gLoadItmIndx).getSizeMassiveLineNumber();
            if (lmChk > 0) {
                loadprstItemsLinesVievMode.getItems().clear();
                for (int i = 0; i < lmChk; i++) {
                    loadprstItemsLinesVievMode.getItems().add(gLoadPrst.getPreset().get(gLoadItmIndx).getMassiveItemLineMain(i).get(0));
                }
            } else {
                loadprstItemsLinesVievMode.getItems().clear();
            }
        }
        loadprstItemsInLoadPrst.getSelectionModel().clearSelection();
    }

    //Конец модуля LoadPreset

    //Модуль ChangePreset
    @FXML
    void ChangePresetComboBoxClicked (MouseEvent event){
        if (gChsLoadPrst == 0) {
            gChsLoadPrst = 1;
            chosedpreset = wrkwthprstChsItm.getValue();
            EventHandler<Event> EHActChngPrstHide = new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (chosedpreset != chngprstComboBox.getValue()) {
                        int lmTmpIndx = chngprstComboBox.getSelectionModel().getSelectedIndex();
                        chngprstLabelPresetName.setText(gAllPrsts.get(lmTmpIndx).getPresetName());
                        chngprstDescription.setText(gAllPrsts.get(lmTmpIndx).getPresetDescription());
                    }
                    chngprstComboBox.removeEventHandler(ComboBox.ON_HIDDEN, this);
                    gChsLoadPrst = 0;
                }
            };
            chngprstComboBox.addEventHandler(ComboBox.ON_HIDDEN, EHActChngPrstHide);
        }
    }

    @FXML
    void ChangePresetLoadAction (ActionEvent event){
        if (chngprstComboBox.getValue() != null) {
            chngprstMainPane.setVisible(false);
            int lmPrstIndx = chngprstComboBox.getSelectionModel().getSelectedIndex();
            gTmpPrstLst.clear();
            gTmpPrstLst.addAll(gAllPrsts.get(lmPrstIndx).getPreset());
            if (wrkwthprstPrstListWrkMod.isSelected()) {
                wrkwthprstAddedPrstVlues.getItems().clear();
                if (!wrkwthprstArrMd.isSelected()) {
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getItemLineMain(i));
                    }
                } else {
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getMassiveItemLineMain(i).get(0));
                    }
                }
            } else {
                wrkwthprstAddedPrstVlues.getItems().clear();
                for (int i = 0; i < gTmpPrstLst.size(); i++) {
                    wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(i).getItemType());
                }
            }

            globalItemsValuePane.setVisible(false);
            wrkwthprstLoadPrstBtn.setVisible(false);
            wrkwthprstChngPrstBtn.setVisible(false);
            globalPresetsMakePane.setVisible(true);
            wrkwthprstSrchPrstItm.setVisible(true);
            wrkwthprstPrstWrkMd.setVisible(true);
            wrkwthprstMakePrstBtn.setVisible(false);
            globalPresetsMakePane.setVisible(true);
            globalPresetPane.setVisible(true);
            chngprstRewrite.setVisible(true);
            chngprstRewriteCancel.setVisible(true);
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(2).get(19));
        }
    }

    @FXML
    void ChangePresetCloseAction (ActionEvent event){
        chngprstMainPane.setVisible(false);
        globalPresetPane.setVisible(true);
    }

    @FXML
    void ChangePresetRewriteAction (ActionEvent event) {
        if(Wm.AppWindow.dialogWindow(gsErrs.get(2).get(1)) == 1) {
            int lmChng = 0, lmChk = 0;
            File lmfMainPrst = new File(gStngBufr1[11].replace("*", "\\Presets\\" + chngprstComboBox.getValue() + ".obj"));
            File lmfRwrtPrst = new File("");
            if (chngprstComboBox.getValue().equals((chngprstLabelPresetName.getText()))) {
                lmfRwrtPrst = new File(gStngBufr1[11].replace("*", "\\Presets\\" + (chngprstComboBox.getValue() + '1')));
            } else {
                lmChng++;
                lmfRwrtPrst = new File(gStngBufr1[11].replace("*", "\\Presets\\" + chngprstLabelPresetName.getText() + ".obj"));
                for (int i = 0; i < gPrstsLoadInGame.size(); i++) {
                    if (gPrstsLoadInGame.get(i).equals(chngprstComboBox.getValue())) {
                        gPrstsLoadInGame.set(i, chngprstLabelPresetName.getText());
                    }
                }
                File lmfOld = new File(gStngBufr1[11].replace("*", "\\FileChanges\\" + chngprstComboBox.getValue()));
                File lmfRwrt = new File(gStngBufr1[11].replace("*", "\\FileChanges\\" + chngprstLabelPresetName.getText() + ".obj"));
                lmfOld.renameTo(lmfRwrt);
                chngprstComboBox.getItems().set(chngprstComboBox.getSelectionModel().getSelectedIndex(), chngprstLabelPresetName.getText() + ".obj");
            }

            if (lmfMainPrst.canRead()) {
                lmChk = 1;
            } else {
                if(Wm.AppWindow.dialogWindow(gsErrs.get(2).get(2)) == 1){
                    lmChk = 1;
                }
            }

            if (lmChk == 1){
                try {
                    FileOutputStream fos = new FileOutputStream(lmfRwrtPrst);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    Preset Prst = new Preset();
                    ArrayList<PresetObject> bufferlist = new ArrayList<PresetObject>();
                    for (int i = 0; i < gTmpPrstLst.size(); i++) {
                        bufferlist.add(gTmpPrstLst.get(i));
                    }
                    Prst.MakePreset(bufferlist, chngprstLabelPresetName.getText(), chngprstDescription.getText());
                    gAllPrsts.set(chngprstComboBox.getSelectionModel().getSelectedIndex(), Prst);
                    oos.writeObject(Prst);
                    oos.flush();
                    oos.close();
                    fos.close();
                } catch (IOException v) {
                    System.out.println(v);
                }
                lmfMainPrst.delete();
                if (lmChng == 0) {
                    lmfRwrtPrst.renameTo(lmfMainPrst);
                }
                chngprstComboBox.setValue(null);
                chngprstLabelPresetName.setText("");
                chngprstDescription.setText("");
                wrkwthprstChngPrstBtn.setVisible(true);
                wrkwthprstLoadPrstBtn.setVisible(true);
                chngprstRewrite.setVisible(false);
                chngprstRewriteCancel.setVisible(false);
                wrkwthprstAddedPrstVlues.getItems().clear();
                gTmpPrstLst.clear();
            }
        }
    }

    @FXML
    void ChangePresetRewriteCancelAction (ActionEvent event){
        if (Wm.AppWindow.dialogWindow(gsErrs.get(2).get(3)) == 1) {
            chngprstComboBox.setValue(null);
            chngprstLabelPresetName.setText("");
            chngprstDescription.setText("");
            wrkwthprstChngPrstBtn.setVisible(true);
            wrkwthprstLoadPrstBtn.setVisible(true);
            chngprstRewrite.setVisible(false);
            chngprstRewriteCancel.setVisible(false);
            wrkwthprstAddedPrstVlues.getItems().clear();
            gTmpPrstLst.clear();
        }
    }

    //Конец модуля ChangePreset


    @FXML
    void SeachPressetItemAction(ActionEvent event) {
        String lmSrchMd = wrkwthprstPrstWrkMd.getValue();
        if ((gItmVlueLnk > -1) && (lmSrchMd != null) && (lmSrchMd.length() > 1)) {
            String lmSrchVlue = "";
            int lmChk = 0;
            if (lmSrchMd.contains("Items on")) {
                lmSrchVlue = wrkwthprstChsScndType.getValue();
            } else if (lmSrchMd.contains("This item")) {
                lmSrchVlue = wrkwthprstChsItm.getValue();
            }
            for (int i = 0; i < wrkwthprstAddedPrstVlues.getItems().size(); i++) {
                if (lmSrchVlue.equals(wrkwthprstAddedPrstVlues.getItems().get(i))) {
                    lmChk++;
                }
            }

            if (wrkwthprstChsItm.getValue() == null ^ wrkwthprstPrstWrkMd.getValue() == null){
                lmChk++;
            }

            if (lmChk == 0) {
                lmChk = 0;
                int lmCls = 0;
                wrkwthprstPrstWrkMd.setDisable(true);
                for (int i = 0; i < wrkwthprstAddedPrstVlues.getItems().size(); i++) {
                    if (wrkwthprstAddedPrstVlues.getItems().get(i).equals(lmSrchVlue)) {
                        lmCls = 1;
                    }
                }
                lmChk = wrkwthprstAddedPrstVlues.getItems().size();
                if (lmCls == 0) {
                    //Оптимизировать подбор, пушо если будет более 100, лагать будет
                    gLnkPrstItm = lmSrchVlue;
                    PresetObject lmTmpPrstObj = new PresetObject();
                    lmTmpPrstObj.setItemType(gLnkPrstItm);
                    lmTmpPrstObj.addFastLinkObject(gFstLnkItm.get(wrkwthprstChsItm.getSelectionModel().getSelectedIndex()));
                    int lmPrstObjBgn = lmTmpPrstObj.getObjBgn();

                    int lmChk2 = 0;
                    if (gTmpPrstLst.size() > 0) {
                        for (int i = (gTmpPrstLst.size() - 1); i > -1; i--) {
                            int lmTmpObjBgn = gTmpPrstLst.get(i).getObjBgn();

                            if (lmPrstObjBgn > lmTmpObjBgn) {
                                i++;
                                gTmpPrstLst.add(i, lmTmpPrstObj);
                                gPrstItmIndx = i;
                                lmChk2++;
                                i = -1;
                            }
                        }
                        if (lmChk2 == 0) {
                            gTmpPrstLst.add(0, lmTmpPrstObj);
                            gPrstItmIndx = 0;
                        }
                    } else {
                        gTmpPrstLst.add(0, lmTmpPrstObj);
                        gPrstItmIndx = 0;
                    }
                    wrkwthprstAddedPrstVlues.getItems().add(gPrstItmIndx, gTmpPrstLst.get(gPrstItmIndx).getItemType());
                    switch (lmSrchMd) {
                        case ("Items on second type"):
                            if (wrkwthprstChsScndType.getValue() != null) {
                                gItmVlueLnk = 0;
                                try {
                                    File lmfItm = new File(gStngBufr1[2]);
                                    FileReader FR = new FileReader(lmfItm);
                                    Scanner scanner = new Scanner(FR);
                                    String lmScnStrng = "";
                                    String lmFstItmLn = "";
                                    int lmLnNmbr = 0;
                                    lmFstItmLn = gFstLnkItm.get(gItmVlueLnk).getprpsbgn();
                                    lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2));
                                    for (int i = 0; i < lmLnNmbr; i++) {
                                        scanner.nextLine();
                                    }
                                    int lmLnNmbrBgn = lmLnNmbr;
                                    lmFstItmLn = gFstLnkItm.get(gItmVlueLnk).getprpsend();
                                    lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2));
                                    for (int i = lmLnNmbrBgn; i < lmLnNmbr - 1; i++) {
                                        lmScnStrng = scanner.nextLine().trim();
                                        wrkwthprstVluesOnPrstItms.getItems().add(lmScnStrng);
                                    }
                                    FR.close();
                                    scanner.close();
                                } catch (IOException v) {
                                    System.out.println(v);
                                }
                            } else {
                                Wm.AppWindow.errorWindow(gsErrs.get(2).get(4));
                            }
                            break;
                        case ("Items in tag"):
                            if (gObTags.size() > 0){

                            } else {
                                Wm.AppWindow.errorWindow(gsErrs.get(2).get(5));
                            }

                            break;
                        case ("This item"):
                            if (wrkwthprstChsItm.getValue() != null) {
                                wrkwthprstVluesOnPrstItms.getItems().clear();
                                wrkwthprstVluesOnPrstItms.getItems().addAll(edtrItemValuesList.getItems());
                            } else {
                                Wm.AppWindow.errorWindow(gsErrs.get(2).get(6));
                            }
                            break;

                    }
                }
                wrkwthprstSrchPrstItm.setDisable(true);
            } else {
                Wm.AppWindow.errorWindow(gsErrs.get(2).get(7));
            }
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(2).get(8));
        }
    }

    @FXML
    void ClearPresetSelectionAction(ActionEvent event) {
        if (Wm.AppWindow.dialogWindow(gsErrs.get(2).get(9)) == 1) {
            wrkwthprstVluesOnPrstItms.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void AddToListAction(ActionEvent event) {
        if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size() > 0){
            //Тут нужно сравнить уже добавленные с добавляемыми по выбранным линиям
            int lmChk = 0, lmChrsNmbr = 0, lmChrsSize = 0;
            if (gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber() > 0) {
                for (int i = (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size() - 1); i > -1;  i--) {
                    for (int o = (gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber() - 1); o > -1 ; o--) {
                        if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i).equals(gTmpPrstLst.get(gPrstItmIndx).getStringNumber(o))) {
                            wrkwthprstVluesOnPrstItms.getSelectionModel().clearSelection(wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i));
                            lmChk ++;
                        }
                        if (lmChk > 0) break;
                    }
                    lmChk = 0;
                }
            }
            if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size() > 0) {
                String lmStrng = ""; String lmCtgry = ""; String lmIdLvl = "";
                int lmStrLngth = 0;
                int lmMainLvl = 0, lmObjLvl = 0, lmGrpIndx = 0;
                int lmScnLn = 0;
                lmChk = 0;
                for (int i = 0; i < wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size(); i++) {
                    ItemLine ItmLn = new ItemLine();
                    do {
                        lmStrng = wrkwthprstVluesOnPrstItms.getItems().get(lmScnLn);
                        lmStrLngth = lmStrng.length() - 1;
                        if (lmStrLngth > 3) {
                            lmChrsNmbr = lmStrLngth;
                            lmChrsSize = (lmStrLngth - 3);
                        } else {
                            if (lmStrLngth == 2){
                                lmChrsNmbr = 1;
                                lmChrsSize = -1;
                            } else {
                                lmChrsNmbr = 0;
                                lmChrsSize = -1;
                            }
                        }
                        for (int o = lmChrsNmbr; o > lmChrsSize; o--) {
                            if (lmChk == 0 && lmStrng.charAt(o) == '[') {
                                lmMainLvl++;
                                if (lmMainLvl == 1) {
                                    lmCtgry = lmStrng;
                                }
                                lmChk = 1;
                            }
                            if (lmChk == 0 && lmStrng.charAt(o) == ']') {
                                if (lmMainLvl == 0) {
                                    lmGrpIndx = 0;
                                }
                                lmMainLvl--;
                                lmChk = 1;
                            }
                            if (lmChk == 0 && lmStrng.charAt(o) == '{') {
                                lmObjLvl++;
                                if (lmMainLvl == 1 & lmObjLvl == 1) {
                                    lmGrpIndx++;
                                    lmIdLvl = wrkwthprstVluesOnPrstItms.getItems().get(lmScnLn + 1);
                                } else if (lmObjLvl == 1) {
                                    lmIdLvl = lmStrng;
                                }
                                lmChk = 1;
                            }
                            if (lmChk == 0 && lmStrng.charAt(o) == '}') {
                                lmObjLvl--;
                            }
                            lmChk = 0;
                        }
                        lmScnLn++;
                    } while (lmScnLn <= wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i));

                    //Первый тип простая строка
                    if (lmChk == 0) {
                        if (lmObjLvl == 0 & lmMainLvl == 0) {
                            lmCtgry = "main";
                            ItmLn.makemainItemLine(lmCtgry, lmStrng);
                            lmChk++;
                        }
                    }

                    //Второй тип строка в объекте без уровней
                    if (lmChk == 0) {
                        if (lmObjLvl == 1 & lmMainLvl == 0) {
                            lmCtgry = "scnd";
                            ItmLn.makeobjectItemLine(lmCtgry, lmIdLvl, lmStrng);
                            lmChk++;
                        }
                    }

                    //Третий тип строка в объекте с уровнями
                    if (lmChk == 0) {
                        if (lmMainLvl > 0) {
                            ItmLn.makeobjectItemLine(lmCtgry, lmIdLvl, lmStrng);
                            lmChk++;
                        }
                    }

                    lmChk = 0;
                    lmGrpIndx = wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i);
                    //Доработать подбор потому что если элементов больше 100 лагать будет
                    //Всего будет 5 частей сначала сравнивается с серединой, а дальше до 100 или до 75 и выискиваются диапазоны поиска, оптимизация
                    if (gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber() > 0) {
                        for (int o = (gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber() - 1); o > -1; o--) {
                            if (lmGrpIndx > gTmpPrstLst.get(gPrstItmIndx).getStringNumber(o)) {
                                gTmpPrstLst.get(gPrstItmIndx).addItemLine(ItmLn, wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i), (o + 1));
                                lmChk++;
                            }
                            if (lmChk > 0) break;
                        }
                        if (lmChk == 0) {
                            gTmpPrstLst.get(gPrstItmIndx).addItemLine(ItmLn, wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i), 0);
                        }
                    } else {
                        gTmpPrstLst.get(gPrstItmIndx).addItemLine(ItmLn, wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i), 0);
                    }
                    lmChk = 0;

                    //Два массива для запоминая объектов внутри объекта сравнивается количество скобок в порядке возрастания для первого, в убывании для увторого
                    //Переписывает строку по совпадению либо добавляет, либо заменяет
                    //У каждого объекта есть name тоесть, строка сохраняемая в объекте будет определятся по нему
                    //Проверяется следующая выбранная строка это следующая строка или нет

                    //Ебануть возможность замены массива строк в виде кнопок, по типу смены патронов для ак
                }
                if (wrkwthprstPrstListWrkMod.isSelected()) {
                    if (!wrkwthprstArrMd.isSelected()) {
                        wrkwthprstAddedPrstVlues.getItems().clear();
                        for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber(); i++) {
                            wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getItemLineMain(i));
                        }
                    }
                }
            }else {
                Wm.AppWindow.errorWindow(gsErrs.get(2).get(10));
            }
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(2).get(11));
        }
    }

    @FXML
    void RemoveLinesAction(ActionEvent event) {
        if(wrkwthprstPrstListWrkMod.isSelected()) {
            if (!wrkwthprstArrMd.isSelected()) {
                if (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().size() > 0) {
                    int lmChk = 0;
                    for (int i = (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().size() - 1); i > -1; i--) {
                        for (int o = (gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber() - 1); o > -1; o--) {
                            if (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().get(i).equals(o)) {
                                gTmpPrstLst.get(gPrstItmIndx).removeItemline(o);
                                lmChk++;
                            }
                            if (lmChk > 0) break;
                        }
                        lmChk = 0;
                    }
                    wrkwthprstAddedPrstVlues.getItems().clear();
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getItemLineMain(i));
                    }
                } else {
                    Wm.AppWindow.errorWindow(gsErrs.get(2).get(12));
                }
            }else {
                if (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().size() > 0) {
                    int lmChk = 0;
                    for (int i = (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().size() - 1); i > -1; i--) {
                        for (int o = (gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber() - 1); o > -1; o--) {
                            if (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().get(i).equals(o)) {
                                gTmpPrstLst.get(gPrstItmIndx).removeMassiveItemline(o);
                                lmChk++;
                            }
                            if (lmChk > 0) break;
                        }
                        lmChk = 0;
                    }
                    wrkwthprstAddedPrstVlues.getItems().clear();
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getMassiveItemLineMain(i).get(0));
                    }
                }else {
                    Wm.AppWindow.errorWindow(gsErrs.get(2).get(12));
                }
            }
        }
    }



    @FXML
    void wrkwthprstArrToChngaction(ActionEvent event) {
        if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size() > 0) {

           for (int i = 0; i < wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size(); i ++) {
               if (!wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedItems().get(i).contains("{") &
                       !wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedItems().get(i).contains("[")) {
                   wrkwthprstVluesOnPrstItms.getSelectionModel().clearSelection(wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i));
               } else if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedItems().get(i).contains("}") |
                       wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedItems().get(i).contains("]")) {
                   wrkwthprstVluesOnPrstItms.getSelectionModel().clearSelection(wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i));
               }
           }
            System.out.println("Количство выбранных элементов в списке " + wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size());

                //Тут нужно сравнить уже выбранные с добавляемыми по выбранным линиям

                if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size() > 0){
                if (gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber() > 0) {
                    System.out.println("Количество строк в отсчете массива " + gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber());
                    for (int i = (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size() - 1); i > -1; i--) {
                        for (int o = (gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber() - 1); o > -1; o--) {
                            System.out.println("Значение в строке массива " + gTmpPrstLst.get(gPrstItmIndx).getStringNumberArray(o));
                            System.out.println("Значение в выбранном индексе " + wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i));
                            System.out.println("Они равны c выбранным индексом "
                                    + wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i).equals(gTmpPrstLst.get(gPrstItmIndx).getStringNumberArray(o)));
                            if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i).equals(gTmpPrstLst.get(gPrstItmIndx).getStringNumberArray(o))) {
                                System.out.println("Уже добавлено");
                                wrkwthprstVluesOnPrstItms.getSelectionModel().clearSelection(wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i));
                                o = -1;
                            }
                        }
                    }
                }
                System.out.println("Количество выбранных строк в листе после отсева " + wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size());
                if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size() > 0) {
                    System.out.println("Work");
                    ArrayList<String> lmTmpStrArr = new ArrayList<String>();
                    ArrayItemLine ItmLn = new ArrayItemLine();

                    String lmStrng = "";
                    String lmCtgry = "";
                    String lmIdLvl = "";
                    char lmChr1 = ' ', lmChr2 = ' ';
                    int lmChk = 0, lmChrsNmbr = 0, lmChrsSize = 0;
                    int lmStrLngth = 0;
                    int lmMainLvl = 0, lmObjLvl = 0, lmGrpIndx = 0;
                    int lmScnLn = 0;
                    Integer[] lmStrNmbr = new Integer[2];

                    for (int i = 0; i < wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size(); i++) {
                        do {
                            lmStrng = wrkwthprstVluesOnPrstItms.getItems().get(lmScnLn);
                            lmStrLngth = lmStrng.length() - 1;
                            if (lmStrLngth > 3) {
                                lmChrsNmbr = lmStrLngth;
                                lmChrsSize = (lmStrLngth - 3);
                            } else {
                                if (lmStrLngth == 2){
                                    lmChrsNmbr = 1;
                                    lmChrsSize = -1;
                                } else {
                                    lmChrsNmbr = 0;
                                    lmChrsSize = -1;
                                }
                            }
                            for (int o = lmChrsNmbr; o > lmChrsSize; o--) {
                                if (lmStrng.charAt(o) == '[') {
                                    lmMainLvl++;
                                    if (lmMainLvl == 1) {
                                        lmCtgry = lmStrng;
                                    }
                                    lmChk = 1;
                                }
                                if (lmChk == 0 && lmStrng.charAt(o) == ']') {
                                    if (lmMainLvl == 0) {
                                        lmGrpIndx = 0;
                                    }
                                    lmMainLvl--;
                                    lmChk = 1;
                                }
                                if (lmChk == 0 && lmStrng.charAt(o) == '{') {
                                    lmObjLvl++;
                                    if (lmMainLvl == 1 & lmObjLvl == 1) {
                                        lmGrpIndx++;
                                        lmIdLvl = wrkwthprstVluesOnPrstItms.getItems().get(lmScnLn + 1);
                                    } else if (lmObjLvl == 1) {
                                        lmIdLvl = lmStrng;
                                    }
                                    lmChk = 1;
                                }
                                if (lmChk == 0 && lmStrng.charAt(o) == '}') {
                                    lmObjLvl--;
                                }
                                lmChk = 0;
                            }
                            lmScnLn++;
                        } while (lmScnLn <= wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i));

                        if (lmStrng.contains("{")) {
                            lmChr1 = '{';
                            lmChr2 = '}';
                            System.out.println("фигурная скобка {");
                        } else if (lmStrng.contains("[")) {
                            System.out.println("квадратная скобка [");
                            lmChr1 = '[';
                            lmChr2 = ']';
                        }

                        lmChk = 1;
                        lmStrNmbr[0] = lmScnLn - 1;

                        lmTmpStrArr.add(lmStrng);

                        do {
                            lmStrng = wrkwthprstVluesOnPrstItms.getItems().get(lmScnLn);
                            lmTmpStrArr.add(lmStrng);
                            lmStrLngth = lmStrng.length() - 1;
                            if (lmStrLngth > 3) {
                                lmChrsNmbr = lmStrLngth;
                                lmChrsSize = (lmStrLngth - 3);
                            } else {
                                if (lmStrLngth == 2){
                                    lmChrsNmbr = 1;
                                    lmChrsSize = -1;
                                } else {
                                    lmChrsNmbr = 0;
                                    lmChrsSize = -1;
                                }
                            }

                            for (int o = lmChrsNmbr; o > lmChrsSize; o--) {
                                if (lmStrng.charAt(o) == lmChr1) {
                                    lmChk++;
                                } else if (lmStrng.charAt(o) == lmChr2) {
                                    lmChk--;
                                }
                            }
                            lmScnLn++;
                        } while (lmChk > 0);

                        lmStrNmbr[1] = lmScnLn - 1;
                        lmChk = 0;

                        //Первый тип простая строка
                        if (lmChk == 0) {
                            if (lmObjLvl == 0 & lmMainLvl == 0) {
                                lmCtgry = "main";
                                ItmLn.makeArrayItemLine(lmCtgry, lmTmpStrArr);
                                lmChk++;
                            }
                        }

                        //Второй тип строка в объекте без уровней
                        if (lmChk == 0) {
                            if (lmObjLvl == 1 & lmMainLvl == 0) {
                                lmCtgry = "scnd";
                                ItmLn.makeObjectArrayItemLine(lmCtgry, lmIdLvl, lmTmpStrArr);
                                lmChk++;
                            }
                        }

                        //Третий тип строка в объекте с уровнями
                        if (lmChk == 0) {
                            if (lmMainLvl > 0) {
                                ItmLn.makeObjectArrayItemLine(lmCtgry, lmIdLvl, lmTmpStrArr);
                                lmChk++;
                            }
                        }

                        lmChk = 0;
                        lmGrpIndx = wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i);
                        //TODO Доработать подбор потому что если элементов больше 100 лагать будет
                        //Всего будет 5 частей сначала сравнивается с серединой, а дальше до 100 или до 75 и выискиваются диапазоны поиска, оптимизация
                        if (gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber() > 0) {
                            for (int o = (gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber() - 1); o > -1; o--) {
                                if (lmGrpIndx > gTmpPrstLst.get(gPrstItmIndx).getStringNumberArray(o)) {
                                    gTmpPrstLst.get(gPrstItmIndx).addMassiveItemLine(ItmLn, lmStrNmbr, (o + 1));
                                    lmChk++;
                                }
                                if (lmChk > 0) break;
                            }
                            if (lmChk == 0) {
                                gTmpPrstLst.get(gPrstItmIndx).addMassiveItemLine(ItmLn, lmStrNmbr, 0);
                            }
                        } else {
                            gTmpPrstLst.get(gPrstItmIndx).addMassiveItemLine(ItmLn, lmStrNmbr, 0);
                        }

                        if ((i + 1) < wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().size()) {
                            if (wrkwthprstVluesOnPrstItms.getSelectionModel().getSelectedIndices().get(i + 1) < lmScnLn) {
                                lmScnLn = 0;
                            } else {
                                lmScnLn++;
                            }
                        }
                        /*Два массива для запоминая объектов внутри объекта сравнивается количество скобок в порядке возрастания для первого, в убывании для второго
                        Переписывает строку по совпадению либо добавляет, либо заменяет
                        У каждого объекта есть name то есть, строка сохраняемая в объекте будет определятся по нему
                        Проверяется следующая выбранная строка это следующая строка или нет*/

                        //Бахнуть возможность замены массива строк в виде кнопок, по типу смены патронов для ак
                        lmChk = 0;
                        lmTmpStrArr = new ArrayList<String>();
                        ItmLn = new ArrayItemLine();
                    }

                    System.out.println("Всего доступно массивов " + gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber());
                    if (wrkwthprstPrstListWrkMod.isSelected()) {
                        if (wrkwthprstArrMd.isSelected()) {
                            wrkwthprstAddedPrstVlues.getItems().clear();
                            for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber(); i++) {
                                System.out.println("\n\n Предмет в пресете " + i);
                                System.out.println("Доступно строк в массиве " + gTmpPrstLst.get(gPrstItmIndx).getMassiveItemLineMain(i).size());
                                wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getMassiveItemLineMain(i).get(0));
                            }
                        }
                    }
                } else {
                    Wm.AppWindow.errorWindow(gsErrs.get(2).get(13));
                }
            } else {
                    Wm.AppWindow.errorWindow(gsErrs.get(2).get(14));
                }
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(2).get(15));
        }
    }

    @FXML
    void PresetOkAction(ActionEvent event) {
        wrkwthprstPrstOk.setVisible(false);
        wrkwthprstPrstCancel.setVisible(false);
        wrkwthprstCancelPrst.setVisible(false);
        wrkwthprstSavePrstBtn.setVisible(false);
        gTmpPrstLst.clear();
        gPrstItmIndx = -1;
        wrkwthprstAddedPrstVlues.getItems().clear();
        globalPresetsMakePane.setVisible(false);
        wrkwthprstSrchPrstItm.setVisible(false);
        wrkwthprstSrchPrstItm.setDisable(false);
        wrkwthprstPrstWrkMd.setVisible(false);
        wrkwthprstPrstWrkMd.setDisable(false);
        wrkwthprstLoadPrstBtn.setVisible(true);
        wrkwthprstChngPrstBtn.setVisible(true);
        wrkwthprstMakePrstBtn.setVisible(true);
        globalPresetsMakePane.setVisible(false);
        globalItemsValuePane.setVisible(true);
    }

    @FXML
    void PresetCancelAction(ActionEvent event) {
        wrkwthprstPrstOk.setVisible(false);
        wrkwthprstPrstCancel.setVisible(false);
        wrkwthprstCancelPrst.setVisible(true);
        wrkwthprstSavePrstBtn.setVisible(true);
    }


    @FXML
    void ChangeItemOnPresetAction(ActionEvent event) {
        if (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().size() == 1){
            gPrstItmIndx = wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndex();
            ArrayList<String> itemlinks = gTmpPrstLst.get(gPrstItmIndx).getObjectStringsIndexArray();
            try{
                File lmfItm = new File(gStngBufr1[2]);
                FileReader FR = new FileReader(lmfItm);
                Scanner scanner = new Scanner(FR);
                String lmScnStrng = "";
                String lmFstItmLn = "";
                int lmLnNmbr = 0;
                lmFstItmLn = itemlinks.get(2);
                lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2, lmFstItmLn.length()));
                for (int i = 0; i < lmLnNmbr; i++){
                    scanner.nextLine();
                }

                int lmBgnLnNmbr = lmLnNmbr;
                lmFstItmLn = itemlinks.get(3);
                lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2, lmFstItmLn.length()));
                wrkwthprstVluesOnPrstItms.getItems().clear();
                for (int i = lmBgnLnNmbr; i < lmLnNmbr - 1; i++){
                    lmScnStrng = scanner.nextLine().trim();
                    wrkwthprstVluesOnPrstItms.getItems().add(lmScnStrng);
                }
                FR.close();
                scanner.close();
            } catch (IOException v){System.out.println(v);}
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(2).get(16));
        }

    }

    @FXML
    void AddNewItemAction(ActionEvent event) {
        gLnkPrstItm = "";
        gPrstItmIndx = -1;
        wrkwthprstPrstWrkMd.setDisable(false);
        wrkwthprstSrchPrstItm.setDisable(false);
    }

    @FXML
    void DeleteItemAction(ActionEvent event) {
        if (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().size() > 0) {
            if (Wm.AppWindow.dialogWindow(gsErrs.get(2).get(17)) == 1) {
                if (!wrkwthprstPrstListWrkMod.isSelected()) {
                    for (int i = (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().size() - 1); i > -1; i--) {
                        for (int o = (gTmpPrstLst.size() - 1); o > -1; o--) {
                            if (wrkwthprstAddedPrstVlues.getSelectionModel().getSelectedIndices().get(i).equals(o)) {
                                System.out.println("Индекс " + i);
                                System.out.println("Отсчет " + i);
                                gTmpPrstLst.remove(o);
                                o = -1;
                            }
                        }
                    }

                    wrkwthprstAddedPrstVlues.getItems().clear();
                    for (int i = 0; i < gTmpPrstLst.size(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(i).getItemType());
                    }
                }else {
                    Wm.AppWindow.dialogWindow(gsErrs.get(2).get(18));
                }
            }
        } else {
            Wm.AppWindow.dialogWindow(gsErrs.get(2).get(18));
        }
    }

    @FXML
    void PresetListWorkModeAction(ActionEvent event) {
        if (gTmpPrstLst.size() > 0){
            if (wrkwthprstPrstListWrkMod.isSelected()) {
                wrkwthprstAddedPrstVlues.getItems().clear();
                if (!wrkwthprstArrMd.isSelected()) {
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getItemLineMain(i));
                    }
                } else {
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getMassiveItemLineMain(i).get(0));
                    }
                }
            } else {
                wrkwthprstAddedPrstVlues.getItems().clear();
                for (int i = 0; i < gTmpPrstLst.size(); i++) {
                    wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(i).getItemType());
                }
            }
        }
    }


    @FXML
    void ArrayModeAction(ActionEvent event) {
        if (wrkwthprstPrstListWrkMod.isSelected()) {
            if (gTmpPrstLst.size() > 0) {
                if (wrkwthprstArrMd.isSelected()) {
                    wrkwthprstAddedPrstVlues.getItems().clear();
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeMassiveLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getMassiveItemLineMain(i).get(0));
                    }
                } else {
                    wrkwthprstAddedPrstVlues.getItems().clear();
                    for (int i = 0; i < gTmpPrstLst.get(gPrstItmIndx).getSizeLineNumber(); i++) {
                        wrkwthprstAddedPrstVlues.getItems().add(gTmpPrstLst.get(gPrstItmIndx).getItemLineMain(i));
                    }
                }
            }
        }
    }

    @FXML
    void WorkModeClick(MouseEvent event) {

    }


    @FXML
    void Menutitlepanepressed(MouseEvent event) {

    }

    //Начало модуля изменения строк в объекте
    @FXML
    void Saveinbackuponaction(ActionEvent event) {
        //TODO Нет возможности опознавания предмета
        mainsidepaneSaveInBckp.setDisable(true);
        UniModls.LnsAddWrk.SaveListChangesInFile(0);
        mainsidepaneSaveInBckp.setDisable(false);
    }

    @FXML
    void Uploadtogameinaction(ActionEvent event) {
        //TODO Нет возможности опознавания предмета надо бахнуть
        mainsidepaneUpldToGame.setDisable(true);
        String lmFileName = "";
        String lmGmFileName = "";
        int lmWrkmd = 0;

        if (globalItemValues.isExpanded()){
                lmFileName = gStngBufr1[2];
                lmGmFileName = gStngBufr1[9];
                lmWrkmd = 1;
        }else if (globalGlobalsEditor.isExpanded()){
                lmFileName = gStngBufr1[3];
                lmGmFileName = gStngBufr1[10];
                lmWrkmd = 1;
        }else if (globalLocationsEditor.isExpanded()) {
            //TODO убрать заглушку и переделать механику замены в файлах
            LocationChgnsStrctr lmLcs = new LocationChgnsStrctr();
            ArrayList<String> lmLctnDt = new ArrayList<String>();
            if ((edtrLctnsLstView.getItems() != null) && (edtrLctnsLstView.getItems().size() > 0)) {
                lmLctnDt.addAll(edtrLctnsLstView.getItems());
                lmLcs.wrtLctnDataToGm(lmLctnDt, edtrChsngLctn.getValue(), edtrChsngLctnGrp.getValue(), gStngBufr1[10]);
            } else {
                Wm.AppWindow.errorWindow(gsErrs.get(5).get(6));
            }
        }else {
                Wm.AppWindow.errorWindow(gsErrs.get(3).get(4));
        }

        if (lmWrkmd == 1) {
            File lmfGmMainFile = new File(lmGmFileName);

            if (!lmfGmMainFile.canRead()) {
                Wm.AppWindow.errorWindow(gsErrs.get(3).get(5));
            } else {
                Wm.Write.writeFile(lmGmFileName, lmFileName,1,false);
            }
        }
        mainsidepaneUpldToGame.setDisable(false);
    }

    @FXML
    void Globaldontregistrateaction(ActionEvent event) {
        mainsidepaneGlblRstAll.setDisable(true);
        if (globalItemValues.isExpanded()) {
            edtrItemValuesList.getItems().clear();
            for (int i = 0; i < gItmEdtrBckp.size(); i++) {
                edtrItemValuesList.getItems().add(gItmEdtrBckp.get(i));
            }
        } else if (globalGlobalsEditor.isExpanded()) {
            edtrGlblLstView.getItems().clear();
            for (int i = 0; i < gGlblEdtrBckp.size(); i++) {
                edtrGlblLstView.getItems().add(gGlblEdtrBckp.get(i));
            }
        }else if (globalLocationsEditor.isExpanded()){
            edtrLctnsLstView.getItems().clear();
            for (int i = 0; i < gLctnEdtrBckp.size(); i++) {
                edtrLctnsLstView.getItems().add(gLctnEdtrBckp.get(i));
            }
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(3).get(6));
        }
        mainsidepaneGlblRstAll.setDisable(false);
    }

    @FXML
    void GlobalSaveInBufferAction(ActionEvent event) {
        if (globalItemValues.isExpanded()){
            gItmEdtrBckp.clear();
            for (int i = 0; i < edtrItemValuesList.getItems().size(); i++){
                gItmEdtrBckp.add(edtrItemValuesList.getItems().get(i));
            }
        } else if(globalGlobalsEditor.isExpanded()){
            gGlblEdtrBckp.clear();
            for (int i = 0; i < edtrGlblLstView.getItems().size(); i++){
                gGlblEdtrBckp.add(edtrGlblLstView.getItems().get(i));
            }
        } else if (globalLocationsEditor.isExpanded()){
            gLctnEdtrBckp.clear();
            for (int i = 0; i < edtrLctnsLstView.getItems().size(); i++){
                gLctnEdtrBckp.add(edtrLctnsLstView.getItems().get(i));
            }
        } else{
            Wm.AppWindow.errorWindow(gsErrs.get(3).get(7));
        }
    }

    @FXML
    void Globalresetchangesaction(ActionEvent event) {
        mainsidepaneRstChngs.setDisable(true);
        String lmTmpItm = "";
        int lmChk = 0;
        ListView<String> lmLstLnk = new ListView<String>();
        ArrayList<String> lmLstEdtrBckpLnk = new ArrayList<String>();

        if (globalItemValues.isExpanded()) {
            lmLstLnk = edtrItemValuesList;
            lmLstEdtrBckpLnk = gItmEdtrBckp;
        } else if (globalGlobalsEditor.isExpanded()) {
            lmLstLnk = edtrGlblLstView;
            lmLstEdtrBckpLnk = gGlblEdtrBckp;
        } else if (globalLocationsEditor.isExpanded()){
            lmLstLnk = edtrLctnsLstView;
            lmLstEdtrBckpLnk = gLctnEdtrBckp;
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(3).get(8));
            lmChk++;
        }

        if(lmChk == 0){
            for (int i = 0; i < lmLstLnk.getSelectionModel().getSelectedIndices().size(); i++){
                lmChk = lmLstLnk.getSelectionModel().getSelectedIndices().get(i);
                lmTmpItm = lmLstEdtrBckpLnk.get(lmChk);
                lmLstLnk.getItems().set(lmChk, lmTmpItm);
            }
        }

        mainsidepaneRstChngs.setDisable(false);
    }

    //Панель для добавление и убирания строк в редакторах, пока скрыт пушто нет модуля анализа смещения строк
    @FXML
    void ItemWorkChangeWorkModeAction(ActionEvent event) {
        if (mainsidepaneItmWrkChngWrkMode.isSelected()){
            mainsidepaneItmWrkGlblLoadVluesPane.setVisible(true);
            mainsidepaneItmWrkGlblLnsAddRmvPane.setVisible(false);
        } else {
            mainsidepaneItmWrkGlblLoadVluesPane.setVisible(false);
            mainsidepaneItmWrkGlblLnsAddRmvPane.setVisible(true);
        }
    }

    @FXML
    void ItemWorkAddLinesAction(ActionEvent event) {
        rdctrpaneAddLns.setDisable(true);
        if (globalItemValues.isExpanded()){
            UniModls.LnsAddWrk.AddOrRemoveLines(0, 0);
        } else if (globalGlobalsEditor.isExpanded()){
            UniModls.LnsAddWrk.AddOrRemoveLines(1, 0);
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(4).get(1));
        }
        rdctrpaneAddLns.setDisable(false);
    }

    @FXML
    void ItemWorkAddMassiveLinesAction(ActionEvent event) {
        rdctrpaneAddLnsArr.setDisable(true);
        mainsidepaneItmWrkGlblLnsAddRmvPane.setVisible(false);

        mainsidepaneItmWrkArrLnsAddPane.setVisible(true);
        rdctrpaneAddLnsArr.setDisable(false);
    }

    ///Третья панель для массива линий
    @FXML
    void ItemWorkOkMassiveLineAddAction(ActionEvent event) {
        if (globalItemValues.isExpanded()){
            UniModls.LnsAddWrk.AddOrRemoveLines(0, 1);
        } else if (globalGlobalsEditor.isExpanded()){
            UniModls.LnsAddWrk.AddOrRemoveLines(1, 1);
        } else {
            Wm.AppWindow.errorWindow(gsErrs.get(4).get(2));
        }
        mainsidepaneItmWrkArrLnsAddPane.setVisible(false);
        mainsidepaneItmWrkGlblLnsAddRmvPane.setVisible(true);
    }

    @FXML
    void ItemWorkCancelMassiveLineAddAction(ActionEvent event) {
        mainsidepaneItmWrkArrLnsAddPane.setVisible(false);
        mainsidepaneItmWrkGlblLnsAddRmvPane.setVisible(true);
    }
    ///Конец третьей панели

    @FXML
    void ItemWorkRemoveLinesAction(ActionEvent event) {
        rdctrpaneRmvLns.setDisable(true);
        if (Wm.AppWindow.dialogWindow(gsErrs.get(4).get(3)) == 1) {
            if (globalItemValues.isExpanded()) {
                UniModls.LnsAddWrk.AddOrRemoveLines(0, 2);
            } else if (globalGlobalsEditor.isExpanded()) {
                UniModls.LnsAddWrk.AddOrRemoveLines(1, 2);
            } else {
                Wm.AppWindow.errorWindow(gsErrs.get(4).get(4));
            }
        }
        rdctrpaneRmvLns.setDisable(false);
    }

    @FXML
    void ItemWorkResetNumberOfLinesAction(ActionEvent event) {
        rdctrpaneRstNmbrOfLns.setDisable(true);
        if (Wm.AppWindow.dialogWindow(gsErrs.get(4).get(5)) == 1) {
            if (globalItemValues.isExpanded()) {
                UniModls.LnsAddWrk.AddOrRemoveLines(0, 3);
            } else if (globalGlobalsEditor.isExpanded()) {
                UniModls.LnsAddWrk.AddOrRemoveLines(1, 3);
            } else {
                Wm.AppWindow.errorWindow(gsErrs.get(4).get(6));
            }
        }
        rdctrpaneRstNmbrOfLns.setDisable(false);
    }

    //Конец модуля изменения строк в объекте

    //Начало модуля Оглавления
    @FXML
    void aboutprogrammonaction(ActionEvent event) {
    }

    @FXML
    void changelogonaction(ActionEvent event) {

    }

    @FXML
    void Settingbutton(ActionEvent event) throws IOException {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Windows/Settings.fxml"));
            Scene scene = new Scene(root);
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e){
            System.out.println("Ошибка открытия сцены Settings: " + e);
        }
    }

    //Конец модуля

    @FXML
    void wrkwthprstResetSelectionaction(ActionEvent event) {

    }

    @FXML
    void itemsregistrateaction(ActionEvent event) {

    }

    //Начало фабрик и универсальных модулей
    class UniversalModules {
       final ChsGrpItm ChsGrpItm = new ChsGrpItm();
        final WrkWthPreset PrstWrk = new WrkWthPreset();
        final AddLnsToItm LnsAddWrk = new AddLnsToItm();

        class ChsGrpItm {

            void FindItmsWthGrps(int gWrkmd, ObservableList<String> gTags) {
                try {
                    wrkwthprstChsItm.getItems().clear();
                    wrkwthprstChsItm.setValue(null);
                    gFstLnkItm.clear();
                    gObChsdTags.clear();

                    ArrayList<Integer> lmFrstType = new ArrayList<>();
                    ArrayList<ArrayList<Integer>> lmScndType = new ArrayList<>();
                    int lmArrSize = gMainItmArr.getarraysize();
                    int lmMainId = 0, lmScndId = 0;

                    if (wrkwthprstChsTypeOfItm.getValue().length() > 1) {
                        for (int j = 0; j < lmArrSize; j++) {
                            if (gMainItmArr.getglblsecondtype(j).contains(wrkwthprstChsTypeOfItm.getValue())) {
                                lmFrstType.add(j);
                                j = lmArrSize + 1;
                            }
                        }
                    } else {
                        for (int j = 0; j < lmArrSize; j++) {
                            lmFrstType.add(j);
                        }
                    }

                    if (gWrkmd == 0) {

                    } else if (gWrkmd == 1) {
                        if (wrkwthprstChsScndType.getValue().length() > 1) {
                            int lmFrstChk;
                            for (int j = 0; j < lmFrstType.size(); j++) {
                                ArrayList<Integer> lmTmpLstBuf = new ArrayList<>();
                                lmFrstChk = lmFrstType.get(j);
                                for (int k = 0; k < gMainItmArr.getarraysizescnd(lmFrstChk); k++) {
                                    if (gMainItmArr.getitemscndtype(lmFrstChk, k).contains(wrkwthprstChsScndType.getValue())) {
                                        lmFrstType.clear();
                                        lmFrstType.add(lmFrstChk);
                                        lmTmpLstBuf.add(k);
                                        lmScndType.add(lmTmpLstBuf);
                                        k = gMainItmArr.getarraysizescnd(lmFrstChk) + 1;
                                        j = lmFrstType.size() + 1;
                                        lmArrSize = -1;
                                    }
                                }
                            }
                        }
                    }

                    if (lmArrSize != -1) {
                        int lmFrstChk;
                        for (int j = 0; j < lmFrstType.size(); j++) {
                            ArrayList<Integer> lmTmpLstBuf = new ArrayList<>();
                            lmFrstChk = lmFrstType.get(j);
                            if (gWrkmd == 0) {
                                for (int k = 0; k < gMainItmArr.getarraysizescnd(lmFrstChk); k++) {
                                    lmTmpLstBuf.add(k);
                                    gObLstScndTp.addAll(gMainItmArr.getitemscndtype(lmFrstChk, k));
                                }
                            } else {
                                for (int k = 0; k < gMainItmArr.getarraysizescnd(lmFrstChk); k++) {
                                    lmTmpLstBuf.add(k);
                                }
                            }
                            lmScndType.add(lmTmpLstBuf);
                        }
                    }

                    //Проверка тегов
                    if(gTags != null && gTags.size() > 0){
                        //Для передачи тегов
                        ArrayList<String> lmTmpStrs = new ArrayList<String>(gTags);
                        for (int i = (lmFrstType.size() - 1); i > -1; i--) {
                            lmMainId = lmFrstType.get(i);
                            for (int o = (lmScndType.get(i).size()- 1); o > -1 ; o--) {
                                lmArrSize = 0;
                                lmScndId = lmScndType.get(i).get(o);
                                if (gMainItmArr.getalltagsthrd(lmMainId, lmScndId).size() > 1) {
                                    gObChsdTags.addAll(gMainItmArr.getalltagsthrd(lmMainId, lmScndId));
                                }
                                for (int k = 0; k < gObTags.size(); k++) {
                                    for (int p = 0; p < gMainItmArr.getalltagsthrd(lmMainId, lmScndId).size(); p++) {
                                        if (gMainItmArr.getalltagsthrd(lmMainId, lmScndId).get(p).contains(gObTags.get(k).toString())){
                                            p = (gMainItmArr.getalltagsthrd(lmMainId, lmScndId).size() + 1);
                                            k = (gObTags.size() + 1);
                                            lmArrSize = 1;
                                        }
                                    }
                                }

                                if (lmArrSize == 0){
                                    lmScndType.get(i).remove(o);
                                }

                            }

                            if (lmScndType.get(i).size() == 0){
                                lmScndType.remove(i);
                                lmFrstType.remove(i);
                            }

                        }

                        for (int i = (lmFrstType.size() - 1); i > -1; i--) {
                            lmMainId = lmFrstType.get(i);
                            for (int o = (lmScndType.get(i).size() - 1); o > -1; o--) {
                                lmArrSize = 0;
                                lmScndId = lmScndType.get(i).get(o);
                                for (int k = 0; k < gObTags.size(); k++) {
                                    for (int p = 0; p < gMainItmArr.getarraysizethrd(lmMainId, lmScndId); p++) {
                                        if (gMainItmArr.getobjects(lmMainId, lmScndId, p).toString().contains(gObTags.get(k).toString())) {
                                            ArrayList<ArrayList<String>> lmArr = gMainItmArr.getobjects(lmMainId, lmScndId, p);
                                            for (int m = 0; m < lmArr.get(2).size(); m++) {
                                                gFstLnkObj.makeobject(lmArr.get(0), lmArr.get(1).get(m), lmArr.get(2).get(m),
                                                        lmArr.get(3).get(m), lmArr.get(4).get(m), lmArr.get(5).get(m), lmArr.get(6).get(m),
                                                        lmMainId, lmScndId, p, m);
                                                gFstLnkItm.add(gFstLnkObj);
                                                gFstLnkObj = new fastlinkobject();
                                                p = -1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        //Заполнение доступных ссылок на предметы без учета тегов
                        for (int i = 0; i < lmFrstType.size(); i++) {
                            lmMainId = lmFrstType.get(i);
                            for (int o = 0; o < lmScndType.get(i).size(); o++) {
                                lmScndId = lmScndType.get(i).get(o);
                                if (gMainItmArr.getalltagsthrd(lmMainId, lmScndId).size() > 1) {
                                    gObChsdTags.addAll(gMainItmArr.getalltagsthrd(lmMainId, lmScndId));
                                }
                                for (int l = 0; l < gMainItmArr.getarraysizethrd(lmMainId, lmScndId); l++) {
                                    ArrayList<ArrayList<String>> lmArr = gMainItmArr.getobjects(lmMainId, lmScndId, l);
                                    for (int m = 0; m < lmArr.get(2).size(); m++) {
                                        gFstLnkObj.makeobject(lmArr.get(0), lmArr.get(1).get(m), lmArr.get(2).get(m),
                                                lmArr.get(3).get(m), lmArr.get(4).get(m), lmArr.get(5).get(m), lmArr.get(6).get(m),
                                                lmMainId, lmScndId, l, m);
                                        gFstLnkItm.add(gFstLnkObj);
                                        gFstLnkObj = new fastlinkobject();
                                    }
                                }
                            }
                        }
                    }

                    if (gFstLnkItm.size() > 0) {
                        for (int i = 0; i < gFstLnkItm.size(); i++) {
                            wrkwthprstChsItm.getItems().add(gFstLnkItm.get(i).getname());
                        }
                    } else {
                        Wm.AppWindow.errorWindow(gsErrs.get(0).get(1));
                    }

                } catch (Exception v) {
                    System.out.println(v);
                }
            }

            void FindItmToEdtr() {
                for (int i = 0; i < gFstLnkItm.size(); i++) {
                    if (gFstLnkItm.get(i).getname().contains(wrkwthprstChsItm.getValue())) {
                        gItmVlueLnk = i;
                        i = gFstLnkItm.size() + 1;
                    }
                }
                vluespnIdLabel1.setText(gFstLnkItm.get(gItmVlueLnk).getid().split(";")[0]);
                vluespnNameLabel1.setText(gFstLnkItm.get(gItmVlueLnk).getgamename().split(";")[0]);
                vluespnLinkLabel1.setText(gFstLnkItm.get(gItmVlueLnk).getlink().split(";")[0]);
                edtrItemValuesList.getItems().clear();
                gItmEdtrBckp.clear();
                try {
                    File lmfItm = new File(gStngBufr1[2]);
                    FileReader FR = new FileReader(lmfItm);
                    Scanner scanner = new Scanner(FR);
                    String lmScnStrng = "";
                    String lmFstItmLn = "";
                    int lmLnNmbr = 0, lmLnNmbrBuf = 0;
                    lmFstItmLn = gFstLnkItm.get(gItmVlueLnk).getprpsbgn();
                    lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2, lmFstItmLn.length()));
                    for (int i = 0; i < lmLnNmbr; i++) {
                        scanner.nextLine();
                    }
                    lmLnNmbrBuf = lmLnNmbr;
                    lmFstItmLn = gFstLnkItm.get(gItmVlueLnk).getprpsend();
                    lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2, lmFstItmLn.length()));
                    for (int i = lmLnNmbrBuf; i < (lmLnNmbr - 1); i++) {
                        lmScnStrng = scanner.nextLine().trim();
                        edtrItemValuesList.getItems().add(lmScnStrng);
                        gItmEdtrBckp.add(lmScnStrng);
                    }
                    FR.close();
                    scanner.close();
                } catch (IOException v) {
                    System.out.println(v);
                }
            }
        }
        //Конец класса ChsGrpItm

        class AddLnsToItm {

            void AddOrRemoveLines(int listnumber, int gWrkmd){
                ListView<String> lmLstLnk = new ListView<String>();
                int lmLnsLoadNmbr = 0;

                //Создание ссылки на опреденный ListView
                switch (listnumber){
                    case 0:
                        lmLstLnk = edtrItemValuesList;
                        break;
                    case 1:
                        lmLstLnk = edtrGlblLstView;
                        break;
                    case 2:
                        //Тут будет третий редактор который есть в программе, но не задействован
                        break;
                }

                //Тип действия
                switch (gWrkmd){
                    case 0:
                        //AddLines
                        if (lmLstLnk.getSelectionModel().getSelectedIndices().size() == 1){
                            lmLnsLoadNmbr = Integer.valueOf(rdctrpaneChngNmbrOfLns.getText());
                            int indx = (lmLstLnk.getSelectionModel().getSelectedIndex() + 1);
                            if (lmLnsLoadNmbr > 0){
                                for (int i = 0; i < lmLnsLoadNmbr; i++){
                                    lmLstLnk.getItems().add(indx,"");
                                }
                            } else {
                                Wm.AppWindow.errorWindow(gsErrs.get(5).get(1));
                            }
                        } else {
                            Wm.AppWindow.errorWindow(gsErrs.get(5).get(2));
                        }
                        break;
                    case 1:
                        //AddMassiveLines
                        if (lmLstLnk.getSelectionModel().getSelectedIndices().size() == 1){
                            lmLnsLoadNmbr = (lmLstLnk.getSelectionModel().getSelectedIndex() + 1);
                            String[] lmSmpLnArr = arraddpaneArrLnsTxtArea.getText().split("$");
                            if (lmSmpLnArr.length > 0) {
                                for (int i = 0; i < lmSmpLnArr.length; i++) {
                                    lmLstLnk.getItems().add(lmLnsLoadNmbr, lmSmpLnArr[i]);
                                }
                            } else {
                                Wm.AppWindow.errorWindow(gsErrs.get(5).get(3));
                            }
                        } else {
                            Wm.AppWindow.errorWindow(gsErrs.get(5).get(4));
                        }
                        break;
                    case 2:
                        //wrkwthprstRmvLines
                        if (lmLstLnk.getSelectionModel().getSelectedIndices().size() > 0){
                            for (int i = (lmLstLnk.getSelectionModel().getSelectedIndices().size() - 1); i > -1 ; i--) {
                                lmLnsLoadNmbr = lmLstLnk.getSelectionModel().getSelectedIndex();
                                lmLstLnk.getItems().remove(lmLnsLoadNmbr);
                            }
                        } else {
                            Wm.AppWindow.errorWindow(gsErrs.get(5).get(5));
                        }
                        break;
                    case 3:
                        //ResetLines
                        if (lmLstLnk.getSelectionModel().getSelectedIndices().size() > 0){
                            lmLstLnk.getItems().clear();
                            switch (listnumber){
                                case (0):
                                    for (int i = 0; i < gItmEdtrBckp.size(); i ++){
                                        lmLstLnk.getItems().add(gItmEdtrBckp.get(i));
                                    }
                                    break;
                                case (1):
                                    for (int i = 0; i < gGlblEdtrBckp.size(); i ++){
                                        lmLstLnk.getItems().add(gGlblEdtrBckp.get(i));
                                    }
                                    break;
                            }
                        } else {
                            Wm.AppWindow.errorWindow(gsErrs.get(5).get(6));
                        }
                        break;
                }

            }

            public void SaveListChangesInFile(int gWrkmd){
                //listtype это items или globals, а gWrkmd это save to backup и to game
                String lmFileName = "";
                String lmGmFileName = "";
                int lmWrkmd = -1;
                int lmWrkTp = -1;

                if (globalItemValues.isExpanded()){
                    lmWrkTp = 0;
                }else if (globalGlobalsEditor.isExpanded()){
                    lmWrkTp = 1;
                } else if (globalLocationsEditor.isExpanded()){
                    Wm.AppWindow.errorWindow(gsErrs.get(5).get(8).replace("//",""));
                } else {
                    lmWrkmd = 4;
                }

                if (lmWrkTp == 0){
                    if ((edtrItemValuesList.getItems() != null) && (edtrItemValuesList.getItems().size() > 0)) {
                        if (gWrkmd == 0){
                            lmFileName = gStngBufr1[2];
                        } else if (gWrkmd == 1) {
                            lmFileName = gStngBufr1[9];
                        }
                        lmWrkTp = 3;
                        gItmEdtrBckp.clear();
                        for (int i = 0; i < edtrItemValuesList.getItems().size(); i++) {
                            gItmEdtrBckp.add(edtrItemValuesList.getItems().get(i));
                        }
                        lmWrkmd = 1;
                    } else {
                        lmWrkmd = 3;
                    }
                } else if (lmWrkTp == 1){
                    if ((edtrGlblLstView.getItems() != null) && (edtrGlblLstView.getItems().size() > 0)) {
                        if (gWrkmd == 0){
                            lmFileName = gStngBufr1[3];
                        } else if (gWrkmd == 1){
                            lmFileName = gStngBufr1[10];
                        }
                        lmWrkTp = 3;
                        gGlblEdtrBckp.clear();
                        for (int i = 0; i < edtrGlblLstView.getItems().size(); i++) {
                            gGlblEdtrBckp.add(edtrGlblLstView.getItems().get(i));
                        }
                        lmWrkmd = 2;
                    } else {
                        lmWrkmd = 3;
                    }
                }

                if (lmWrkmd == 3) {
                    Wm.AppWindow.errorWindow(gsErrs.get(5).get(6));
                }

                if (lmWrkmd == 4) {
                    Wm.AppWindow.errorWindow(gsErrs.get(5).get(7));
                }

                if (lmWrkTp == 3) {
                    if (lmWrkmd == 1) {
                        lmGmFileName = lmFileName.replace(".json", "1.json");
                        try {
                            File lmfMainFile = new File(lmFileName);
                            File lmfScndFile = new File(lmGmFileName);

                            FileReader FR = new FileReader(lmfMainFile);
                            Scanner scanner = new Scanner(FR);
                            FileWriter FW = new FileWriter(lmfScndFile);
                            BufferedWriter BW = new BufferedWriter(FW);

                            ArrayList<String> lmTmpBfr = new ArrayList<String>();

                            String lmFstItmLn = "";
                            int lmLnNmbr = 0;
                            int lmChk = 0, lmLnWrtd = 0;
                            String lmScnLn = "";

                            lmFstItmLn = gFstLnkItm.get(gItmVlueLnk).getprpsbgn();
                            lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2));

                            do {
                                if ((lmChk + 30000) < lmLnNmbr) {
                                    lmChk += 30000;
                                } else {
                                    lmChk = lmLnNmbr;
                                }
                                for (int i = lmLnWrtd; i < lmChk; i++) {
                                    BW.write(scanner.nextLine());
                                    BW.newLine();
                                }
                                BW.flush();
                                lmLnWrtd = lmChk;
                            } while (lmLnWrtd < lmLnNmbr);
                            int lmAlrdScn = lmLnNmbr;

                            lmFstItmLn = gFstLnkItm.get(gItmVlueLnk).getprpsend();
                            lmLnNmbr = Integer.valueOf(lmFstItmLn.substring(lmFstItmLn.lastIndexOf("; ") + 2));

                            lmChk = 0;
                            int itmarrsize = edtrItemValuesList.getItems().size();
                            for (int i = 0; i < itmarrsize; i++) {
                                BW.write(edtrItemValuesList.getItems().get(i));
                                BW.newLine();
                                ++lmChk;
                            }
                            BW.flush();
                            for (int i = lmAlrdScn; i < (lmLnNmbr - 1); i++) {
                                ++lmAlrdScn;
                                scanner.nextLine();
                            }

                            lmScnLn = scanner.nextLine();
                            lmTmpBfr.add(lmScnLn);
                            lmScnLn = scanner.nextLine();

                            if (lmScnLn.contains("\"_proto\":")) {
                                BW.write("},");
                                BW.newLine();
                                BW.write(lmScnLn);
                                BW.newLine();
                                BW.write("},");
                                BW.newLine();
                                lmScnLn = scanner.nextLine();
                            } else {

                                lmTmpBfr.add(lmScnLn);

                                lmChk = 1;
                                int lmWMd = 0, lmSAr = 0;

                                do {
                                    lmScnLn = scanner.nextLine();
                                    lmTmpBfr.add(lmScnLn);

                                    lmChk++;
                                    if (lmScnLn.contains("\"_id\":")) {

                                        if ((lmChk > 2) && (lmTmpBfr.get((lmChk - 3)).contains("\"_proto\":"))){
                                            lmAlrdScn = lmChk - 3;
                                            lmWMd = 2;
                                        } else {
                                            lmAlrdScn = lmChk - 2;
                                            lmWMd = 1;
                                        }

                                        if (lmTmpBfr.size() > 3){
                                            lmSAr = 1;
                                        } else {
                                            lmSAr = 0;
                                        }

                                        lmChk = -1;
                                    }
                                } while (lmChk != -1);

                                if (lmSAr == 1){
                                    if ((lmAlrdScn - 2) >= 0) {
                                        for (int i = 0; i <= (lmAlrdScn - 2); i++) {
                                            BW.write(lmTmpBfr.get(i));
                                            BW.newLine();
                                        }
                                    }
                                }

                                if (lmTmpBfr.get(lmAlrdScn).contains("\"_proto\":")) {

                                    BW.write("},");
                                    BW.newLine();
                                    BW.write(lmTmpBfr.get(lmAlrdScn));
                                    BW.newLine();
                                    BW.write("},");
                                    BW.newLine();

                                } else {

                                    BW.write("}");
                                    BW.newLine();
                                    BW.write("},");
                                    BW.newLine();
                                }

                                lmAlrdScn += lmWMd;
                                for (int i = lmAlrdScn; i < lmTmpBfr.size(); i++){
                                    BW.write(lmTmpBfr.get(i));
                                    BW.newLine();
                                }

                            }

                            if (scanner.hasNextLine()) {
                                BW.write(scanner.nextLine());
                            }

                            lmChk = 30000;
                            do {
                                for (int i = 0; i < lmChk; i++) {
                                    if (scanner.hasNextLine()) {
                                        BW.newLine();
                                        BW.write(scanner.nextLine());
                                    }
                                }
                                BW.flush();
                            } while (scanner.hasNextLine());

                            FR.close();
                            scanner.close();
                            BW.close();
                            FW.close();
                            lmfMainFile.delete();
                            lmfScndFile.renameTo(lmfMainFile);
                        } catch (IOException v) {
                            System.out.println(v);
                        }
                    } else if (lmWrkmd == 2) {
                        try {
                            File lmfMainFile = new File(lmFileName);
                            File lmfScndFile = new File(lmFileName + 1);
                            FileReader FR = new FileReader(lmfMainFile);
                            Scanner scanner = new Scanner(FR);
                            FileWriter FW = new FileWriter(lmfScndFile);
                            BufferedWriter BW = new BufferedWriter(FW);
                            ArrayList<String> lmTmpLstBfr = new ArrayList<String>();
                            String lmFstItmLn = "";
                            int indx = edtrChsngGlblGrp.getSelectionModel().getSelectedIndex();
                            int lmArrSize = gGlblsSectnArr.getitemsarray().get(indx).getitemsarraysize();
                            int[] arrlines = new int[2];
                            int itmarrsize = 0;
                            int lmLnNmbr = 0;
                            int lmChk = 0, lmLnWrtd = 0, lmLnScnd = 0;

                            //Перезапись многомерного массива global
                            lmLnNmbr = edtrGlblLstView.getItems().size();
                            for (int i = 0; i < lmArrSize; i++) {
                                ArrayList<String> lmBfrArr = new ArrayList<String>();
                                do {
                                    lmBfrArr.add(edtrGlblLstView.getItems().get(lmChk));
                                    lmLnWrtd++;
                                    lmChk++;
                                }while (lmLnWrtd < gGlblsSectnArr.getitemsarray().get(indx).getitemsarray().get(i).getvaluessize());
                                gGlblsSectnArr.getitemsarray().get(indx).getitemsarray().get(i).putarray(lmBfrArr);
                                lmLnWrtd = 0;
                            }

                            lmLnNmbr = 0;
                            lmChk = 0;

                            //Сохранение массива в файл

                            for (int i = 0; i < lmArrSize; i++) {
                                arrlines = gGlblsSectnArr.getitemsarray().get(indx).getitemsarray().get(i).getarraylines();
                                lmLnNmbr = arrlines[0];
                                do {
                                    lmChk = (lmLnNmbr - 1);
                                    for (int o = lmLnScnd; o < lmChk; o++) {
                                        BW.write(scanner.nextLine());
                                        BW.newLine();
                                        lmLnScnd++;
                                    }
                                    BW.flush();
                                } while (lmChk < lmLnNmbr - 1);

                                if (i != (lmArrSize - 1)){
                                    lmLnNmbr = arrlines[1];
                                } else {
                                    lmLnNmbr = (arrlines[1] - 1);
                                }
                                for (int o = lmLnScnd; o < lmLnNmbr; o++) {
                                    scanner.nextLine();
                                    lmLnScnd++;
                                }

                                lmChk = 0;
                                itmarrsize = gGlblsSectnArr.getitemsarray().get(indx).getitemsarray().get(i).getvaluessize();
                                lmTmpLstBfr = gGlblsSectnArr.getitemsarray().get(indx).getitemsarray().get(i).getarray();
                                for (int o = 0; o < itmarrsize; o++) {
                                    BW.write(lmTmpLstBfr.get(o));
                                    BW.newLine();
                                    ++lmChk;
                                }
                                BW.flush();
                            }

                            lmChk = 30000;
                            do {
                                for (int i = 0; i < lmChk; i++) {
                                    if (scanner.hasNextLine()) {
                                        lmLnScnd++;
                                        BW.write(scanner.nextLine());
                                        BW.newLine();
                                    } else break;
                                }
                                BW.flush();
                            } while (scanner.hasNextLine());

                            FR.close();
                            scanner.close();
                            BW.close();
                            FW.close();
                            lmfMainFile.delete();
                            lmfScndFile.renameTo(lmfMainFile);
                        } catch (IOException v) {
                            System.out.println(v);
                        }
                    }
                }
            }
        }
        //Конец класса AddLnsToItm

        class WrkWthPreset {

            void ChangeMainItemFile(int lmChk, String presetname, int writemode) {
                loadprstLoadBtn.setDisable(true);
                File lmfDir = new File(gStngBufr1[11].replace("*", "FileChangesMain")); //path указывает на директорию
                File lmfRead = new File("");
                File[] lmfListFls = lmfDir.listFiles();
                ArrayList<String> lmLnsShft = new ArrayList<String>();
                String lmTmpDynAdrs = new String("");
                Preset lmTmpPrst = new Preset();
                if (writemode == 0) {
                    lmTmpDynAdrs = presetname;
                    try {
                        try {
                            File lmfPrst = new File(gStngBufr1[11].replace("*", "Presets\\" + gAllPrsts.get(lmChk).getPresetName() + ".obj"));
                            FileInputStream fis = new FileInputStream(lmfPrst);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            lmTmpPrst = (Preset) ois.readObject();
                            fis.close();
                            System.out.println("Загрузилося пресет " + lmTmpPrst.getPresetName());
                        } catch (IOException b) {
                            System.out.println("В загрузке пресетов из папки ошибка: " + b);
                        }
                    }catch (ClassNotFoundException f) {
                        System.out.println("В загрузке пресетов из папки ошибка класс не найден: " + f);
                    }
                } else if (writemode == 1) {
                    lmTmpDynAdrs = presetname;
                    File lmfLoadPrst = new File(gStngBufr1[11].replace("*", "\\FileChanges\\" + gPrstsLoadInGame.get(lmChk) + ".obj"));
                    try {
                        try {
                            FileInputStream fos = new FileInputStream(lmfLoadPrst);
                            ObjectInputStream oos = new ObjectInputStream(fos);
                            lmTmpPrst = (Preset) oos.readObject();
                            oos.close();
                            fos.close();
                        } catch (ClassNotFoundException f) {
                            System.out.println(f);
                        }
                    } catch (IOException v) {
                        System.out.println(v);
                    }
                }
                System.out.println("work");

                //Модуль учитываюший смещение после определенной строки, но пока в программе это не реализовано
        /*
        for (int o = 0; o < lmfListFls.length; o++) {
            if (lmfListFls[o].getName().contains(lmTmpDynAdrs + ".txt")) {
                read = new File(lmfListFls[o].getPath());
                try {
                    String lmScnLn = new String("");
                    FileReader FR = new FileReader(read);
                    BufferedReader BR = new BufferedReader(FR);
                    Scanner scanner = new Scanner(BR);
                    do {
                        lmScnLn = scanner.nextLine();
                        lmLnsShft.add(lmScnLn);
                    } while (scanner.hasNextLine());
                } catch (IOException v) {
                    System.out.println(v);
                }
            }
        }
        */

                try {
                    String lmScnLn = new String("");
                    String lmLnObjBgn = new String(""), lmLnObjEnd = new String("");
                    int lmLnsToRead = 0;
                    int lmLnsRd = 0;
                    int lmObjBgn = 0, lmObjEnd = 0;
                    //Модификатор позволяющий учитывать смещение строк и отсев уже пройденых значений
                    int lmLnShft[] = new int[2];
                    lmLnShft[0] = 0;
                    lmLnShft[1] = 0;
                    //Собственно модуль проверок учитывающий количество замен линий без массива и с массивом
                    //Значения для проверки количества обычных линий и массивов в объекте замены
                    int lmCngWrk = 0;
                    //Количество заменов по типу
                    int lmLnCng = 0, lmLnArrCng = 0;
                    //Отсчет уже сделаных замен
                    int lmLnCngAlrdy = 0, lmLnArrCngAlrdy = 0;
                    //Общее количество линий
                    int lmChkLnsVlue = 0;
                    //Режим записи
                    int lmWrkmd = 0;
                    //Переменная для мат. операций
                    int q = 0;

                    //Массив значений для работы записи grpindx, objlvl и т.д.; Для чего смотреть в модуле анализа перед записью
                    ArrayList<Integer[]> lmArrListIndx = new ArrayList<Integer[]>();
                    ArrayList<String[]> lmArrListStrIndx = new ArrayList<String[]>();


                    // Модуль чтения
                    int lmChk2 = 0, lmChrsNmbr = 0, lmChrsSize = 0;
                    String lmCtgry = "", lmIdLvl = "";
                    int lmStrLngth = 0;
                    int lmMainLvl = 0, lmObjLvl = 0, lmGrpIndx = 0, lmRIdx = 0;

                    /// Список для перезаписи предметами пресетов
                    ArrayList<String> lmArrBfrPrstRwrt = new ArrayList<String>();

                    //Переменный для сохранения и работы с массивом
                    char lmChr1 = ' ', lmChr2 = ' ';
                    Integer[] lmStrNmbr = new Integer[2];

                    lmfRead = new File(gStngBufr1[9]);
                    File lmfWrite = new File(gStngBufr1[9].replace(".json", "1.json"));
                    FileReader FR = new FileReader(lmfRead);
                    BufferedReader BR = new BufferedReader(FR);
                    Scanner scanner = new Scanner(BR);
                    FileWriter FW = new FileWriter(lmfWrite);
                    BufferedWriter BW = new BufferedWriter(FW);
                    //Запись линий
                    //Включение модуля замены строк
                    for (int b = 0; b < lmTmpPrst.getPreset().size() ; b++) {

                        //Просчет количества замен в объекте
                        if (lmTmpPrst.getPreset().get(b).getSizeLineNumber() != null) {
                            lmLnCng = lmTmpPrst.getPreset().get(b).getSizeLineNumber();
                        }else{
                            lmLnCng = 0;
                        }

                        if (lmTmpPrst.getPreset().get(b).getSizeMassiveLineNumber() != null) {
                            lmLnArrCng = lmTmpPrst.getPreset().get(b).getSizeMassiveLineNumber();
                        } else {
                            lmLnArrCng = 0;
                        }

                        lmChkLnsVlue = lmLnCng + lmLnArrCng;

                                /*
                                //Учет смещения
                                if (lmLnShft[1] > -1 & lmLnsShft.size() > 0) {
                                    lmLnShft[0] = 0;
                                    for (int j = lmLnShft[1]; ((lmLnShft[0] == 0) & (j < lmLnsShft.size())); j++) {
                                        lmLnShft[0] = Integer.valueOf(lmLnsShft.get(j).substring(0, (lmLnsShft.get(j).lastIndexOf(';') - 1)));
                                        if ((lmLnShft[0] >
                                                lmTmpPrst.getPreset().get(b).getStringNumber(lmLnCngAlrdy) & (lmLnCngAlrdy <= lmLnCng))) {

                                            if (j > 0) {
                                                lmLnShft[1] = j - 1;
                                                lmLnShft[0] = Integer.valueOf(lmLnsShft.get(j - 1).substring(0, (lmLnsShft.get(j).lastIndexOf(';') - 1)));
                                            } else {
                                                lmLnShft[1] = -1;
                                                lmLnShft[0] = -1;
                                            }
                                            lmLnShft[1] = j;
                                            if (lmLnCngAlrdy <= lmLnCng) lmLnCngAlrdy++;
                                        } else if ((lmLnShft[0] >
                                                lmTmpPrst.getPreset().get(b).getStringNumberArray(lmLnArrCngAlrdy) & (lmLnArrCng <= lmLnArrCngAlrdy))) {

                                            if (j > 0) {
                                                lmLnShft[1] = j - 1;
                                                lmLnShft[0] = Integer.valueOf(lmLnsShft.get(j - 1).substring(0, (lmLnsShft.get(j).lastIndexOf(';') - 1)));
                                            } else {
                                                lmLnShft[1] = -1;
                                                lmLnShft[0] = -1;
                                            }
                                            lmLnShft[1] = j;
                                            if (lmLnArrCngAlrdy <= lmLnArrCng) lmLnArrCngAlrdy++;
                                        } else {
                                            lmLnShft[0] = 0;
                                        }
                                    }
                                    if (lmLnShft[0] == 0) {
                                        lmLnShft[1] = -1;
                                    }
                                }
                                */

                        //Считывания начала объекта
                        lmLnObjBgn = lmTmpPrst.getPreset().get(b).getObjectStringsIndexArray().get(2);
                        lmObjBgn = (Integer.valueOf(lmLnObjBgn.substring(lmLnObjBgn.lastIndexOf("; ") + 2)));

                        lmLnObjEnd = lmTmpPrst.getPreset().get(b).getObjectStringsIndexArray().get(3);
                        lmObjEnd = ((Integer.valueOf(lmLnObjEnd.substring(lmLnObjEnd.lastIndexOf("; ") + 2))) - 1);

                        //Пролистывание до начала объекта
                        if (lmLnsRd < lmObjBgn) {
                            int n = 0;
                            while (lmLnsRd < lmObjBgn) {
                                if ((lmLnsRd + 10000) < lmObjBgn) {
                                    n = 10000;
                                } else {
                                    n = lmObjBgn - lmLnsRd;
                                }

                                for (int p = 0; p < n; p++) {
                                    lmScnLn = scanner.nextLine();
                                    lmLnsRd++;
                                    BW.write(lmScnLn);
                                    BW.newLine();
                                }
                                BW.flush();
                            }

                            //Cчитывания объекта в файле в буферрный список
                            while (lmLnsRd < lmObjEnd) {
                                if ((lmLnsRd + 100) < lmObjEnd) {
                                    n = 100;
                                } else {
                                    n = (lmObjEnd - lmLnsRd);
                                }

                                for (int p = 0; p < n; p++) {
                                    lmScnLn = scanner.nextLine();
                                    lmLnsRd++;
                                    lmArrBfrPrstRwrt.add(lmScnLn);
                                }
                            }
                        }

                        //Включение цикла замены при соблюдении условий
                        if (lmChkLnsVlue > 0) {
                            //Сначала пишутся массивы потом отдельные строки, массивы пишутся с первичным поиском начала (несколько проходов)
                            //Строки пишутся за один проход
                            //Порядок

                            //Анализ строк
                            int objChk = 0;
                            for (int i = 0; i < (lmLnArrCng + lmLnCng); i++) {
                                if ((lmLnArrCng != 0) && i < lmLnArrCng) {
                                    lmLnsToRead = lmTmpPrst.getPreset().get(b).getStringNumberArray(i);
                                } else if (lmLnCng != 0) {
                                    lmLnsToRead = lmTmpPrst.getPreset().get(b).getStringNumber((i - lmLnArrCng));
                                }

                                for (int o = objChk; o < lmArrBfrPrstRwrt.size(); o++) {
                                    lmScnLn = lmArrBfrPrstRwrt.get(o);
                                    lmStrLngth = lmScnLn.length() - 1;
                                    if (lmStrLngth > 3) {
                                        lmChrsNmbr = lmStrLngth;
                                        lmChrsSize = (lmStrLngth - 3);
                                    } else {
                                        if (lmStrLngth == 2){
                                            lmChrsNmbr = 1;
                                            lmChrsSize = -1;
                                        } else {
                                            lmChrsNmbr = 0;
                                            lmChrsSize = -1;
                                        }
                                    }

                                    for (int g = lmChrsNmbr; g > lmChrsSize; g--) {
                                        lmChk2 = 0;
                                        if (lmScnLn.charAt(g) == '[') {
                                            lmMainLvl++;
                                            if (lmMainLvl == 1) {
                                                lmCtgry = lmScnLn;
                                            }
                                            lmChk2++;
                                        }
                                        if (lmScnLn.charAt(g) == ']') {
                                            lmMainLvl--;
                                            lmChk2++;
                                        }
                                        if (lmScnLn.charAt(g) == '{') {
                                            lmObjLvl++;
                                            if (lmMainLvl == 1 & lmObjLvl == 1) {
                                                lmGrpIndx++;
                                                lmIdLvl = lmArrBfrPrstRwrt.get(o + 1);
                                            } else if (lmObjLvl == 1) {
                                                lmIdLvl = lmScnLn;
                                            }
                                            lmChk2++;
                                        }
                                        if (lmScnLn.charAt(g) == '}') {
                                            if (lmMainLvl == 1 & lmObjLvl == 1) {
                                                lmGrpIndx = 0;
                                            }
                                            lmObjLvl--;
                                            lmChk2++;
                                        }

                                        if (lmChk2 == 2) {
                                            g = lmChrsNmbr;
                                            lmChk2 = 0;
                                        }
                                    }

                                    if (o == lmLnsToRead) {

                                        Integer[] ind = {lmMainLvl, lmObjLvl, lmGrpIndx, o};
                                        lmArrListIndx.add(ind);
                                        String[] sInd = {lmCtgry, lmIdLvl};
                                        lmArrListStrIndx.add(sInd);
                                        lmChk2 = 0;
                                        if (i < lmLnArrCng) {
                                            objChk = 0;
                                        } else {
                                            objChk = o;
                                        }
                                        o = (lmArrBfrPrstRwrt.size() + 1);
                                    }
                                }
                            }
                            lmChk2 = 0;


                            //Включение записи массива
                            if (lmTmpPrst.getPreset().get(b).getSizeMassiveLineNumber() > 0) {
                                for (int o = 0; o < lmLnArrCng; o++) {
                                    lmMainLvl = lmArrListIndx.get(o)[0];
                                    lmObjLvl = lmArrListIndx.get(o)[1];
                                    lmGrpIndx = lmArrListIndx.get(o)[2];
                                    lmRIdx = lmArrListIndx.get(o)[3];
                                    lmCtgry = lmArrListStrIndx.get(o)[0];
                                    lmIdLvl = lmArrListStrIndx.get(o)[1];

                                    for (int k = 0; k < lmArrBfrPrstRwrt.size(); k++) {

                                        if (k == lmRIdx) {
                                            lmScnLn = lmArrBfrPrstRwrt.get(k).trim();
                                            switch (lmTmpPrst.getPreset().get(b).getMassiveItemLine(o).getCategory()) {
                                                case ("main"):
                                                    if (lmScnLn.substring(0, lmScnLn.indexOf(':')).equals(lmTmpPrst.getPreset().get(b).getMassiveItemLineMain(o).get(0).trim().substring(0, lmScnLn.indexOf(':')))) {
                                                        lmChk2 = 1;
                                                    }
                                                    break;
                                                case ("scnd"):
                                                    if (lmTmpPrst.getPreset().get(b).getMassiveItemLine(o).getScndCategory().equals(lmIdLvl)) {
                                                        if (lmScnLn.substring(0, lmScnLn.indexOf(':')).equals(lmTmpPrst.getPreset().get(b).getMassiveItemLineMain(o).get(0).trim().substring(0, lmScnLn.indexOf(':')))) {
                                                            lmChk2 = 1;
                                                        }
                                                    }
                                                    break;
                                                default:
                                                    if (lmTmpPrst.getPreset().get(b).getItemLineCategory(o).equals(lmCtgry)) {
                                                        if (lmTmpPrst.getPreset().get(b).getMassiveItemLine(o).getScndCategory().equals(lmIdLvl)) {
                                                            if (lmScnLn.substring(0, lmScnLn.indexOf(':')).equals(lmTmpPrst.getPreset().get(b).getMassiveItemLineMain(o).get(0).trim().substring(0, lmScnLn.indexOf(':')))) {
                                                                lmChk2 = 1;
                                                            }
                                                        }
                                                    }
                                                    break;
                                            }
                                            //Работа с массивом

                                            ArrayList<String> lmTmpStrArr = new ArrayList<String>();

                                            for (int p = 0; p < lmTmpPrst.getPreset().get(b).getMassiveItemLine(o).getmainItemLines().size(); p++) {
                                                q = (k + p);
                                                lmTmpStrArr.add(lmArrBfrPrstRwrt.get(q));
                                                lmArrBfrPrstRwrt.set(q, lmTmpPrst.getPreset().get(b).getMassiveItemLine(o).getmainItemLines().get(p));
                                            }

                                            if (writemode == 0) {
                                                lmTmpPrst.getPreset().get(b).getMassiveItemLine(o).setBackupArrayItemLine(lmTmpStrArr);
                                            }

                                            k = (lmArrBfrPrstRwrt.size() + 1);
                                        }
                                    }
                                }
                            }

                            //Включение записи строк
                            if (lmTmpPrst.getPreset().get(b).getSizeLineNumber() > 0) {
                                for (int o = 0; o < lmLnCng; o++) {

                                    lmCngWrk = lmLnArrCng + o;
                                    lmMainLvl = lmArrListIndx.get(lmCngWrk)[0];
                                    lmObjLvl = lmArrListIndx.get(lmCngWrk)[1];
                                    lmGrpIndx = lmArrListIndx.get(lmCngWrk)[2];
                                    lmRIdx = lmArrListIndx.get(lmCngWrk)[3];
                                    lmCtgry = lmArrListStrIndx.get(lmCngWrk)[0];
                                    lmIdLvl = lmArrListStrIndx.get(lmCngWrk)[1];

                                    for (int k = lmChk2; k < lmArrBfrPrstRwrt.size(); k++) {
                                        if (k == lmRIdx) {

                                            lmChk2 = k;
                                            lmScnLn = lmArrBfrPrstRwrt.get(k).trim();

                                            switch (lmTmpPrst.getPreset().get(b).getItemLine(o).getCategory()) {
                                                case ("main"):
                                                    if (lmScnLn.substring(0, lmScnLn.indexOf(':')).equals(lmTmpPrst.getPreset().get(b).getItemLineMain(o).substring(0, lmScnLn.indexOf(':')))) {
                                                        lmArrBfrPrstRwrt.set(k, lmTmpPrst.getPreset().get(b).getItemLineMain(o));
                                                    }
                                                    break;
                                                case ("scnd"):
                                                    if (lmTmpPrst.getPreset().get(b).getItemLine(o).getScndCategory().equals(lmIdLvl.trim())) {
                                                        System.out.println(lmScnLn.substring(0, lmScnLn.indexOf(':')));
                                                        System.out.println(lmTmpPrst.getPreset().get(b).getItemLineMain(o).substring(0, lmScnLn.indexOf(':')));
                                                        if (lmScnLn.substring(0, lmScnLn.indexOf(':')).equals(lmTmpPrst.getPreset().get(b).getItemLineMain(o).substring(0, lmScnLn.indexOf(':')))) {
                                                            lmArrBfrPrstRwrt.set(k, lmTmpPrst.getPreset().get(b).getItemLineMain(o));
                                                        }
                                                    }
                                                    break;
                                                default:
                                                    if (lmTmpPrst.getPreset().get(b).getItemLineCategory(o).equals(lmCtgry.trim())) {
                                                        if (lmTmpPrst.getPreset().get(b).getItemLine(o).getScndCategory().equals(lmIdLvl.trim())) {
                                                            if (lmScnLn.substring(0, lmScnLn.indexOf(':')).equals(lmTmpPrst.getPreset().get(b).getItemLineMain(o).substring(0, lmScnLn.indexOf(':')))) {
                                                                lmArrBfrPrstRwrt.set(k, lmTmpPrst.getPreset().get(b).getItemLineMain(o));
                                                            }
                                                        }
                                                    }
                                                    break;
                                            }
                                            if (writemode == 0) {
                                                lmTmpPrst.getPreset().get(b).getItemLine(o).setBackupItemLine(lmScnLn);
                                            }
                                            k = (lmArrBfrPrstRwrt.size() + 1);
                                        }
                                    }
                                }
                            }

                            //Запись предмета
                            for (int k = 0; k < lmArrBfrPrstRwrt.size(); k++) {
                                BW.write(lmArrBfrPrstRwrt.get(k));
                                BW.newLine();
                            }
                            BW.flush();

                            //Конец модуля замены строк
                            //TODO Учесть возмжность востановление числа после выгрузки модуля
                        }
                        lmScnLn = new String("");
                        lmLnsToRead = 0;
                        lmObjBgn = 0;
                        lmObjEnd = 0;
                        lmLnShft[0] = 0;
                        lmLnShft[1] = 0;
                        lmLnCng = 0;
                        lmLnArrCng = 0;
                        lmLnCngAlrdy = 0;
                        lmLnArrCngAlrdy = 0;
                        lmChkLnsVlue = 0;
                        lmWrkmd = 0;
                        lmChk2 = 0;
                        lmChrsNmbr = 0;
                        lmChrsSize = 0;
                        lmCtgry = "";
                        lmIdLvl = "";
                        lmStrLngth = 0;
                        lmMainLvl = 0;
                        lmObjLvl = 0;
                        lmGrpIndx = 0;
                        lmChr1 = ' ';
                        lmChr2 = ' ';
                        lmStrNmbr[0] = 0;
                        lmStrNmbr[1] = 0;
                        lmArrBfrPrstRwrt.clear();
                        lmArrListIndx.clear();
                        lmArrListStrIndx.clear();
                    }
                    do {
                        BW.write(scanner.nextLine());
                        BW.newLine();
                    } while (scanner.hasNextLine());


                    if (writemode == 0) {
                        try {
                            File n = new File(gStngBufr1[11].replace("*", "\\FileChanges\\" + lmTmpPrst.getPresetName() + ".obj"));
                            gPrstLoadIndx++;
                            lmTmpPrst.setLoadIndex(gPrstLoadIndx);
                            if (!n.exists()) {
                                if (!n.canWrite()) {
                                    n.createNewFile();
                                }
                                FileOutputStream fos = new FileOutputStream(n);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(lmTmpPrst);
                                oos.flush();
                                oos.close();
                                fos.close();
                            }
                        } catch (IOException v) {
                            System.out.println(v);
                        }
                        gPrstsLoadInGame.add(lmTmpPrst.getPresetName());
                    } else {
                        if (lmTmpPrst.getLoadIndex() == gPrstLoadIndx){
                            gPrstLoadIndx--;
                        }
                        File lmfDlt = new File(gStngBufr1[11].replace("*", "\\FileChanges\\" + lmTmpPrst.getPresetName() + ".obj"));
                        lmfDlt.delete();
                    }

                    FR.close();
                    BR.close();
                    BW.close();
                    FW.close();
                    lmfRead.delete();
                    lmfWrite.renameTo(lmfRead);
                } catch (IOException v) {
                    System.out.println(v);
                }
                lmChk = 0;
                loadprstLoadBtn.setDisable(false);
            }
        }
        //Конец класса WrkWthPreset
    }
    ///Конец UniversalModules
}
////Конец класса Controller


class ItemsValuesListCell extends ListCell<String> {
    private final TextField lmTxtFld = new TextField();
    private final int[] lmNmPos = new int[2];

    public ItemsValuesListCell() {
        lmTxtFld.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
        lmTxtFld.setOnAction(e -> {
            String lmScnLn = getItem().substring(0,lmNmPos[0]);
            lmScnLn += lmTxtFld.getText();
            lmScnLn += getItem().substring(lmNmPos[1]);
            commitEdit(lmScnLn);
            cancelEdit();
        });
        setGraphic(lmTxtFld);
    }

    private void starteditvalue(String lmScnLn){
        if (lmScnLn.contains("\"")) {
            for (int i = 0; lmNmPos[0] == 0 ; i++) {
                if (lmScnLn.charAt(i) == ':') {
                    if (lmScnLn.charAt(i + 2) == '"') {
                        lmNmPos[0] = i + 3;
                    } else {
                        lmNmPos[0] = i + 2;
                    }
                }
            }
            if (lmScnLn.charAt(lmScnLn.length() - 1) == ',') {
                if (lmScnLn.charAt(lmScnLn.length() - 2) == '\"') {
                    lmNmPos[1] = lmScnLn.length() - 2;
                } else {
                    lmNmPos[1] = lmScnLn.length() - 1;
                }
            } else {
                if (lmScnLn.charAt(lmScnLn.length() - 1) == '\"') {
                    lmNmPos[1] = lmScnLn.length() - 1;
                } else {
                    lmNmPos[1] = lmScnLn.length();
                }
            }
        }
    }

    @Override
    protected void updateItem(String name, boolean empty) {
        super.updateItem(name, empty);
        if (isEditing()) {
            lmTxtFld.setText(name);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            setContentDisplay(ContentDisplay.TEXT_ONLY);
            if (empty) {
                setText(null);
            } else {
                setText(name);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        String lmScnLn = getItem();
        starteditvalue(lmScnLn);
        lmTxtFld.setText(lmScnLn.substring(lmNmPos[0], lmNmPos[1]));
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        lmTxtFld.requestFocus();
        lmTxtFld.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        lmNmPos[0] = 0;
        lmNmPos[1] = 0;
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
}
