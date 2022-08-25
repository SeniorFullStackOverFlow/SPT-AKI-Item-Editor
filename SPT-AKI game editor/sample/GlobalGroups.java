package sample;

import java.util.ArrayList;

class GlobalGroups {

}

 class GlobalsSectionArray{
    private final ArrayList<itemgroup> grouparray = new ArrayList<itemgroup>();

    private String arraytype;

    public String getarraytype(){
        return arraytype;
    }

    public ArrayList<itemgroup> getitemsarray(){
        return grouparray;
    }

    public int getgMainItmArrsize(){
        return grouparray.size();
    }

    public int[] itemsarraylines(int firstindexarray, int secondindexarray){
        return grouparray.get(firstindexarray).getitemsarray().get(secondindexarray).getarraylines();
    }


    public void putarray(itemgroup array){
        grouparray.add(array);
    }

    public void setname(String Arrayname){
        arraytype = Arrayname;
    }

    public void setgroupname(String Arrayname){
        grouparray.get(0).setname(Arrayname);
    }
 }

 class itemgroup{
    private final ArrayList<itemvaluesarray> itemsarray = new ArrayList<itemvaluesarray>();

    private String globalname;

    public String getglobalname(){
        return globalname;
    }

    public ArrayList<itemvaluesarray> getitemsarray(){
        return itemsarray;
    }

    public int getitemsarraysize(){
        return itemsarray.size();
    }

    public void putarray(itemvaluesarray array){
        itemsarray.add(array);
    }

    public void setname(String Arrayname){
        globalname = Arrayname;
    }

 }

 class itemvaluesarray{
    private ArrayList<String> lmTmpItmValues = new ArrayList<String>();

    private String name;

    private final int[] arrline = new int[2];


    public String getname(){
        return name;
    }

    public ArrayList<String> getarray(){
        return lmTmpItmValues;
    }

    public int getvaluessize(){
        return lmTmpItmValues.size();
    }

    public int[] getarraylines(){
        return arrline;
    }

    public void putarraylines(int beginline, int endline){
        arrline[0] = beginline;
        arrline[1] = endline;
    }

    public void putarray(ArrayList<String> array){
        lmTmpItmValues = array;
    }

    public void putsting(String value){
        lmTmpItmValues.add(value);
    }

    public void setname(String Arrayname){
        name = Arrayname;
    }

 }

