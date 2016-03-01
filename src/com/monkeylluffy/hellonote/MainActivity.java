package com.monkeylluffy.hellonote;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button textbtn, imgbtn, videobtn;
	private ListView lv;

	private Intent intent;
	private MyAdapter myAdapter;
	private NoteDB noteDB;
	private SQLiteDatabase dbReader;
	Cursor cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	public void initView() {
		textbtn = (Button) findViewById(R.id.text);
		imgbtn = (Button) findViewById(R.id.img);
		videobtn = (Button) findViewById(R.id.video);
		lv = (ListView) findViewById(R.id.list);
		
		textbtn.setOnClickListener(this);
		imgbtn.setOnClickListener(this);
		videobtn.setOnClickListener(this);

		noteDB = new NoteDB(this);
		dbReader = noteDB.getReadableDatabase();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				cursor.moveToPosition(position);
				Intent i = new Intent(MainActivity.this,SeclectAct.class);
				i.putExtra(NoteDB.ID, cursor.getInt(cursor.getColumnIndex(NoteDB.ID)));
				i.putExtra(NoteDB.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT)));
				i.putExtra(NoteDB.TIME, cursor.getString(cursor.getColumnIndex(NoteDB.TIME)));
				i.putExtra(NoteDB.PATH, cursor.getString(cursor.getColumnIndex(NoteDB.PATH)));
				i.putExtra(NoteDB.VIDEO, cursor.getString(cursor.getColumnIndex(NoteDB.VIDEO)));
				startActivity(i);
				
			}
		});
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		selectDB();
	}
	
	
	private void selectDB() {
		// TODO Auto-generated method stub
		cursor = dbReader.query(NoteDB.TABLE_NAME, null, null, null, null, null, null);
		myAdapter = new MyAdapter(this, cursor);
		lv.setAdapter(myAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		intent = new Intent(MainActivity.this,AddContent.class);
		switch (v.getId()) {
		case R.id.text:
			intent.putExtra("flag", "1");
			startActivity(intent);
			break;
		case R.id.img:
			intent.putExtra("flag","2");
			startActivity(intent);
			break;
		case R.id.video:
			intent.putExtra("flag", "3");
			startActivity(intent);
			break;

		}
	}

}
