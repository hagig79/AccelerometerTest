package jp.skr.soundwing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class AcceletometerTestActivity extends Activity {

	boolean running;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		running = true;
		MyView mv = new MyView(this);
		setContentView(mv);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		running = false;
	}

	class MyView extends View implements Runnable {

		private static final int POINT_MAX = 10;
		int[] x = new int[POINT_MAX];
		int[] y = new int[POINT_MAX];
		private Handler handler;

		public MyView(Context context) {
			super(context);
			Thread thread = new Thread(this);
			thread.start();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			handler = getHandler();

			canvas.drawColor(Color.WHITE);

			Paint p = new Paint();
			p.setStyle(Paint.Style.FILL);
			p.setARGB(255, 255, 20, 0);
			p.setAntiAlias(true);
			for (int i = 0; i < POINT_MAX; i++) {
				canvas.drawCircle(x[i], y[i], 10, p);
			}
		}

		@Override
		public void run() {
			int current = 0;
			int pos = 0;
			double theta;
			int r = 150; // 半径
			while (running) {
				theta = Math.PI * pos / POINT_MAX / 2;
				x[current] = this.getWidth() / 2 + (int) (r * Math.cos(theta));
				y[current] = this.getHeight() / 2 + (int) (r * Math.sin(theta));

				current = (current + 1) % POINT_MAX;
				pos = (pos + 1) % (POINT_MAX * 4);
				if (handler != null) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							invalidate();
						}
					});
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}