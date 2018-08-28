package hand.example.com.myapplication;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.app.ListActivity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class FileChooser extends ListActivity {

	private File currentDir;
	private FileArrayAdapter adapter;
	String mydir="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mydir=getIntent().getStringExtra("dir");
		currentDir = new File(Environment.getExternalStorageDirectory()
				.getPath() + mydir);

		fill(currentDir);
	}

	private void fill(File f) {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				File sel = new File(dir, filename);
				// Filters based on whether the file is hidden or not
				return (sel.isFile() || sel.isDirectory())
						&& !sel.isHidden();

			}
		};

		//String[] fList = path.list(filter);
		String[] dirs = f.list(filter);
		this.setTitle("Current Dir: " + f.getName());
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		try {
			for (int i = 0; i < dirs.length; i++) {
				File sel = new File(currentDir, dirs[i]);
				if (sel.isDirectory())
					dir.add(new Option(sel.getName(), "Folder", sel
							.getAbsolutePath()));
				else {
					fls.add(new Option(sel.getName(), "File Size: "
							+ sel.length(), sel.getAbsolutePath()));
				}
			}


		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Option("..", "Parent Directory", f.getParent()));
		adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view,
				dir);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if (o.getData().equalsIgnoreCase("folder")
				|| o.getData().equalsIgnoreCase("parent directory")) {
			currentDir = new File(o.getPath());
			if(!mydir.equalsIgnoreCase("/gesture")){
                fill(currentDir);
            }

		} else {
			// onFileClick(o);


                filePath(o);

                Intent forward = new Intent();

                forward.putExtra("FILEPATH", filePath(o));
                setResult(RESULT_OK,forward);
                finish();

		}
	}

	private void onFileClick(Option o) {

		Toast.makeText(this, "File selected: " + o.getName(),
				Toast.LENGTH_SHORT).show();
	}

	public String filePath(Option o) {
		String path = "";
		path = o.getPath();// o.getName()
		// Toast.makeText(this, "File Path: "+o.getPath(),
		// Toast.LENGTH_SHORT).show();
		return path;
	}
}
