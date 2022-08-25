package sample;

import jdk.jfr.DataAmount;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//Все доступные для спавна зоны пишутся в "OpenZones":

public class LocationChgnsStrctr {
    private ArrayList<String> LctnsAdrs = new ArrayList<String>(Arrays.asList("bigmap","factory4_day",
            "factory4_night","interchange","laboratory", "lighthouse","rezervbase","shoreline",
            "woods"));

    private ArrayList<String> LctnsNm = new ArrayList<String>(Arrays.asList("Customs","factory_day",
            "factory_night","interchange","laboratory", "lighthouse","rezervbase","shoreline",
            "woods"));

    ////
    //Начало считывания групп данных в локациях
    private ArrayList<String> LctnsGrpsBgn = new ArrayList<String>(Arrays.asList("AirdropPara",
            "waves", "\"exits","BossLo","\"BotMa", "AveragePlayTime","MaxBotP","GlobalLoot","escape_","exit_a"));
    //Окончание групп
    private ArrayList<String> LctnsGrpsEnd = new ArrayList<String>(Arrays.asList("],", "],","],","],",
            "*Bot","*Aver","0","0","0","0"));

    //Наименования групп настроек
    private ArrayList<String> LctnsGrpsNm = new ArrayList<String>(Arrays.asList("Airdrop",
            "Waves", "Exits work chance","Boss spawn chance&locations", "Bot main settins",
            "Play time on location","Bot per zone","Loot coeficient","Escape time on location",
            "Exit acess time"));

    //Что нужно пропустить в файле
    private String vzh = "\"SpawnPointParams\": [";


    public ArrayList<Location> getLctnsData(String pathToGmLctns) {
        ArrayList<Location> lctnsArr = new ArrayList<Location>();

        try {
        //10 stngbfr

        //Переменные для работы
            ArrayList<String> prmtrsArrRtr;

            String str = "", strbfr = "";
            char[] chrs = new char[] {'A','w','e','B','M','G','S'};

            int chk = -1;
            int n;


            //Модуль считывания
            for (int i = 0; i < LctnsAdrs.size(); i++) {
                //Для оптимизации сравнения
                int[][] indxLctn = new int[][]{{0,5},{1},{2,8,9},{3,4},{6},{7}};

                Location Lctn = new Location();
                Lctn.setLocationName(LctnsNm.get(i));

                File Lctnbase = new File(pathToGmLctns.replace("globals.json", ("\\locations\\" + LctnsAdrs.get(i)
                        + "\\base.json")));

                FileReader FR = new FileReader(Lctnbase);
                Scanner scanner = new Scanner(FR);

                do {
                    str = scanner.nextLine();
                    strbfr = str.trim();
                    //Первичная проверка на соответсвие
                    for (int p = 0; p < chrs.length; p++) {
                        if ((strbfr.length() > 5) && strbfr.charAt(1) == chrs[p]) {
                            if (p != 6) {
                                chk = p;
                                p = 10;
                            } else {
                                if (str.contains(vzh) && !str.contains("]")) {
                                    do {
                                        scanner.nextLine();
                                    } while (!str.contains("\"maxIte") ^ !str.contains("\"Unix"));
                                }
                            }
                        }
                    }

                    if (chk > -1) {
                        for (int p = 0; p < indxLctn[chk].length; p++) {
                            if (indxLctn[chk][p] != -1) {
                                if (str.contains(LctnsGrpsBgn.get(indxLctn[chk][p]))) {
                                    DataType LctnGrp = new DataType();
                                    LctnGrp.setDataName(LctnsGrpsNm.get(indxLctn[chk][p]));

                                    prmtrsArrRtr = new ArrayList<String>();
                                    prmtrsArrRtr.add(str);

                                    if (LctnsGrpsEnd.get(indxLctn[chk][p]).charAt(0) == ']') {
                                        n = 1;
                                    } else if ((LctnsGrpsEnd.get(indxLctn[chk][p]).charAt(0) == '*')) {
                                        n = 2;
                                    } else {
                                        n = 3;
                                    }

                                    if (n != 3) {
                                        switch (n) {
                                            case (1):
                                                int lvl = 0;
                                                if (!str.contains("]")) {
                                                    do {
                                                        str = scanner.nextLine();
                                                        prmtrsArrRtr.add(str);
                                                        if (str.contains("[")) {
                                                            lvl++;
                                                        } else if (str.contains("]")) {
                                                            lvl--;
                                                        }
                                                    } while (lvl != -1);
                                                }
                                                break;
                                            case (2):
                                                int b = 0;
                                                String nm = LctnsGrpsEnd.get(indxLctn[chk][p]).substring(1);
                                                do {
                                                    str = scanner.nextLine();

                                                    if (str.contains(nm)) {
                                                        prmtrsArrRtr.add(str);
                                                    } else {
                                                        b = 1;
                                                    }
                                                } while (b != 1);
                                                break;
                                        }
                                    }
                                    //Добавление группы настроек локации в массив групп локации
                                    LctnGrp.addDataStrngsArr(prmtrsArrRtr);
                                    Lctn.addDataTypeArr(LctnGrp);

                                    indxLctn[chk][p] = -1;
                                }
                            }
                        }
                    }
                    chk = -1;
                } while (scanner.hasNextLine());
                lctnsArr.add(Lctn);
            }

        } catch (IOException v) {
            System.out.println("По итогу создания списка параметров локации ошибка: " + v);
        }
        return lctnsArr;
    }

    public void wrtLctnDataToGm (ArrayList<String> dataArr, String locationNm, String lctnGrpNm, String stngBfr10) {
        try {
            ArrayList<String> lctnsDataArr = dataArr;

            String strBfr = new String();
            String grpNm = new String();

            int chk = 0;
            char[] chr = new char[1];

            int wrtIndx = 0;


            File Lctnbase = new File("");
            File Lctnbasecpy = new File("");

            for (int i = 0; i < LctnsNm.size(); i++) {
                if (locationNm.contains(LctnsNm.get(i))) {
                    Lctnbase = new File(stngBfr10.replace("globals.json", ("\\locations\\" + LctnsAdrs.get(i)
                            + "\\base.json")));
                    Lctnbasecpy = new File(stngBfr10.replace("globals.json", ("\\locations\\" + LctnsAdrs.get(i)
                            + "\\base1.json")));
                    i = 20;
                }
            }

            for (int i = 0; i < LctnsGrpsNm.size(); i++) {
                if (lctnGrpNm.contains(LctnsGrpsNm.get(i))) {
                    grpNm = LctnsGrpsBgn.get(i);
                    chr[0] = LctnsGrpsBgn.get(i).replace("\"","").charAt(0);
                    i = 20;
                }
            }

            FileReader FR = new FileReader(Lctnbase);
            Scanner scanner = new Scanner(FR);

            FileWriter FW = new FileWriter(Lctnbasecpy);
            BufferedWriter BW = new BufferedWriter(FW);


            do {
                wrtIndx++;
                strBfr = scanner.nextLine().trim();
                if ((strBfr.length() > 1) && (strBfr.charAt(1) == chr[0])){
                    if (strBfr.contains(grpNm)){
                        chk = 1;
                    }
                }

                if (chk != 1) {
                    BW.write(strBfr);
                    BW.newLine();
                }
            }while (chk == 0);
            BW.flush();
            chk = 0;

            for (int i = 0; i < lctnsDataArr.size(); i++) {
                BW.write(lctnsDataArr.get(i));
                BW.newLine();
            }

            if (lctnsDataArr.size() > 1) {
                for (int i = wrtIndx; i < (wrtIndx + lctnsDataArr.size() - 1); i++) {
                    scanner.nextLine();
                }
            }

            BW.write(scanner.nextLine());
            BW.flush();

            do {
                for (int i = 0; i < 10000; i++){
                    if (scanner.hasNextLine()) {
                        BW.newLine();
                        BW.write(scanner.nextLine());
                    }
                }
                BW.flush();
            } while (scanner.hasNextLine());


            FW.flush();

            BW.close();
            FW.close();
            scanner.close();

            Lctnbase.delete();
            Lctnbasecpy.renameTo(Lctnbase);

        } catch (IOException v) {
            System.out.println("По итогу создания списка параметров локации ошибка: " + v);
        }
    }
}

class AllLocations{
    private ArrayList<Location> Lctns = new ArrayList<Location>();

    //Экспорт
    public int getLcntsSize (){
        return Lctns.size();
    }

    public Location getLocation (int indx){
        return Lctns.get(indx);
    }

    //Импорт
    public void addLocation (Location location){
        Lctns.add(location);
    }

    public void addLocations (ArrayList<Location> location){
        Lctns.addAll(location);
    }
}

class Location {
    private String LocNm = new String();
    private ArrayList<DataType> GrpsInLocation = new ArrayList<DataType>();

    //Экспорт
    public String getLocNm (){
        return LocNm;
    }

    public String getGrpNm (int indx){
        return GrpsInLocation.get(indx).getGrpNm();
    }

    public Integer getGrpSz (){
        return GrpsInLocation.size();
    }

    public ArrayList<String> getArrInGrp (int indx){
        return GrpsInLocation.get(indx).getArr();
    }

    public Integer getArrSizeInGrp (int indx){
        return GrpsInLocation.get(indx).getArrSz();
    }

    //Импорт
    public void setLocationName(String name){
        LocNm = name;
    }

    public void addDataTypeArr (DataType dataType){
        GrpsInLocation.add(dataType);
    }

}

class DataType {
    private String DataTpNm = new String();
    private ArrayList<String> StrngsDt = new ArrayList<String>();

    //Экспорт
    public String getGrpNm (){
        return DataTpNm;
    }

    public ArrayList<String> getArr (){
        return StrngsDt;
    }

    public Integer getArrSz (){
        return StrngsDt.size();
    }

    //Импорт
    public void setDataName (String name){
        DataTpNm = name;
    }

    public void addDataStrngsArr (ArrayList<String> array){
        StrngsDt.addAll(array);
    }
}
