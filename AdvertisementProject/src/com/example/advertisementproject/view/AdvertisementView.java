package com.example.advertisementproject.view;

import java.util.ArrayList;

import com.example.advertisementproject.R;
import com.example.advertisementproject.view.entity.AdvertisementObject;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * ���λ�ֲ�
 * 
 * @author Linhai Gu
 * 
 */
public class AdvertisementView extends RelativeLayout implements
		OnItemClickListener, OnItemSelectedListener {

	private Context mContext;

	public final int ADVER_HEIGHT = 50;

	/**
	 * ָʾ������
	 */
	private LinearLayout ll_group;
	/**
	 * ָʾ��
	 */
	private IndicatorView mIndicatorView;
	private RelativeLayout rl_group;

	/**
	 * ���ͼ���
	 */
	private float mWidth;

	/**
	 * ���ͼ�ĸ߶�
	 */
	private int mHeight;

	private Gallery mGallery;

	private ImageSwitcherAdapter mImageSwitcherAdapter;

	/**
	 * ����Դ
	 */
	private ArrayList<AdvertisementObject> mAdvertisementObjectList = new ArrayList<AdvertisementObject>();
	/**
	 * �ǲ��Ǳ�����ԴID
	 */
	private boolean isResourceId;
	private int mMax;

	private OnItemClickListener mOnItemClickListener;

	public AdvertisementView(Context context) {
		this(context, null);
	}

	public AdvertisementView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AdvertisementView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initData();
		initView();
		initAdapter();
		initEvent();
	}

	/**
	 * ���ݳ�ʼ��
	 */
	private void initData() {
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
	}

	/**
	 * ��ʼ��View
	 */
	private void initView() {
		View rootView = LayoutInflater.from(mContext).inflate(
				R.layout.advertisement_view_layout, this);
		ll_group = (LinearLayout) rootView.findViewById(R.id.ll_group);
		mGallery = (Gallery) rootView.findViewById(R.id.gl_images);
		rl_group = (RelativeLayout) rootView.findViewById(R.id.rl_group);
		mIndicatorView = (IndicatorView) rootView
				.findViewById(R.id.view_indicator);
		/*
		 * ����Բ��������
		 */
		mIndicatorView.setSpace(6);
		LayoutParams params = new LayoutParams(new LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, mHeight));
		this.setLayoutParams(params);
	}

	private void setLayoutParams() {
		LayoutParams params = new LayoutParams(new LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, mHeight));
		rl_group.setLayoutParams(params);
	}

	/**
	 * �����ռ��������
	 */
	private void initAdapter() {
		mImageSwitcherAdapter = new ImageSwitcherAdapter();
		mGallery.setAdapter(mImageSwitcherAdapter);
	}

	private void initEvent() {
		mGallery.setOnItemClickListener(this);
		mGallery.setOnItemSelectedListener(this);
	}

	/**
	 * ����ͼƬ��С
	 * 
	 * @param imageView
	 */
	private void setPxImage(ImageView imageView) {
		android.view.ViewGroup.LayoutParams lp;
		lp = imageView.getLayoutParams();
		lp.height = mHeight;
		lp.width = (int) mWidth;
		imageView.setLayoutParams(lp);
	}

	public class ImageSwitcherAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			final int count = mAdvertisementObjectList == null ? 0
					: mAdvertisementObjectList.size();
			return count > 1 ? Integer.MAX_VALUE : count;
		}

		@Override
		public Object getItem(int position) {
			mAdvertisementObjectList.get(position % mMax);
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.advertisement_item_layout, null);
				viewHolder.iv_image = (ImageView) convertView
						.findViewById(R.id.iv_image);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			AdvertisementObject advertisementObject = mAdvertisementObjectList
					.get(position % mMax);
			setPxImage(viewHolder.iv_image);
			if (isResourceId) {
				/**
				 * �ӱ��ؼ���
				 */
				viewHolder.iv_image
						.setImageResource(advertisementObject.mResourceId);
			} else {
				/**
				 * ���������
				 */

			}

			return convertView;
		}
	}

	static class ViewHolder {
		ImageView iv_image;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mOnItemClickListener.onItemClick(position % mMax);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i("TAG", "position:" + position);
		mIndicatorView.setSelected(position % mMax);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	/**
	 * ����ָʾ��λ��
	 */
	private void setIndicatorParams() {
		LayoutParams params = (LayoutParams) ll_group.getLayoutParams();
		params.leftMargin = (int) (mWidth / 2 - mIndicatorView.getWidth() / 2);
		ll_group.setLayoutParams(params);
	}

	public interface OnItemClickListener {
		void onItemClick(int position);
	}

	protected void switchItem() {
		mGallery.onScroll(null, null, 1, 0);
		mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
	}

	private Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// �����ֲ�
			switchItem();
			handler.postDelayed(this, 1000);
		}
	};

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * �������� �����ͼƬ��URL�Ļ���_isResourceIdΪfalse
	 * 
	 * @param data
	 * @param _isResourceId
	 *            true:����ͼƬ��ԴID false:����ͼƬURL
	 */
	public void setData(ArrayList<AdvertisementObject> _data,
			boolean _isResourceId) {
		this.mAdvertisementObjectList = _data;
		this.isResourceId = _isResourceId;
		mMax = mAdvertisementObjectList == null ? 0 : mAdvertisementObjectList
				.size();
		// ����ָʾ��
		mIndicatorView.setTotal(mMax);
		setIndicatorParams();
		mImageSwitcherAdapter.notifyDataSetChanged();
	}

	/**
	 * ���ù��ͼ�Ŀ�߱���
	 * 
	 * @param width
	 * @param height
	 */
	public void setAdvertisementRate(float width, float height) {
		mHeight = (int) ((mWidth / (width + height)) * height);
		setLayoutParams();
	}

	/**
	 * �����ֲ�
	 */
	public void start() {
		handler.postDelayed(runnable, 2000);
	}

	/**
	 * �ر��ֲ�
	 */
	public void stop() {
		handler.removeCallbacks(runnable);
	}

	public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
		this.mOnItemClickListener = _onItemClickListener;
	}

}
