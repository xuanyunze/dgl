package com.rxoa.zlpay.view;

import com.rxoa.zlpay.R;
import com.rxoa.zlpay.listener.DialogListener;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MyDialog extends Dialog{
	static final int BACKGROUND_COLOR = -1;
	static final int BRUSH_COLOR = -16777216;
	Context context;
	DialogListener dialogListener;
	int mColorIndex;
	PaintView mView;
	LayoutParams p;

	public MyDialog(Context paramContext, DialogListener paramDialogListener){
		super(paramContext, R.style.dialog_screen);
		this.context = paramContext;
		this.dialogListener = paramDialogListener;
	}

	protected void onCreate(Bundle paramBundle){
		super.onCreate(paramBundle);
		requestWindowFeature(1);
		requestWindowFeature(2);
		setContentView(R.layout.acty_makesignature_dialog);
		this.p = getWindow().getAttributes();
		Display localDisplay = getWindow().getWindowManager().getDefaultDisplay();
		this.p.height = ((int)(0.8D * localDisplay.getHeight()));
		this.p.width = (1 * localDisplay.getWidth());
		getWindow().setAttributes(this.p);
		this.mView = new PaintView(this.context);
		((FrameLayout)findViewById(R.id.input_signature_edtv)).addView(this.mView);
		this.mView.requestFocus();
		((Button)findViewById(R.id.rewrite_btn)).setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){
				MyDialog.this.mView.clear();
			}
		});
		((Button)findViewById(R.id.clickmake_btn)).setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){
				try{
					MyDialog.this.dialogListener.refreshActivity(MyDialog.this.mView.getCachebBitmap());
					MyDialog.this.dismiss();
					return;
				}catch (Exception localException){
					localException.printStackTrace();
				}
			}
		});
		((ImageView)findViewById(R.id.imageView1)).setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){}
		});
	}

	class PaintView extends View{
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private float cur_x;
		private float cur_y;
		private Paint paint;
		private Path path;

		public PaintView(Context arg2){
			super(arg2);
			init();
		}

		private void init(){
			this.paint = new Paint();
			this.paint.setAntiAlias(true);
			this.paint.setStrokeWidth(3.0F);
			this.paint.setStyle(Style.STROKE);
			this.paint.setColor(-16777216);
			this.path = new Path();
			this.cachebBitmap = Bitmap.createBitmap(MyDialog.this.p.width, MyDialog.this.p.height, Config.ARGB_8888);
			this.cacheCanvas = new Canvas(this.cachebBitmap);
			this.cacheCanvas.drawColor(-1);
		}

		public void clear(){
			if (this.cacheCanvas != null){
				this.paint.setColor(-1);
				this.cacheCanvas.drawPaint(this.paint);
				this.paint.setColor(-16777216);
				this.cacheCanvas.drawColor(-1);
				invalidate();
			}
		}

		public Bitmap getCachebBitmap(){
			return this.cachebBitmap;
		}

		protected void onDraw(Canvas paramCanvas){
			paramCanvas.drawBitmap(this.cachebBitmap, 0.0F, 0.0F, null);
			paramCanvas.drawPath(this.path, this.paint);
		}

		protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4){
			int i; int j;
			if (this.cachebBitmap != null){
				i = this.cachebBitmap.getWidth();
				j = this.cachebBitmap.getHeight();
			}else{
				i = 0;
				j = 0;
			}
			if(i>paramInt1&&j>paramInt2) return;
			if (i < paramInt1)
				i = paramInt1;
			if (j < paramInt2)
				j = paramInt2;
			Bitmap localBitmap = Bitmap.createBitmap(i, j, Config.ARGB_8888);
			Canvas localCanvas = new Canvas();
			localCanvas.setBitmap(localBitmap);
			if (this.cachebBitmap != null)
				localCanvas.drawBitmap(this.cachebBitmap, 0.0F, 0.0F, null);
			this.cachebBitmap = localBitmap;
			this.cacheCanvas = localCanvas;
		}

		public boolean onTouchEvent(MotionEvent paramMotionEvent){
			float f1 = paramMotionEvent.getX();
			float f2 = paramMotionEvent.getY();
		switch (paramMotionEvent.getAction()){
			default:break;
			case 0:
		        this.cur_x = f1;
		        this.cur_y = f2;
		        this.path.moveTo(this.cur_x, this.cur_y);
		        break;
			case 2:
		        this.path.quadTo(this.cur_x, this.cur_y, f1, f2);
		        this.cur_x = f1;
		        this.cur_y = f2;
		        break;
			case 1:
		        this.cacheCanvas.drawPath(this.path, this.paint);
		        this.path.reset();
		        break;
		}
        invalidate();
        return true;
    }
  }
}