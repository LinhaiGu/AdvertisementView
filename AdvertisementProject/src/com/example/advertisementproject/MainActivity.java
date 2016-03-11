package com.example.advertisementproject;

import java.util.ArrayList;

import com.example.advertisementproject.view.AdvertisementView;
import com.example.advertisementproject.view.AdvertisementView.OnItemClickListener;
import com.example.advertisementproject.view.entity.AdvertisementObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	private AdvertisementView mAdvertisementView;
	private ArrayList<AdvertisementObject> advertisementObjects = new ArrayList<AdvertisementObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		initViews();
		initEvent();
	}

	private void initData() {
		AdvertisementObject advertisementObject = new AdvertisementObject();
		advertisementObject.mResourceId = R.drawable.image1;
		advertisementObjects.add(advertisementObject);
		advertisementObject = new AdvertisementObject();
		advertisementObject.mResourceId = R.drawable.image2;
		advertisementObjects.add(advertisementObject);
		advertisementObject = new AdvertisementObject();
		advertisementObject.mResourceId = R.drawable.image3;
		advertisementObjects.add(advertisementObject);
	}

	private void initViews() {
		mAdvertisementView = (AdvertisementView) findViewById(R.id.view_advertisement);
		mAdvertisementView.setAdvertisementRate(2, 1);
		mAdvertisementView.setData(advertisementObjects, true);
		mAdvertisementView.start();
	}

	private void initEvent() {
		mAdvertisementView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				Toast.makeText(MainActivity.this, "position:" + position,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onPause() {
		mAdvertisementView.stop();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mAdvertisementView.stop();
		super.onDestroy();
	}

}