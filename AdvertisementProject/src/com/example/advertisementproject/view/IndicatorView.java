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
 * ָʾ��
 * 
 * @author Linhai Gu
 */
public class IndicatorView extends View {

	private LayoutParams mParams;
	/**
	 * ѡ�е�ͼ��
	 */
	private Bitmap mSelectorIcon;
	/**
	 * δѡ�е�ͼ��
	 */
	private Bitmap mUnselectorIcon;
	/**
	 * ����
	 */
	private Paint mPaint;
	/**
	 * ͼ�����
	 */
	private int mCount;

	/**
	 * ��ǰ������ʾԲ���λ��
	 */
	private int mSelectedIndex;

	/**
	 * Բ��Ŀ��
	 */
	private int mIconWidth;
	/**
	 * Բ��ĸ߶�
	 */
	private int mIconHeight;

	/**
	 * ָʾ���Ŀ��
	 */
	private int mWidth;

	/**
	 * ָʾ���ĸ߶�
	 */
	private int mHeight;

	/**
	 * Բ���Ŀո�
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
		 * ��������
		 */
		mPaint = new Paint();
		/**
		 * ��ȡѡ��ͼ���bitmap
		 */
		mSelectorIcon = BitmapFactory.decodeResource(_context.getResources(),
				R.drawable.selector_point);
		/**
		 * ��ȡδѡ��ͼ���bitmap
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
	 * ����ͼ��
	 * 
	 * @param _select
	 * @param _canvas
	 */
	private void drawSelectIcon(int _select, Canvas _canvas) {
		for (int i = 0; i < mCount; i++) {
			if (i == _select) {
				/**
				 * ��ѡ�еĵ�ǰ��ʾ��ָʾ��Բ��
				 */
				drawSelectedIcon(i, _canvas);
			} else {
				/**
				 * δѡ��ʱ��ָʾ��Բ��
				 */
				drawUnselectedIcon(i, _canvas);
			}
		}
	}

	/**
	 * ����ѡ��ʱ��Բ��
	 * 
	 * @param _index
	 * @param _canvas
	 */
	private void drawSelectedIcon(int _index, Canvas _canvas) {
		_canvas.drawBitmap(mSelectorIcon, _index * (mIconWidth + mSpace), 0,
				mPaint);
	}

	/**
	 * ����δѡ��ʱ��Բ��
	 * 
	 * @param _index
	 * @param _canvas
	 */
	private void drawUnselectedIcon(int _index, Canvas _canvas) {
		_canvas.drawBitmap(mUnselectorIcon, _index * (mIconWidth + mSpace), 0,
				mPaint);
	}

	/**
	 * ָʾ��Բ����ܸ���
	 * 
	 * @param _count
	 *            ����
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
	 * ѡ�е�Բ���λ��
	 * 
	 * @param _selected
	 *            ����Բ��λ��
	 */
	public void setSelected(int _selected) {
		mSelectedIndex = _selected;
		invalidate();
	}

	/**
	 * ����Բ���Ŀո�
	 * 
	 * @param _space
	 */
	public void setSpace(int _space) {
		this.mSpace = _space;
	}

	/**
	 * ��ȡָʾ�����
	 * 
	 * @return
	 */
	public int getIndicatorWidth() {
		return mWidth;
	}

}
