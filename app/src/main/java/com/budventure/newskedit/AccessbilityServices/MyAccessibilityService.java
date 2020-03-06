package com.budventure.newskedit.AccessbilityServices;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.budventure.newskedit.R;

import java.util.List;

import static android.view.accessibility.AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE;

public class MyAccessibilityService extends AccessibilityService {


    public static int WINDOW_CONTENT_CHANGED=0;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyAccessibilityService", "onCreate");
    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes=AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        info.packageNames =new String[]{"com.whatsapp"};
        setServiceInfo(info);
        Toast.makeText(this, "onServiceConnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType=event.getEventType();
        switch (eventType){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if(WINDOW_CONTENT_CHANGED==0){
                    clickOnSearchView(event);
                }else if(WINDOW_CONTENT_CHANGED==1){
                    setSearchView(event);
                }else if(WINDOW_CONTENT_CHANGED==2){
                    clickOnListItem(event);
                }else if(WINDOW_CONTENT_CHANGED==3){
                    clickOnImageButton(event);
                }else if(WINDOW_CONTENT_CHANGED==4){
                    setMessageText(event);
                }else if(WINDOW_CONTENT_CHANGED==5){
                   BackPressApp();
                }
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                setSearchView(event);
                break;



        }



        /*if (getRootInActiveWindow () == null) {
            return;
        }

        AccessibilityNodeInfo currentNode=getRootInActiveWindow();



        AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap (getRootInActiveWindow ());

        // Whatsapp Message EditText id
        List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/entry");
        if (messageNodeList == null || messageNodeList.isEmpty ()) {
            return;
        }
        // check if the whatsapp message EditText field is filled with text and ending with your suffix (explanation above)
        AccessibilityNodeInfoCompat messageField = messageNodeList.get (0);
        if (messageField.getText () == null || messageField.getText ().length () == 0
                || !messageField.getText ().toString ().endsWith (getApplicationContext ().getString (R.string.whatsapp_suffix))) { // So your service doesn't process any message, but the ones ending your apps suffix
            return;
        }

        // Whatsapp send button id
        List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId ("com.whatsapp:id/send");
        if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty ()) {
            return;
        }

        AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get (0);
        if (!sendMessageButton.isVisibleToUser ()) {
            return;
        }

        // Now fire a click on the send button
        sendMessageButton.performAction (AccessibilityNodeInfo.ACTION_CLICK);

        // Now go back to your app by clicking on the Android back button twice:
        // First one to leave the conversation screen
        // Second one to leave whatsapp
        try {
            Thread.sleep (500); // hack for certain devices in which the immediate back click is too fast to handle
            performGlobalAction (GLOBAL_ACTION_BACK);
            Thread.sleep (500);  // same hack as above
        } catch (InterruptedException ignored) {}
        performGlobalAction (GLOBAL_ACTION_BACK);*/
    }

    private void BackPressApp() {
        if(WINDOW_CONTENT_CHANGED==5){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    performGlobalAction (GLOBAL_ACTION_BACK);
                    WINDOW_CONTENT_CHANGED++;
                }
            },200);
        }
    }

    public void clickOnSearchView(AccessibilityEvent event){
        if(event.getSource().getPackageName().equals("com.whatsapp")){
            if(WINDOW_CONTENT_CHANGED==0){
                AccessibilityNodeInfo currentNode=getRootInActiveWindow();
                if(currentNode!=null && currentNode.getClassName().equals("android.widget.FrameLayout") && currentNode.getChild(2)!=null &&
                        currentNode.getChild(2).getClassName().equals("android.widget.TextView") && currentNode.getChild(2).getContentDescription().equals("Search")){
                    currentNode.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    WINDOW_CONTENT_CHANGED++;
                }
            }
        }

    }
    public void setSearchView(AccessibilityEvent event){
        if(event.getSource().getPackageName().equals("com.whatsapp")) {
            if (WINDOW_CONTENT_CHANGED == 1) {
                AccessibilityNodeInfo currentNode = getRootInActiveWindow();
                for (int i = 0; i < currentNode.getChildCount(); i++) {
                    String name = (String) currentNode.getChild(i).getClassName();
                    for (int j = 0; j < currentNode.getChild(i).getChildCount(); j++) {
                        String newname = (String) currentNode.getChild(i).getChild(j).getClassName();
                        if ("android.widget.EditText".equals((String) currentNode.getChild(i).getChild(j).getClassName())) {
                            Bundle args = new Bundle();
                            args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "ChetanbhaiPhP");
                            currentNode.getChild(i).getChild(j).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);
                            WINDOW_CONTENT_CHANGED++;

                        }

                    }
                }
            }
        }
    }

    public void clickOnListItem(AccessibilityEvent event){
        if(event.getSource().getPackageName().equals("com.whatsapp")) {
            if (WINDOW_CONTENT_CHANGED == 2) {
                AccessibilityNodeInfo currentNode = getRootInActiveWindow();
                for (int i = 0; i < currentNode.getChildCount(); i++) {
                    String name = (String) currentNode.getChild(i).getClassName();
                    for (int j = 0; j < currentNode.getChild(i).getChildCount(); j++) {
                        String newname = (String) currentNode.getChild(i).getChild(j).getClassName();
                        if ("android.widget.RelativeLayout".equals((String) currentNode.getChild(i).getChild(j).getClassName())) {
                            AccessibilityNodeInfo nodeInfo = currentNode.getChild(i).getChild(j);
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }

                WINDOW_CONTENT_CHANGED++;

            }
        }
    }

    public void clickOnImageButton(AccessibilityEvent event){
        if(event.getSource().getPackageName().equals("com.whatsapp")) {
            if(WINDOW_CONTENT_CHANGED==3){
                AccessibilityNodeInfo currentNode=getRootInActiveWindow();
                for(int i=0;i<currentNode.getChildCount();i++) {
                    String name = (String) currentNode.getChild(i).getClassName();
                    if("android.widget.ImageButton".equals(name)){
                        AccessibilityNodeInfo nodeInfo=currentNode.getChild(i);
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        WINDOW_CONTENT_CHANGED++;
                    }
                }

            }
        }
    }

    public void setMessageText(AccessibilityEvent event){
        if(event.getSource().getPackageName().equals("com.whatsapp")) {
            if(WINDOW_CONTENT_CHANGED==4){
                List<AccessibilityNodeInfo> messageNodeList = getRootInActiveWindow().findAccessibilityNodeInfosByViewId ("com.whatsapp:id/entry");
                if (messageNodeList == null || messageNodeList.isEmpty ()) {
                    return;
                }

                AccessibilityNodeInfo messageField = messageNodeList.get (0);

                Bundle args = new Bundle();
                args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "Hello "+getApplicationContext().getResources().getString(R.string.whatsapp_suffix));
                messageField.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);

                if (messageField.getText () == null || messageField.getText ().length () == 0
                        || !messageField.getText ().toString ().endsWith (getApplicationContext ().getString (R.string.whatsapp_suffix))) { // So your service doesn't process any message, but the ones ending your apps suffix
                    return;
                }


                List<AccessibilityNodeInfo> sendMessageNodeInfoList = getRootInActiveWindow().findAccessibilityNodeInfosByViewId ("com.whatsapp:id/send");
                if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty ()) {
                    return;
                }

                AccessibilityNodeInfo sendMessageButton = sendMessageNodeInfoList.get (0);
                if (!sendMessageButton.isVisibleToUser ()) {
                    return;
                }

                // Now fire a click on the send button
                sendMessageButton.performAction (AccessibilityNodeInfo.ACTION_CLICK);

                try {
                    Thread.sleep (500); // hack for certain devices in which the immediate back click is too fast to handle
                    performGlobalAction (GLOBAL_ACTION_BACK);
                    Thread.sleep (500);// same hack as above
                } catch (InterruptedException ignored) {
                    Toast.makeText(this, "BackError", Toast.LENGTH_SHORT).show();
                }

                performGlobalAction (GLOBAL_ACTION_BACK);
                WINDOW_CONTENT_CHANGED++;

               /* for (int i = 0; i < currentNode.getChildCount(); i++) {
                    String name = (String) currentNode.getChild(i).getClassName();
                     if ("android.widget.EditText".equals((String) currentNode.getChild(i).getClassName())) {
                            AccessibilityNodeInfo nodeInfo = currentNode.getChild(i);
                            Bundle args = new Bundle();
                            args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "Hello");
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);

                           List<AccessibilityNodeInfo> sendMessageNodeInfoList = getRootInActiveWindow().findAccessibilityNodeInfosByViewId ("com.whatsapp:id/send");
                           if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty ()) {
                             return;
                           }

                           AccessibilityNodeInfo sendMessageButton = sendMessageNodeInfoList.get (0);
                           if (!sendMessageButton.isVisibleToUser ()) {
                             return;
                            }

                         // Now fire a click on the send button
                          sendMessageButton.performAction (AccessibilityNodeInfo.ACTION_CLICK);

                         *//*  new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   performGlobalAction (GLOBAL_ACTION_BACK);
                               }
                           },500);*//*

                         try {
                             Thread.sleep (500); // hack for certain devices in which the immediate back click is too fast to handle
                             performGlobalAction (GLOBAL_ACTION_BACK);
                             Thread.sleep (500);// same hack as above
                           } catch (InterruptedException ignored) {
                             Toast.makeText(this, "BackError", Toast.LENGTH_SHORT).show();
                         }

                         performGlobalAction (GLOBAL_ACTION_BACK);


                         WINDOW_CONTENT_CHANGED++;

                        }*/
                    /*for (int j = 0; j < currentNode.getChild(i).getChildCount(); j++) {
                        String newname = (String) currentNode.getChild(i).getChild(j).getClassName();
                       *//* if ("android.widget.RelativeLayout".equals((String) currentNode.getChild(i).getChild(j).getClassName())) {
                            AccessibilityNodeInfo nodeInfo = currentNode.getChild(i).getChild(j);
                          //  nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }*//*
                       Log.d("sajid",newname);
                    }*/


            }
        }
    }




    @Override
    public void onInterrupt() {
        Log.d("MyAccessibilityService", "onInterrupt");
    }

}
