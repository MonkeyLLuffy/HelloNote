package com.monkeylluffy.hellonote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

public class AddContent extends Activity implements OnClickListener {
	private String val;

	private Button savebtn, deletebtn;
	private ImageView c_img;
	private VideoView c_video;
	private EditText ettext;
	private NoteDB noteDB;
	private SQLiteDatabase dbWriter;
	
	private File phoneFile;
	private File videoFile;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcontent);
		val = getIntent().getStringExtra("flag");
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		savebtn = (Button) findViewById(R.id.save);
		deletebtn = (Button) findViewById(R.id.delete);
		c_video = (VideoView) findViewById(R.id.c_video);
		c_img = (ImageView) findViewById(R.id.c_img);
		ettext = (EditText) findViewById(R.id.ettext);

		savebtn.setOnClickListener(this);
		deletebtn.setOnClickListener(this);
		noteDB = new NoteDB(this);
		dbWriter = noteDB.getWritableDatabase();
		
		if (val.equals("1")) {
			c_img.setVisibility(View.GONE);
			c_video.setVisibility(View.GONE);
			
		}else if (val.equals("2")) {
			c_img.setVisibility(View.VISIBLE);
			c_video.setVisibility(View.GONE);
			//跳转系统相机进行拍照
			Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			phoneFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+getTime()+".jpg");
			iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
			startActivityForResult(iimg, 1);
		}else {
			c_img.setVisibility(View.GONE);
			c_video.setVisibility(View.VISIBLE);
			//跳转系统相机进行录视频
			Intent iimg = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			videoFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+getTime()+".mp4");
			iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
			startActivityForResult(iimg, 2);
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save:
			addDB();
			finish();
			break;
		case R.id.delete:
			finish();
			break;
		}
	}
	
	
	public void addDB() {
		ContentValues cv = new ContentValues();
		cv.put(NoteDB.CONTENT, ettext.getText().toString());
		cv.put(NoteDB.TIME, getTime());
		cv.put(NoteDB.PATH, phoneFile+"");
		cv.put(NoteDB.VIDEO, videoFile+"");
		dbWriter.insert(NoteDB.TABLE_NAME, null, cv);
	}
	
	public String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
		Date curDate = new Date();
		String string = format.format(curDate);
		return string;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			Bitmap bitmap = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
			c_img.setImageBitmap(bitmap);
		}
		if (resultCode == 2) {
			c_video.setVideoURI(Uri.fromFile(videoFile));
			c_video.start();
		}
		
	}
}
