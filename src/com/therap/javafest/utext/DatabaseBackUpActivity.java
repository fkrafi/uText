package com.therap.javafest.utext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.therap.javafest.utext.backup.DBExport;

public class DatabaseBackUpActivity extends Activity implements
		OnItemClickListener, OnClickListener {

	// private CloudBackUp cb;
	private DBExport dBExport;
	private ListView lvExportTypes;

	private Dialog dialogExportAsOption, dialogBackUpOption;
	private Button bAsCSV, bAsTXT, bAsXML, bUpload, bDownload;

	private static final String[] exportTypes = new String[] { "Export As",
			"Cloud System", "Log Out" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_database);
		Init();
		renderView();
	}

	private void Init() {
		// cb = new CloudBackUp();
		dBExport = new DBExport(this);
	}

	private void renderView() {
		lvExportTypes = (ListView) findViewById(R.id.lvExportTypes);
		lvExportTypes.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, exportTypes));
		lvExportTypes.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		switch (position) {
		case 0:
			dialogExportAsOption = new Dialog(this);
			dialogExportAsOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogExportAsOption.setContentView(R.layout.export_as_dialog_item);
			bAsXML = (Button) dialogExportAsOption.findViewById(R.id.bAsXML);
			bAsXML.setOnClickListener(this);
			bAsCSV = (Button) dialogExportAsOption.findViewById(R.id.bAsCSV);
			bAsCSV.setOnClickListener(this);
			bAsTXT = (Button) dialogExportAsOption.findViewById(R.id.bAsTXT);
			bAsTXT.setOnClickListener(this);
			dialogExportAsOption.show();
			break;
		case 1:
			dialogBackUpOption = new Dialog(this);
			dialogBackUpOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogBackUpOption.setContentView(R.layout.backup_dialog_items);
			bUpload = (Button) dialogBackUpOption.findViewById(R.id.bUpload);
			bUpload.setOnClickListener(this);
			bDownload = (Button) dialogBackUpOption
					.findViewById(R.id.bDownload);
			bDownload.setOnClickListener(this);
			dialogBackUpOption.show();
			break;
		case 2:
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bAsXML:
			dialogExportAsOption.dismiss();
			dBExport.ExportAsXML();
			Toast.makeText(this, "Export as XML done!", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.bAsCSV:
			dialogExportAsOption.dismiss();
			dBExport.ExportAsCSV();
			Toast.makeText(this, "Export as CSV done!", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.bAsTXT:
			dialogExportAsOption.dismiss();
			dBExport.ExportAsXML();
			Toast.makeText(this, "Export as TXT done!", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.bUpload:
			dialogBackUpOption.dismiss();
			// String data = dBExport.ExportAsSQL();
			break;
		case R.id.bDownload:
			break;
		}

	}
}
