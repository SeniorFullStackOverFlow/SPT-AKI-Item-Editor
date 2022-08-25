package sample;

import java.io.*;
import java.util.ArrayList;

class PresetFile {

    public ArrayList<Preset> getDesPresets() {
        ArrayList<Preset> presetsArrLst = new ArrayList<Preset>();
        try {
            File lmfDir = new File(System.getProperty("user.dir") + "\\Save\\Presets"); //path указывает на директорию
            File[] lmfListFls = lmfDir.listFiles();

            for (int i = 0; i < lmfListFls.length; i++) {
                if (lmfListFls[i].getName().contains(".obj")) {
                    try {
                        File lmfPrstLoad = new File(lmfListFls[i].getPath());
                        FileInputStream fis = new FileInputStream(lmfPrstLoad);
                        ObjectInputStream ois = new ObjectInputStream(fis);

                        Preset preset = new Preset();

                        preset = (Preset) ois.readObject();
                        presetsArrLst.add(preset);

                        fis.close();
                    } catch (IOException b) {
                        System.out.println("В десериализации пресета ошибка: " + b);
                    }
                }
            }
                /*Потом сделать считывание индекса загрузки пресета
                if (pr.getLoadIndex() > gPrstLoadIndx){
                    gPrstLoadIndx = pr.getLoadIndex();
                }
                */
        } catch (Exception v) {
            System.out.println("По итогу десериализации ошибка: " + v);
        }
        return presetsArrLst;
    }
}

class Preset implements Serializable {
    private ArrayList<PresetObject> PrstObjcts = new ArrayList<PresetObject>();
    private String PresetName = "";
    private String PresetDescription = "";
    private String TypePreset = "";
    private Integer LoadIndex = -1;

    public void MakePreset(ArrayList<PresetObject> PresetArray, String Name, String Description) {
        PrstObjcts = PresetArray;
        PresetName = Name;
        PresetDescription = Description;
    }

    public String getPresetName() {
        return PresetName;
    }

    public String getPresetDescription() {
        return PresetDescription;
    }

    public String getTypePreset() {
        return TypePreset;
    }

    public void setTypePreset(String presettype) {
        TypePreset = presettype;
    }

    public void setLoadIndex(int loadindex) {
        LoadIndex = loadindex;
    }

    public Integer getLoadIndex() {
        return LoadIndex;
    }

    public ArrayList<PresetObject> getPreset() {
        return PrstObjcts;
    }

}

 class PresetObject implements Serializable {
    private String ItemType = "";
    private fastlinkobject FastLinkObject;
    private final ArrayList<ItemLine> ItemLines = new ArrayList<ItemLine>();
    private final ArrayList<ArrayItemLine> ItemLinesArray = new ArrayList<ArrayItemLine>();
    private final ArrayList<Integer> StringNumber = new ArrayList<Integer>();
    private final ArrayList<Integer[]> StringNumberInArray = new ArrayList<Integer[]>();

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    public String getItemType() {
        return ItemType;
    }

    public void addFastLinkObject(fastlinkobject object) {
        FastLinkObject = object;
    }

    public void addItemLine(ItemLine ItemLine, Integer stringNumber, int position) {
        ItemLines.add(position, ItemLine);
        StringNumber.add(position, stringNumber);
    }

    public void addMassiveItemLine(ArrayItemLine ArrayItemLines, Integer[] NumberMassive, int position) {
        ItemLinesArray.add(position, ArrayItemLines);
        StringNumberInArray.add(position, NumberMassive);
    }

    public int getObjBgn(){
        String lmLnObjBgn = FastLinkObject.getprpsbgn();
        int lmObjBgn = (Integer.valueOf(lmLnObjBgn.substring(lmLnObjBgn.lastIndexOf("; ") + 2)));
        return lmObjBgn;
    }

     public int getObjEnd(){
         String lmLnObjEnd = FastLinkObject.getprpsend();
         int lmObjEnd = ((Integer.valueOf(lmLnObjEnd.substring(lmLnObjEnd.lastIndexOf("; ") + 2))) - 1);
         return lmObjEnd;
     }

    public Integer getSizeMassiveLineNumber() {
        return StringNumberInArray.size();
    }

    public String getMassiveItemLineCategory(int index) {
        return ItemLinesArray.get(index).getCategory();
    }

    public String getMassiveItemLineSecondCategory(int index) {
        return ItemLinesArray.get(index).getScndCategory();
    }

    public ArrayList<String> getMassiveItemLineMain(int index) {
        return ItemLinesArray.get(index).getmainItemLines();
    }

    public ArrayItemLine getMassiveItemLine(int index) {
        return ItemLinesArray.get(index);
    }

    public ItemLine getItemLine(int index) {
        return ItemLines.get(index);
    }

    public int getStringNumberArray(int index) {
        return StringNumberInArray.get(index)[0];
    }

    public Integer getSizeLineNumber() {
        return StringNumber.size();
    }

    public String getItemLineCategory(int index) {
        return ItemLines.get(index).getCategory();
    }

    public String getItemLineMain(int index) {
        return ItemLines.get(index).getmainItemLine();
    }

    public String[] getItemLineInObject(int index) {
        return ItemLines.get(index).getobjectItemLine();
    }

    public int getStringNumber(int index) {
        return StringNumber.get(index);
    }

    public int[] getObjectIndexArray() {
        return FastLinkObject.getarraysindexs();
    }

    public ArrayList<String> getObjectStringsIndexArray() {
        return FastLinkObject.getall();
    }

    public void removeItemline(int position) {
        ItemLines.remove(position);
        StringNumber.remove(position);
    }

    public void removeMassiveItemline(int position) {
        ItemLinesArray.remove(position);
        StringNumberInArray.remove(position);
    }

 }

 class ItemLine implements Serializable {
     //отношение к чему
     private String category = "";
     //дополнительное отношение к чему то, либо подвид объекта по типу 3 карман либо ручка
     private String scndcategory = "";
     //что перед
     // private String chkline = "";
     //сама строка
     private String presetline = "";


     public String getCategory() {
         return category;
     }

     public String getScndCategory() {
         return scndcategory;
     }

     public String getmainItemLine() {
         return presetline;
     }

     public String[] getobjectItemLine() {
         String[] n = new String[2];
         n[1] = scndcategory;
         n[2] = presetline;
         return n;
     }

     public void setBackupItemLine(String line) {
         presetline = line;
     }

     public void makemainItemLine(String frstcategory, String line) {
         category = frstcategory;
         presetline = line;
     }

     public void makeobjectItemLine(String frstcategory, String secndcategory, String line) {
         category = frstcategory;
         scndcategory = secndcategory;
         presetline = line;
     }

 }

 class ArrayItemLine implements Serializable {
    //отношение к чему
    private String category = "";
    //дополнительное отношение к чему то, либо подвид объекта по типу 3 карман либо ручка
    private String scndcategory = "";
    //сами строки
    private final ArrayList<String> ArrayLines = new ArrayList<String>();

    public String getCategory() {
        return category;
    }

    public String getScndCategory() {
        return scndcategory;
    }

    public ArrayList<String> getmainItemLines() {
        return ArrayLines;
    }

    public void setBackupArrayItemLine(ArrayList<String> lines) {
        ArrayLines.clear();
        for (int i = 0; i < lines.size(); i++){
            ArrayLines.add(lines.get(i));
        }
    }

    public void makeArrayItemLine(String frstcategory, ArrayList<String> lines) {
        category = frstcategory;
        ArrayLines.clear();
        for (int i = 0; i < lines.size(); i++){
            ArrayLines.add(lines.get(i));
        }
    }

    public void makeObjectArrayItemLine(String frstcategory, String secndcategory, ArrayList<String> lines) {
        category = frstcategory;
        ArrayLines.clear();
        scndcategory = secndcategory;
        for (int i = 0; i < lines.size(); i++){
            ArrayLines.add(lines.get(i));
        }
    }
 }
