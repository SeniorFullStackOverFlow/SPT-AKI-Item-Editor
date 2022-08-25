package sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

class ReadObjectStructure {

}

 class fastlinkobject implements Serializable {
    private ArrayList<String> tag = new ArrayList<String>();
    private String itemname = "";
    private String id = "";
    private String gameitemname = "";
    private String itempropsbegin = "";
    private String itempropsend = "";
    private String linkforotherobjects = "";
    private int frstindxarr = 0;
    private int scndtindxarr = 0;
    private int thrdtindxarr = 0;
    private int frthtindxarr = 0;

    public ArrayList<String> gettagsarray(){return tag;}
    public String getname(){return itemname;}
    public String getid(){return id;}
    public String getgamename(){return gameitemname;}
    public String getprpsbgn(){return itempropsbegin;}
    public String getprpsend(){return itempropsend;}
    public String getlink(){return linkforotherobjects;}

    public int[] getarraysindexs(){
        int[] i = new int[4];
        i[0] = frstindxarr;
        i[1] = scndtindxarr;
        i[2] = thrdtindxarr;
        i[3] = frthtindxarr;
        return i;
    }

    public ArrayList<String> getall(){
        ArrayList<String> all = new ArrayList<String>();
        all.add(id);
        all.add(gameitemname);
        all.add(itempropsbegin);
        all.add(itempropsend);
        all.add(linkforotherobjects);
        return all;
    }
    public void makeobject(ArrayList<String> tags, String name, String id1, String gamename, String prpsbegin,
                           String prpsend, String linksforother, int frstindx, int scndtindx, int thrdtindx,
                           int frthindx){
        tag = tags;
        itemname = name;
        id = id1;
        gameitemname = gamename;
        itempropsbegin = prpsbegin;
        itempropsend = prpsend;
        linkforotherobjects = linksforother;
        frstindxarr = frstindx;
        scndtindxarr = scndtindx;
        thrdtindxarr = thrdtindx;
        frthtindxarr = frthindx;
    }
 }

 class Fastlinksitemsarray implements Serializable {
    private static final long serialVersionUID = 4L;
    private String arrayname = new String("");
    private ArrayList<String> tags = new ArrayList<String>();
    private final ArrayList<String> itemsname = new ArrayList<String>();
    private final ArrayList<String> mainid = new ArrayList<String>();
    private final ArrayList<String> gameitemname = new ArrayList<String>();
    private final ArrayList<String> grouptype = new ArrayList<String>();
    private final ArrayList<String> itemtype = new ArrayList<String>();
    private final ArrayList<String> itempropsbegin = new ArrayList<String>();
    private final ArrayList<String> itempropsend = new ArrayList<String>();
    private final ArrayList<String> linkforotherobjects = new ArrayList<String>();

    public void settags (ArrayList tag){
        tags = tag;
    }
    public void setname(String name){
        arrayname = name;
    }
    public String getname(){
        return  arrayname;
    }
    public ArrayList<ArrayList> getnameandtags(){
        ArrayList<ArrayList> b = new ArrayList<ArrayList>();
        b.add(tags);
        b.add(itemsname);
        return b;
    };
    public ArrayList<String> getarray(){
        return itemsname;
    }
    public ArrayList<ArrayList<String>> getobject(){
        ArrayList<ArrayList<String>> obj = new ArrayList<ArrayList<String>>();
        obj.add(tags);
        obj.add(itemsname);
        obj.add(mainid);
        obj.add(gameitemname);
        obj.add(itempropsbegin);
        obj.add(itempropsend);
        obj.add(linkforotherobjects);
        return obj;
    }
    public ArrayList<Integer> getarrayssize(){
        ArrayList <Integer> rtrn = new ArrayList<Integer>();
        rtrn.add(itemsname.size());
        rtrn.add(mainid.size());
        rtrn.add(gameitemname.size());
        rtrn.add(grouptype.size());
        rtrn.add(itemtype.size());
        rtrn.add(itempropsbegin.size());
        rtrn.add(itempropsend.size());
        rtrn.add(linkforotherobjects.size());
        return rtrn;
    }
 }

 class Globalarray implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<Itemswithglobalsecondtypearray> Itemsarray = new ArrayList<Itemswithglobalsecondtypearray>();

    public ArrayList<ArrayList<String>> getobjects(int frstindex, int scndindex, int thrdindex){
        ArrayList<ArrayList<String>>  Fstarray = Itemsarray.get(frstindex).getobject(scndindex).getobject(thrdindex).getobject();
        return Fstarray;
    }
    public String getglblsecondtype(int index){
        return Itemsarray.get(index).gettype();
    }
    public String getitemscndtype(int frstindex,int scndindex){
        return Itemsarray.get(frstindex).getobject(scndindex).gettype();
    }
    public int getarraysize(){return Itemsarray.size();}
    public int getarraysizescnd(int arrayindex){return Itemsarray.get(arrayindex).itemsscndarray();}
    public int getarraysizethrd(int frstindex,int scndindex){return Itemsarray.get(frstindex).getobject(scndindex).getarraysize();}
    public ArrayList<String> getalltagsthrd(int frstindex,int scndindex){return Itemsarray.get(frstindex).getobject(scndindex).getalltags();}
    public String getalltagsthrdstr(int frstindex,int scndindex){return Itemsarray.get(frstindex).getobject(scndindex).getalltagsstr();}
    public ArrayList<ArrayList> getnameandtagsonfst(int firstindex,int secondindex,int thirdinex){
        return Itemsarray.get(firstindex).getobject(secondindex).getobject(thirdinex).getnameandtags();
    }
    public ArrayList<String> getitmes(int Itemsarrayindex, int Objectindex, int Getarrayobjectindex){
        return Itemsarray.get(Itemsarrayindex).getobject(Objectindex).getarrayobject(Getarrayobjectindex);
    }
 }

 class Itemswithglobalsecondtypearray implements Serializable {
    private static final long serialVersionUID = 2L;
    private final ArrayList<itemsarraywithsecondtype> arrayname = new ArrayList<itemsarraywithsecondtype>();
    private final String glbltype = new String("");
    public int itemsscndarray(){
        return arrayname.size();
    }
    public String gettype(){
        return glbltype;
    }
    public itemsarraywithsecondtype getobject(int index){
        return arrayname.get(index);
    }
 }

 class itemsarraywithsecondtype implements Serializable {
    private static final long serialVersionUID = 3L;
    private final ArrayList<Fastlinksitemsarray> objectsname = new ArrayList<Fastlinksitemsarray>();
    private final String scndtype = new String("");
    private final String alltagsinscndtype = new String("");

    public int getarraysize(){
        return objectsname.size();
    }
    public String gettype(){
        return scndtype;
    }
    public Fastlinksitemsarray getobject(int index){
        //Возвращаяет массив данных
        return objectsname.get(index);
    }
    public ArrayList<String> getarrayobject(int index){
        //Возвращаяет массив данных
        return objectsname.get(index).getarray();
    }
    public ArrayList<String> getalltags(){
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(alltagsinscndtype.replaceAll(" ","").split(";")));
        return myList;
    }

     public String getalltagsstr(){
         return alltagsinscndtype;
     }
 }

class readobjectarray {
    public Globalarray readarray() throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\ItemsData.obj");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Globalarray s = (Globalarray) ois.readObject();
        fis.close();
        return s;
    }
    public int readarray(Globalarray Array){
        return Array.getarraysize();
    }

}
