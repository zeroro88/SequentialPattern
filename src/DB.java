import java.io.*;
import java.util.ArrayList;

/**
 * Created by YaHung on 2015/8/27.
 */
public class DB {
    final int daySecond = 86400;

    int SDLEQuantity;
    /**unit is must second*/
    int timeInterval;
    /**0:second, 1:minute, 2:hour, 3:day*/
    int option;

    ArrayList<ArrayList<ArrayList<String>>> instanceLabel = new ArrayList<ArrayList<ArrayList<String>>>();

    DB(){
        File file = new File("db/readme.txt");
        if(file.exists()) {
            try {
                FileReader fr = new FileReader("db/readme.txt");
                BufferedReader br = new BufferedReader(fr);
                String line = br.readLine();
                String[] timeInterval = line.split(":");
                this.timeInterval = Integer.valueOf(timeInterval[1]);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("There is no database in the directory....");
        }
    }

    /**Generate Database
     * @param  timeInterval set time interval
     * @param  option 0:second, 1:minute, 2:hour, 3:day*/
    DB(int timeInterval, int option){
        this.option = option;
        this.timeInterval = timeInterval;
        switch(option){
            case 0:
                SDLEQuantity = daySecond/timeInterval;
                this.timeInterval = timeInterval;
                break;
            case 1:
                SDLEQuantity = (daySecond/60)/timeInterval;
                this.timeInterval = timeInterval*60;
                break;
            case 2:
                SDLEQuantity = ((daySecond/60)/60)/timeInterval;
                this.timeInterval = timeInterval*60*60;
                break;
            case 3:
                SDLEQuantity = 7/timeInterval;
                this.timeInterval = timeInterval*60*60*24;
                break;
            case 4:
                SDLEQuantity = 365/timeInterval;
                this.timeInterval = timeInterval*60*60*24;
                break;
        }

        for(int i=0; i<SDLEQuantity; i++){
            ArrayList<ArrayList<String>> SDLEModel= new ArrayList<ArrayList<String>>();
            instanceLabel.add(SDLEModel);
        }
    }

    private int[] getDate(long unixTimestamp){
        String date = new java.text.SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").format(new java.util.Date(unixTimestamp * 1000));
        String[] dateStr = date.split(":");
        int[] dateInt = new int [dateStr.length];
        for(int i=0; i<dateInt.length; i++){
            dateInt[i] = Integer.valueOf(dateStr[i]);
        }
        return dateInt;
    }

    private int getDaySecond(int[] date){
        int second = date[5];
        int minute = date[4];
        int hour = date[3];
        return hour*60*60+minute*60+second;
    }

    public int belongToWhichSDLE(long unixTimestamp){
        int daySecond = getDaySecond(getDate(unixTimestamp));
        int No = 0;
        for(int i=0, t=timeInterval; t<=this.daySecond; i++, t+=timeInterval){
            if(daySecond<=t){
                No = i;
                break;
            }
        }
        return No;
    }

    public void addInstance(String[] label, long unixTimestamp){
        ArrayList<String> instance = new ArrayList<String>();
        for(int i=0; i<label.length; i++){
            instance.add(label[i]);
        }
        instanceLabel.get(belongToWhichSDLE(unixTimestamp)).add(instance);
    }


    public void printDB(){
        String SDLEDBName = "db/SDLE";
        try {
            for (int i = 0; i < SDLEQuantity; i++) {
                FileWriter fw = new FileWriter(SDLEDBName + i + ".txt");
                for(int j=0; j< instanceLabel.get(i).size(); j++){
                    String instance = "";
                    for(int k=0; k< instanceLabel.get(i).get(j).size()-1; k++){
                        instance += instanceLabel.get(i).get(j).get(k) + ",";
                    }
                    instance += instanceLabel.get(i).get(j).get(instanceLabel.get(i).get(j).size()-1) + "\r\n";
                    fw.write(instance);
                }
                fw.close();
            }
            FileWriter fw_readme = new FileWriter("db/readme.txt");
            fw_readme.write("time interval:"+timeInterval+"\r\n");
            fw_readme.write("accumulated day:");
            fw_readme.close();
        }
        catch(Exception e){

        }
    }


    public int getTimeInterval(){
        return timeInterval;
    }


}
