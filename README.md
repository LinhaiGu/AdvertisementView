# AdvertisementView
广告轮播效果的实现
##前言
最近刚把驾校的科目一考完，也有充足的时间去研究android，目前自己的打算是这样的，先从自定义控件开始一步一步的学习研究，之后会去写一些设计模式之类的文章，到时候欢迎大家来浏览。
‘广告位’这个词汇大家一定不陌生，现在大部分的APP都会有广告位，打开某款APP，首页顶部就会出现一直自动播放的广告，这种效果还是挺炫的，那如何实现这个功能呢，这篇文章将会揭晓广告位的神秘面纱。

今天实现的效果是这样的：

![广告位](http://img.blog.csdn.net/20160311190007895)

##打造广告位的前奏

在开始敲代码前，先来看看这个广告位到底应该怎样实现，从效果图看我们发现广告位有以下特点：

 - 广告位可以自动轮播的。
 - 广告位是可以向右无限轮播的。
 - 广告位底部有一个指示器（三个小圆圈），指示器内的圆点个数是按图片的个数来设定的。
 - 切换到哪个图片，底下的指示器也切到相应的位置。
 
####广告位可以自动轮播的

广告位可以自动轮播的，自动轮播意味着我们需要一个定时器，如何实现定时器，有三种方式：

 1. Handler+Thread
 2. Handler类自带的postDelyed
 3. Handler+Timer+TimerTask
 
这三种方式效果都一样，我用的是第二种：Handler类自带的postDelyed

```
private Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// 开启轮播
			switchItem();
			handler.postDelayed(this, 1000);
		}
};
/**
* 开启轮播
*/
public void start() {
	handler.postDelayed(runnable, 2000);
}

/**
* 关闭轮播
*/
public void stop() {
	handler.removeCallbacks(runnable);
}
```
代码非常的easy,间隔1秒的轮播。

####广告位是可以向右无限轮播的
这里面实现广告位的控件，我是选用Gallery控件，通过为Gallery绑定相应的适配器，我们就能通过手指来回切换图片，那如何无限向右轮播的呢？Gallery通过绑定适配器，自定义适配器时，我们要创建一个继承BaseAdapter的类，重写里面的几个方法，看到有个getCount方法，它主要要来告诉Gallery有多少个Item，这时可以这样改动：

```
		@Override
		public int getCount() {
			final int count = mAdvertisementObjectList == null ? 0
					: mAdvertisementObjectList.size();
			return count > 1 ? Integer.MAX_VALUE : count;
		}
```
将显示的Item数改为Integer.MAX_VALUE。

####广告位指示器实现
从效果图能看到底部居中有个指示器，指示器的圆点个数是可控，这里是通过自定义View来实现的，通过绘制图片数的圆点。具体代码如下：

```
package com.example.advertisementproject.view;

import com.example.advertisementproject.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

/**
 * 指示器
 * 
 * @author Linhai Gu
 */
public class IndicatorView extends View {

	private LayoutParams mParams;
	/**
	 * 选中的图标
	 */
	private Bitmap mSelectorIcon;
	/**
	 * 未选中的图标
	 */
	private Bitmap mUnselectorIcon;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 图标个数
	 */
	private int mCount;

	/**
	 * 当前高亮显示圆点的位置
	 */
	private int mSelectedIndex;

	/**
	 * 圆点的宽度
	 */
	private int mIconWidth;
	/**
	 * 圆点的高度
	 */
	private int mIconHeight;

	/**
	 * 指示器的宽度
	 */
	private int mWidth;

	/**
	 * 指示器的高度
	 */
	private int mHeight;

	/**
	 * 圆点间的空隔
	 */
	private int mSpace;

	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IndicatorView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context _context) {
		/**
		 * 创建画笔
		 */
		mPaint = new Paint();
		/**
		 * 获取选中图标的bitmap
		 */
		mSelectorIcon = BitmapFactory.decodeResource(_context.getResources(),
				R.drawable.selector_point);
		/**
		 * 获取未选中图标的bitmap
		 */
		mUnselectorIcon = BitmapFactory.decodeResource(_context.getResources(),
				R.drawable.unselector_point);
		mIconWidth = mUnselectorIcon.getWidth();
		mIconHeight = mUnselectorIcon.getHeight();
		mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, mIconHeight);
		setLayoutParams(mParams);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawSelectIcon(mSelectedIndex, canvas);
	}

	/**
	 * 绘制图标
	 * 
	 * @param _select
	 * @param _canvas
	 */
	private void drawSelectIcon(int _select, Canvas _canvas) {
		for (int i = 0; i < mCount; i++) {
			if (i == _select) {
				/**
				 * 被选中的当前显示的指示器圆点
				 */
				drawSelectedIcon(i, _canvas);
			} else {
				/**
				 * 未选中时的指示器圆点
				 */
				drawUnselectedIcon(i, _canvas);
			}
		}
	}

	/**
	 * 绘制选中时的圆点
	 * 
	 * @param _index
	 * @param _canvas
	 */
	private void drawSelectedIcon(int _index, Canvas _canvas) {
		_canvas.drawBitmap(mSelectorIcon, _index * (mIconWidth + mSpace), 0,
				mPaint);
	}

	/**
	 * 绘制未选中时的圆点
	 * 
	 * @param _index
	 * @param _canvas
	 */
	private void drawUnselectedIcon(int _index, Canvas _canvas) {
		_canvas.drawBitmap(mUnselectorIcon, _index * (mIconWidth + mSpace), 0,
				mPaint);
	}

	/**
	 * 指示器圆点的总个数
	 * 
	 * @param _count
	 *            总数
	 * 
	 */
	public void setTotal(int _count) {
		this.mCount = _count;
		this.setLayoutParams(mParams);
		if (_count == 0) {
			this.setVisibility(View.INVISIBLE);
		} else {
			mWidth = mIconWidth * _count + (_count - 1) * mSpace;
			mHeight = mIconWidth;
			this.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 选中的圆点的位置
	 * 
	 * @param _selected
	 *            高亮圆点位置
	 */
	public void setSelected(int _selected) {
		mSelectedIndex = _selected;
		invalidate();
	}

	/**
	 * 设置圆点间的空隔
	 * 
	 * @param _space
	 */
	public void setSpace(int _space) {
		this.mSpace = _space;
	}

	/**
	 * 获取指示器宽度
	 * 
	 * @return
	 */
	public int getIndicatorWidth() {
		return mWidth;
	}

}

```
绘制圆点时，我们要把握好绘制的位置，drawBitmap有以下几个参数：

```
public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) 
```

> bitmap是我们要绘制的圆点
> left是距离屏幕左边的距离
> top是距离顶部的距离
> paint是我们定义的画笔

圆点与圆点有空隔，如果有两个图片的话，就有一个空隔，三个图片的话就有两个空隔，因此整个指示器的宽度就可以计算出来：指示器宽度=圆点自身宽度*圆点总数+（圆点总数-1）间隔的距离。

```
mWidth = mIconWidth * _count + (_count - 1) * mSpace;
```
怎样确定绘制圆点的位置，比如要绘制第一个位置的圆点，这时距离左边距离应该是(这里的起始位置从0开始): 0*（绘制圆点的宽度+空隔）=0，也就是在0,0位置绘制；如果是绘制第二个位置：1*（绘制圆点的宽度+空隔），这样依次类推，两圆点绘制的距离是圆点宽度加上空隔的距离。

```
	/**
	 * 绘制选中时的圆点
	 * 
	 * @param _index
	 * @param _canvas
	 */
	private void drawSelectedIcon(int _index, Canvas _canvas) {
		_canvas.drawBitmap(mSelectorIcon, _index * (mIconWidth + mSpace), 0,
				mPaint);
	}

	/**
	 * 绘制未选中时的圆点
	 * 
	 * @param _index
	 * @param _canvas
	 */
	private void drawUnselectedIcon(int _index, Canvas _canvas) {
		_canvas.drawBitmap(mUnselectorIcon, _index * (mIconWidth + mSpace), 0,
				mPaint);
	}
```

##（正餐）打造广告位

创建我们的AdvertisementView并继承RelativeLayout，在这里加载我们的广告位的布局：

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/rl_group"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" >

    <Gallery
        android:id="@+id/gl_images"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <LinearLayout
        android:id="@+id/ll_group"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_marginBottom="4dp"
        android:orientation="horizontal" >

        <com.example.advertisementproject.view.IndicatorView
            android:id="@+id/view_indicator"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center" >
        </com.example.advertisementproject.view.IndicatorView>

    </LinearLayout>

</RelativeLayout>
```

布局的效果就是将指示器放在底部，居中操作我们放在代码中实现，这时因为我们要获取指示器的宽度，通过屏幕宽度/2-（指示器宽度/2）得到指示器放置的起始位置。

整体的代码比较简单，一下是整个广告位轮播的源码：

```
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
 * 广告位轮播
 * 
 * @author Linhai Gu
 * 
 */
public class AdvertisementView extends RelativeLayout implements
		OnItemClickListener, OnItemSelectedListener {

	private Context mContext;

	public final int ADVER_HEIGHT = 50;

	/**
	 * 指示器容器
	 */
	private LinearLayout ll_group;
	/**
	 * 指示器
	 */
	private IndicatorView mIndicatorView;
	private RelativeLayout rl_group;

	/**
	 * 广告图宽度
	 */
	private float mWidth;

	/**
	 * 广告图的高度
	 */
	private int mHeight;

	private Gallery mGallery;

	private ImageSwitcherAdapter mImageSwitcherAdapter;

	/**
	 * 数据源
	 */
	private ArrayList<AdvertisementObject> mAdvertisementObjectList = new ArrayList<AdvertisementObject>();
	/**
	 * 是不是本地资源ID
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
	 * 数据初始化
	 */
	private void initData() {
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
	}

	/**
	 * 初始化View
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
		 * 设置圆点间隔距离
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
	 * 给广告空间绑定适配器
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
	 * 设置图片大小
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
				 * 从本地加载
				 */
				viewHolder.iv_image
						.setImageResource(advertisementObject.mResourceId);
			} else {
				/**
				 * 从网络加载
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
	 * 设置指示器位置
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
			// 开启轮播
			switchItem();
			handler.postDelayed(this, 1000);
		}
	};

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 设置数据 如果是图片的URL的话，_isResourceId为false
	 * 
	 * @param data
	 * @param _isResourceId
	 *            true:本地图片资源ID false:网络图片URL
	 */
	public void setData(ArrayList<AdvertisementObject> _data,
			boolean _isResourceId) {
		this.mAdvertisementObjectList = _data;
		this.isResourceId = _isResourceId;
		mMax = mAdvertisementObjectList == null ? 0 : mAdvertisementObjectList
				.size();
		// 设置指示器
		mIndicatorView.setTotal(mMax);
		setIndicatorParams();
		mImageSwitcherAdapter.notifyDataSetChanged();
	}

	/**
	 * 设置广告图的宽高比例
	 * 
	 * @param width
	 * @param height
	 */
	public void setAdvertisementRate(float width, float height) {
		mHeight = (int) ((mWidth / (width + height)) * height);
		setLayoutParams();
	}

	/**
	 * 开启轮播
	 */
	public void start() {
		handler.postDelayed(runnable, 2000);
	}

	/**
	 * 关闭轮播
	 */
	public void stop() {
		handler.removeCallbacks(runnable);
	}

	public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
		this.mOnItemClickListener = _onItemClickListener;
	}

}

```

整体流程是这样的，初始化我们的View,为我们的Gallery绑定适配器，并监听点击事件与选中事件，这样方便回调，接着定义我们的定时器，这里传递数据源我写了一个方法：

```
	/**
	 * 设置数据 如果是图片的URL的话，_isResourceId为false
	 * 
	 * @param data
	 * @param _isResourceId
	 *            true:本地图片资源ID false:网络图片URL
	 */
	public void setData(ArrayList<AdvertisementObject> _data,
			boolean _isResourceId) {
		this.mAdvertisementObjectList = _data;
		this.isResourceId = _isResourceId;
		mMax = mAdvertisementObjectList == null ? 0 : mAdvertisementObjectList
				.size();
		// 设置指示器
		mIndicatorView.setTotal(mMax);
		setIndicatorParams();
		mImageSwitcherAdapter.notifyDataSetChanged();
	}
```
_isResourceId是用于区分是网络加载还是本地的资源ID，这样的原因是方便测试，因为通过网络加载图片不是本篇的重点，我在AdvertisementObject里放了两个参数，分别是资源ID，和通过网络加载的图片URL：

```
public class AdvertisementObject implements Serializable {
	public String mImageUrl;// 图片url
	public int mResourceId;//本地图片资源ID
}
```
当然你也可以加上更多的信息，完全取决于业务需求。

最后看看如何使用这个广告控件：

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <com.example.advertisementproject.view.AdvertisementView
        android:id="@+id/view_advertisement"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" >
    </com.example.advertisementproject.view.AdvertisementView>

</LinearLayout>
```

```
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
```

用法是不是很简单，最近写下一些自定义控件，主要是为了分享自定义控件的一个思路，希望大家喜欢。
