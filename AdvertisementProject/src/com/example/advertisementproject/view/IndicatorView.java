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
