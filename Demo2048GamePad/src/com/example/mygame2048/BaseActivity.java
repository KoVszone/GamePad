package com.example.mygame2048;


import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import cn.vszone.gamepad.GamePadManager;
import cn.vszone.gamepad.OnPlayerListener;
import cn.vszone.gamepad.android.ImageUtils;
import cn.vszone.gamepad.bean.Player;
import cn.vszone.gamepad.mapping.Joystick2DpadEvent;
import cn.vszone.gamepad.mapping.Joystick2DpadEvent$OnDpadKeyEventLinster;
import cn.vszone.gamepad.utils.InputDeviceUtils;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageUtils.getInstance().init(getApplicationContext());
        mOnPlayerListener = new OnPlayerListenerIMP();
    }
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        ev = GamePadManager.getInstance().dispatchGenericMotionEvent(ev);
        if (!InputDeviceUtils.isValueMotionEvent(ev)) { return true; }
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        event = GamePadManager.getInstance().dispatchKeyEvent(event);
        // must return true;
        if (event.getKeyCode() == KeyEvent.KEYCODE_UNKNOWN) { return true; }
        return super.dispatchKeyEvent(event);
    }
    @Override
    public void onDetachedFromWindow() {
        GamePadManager.getInstance().setMode(GamePadManager.EVENT_MODE_GAME);
        GamePadManager.getInstance().unregistOnPlayerListener(mOnPlayerListener);
        GamePadManager.getInstance().destory();
        super.onDetachedFromWindow();
    }
    @Override
    protected void onResume() {
        //在游戏模式下 当窗口可操作时 设备SDK模式为游戏模式,并注册事件监听
        GamePadManager.getInstance().setMode(GamePadManager.EVENT_MODE_SYSTEM);
        GamePadManager.getInstance().registOnPlayerListener(mOnPlayerListener);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在游戏模式下 当窗口可操作时 设备SDK模式为游戏模式,并注销事件监听
        GamePadManager.getInstance().unregistOnPlayerListener(mOnPlayerListener);

    }
    
    OnPlayerListenerIMP mOnPlayerListener ; ;

    class OnPlayerListenerIMP implements OnPlayerListener {
        Joystick2DpadEvent           mJoystick2DpadEvent = new Joystick2DpadEvent();
        boolean                      DEBUG               = false;
        protected SparseArray<Float> mAxisValues = new SparseArray<Float>();

        OnPlayerListenerIMP() {
            mJoystick2DpadEvent = new Joystick2DpadEvent();
            mJoystick2DpadEvent.setOnDpadKeyListener(new Joystick2DpadEvent$OnDpadKeyEventLinster() {

                @Override
                public void onDpadKey(int arg0, KeyEvent arg1) {
                    handKeyEvent(arg1);
                    
                }
            });
           
        }

        @Override
        public void OnPlayerEnter(Player player) {
            // 有手柄插入 其中player.id 对应P1 P2 P3 P4...
            //player.id;
        }

        @Override
        public void OnPlayerExit(Player player) {
            // 有手柄拔出

        }

        @Override
        public boolean onPlayerMotionEvent(Player player, MotionEvent ev) {
            //摇杆的响应不能有延迟 通常不超过1ms为最佳   否则会造成摇杆事件丢失
            mAxisValues.clear();
            mAxisValues.put(0, ev.getAxisValue(MotionEvent.AXIS_X));
            mAxisValues.put(1, ev.getAxisValue(MotionEvent.AXIS_Y));
            mAxisValues.put(15, ev.getAxisValue(MotionEvent.AXIS_HAT_X));
            mAxisValues.put(16, ev.getAxisValue(MotionEvent.AXIS_HAT_Y));
            mJoystick2DpadEvent.handleMotionEvent(player.getId(), mAxisValues);
            return false;
        }
        @Override
        public boolean onPlayerKeyEvent(Player player, KeyEvent ev) {
            //按键的响应不能有延迟,通常不超过2ms为最佳  否则会造成按键丢失
            handKeyEvent(ev);
            return true;
        }

        private void handKeyEvent(KeyEvent ev) {
            if (ev.getAction() == KeyEvent.ACTION_UP) {
                switch (ev.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        swipeUp();
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        swipeDown();
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        swipeLeft();
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        swipeRight();
                        break;
                }
            }
        }

       
    };
    protected void exit() {
        GamePadManager.getInstance().destory();

    }
    protected void swipeRight() {
        // TODO 此处添加代码
        
    }

    protected void swipeLeft() {
        // TODO 此处添加代码
        
    }

    protected void swipeDown() {
        // TODO 此处添加代码
        
    }
    protected void swipeUp() {
        // TODO 此处添加代码
        
    }


}
