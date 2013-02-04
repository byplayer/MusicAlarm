package jp.fedom.android.musicalarm.activity;

import java.util.ArrayList;

import jp.fedom.android.musicalarm.R;
import jp.fedom.android.musicalarm.item.ConfigItem;
import jp.fedom.android.musicalarm.item.ConfigPreference;
import jp.fedom.android.musicalarm.service.AlarmRegisterService;
import jp.fedom.android.musicalarm.service.AlarmRingService;
import jp.fedom.android.musicalarm.util.bluetooth.BluetoothA2DPWrapper;
import jp.fedom.android.musicalarm.util.music.MusicWapper;

import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * This is dummy comment.
 * TODO: update comment
 * @author taka2
 */
public final class MainActivity extends Activity {

    /** for logging. */
    public static final String TAG = "MainActivity";

    /** dummy comment. TODO:update comment */
    private ListView listview;

    /** dummy comment. TODO:update comment */
    private ArrayList<ConfigItem> dataList = new ArrayList<ConfigItem>();

    /** dummy comment. TODO:update comment */
    private ConfigAdapter adapter;

    /** dummy comment. TODO:update comment */
    private static final String SPEAKER_MAC_AD = "30:F9:ED:8F:35:B0";

    
    private class TimePickerListenerWrapper implements TimePickerDialog.OnTimeSetListener{

    	private int itemPosition = -1;
    	
    	public void setItemPosition(final int itemPosition_arg){
    		this.itemPosition = itemPosition_arg;
    	}
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            Log.d("timepicker","onTimeset called");
	            if (itemPosition != -1){
	              dataList.get(itemPosition).setTime(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
	  	          Log.d("timepicker","time set to " + itemPosition + "," + String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute)) ;
	              itemPosition = -1;
	              adapter.notifyDataSetChanged();
	            }
	            
			}
    	
    }
    // the callback received when the user "sets" the time in the dialog
    private TimePickerListenerWrapper mTimeSetListener = new TimePickerListenerWrapper();
   
    /**
     * dummy comment.
     * TODO:update comment
     * @author taka
     */
    private class ConfigAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public ConfigItem getItem(final int index) {
            return dataList.get(index);
        }

        @Override
        public long getItemId(final int itemId) {
            return itemId;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.configlist_layout, null);
            }
            final ConfigItem citem = (ConfigItem) getItem(position);
            if (citem != null) {
                ((TextView) view.findViewById(R.id.config_title_text))
                        .setText(citem.getTitle());
                ((TextView) view.findViewById(R.id.config_time_text))
                        .setText(citem.getTime());
                ((TextView) view.findViewById(R.id.config_music_text))
                        .setText(citem.getPath());
                ((ToggleButton) view.findViewById(R.id.config_enable_toggle)).setChecked(citem.isEnable());
                
                view.findViewById(R.id.config_music_text).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Log.i("Click", "music_text");
                    }
                });
                view.findViewById(R.id.config_time_text).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Log.i("Click", "config_time_text  index : " + dataList.indexOf(view));
                    	mTimeSetListener.setItemPosition(listview.getPositionForView(view));
                    	
                    	int hour = Integer.valueOf(dataList.get(listview.getPositionForView(view)).getTime().split(":")[0]);
                    	int minute = Integer.valueOf(dataList.get(listview.getPositionForView(view)).getTime().split(":")[1]);
                    	
                    	TimePickerDialog tpDialog = new TimePickerDialog(MainActivity.this, mTimeSetListener, hour, minute, true);
                    	tpDialog.show();
                    	listview.invalidate();
                    }
                });
                view.findViewById(R.id.config_title_text).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Log.i("Click", "config_title_text");
                    }
                });
            }
            return view;
        }

    }


    @Override
    /**
     * This is dummy comment.
     * TODO:describe comment
     */
    public void onCreate(final Bundle savedState /* =savedInstanceState */) {
        super.onCreate(savedState);
        Log.i("onCreate", "onCreate");

        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.ConfigList);

        final ConfigPreference pref = new ConfigPreference(PreferenceManager.getDefaultSharedPreferences(this));
        dataList = (ArrayList<ConfigItem>) pref.loadConfigItems();

        adapter = new ConfigAdapter();
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }
    
    public void saveConfig(){
    	Log.d("menu","called save");
        final ConfigPreference pref = new ConfigPreference(PreferenceManager.getDefaultSharedPreferences(this));
        for(int i = 0; i < dataList.size(); i++){
        	dataList.get(i).setEnable(   ((ToggleButton)listview.getChildAt(i).findViewById(R.id.config_enable_toggle)).isChecked() );
        }
        pref.saveConfigItems(dataList);
		(Toast.makeText(this, "Saved", Toast.LENGTH_LONG)).show();
    }

    
    @Override
    /**
     * This is dummy comment.
     * TODO:describe comment
     */
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_save:
        	saveConfig();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This is dummy comment.
     * @param v
     * TODO:describe comment
     */
    public void onClickStartMusic(final View v) {
        BluetoothA2DPWrapper.getInstance().connect(SPEAKER_MAC_AD);
        MusicWapper.getInstance().start((AudioManager) getSystemService(Context.AUDIO_SERVICE));
    }

    /**
     * This is dummy comment.
     * @param v
     * TODO:describe comment
     */
    public void onClickStopMusic(final View v) {
        BluetoothA2DPWrapper.getInstance().disconnect(SPEAKER_MAC_AD);
        MusicWapper.getInstance().stop();
    }

    /**
     * This is dummy comment.
     * @param v
     * TODO:describe comment
     */
   public void onClickStartAlarm(final View v) {
        Log.d("onClick", "called onClickStartAlarm");
   }

    /**
     * This is dummy comment.
     * TODO:describe comment
     * @param v view
     */
    public void onClickStopAlarm(final View v) {
        Log.d("onClick", "called onClickStopAlarm");
        Intent intent = new Intent(this, AlarmRingService.class);
        startService(intent);
    }
    
    
    /**
     * This is dummy comment.
     * TODO:describe comment
     * @param v view
     */
    public void onClickStartService(final View v){
    	Log.d("click","called onClickStartService");
    	try{
    	ComponentName name = startService( new Intent(MainActivity.this, AlarmRegisterService.class));
    	if(name != null){
    		(Toast.makeText(this, name.getClassName(), Toast.LENGTH_SHORT)).show();
    	}else{
    		(Toast.makeText(this, "name is null", Toast.LENGTH_SHORT)).show();
    	}
    	}catch(Exception e){
    		Log.d("click",e.getMessage());
    	}
    }


    /**
     * This is dummy comment.
     * TODO:describe comment
     * @param v view
     */
    public void onClickStopService(final View v){
    	Log.d("click","called onClickStopService");
    	Intent intent = new Intent(MainActivity.this, AlarmRegisterService.class);
    	stopService(intent);
    }
    
    private boolean isAlarmServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AlarmRegisterService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is dummy comment.
     * TODO:describe comment
     * @param v view
     */
    public void onClickCheckService(final View v){
    	Log.d("click","called onClickCheckService");
    	String str = isAlarmServiceRunning() ? "service is running" : "service is NOT running";
    	(Toast.makeText(this, str, Toast.LENGTH_SHORT)).show();
    }

}
