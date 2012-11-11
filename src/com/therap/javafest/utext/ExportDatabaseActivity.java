package com.therap.javafest.utext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.therap.javafest.utext.backup.DBExport;

public class ExportDatabaseActivity extends Activity implements
		OnItemClickListener {

	DBExport dBExport;
	private ListView lvExportTypes;
	private static final String[] exportTypes = new String[] { "Export as XML",
			"Export as CSV", "Cloud System" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_database);

		lvExportTypes = (ListView) findViewById(R.id.lvExportTypes);
		lvExportTypes.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, exportTypes));
		lvExportTypes.setOnItemClickListener(this);
		dBExport = new DBExport(this);
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		switch (position) {
		case 0:
			dBExport.ExportAsXML();
			break;
		case 1:
			dBExport.ExportAsCSV();
			break;
		}
	}
}
