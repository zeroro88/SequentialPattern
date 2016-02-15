package LifePattern;


/**
 * Created by MingJe on 2016/2/8.
 */
public class ActivityInstance {
    private String activity;
    private String startTime;
    private long duration;

    public ActivityInstance(String activity, String startTime, long duration) {
        this.activity = activity;
        this.startTime = startTime;
        this.duration = duration;

    }
    public String toSting() {
        return activity + " " + startTime + " " + duration;
    }
}
