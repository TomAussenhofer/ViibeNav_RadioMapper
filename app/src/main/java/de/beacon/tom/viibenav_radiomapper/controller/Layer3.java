package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.RadioMap;

/**
 * Created by TomTheBomb on 28.08.2015.
 */
public class Layer3 {

    private Activity main;
    // Layer 3
    private EditText x_koord, y_koord, etage;
    private Handler layer3Handler;

    public Layer3(Activity main){
        this.main = main;

        initGUI();
        initHandler();
    }


    public void initGUI(){

        // Layer 3
        x_koord = (EditText) main.findViewById(R.id.x_koord);
        x_koord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(TextUtils.isEmpty(x_koord.getText().toString()))
                        x_koord.setError("x - can not be empty");
                    else
                        RadioMap.getRadioMap().setPosition_x(Integer.valueOf(x_koord.getText().toString()));
                    return true;
                }
                return false;
            }
        });

        y_koord = (EditText) main.findViewById(R.id.y_koord);
        y_koord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(TextUtils.isEmpty(y_koord.getText().toString()))
                        y_koord.setError("y - can not be empty");
                    else
                        RadioMap.getRadioMap().setPosition_y(Integer.valueOf(y_koord.getText().toString()));
                    return true;
                }
                return false;
            }
        });

        etage = (EditText) main.findViewById(R.id.etage);
        etage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(TextUtils.isEmpty(etage.getText().toString()))
                        etage.setError("etage - can not be empty");
                    else
                        RadioMap.getRadioMap().setFloor(Integer.valueOf(etage.getText().toString()));
                    return true;
                }
                return false;
            }
        });
    }

    public void initHandler(){
        layer3Handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                updateLayer3();
            }
        };
    }

    private void updateLayer3(){
        x_koord.setText(String.valueOf(RadioMap.getRadioMap().getPosition_x()));
        y_koord.setText(String.valueOf(RadioMap.getRadioMap().getPosition_y()));
        etage.setText(String.valueOf(RadioMap.getRadioMap().getFloor()));
    }

    public void clickUp(View view){
        RadioMap.getRadioMap().setY_up();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                long futureTime = System.currentTimeMillis()+100;
                while(System.currentTimeMillis() < futureTime){
                    synchronized(this){
                        try{
                            wait(futureTime- System.currentTimeMillis());
                        }catch(Exception e){}
                    }
                }
                layer3Handler.sendEmptyMessage(0);
            }
        };
        Thread th = new Thread(r);
        th.start();
    }

    public void clickRight(View view){
        RadioMap.getRadioMap().setX_up();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                long futureTime = System.currentTimeMillis()+100;
                while(System.currentTimeMillis() < futureTime){
                    synchronized(this){
                        try{
                            wait(futureTime- System.currentTimeMillis());
                        }catch(Exception e){}
                    }
                }
                layer3Handler.sendEmptyMessage(0);
            }
        };
        Thread th = new Thread(r);
        th.start();
    }

    public void clickDown(View view){
        RadioMap.getRadioMap().setY_down();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                long futureTime = System.currentTimeMillis()+100;
                while(System.currentTimeMillis() < futureTime){
                    synchronized(this){
                        try{
                            wait(futureTime- System.currentTimeMillis());
                        }catch(Exception e){}
                    }
                }
                layer3Handler.sendEmptyMessage(0);
            }
        };
        Thread th = new Thread(r);
        th.start();
    }

    public void clickLeft(View view){
        RadioMap.getRadioMap().setX_down();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                long futureTime = System.currentTimeMillis()+100;
                while(System.currentTimeMillis() < futureTime){
                    synchronized(this){
                        try{
                            wait(futureTime- System.currentTimeMillis());
                        }catch(Exception e){}
                    }
                }
                layer3Handler.sendEmptyMessage(0);
            }
        };
        Thread th = new Thread(r);
        th.start();
    }

    public Handler getLayer3Handler() {
        return layer3Handler;
    }

}
