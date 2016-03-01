package com.monkeylluffy.hellonote;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private Context context;
	private Cursor cursor;
	public MyAdapter(Context context,Cursor cursor) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.cursor = cursor;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cursor.getPosition();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {

			LayoutInflater inflater = LayoutInflater.from(context);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.cell, null);
			viewHolder.contenttv = (TextView) convertView.findViewById(R.id.list_content);
			viewHolder.timetv = (TextView) convertView.findViewById(R.id.list_time);
			viewHolder.imgiv = (ImageView)convertView.findViewById(R.id.list_img);
			viewHolder.videoiv = (ImageView)convertView.findViewById(R.id.list_video);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		cursor.moveToPosition(position);
		String content = cursor.getString(cursor.getColumnIndex("content"));
		String time = cursor.getString(cursor.getColumnIndex("time"));
		String url = cursor.getString(cursor.getColumnIndex("path"));
		String urlvideo  =cursor.getString(cursor.getColumnIndex("video"));
		
		viewHolder.contenttv.setText(content);
		viewHolder.timetv.setText(time);
		viewHolder.videoiv.setImageBitmap(getVideoThumbnail(urlvideo, 200, 200, MediaStore.Images.Thumbnails.MICRO_KIND));
		viewHolder.imgiv.setImageBitmap(getImageThumbnail(url,200,200));
		return convertView;
	}

	
	
	public Bitmap getImageThumbnail(String uri,int width,int height) {
		Bitmap bitmap = null;
		//获取缩略图
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(uri,options);
		options.inJustDecodeBounds = false;
		int bewidth = options.outWidth/width;
		int beheight = options.outHeight/height;
		int be = 1;
		if (bewidth < beheight) {
			be = bewidth;
		}else {
			be = beheight;
		}
		
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(uri);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		
		return bitmap;
	}
	
	//视频获取缩略图
	public Bitmap getVideoThumbnail(String uri,int width,int height,int kind) {
		Bitmap bitmap = null;
		//获取缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	
	public static class ViewHolder{
		public ImageView imgiv;
		public ImageView videoiv;
		public TextView contenttv;
		public TextView timetv;
	}
	
	
	
	
}
