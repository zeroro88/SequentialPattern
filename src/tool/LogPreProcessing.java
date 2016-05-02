package tool;

import DataStructure.ActivityInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by g2525_000 on 2016/4/23.
 */
public class LogPreProcessing {
    public static ArrayList<ActivityInstance> preProcessing(ArrayList<ActivityInstance> activityList) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        ArrayList<ActivityInstance> proceededList = new ArrayList();
        try {
            for (int i = 1; i < activityList.size(); i++) {
                ActivityInstance currentActivityInstance = activityList.get(i);
                ActivityInstance preActivityInstance = activityList.get(i - 1);
                long currentStartTime = timeFormat.parse(currentActivityInstance.getStartTime()).getTime() / 1000 / 60; //unit : minute
                long preEndTime = timeFormat.parse(preActivityInstance.getEndTime()).getTime() / 1000 / 60;
                if (currentActivityInstance.getActivity().equals(preActivityInstance.getActivity())) {
                    if (currentStartTime >= preEndTime && (currentStartTime - preEndTime) <= 10) {
                        String activity = preActivityInstance.getActivity();
                        String startTime = preActivityInstance.getStartTime();
                        String endTime = currentActivityInstance.getEndTime();
                        long duration = preActivityInstance.getDuration() + currentActivityInstance.getDuration();
                        int dayOfWeek = preActivityInstance.getDayOfWeek();
                        proceededList.add(new ActivityInstance(activity, startTime, endTime, duration, dayOfWeek));
                        i++;
                    } else if (currentStartTime < preEndTime && (287 - preEndTime + currentStartTime) <= 10) {
                        String activity = preActivityInstance.getActivity();
                        String startTime = preActivityInstance.getStartTime();
                        String endTime = currentActivityInstance.getEndTime();
                        long duration = preActivityInstance.getDuration() + currentActivityInstance.getDuration();
                        int dayOfWeek = preActivityInstance.getDayOfWeek();
                        proceededList.add(new ActivityInstance(activity, startTime, endTime, duration, dayOfWeek));
                        i++;
                    } else {
                        proceededList.add(new ActivityInstance(preActivityInstance));
                    }
                } else if (preActivityInstance.getActivity().equals("Leave_Home") && currentActivityInstance.getActivity().equals("Enter_Home")) {
                    if (currentActivityInstance.getStartTime().equals(preActivityInstance.getEndTime())) {
                        proceededList.add(new ActivityInstance(preActivityInstance));
                        continue;
                    }
                    preActivityInstance.setEndTime(currentActivityInstance.getStartTime());
                    if (currentStartTime >= preEndTime) {
                        preActivityInstance.setDuration(currentStartTime - preEndTime);
                    } else {
                        preActivityInstance.setDuration(287 - preEndTime + currentStartTime);
                    }
                    proceededList.add(new ActivityInstance(preActivityInstance));
                } else {
                    proceededList.add(new ActivityInstance(preActivityInstance));
                }
            }
            proceededList.add(new ActivityInstance(activityList.get(activityList.size() - 1)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return proceededList;
    }
}