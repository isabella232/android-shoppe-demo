package com.fullstorydev.shoppedemo.fssegment;

import android.content.Context;

import com.fullstory.FS;
import com.segment.analytics.Analytics;
import com.segment.analytics.Options;
import com.segment.analytics.Properties;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsWithFS {
    private Analytics analytics;
    private static volatile AnalyticsWithFS singleton = null;

    private AnalyticsWithFS(Context context){
        this.analytics = Analytics.with(context);
    }

    // mimicking the design pattern for Segment Analytics to preserve the syntax: Analytics.with(context)....
    public static AnalyticsWithFS with(Context context) {
        if (singleton == null) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            synchronized (AnalyticsWithFS.class) {
                if (singleton == null) {
                    singleton = new AnalyticsWithFS(context);
                }
            }
        }
        return singleton;
    }

    public void identify(String id){
        this.analytics.identify(id);
        FS.identify(id);
    }

    public void reset(){
        this.analytics.reset();
        FS.anonymize();
    }

    public void track(String name){
        this.analytics.track(name);
        name = "Segment-" + name;
        FS.event(name,new HashMap<>());
    }

    public void track(String name, Properties properties){
        this.analytics.track(name, properties);
        name = "Segment-" + name;
        FS.event(name,properties);
    }

    public void track(String name, Properties properties, Options options){
        this.analytics.track(name,properties,options);
        name = "Segment-" + name;
        HashMap eventFS = new HashMap();
        eventFS.put("properties",properties);
        eventFS.put("options",options);
        FS.event(name,eventFS);
    }

}