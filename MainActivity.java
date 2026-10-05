package com.equatube.dsp;

import android.media.*;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
 private Equalizer eq; private AudioTrack track; private Thread thread; private volatile boolean running;
 private final int[] freq={60,120,250,500,1000,2000,4000,8000,12000,16000}; private final SeekBar[] bars=new SeekBar[10]; private TextView status;
 @Override public void onCreate(Bundle b){super.onCreate(b); buildUi();}
 private void buildUi(){
  LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(10),dp(8),dp(10),dp(8)); root.setBackgroundColor(Color.rgb(16,16,16));
  TextView title=t("EQUATUBE DSP",24,0xFFF2E7D0); title.setGravity(17); title.setTypeface(null,Typeface.BOLD); root.addView(title,new LinearLayout.LayoutParams(-1,dp(46)));
  status=t("DSP: READY",13,0xFFD6A84F); status.setGravity(17); root.addView(status,new LinearLayout.LayoutParams(-1,dp(28)));
  LinearLayout eqrow=new LinearLayout(this); eqrow.setGravity(Gravity.CENTER);
  for(int i=0;i<10;i++){ LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setGravity(17); TextView val=t("0 dB",10,0xFFF2E7D0); val.setGravity(17); SeekBar s=new SeekBar(this); s.setMax(24); s.setProgress(12); s.setRotation(-90); bars[i]=s; TextView f=t(freq[i]>=1000?(freq[i]/1000)+"k":String.valueOf(freq[i]),10,0xFFD6A84F); f.setGravity(17); final int n=i; s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onProgressChanged(SeekBar x,int p,boolean u){val.setText(String.format(Locale.US,"%+d dB",p-12)); if(eq!=null)try{eq.setBandLevel((short)n,(short)((p-12)*125));}catch(Exception ignored){}} public void onStartTrackingTouch(SeekBar x){} public void onStopTrackingTouch(SeekBar x){}}); c.addView(val,new LinearLayout.LayoutParams(dp(44),dp(24))); c.addView(s,new LinearLayout.LayoutParams(dp(50),dp(210))); c.addView(f,new LinearLayout.LayoutParams(dp(44),dp(26))); eqrow.addView(c,new LinearLayout.LayoutParams(0,-2,1)); }
  root.addView(eqrow,new LinearLayout.LayoutParams(-1,dp(270)));
  LinearLayout p=new LinearLayout(this); p.setGravity(17); String[] names={"FLAT","ROCK","VOCAL","BASS"}; int[][] vals={{0,0,0,0,0,0,0,0,0,0},{4,3,1,-1,-2,1,3,4,4,3},{-2,-2,-1,2,4,4,3,1,0,0},{6,5,4,2,0,-1,-2,-2,-2,-2}}; for(int j=0;j<4;j++){Button b=new Button(this); b.setText(names[j]); final int[] v=vals[j]; b.setOnClickListener(x->{for(int i=0;i<10;i++)bars[i].setProgress(v[i]+12);}); p.addView(b,new LinearLayout.LayoutParams(0,dp(46),1));} root.addView(p);
  Button go=new Button(this); go.setText("▶  TEST AUDIO / DSP"); go.setOnClickListener(v->{if(running){stop();go.setText("▶  TEST AUDIO / DSP");status.setText("DSP: READY");}else{start();go.setText("■  STOP TEST");status.setText("DSP: RUNNING");}}); root.addView(go,new LinearLayout.LayoutParams(-1,dp(50)));
  TextView note=t("EquaTube DSP v1.0 • 10-band • Android AudioEffect\nTest tone built in. Music/file player can be added next.",11,Color.GRAY); note.setGravity(17); root.addView(note,new LinearLayout.LayoutParams(-1,dp(55))); setContentView(root);
  try{eq=new Equalizer(0,0);eq.setEnabled(true);}catch(Exception e){status.setText("DSP: AudioEffect unavailable");}
 }
 private void start(){int sr=44100; int min=AudioTrack.getMinBufferSize(sr,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT); track=new AudioTrack(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),new AudioFormat.Builder().setSampleRate(sr).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build(),Math.max(min,4096),AudioTrack.MODE_STREAM,AudioManager.AUDIO_SESSION_ID_GENERATE); try{if(eq!=null){eq.setAudioSession(track.getAudioSessionId());eq.setEnabled(true);}}catch(Exception ignored){} running=true; track.play(); thread=new Thread(()->{short[] d=new short[1024];double ph=0,step=2*Math.PI*220/sr;while(running){for(int i=0;i<d.length;i++){d[i]=(short)(Math.sin(ph)*6000);ph+=step;if(ph>2*Math.PI)ph-=2*Math.PI;}try{track.write(d,0,d.length);}catch(Exception e){break;}}});thread.start();}
 private void stop(){running=false;if(thread!=null)try{thread.join(300);}catch(Exception ignored){}thread=null;if(track!=null){try{track.stop();}catch(Exception ignored){}track.release();track=null;}}
 private TextView t(String s,int z,int c){TextView x=new TextView(this);x.setText(s);x.setTextSize(z);x.setTextColor(c);return x;} private int dp(int n){return(int)(n*getResources().getDisplayMetrics().density+.5f);}
 @Override protected void onDestroy(){stop();if(eq!=null)eq.release();super.onDestroy();}
}
