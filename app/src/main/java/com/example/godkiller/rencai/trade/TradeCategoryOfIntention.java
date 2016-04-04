package com.example.godkiller.rencai.trade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.godkiller.rencai.R;
import com.example.godkiller.rencai.db.TradeCategoryDBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TradeCategoryOfIntention extends Activity {
	private BaseAdapter adapter;
	private ListView TradeLv;
	private TextView overlay;
	private HashMap<String, Integer> categoryIndexer;
	private String[] sections;
	private Handler handler;
	private OverlayThread overlayThread;
	private SQLiteDatabase database;
	private ArrayList<TradeModel> tradeNames;
	private WindowManager windowManager;
	private Button backBtn;
	public static final int TRADE_RESULT_OK = 01;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trade_category_page);

		backBtn = (Button) findViewById(R.id.back_button_tc);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TradeLv = (ListView) findViewById(R.id.trade_list);
		TradeCategoryDBManager tradeCategoryDBManager = new TradeCategoryDBManager(this);
		tradeCategoryDBManager.openDateBase();
		tradeCategoryDBManager.closeDatabase();
		database = SQLiteDatabase.openOrCreateDatabase(TradeCategoryDBManager.DB_PATH + "/"
				+ TradeCategoryDBManager.TRADE_DB_NAME, null);
		tradeNames = getTradeNames();
		database.close();
		categoryIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
		setAdapter(tradeNames);
		TradeLv.setOnItemClickListener(new TradeListOnItemClick());

	}

	private ArrayList<TradeModel> getTradeNames() {
		ArrayList<TradeModel> names = new ArrayList<TradeModel>();
		Cursor cursor = database.rawQuery(
				"SELECT * FROM trade ORDER BY TradeCategory", null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			TradeModel tradeModel = new TradeModel();
			tradeModel.setTradeName(cursor.getString(cursor.getColumnIndex("TradeName")));
			tradeModel.setTradeSort(cursor.getString(cursor.getColumnIndex("TradeCategory")));
			names.add(tradeModel);
		}
		return names;
	}

	class TradeListOnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			TradeModel tradeModel = (TradeModel) TradeLv.getAdapter()
					.getItem(pos);
			Intent tradeIntent = new Intent();
			Bundle b = new Bundle();
			b.putString("trade", tradeModel.getTradeName());
			tradeIntent.putExtras(b);
			setResult(TRADE_RESULT_OK, tradeIntent);
			finish();
		}

	}

	private void setAdapter(List<TradeModel> list) {
		if (list != null) {
			adapter = new ListAdapter(this, list);
			TradeLv.setAdapter(adapter);
		}

	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<TradeModel> list;

		public ListAdapter(Context context, List<TradeModel> list) {

			this.inflater = LayoutInflater.from(context);
			this.list = list;
			categoryIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {

				String currentStr = list.get(i).getTradeSort();

				String previewStr = (i - 1) >= 0 ? list.get(i - 1)
						.getTradeSort() : " ";
				if (!previewStr.equals(currentStr)) {
					String name = list.get(i).getTradeSort();
					categoryIndexer.put(name, i);
					sections[i] = name;
				}
			}

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.trade_list_item, null);
				holder = new ViewHolder();
				holder.category = (TextView) convertView.findViewById(R.id.category);
				holder.name = (TextView) convertView.findViewById(R.id.name2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(list.get(position).getTradeName());
			String currentStr = list.get(position).getTradeSort();
			String previewStr = (position - 1) >= 0 ? list.get(position - 1)
					.getTradeSort() : " ";
			if (!previewStr.equals(currentStr)) {
				holder.category.setVisibility(View.VISIBLE);
				holder.category.setText(currentStr);
			} else {
				holder.category.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView category;
			TextView name;
		}

	}


	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}


	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onDestroy() {
		windowManager.removeView(overlay);
		super.onDestroy();
	}
}