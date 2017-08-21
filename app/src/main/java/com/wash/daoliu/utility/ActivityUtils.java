package com.wash.daoliu.utility;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;



public class ActivityUtils {

	private static ActivityUtils activityTransitionUtils;
	public static List<Activity> activityList = new ArrayList<Activity>();

	public static ActivityUtils getInstance() {
		if (activityTransitionUtils == null) {
			activityTransitionUtils = new ActivityUtils();
		}
		return activityTransitionUtils;
	}

	public void pushActivity(Activity activity) {
		activityList.add(activity);
	}
	
	public static void finishAll() {
		// TODO Auto-generated method stub
		for (Activity activity:activityList) {
			activity.finish();
		}
		activityList.clear();
	}
	
}
