package org.app.material.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.StateSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.app.material.R;
import org.app.material.adapter.BaseAdapter;
import org.app.material.cell.DoubleCell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class FilePicker extends FrameLayout {

    private ListView listView;
    private ListAdapter adapter;
    private File currentDir;
    private ArrayList<List> items = new ArrayList<>();
    private ArrayList<Entry> entry = new ArrayList<>();
    private static String entryText;
    private boolean receiverRegistered = false;

    public class List {
        public int icon;
        public File file = null;
        public String title = null;
        public String value = null;
    }

    public class Entry {
        public File dir;
        public String text;
        public int scrollItem;
        public int scrollOffset;
    }

    public FilePicker(Context context) {
        super(context);

        adapter = new ListAdapter(context);

        if (!receiverRegistered) {
            receiverRegistered = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
            filter.addAction(Intent.ACTION_MEDIA_CHECKING);
            filter.addAction(Intent.ACTION_MEDIA_EJECT);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_NOFS);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            filter.addAction(Intent.ACTION_MEDIA_SHARED);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            filter.addDataScheme("ic_file");
            context.registerReceiver(receiver, filter);
        }

        listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position < 0 || position >= items.size()) {
                    return;
                }

                List item = items.get(position);
                File file = item.file;

                if (file == null) {
                    Entry en = entry.remove(entry.size() - 1);
                    entryText = en.text;

                    if (en.dir != null) {
                        listFiles(en.dir);
                    } else {
                        listRoots();
                    }

                    listView.setSelectionFromTop(en.scrollItem, en.scrollOffset);
                } else if (file.isDirectory()) {
                    Entry en = new Entry();
                    en.scrollItem = listView.getFirstVisiblePosition();
                    en.scrollOffset = listView.getChildAt(0).getTop();
                    en.dir = currentDir;
                    en.text = entryText;

                    if (!listFiles(file)) {
                        return;
                    }

                    entry.add(en);
                    entryText = item.title;
                    listView.setSelection(0);
                } else {
                    if (!file.canRead()) {
                        //showMessage(getResources().getString(R.string.CanRead));
                        return;
                    }

                    if (file.isFile()) {
                        //showBookDetails(ic_file);
                    }
                }
            }
        });

        listRoots();
        addView(listView, LayoutHelper.makeFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    public FilePicker showDivider() {
        listView.setDividerHeight(1);
        return this;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        if (currentDir == null) {
                            listRoots();
                        } else {
                            listFiles(currentDir);
                        }
                    } catch (Exception ignored) {}
                }
            };

            if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
                listView.postDelayed(r, 1000);
            } else {
                r.run();
            }
        }
    };

    private void listRoots() {
        currentDir = null;
        items.clear();
        List list;

        list = new List();
        list.title = "Internal Storage";
        list.value = getRootSubtitle(Environment.getExternalStorageDirectory().getAbsolutePath());
        list.icon = R.drawable.ic_storage;
        list.file = Environment.getExternalStorageDirectory();
        items.add(list);

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            HashMap<String, ArrayList<String>> aliases = new HashMap<>();
            ArrayList<String> result = new ArrayList<>();
            String extDevice = null;

            while ((line = reader.readLine()) != null) {
                if ((!line.contains("/mnt") && !line.contains("/storage") && !line.contains("/sdcard")) || line.contains("asec") || line.contains("tmpfs") || line.contains("none")) {
                    continue;
                }

                String[] info = line.split(" ");

                if (!aliases.containsKey(info[0])) {
                    aliases.put(info[0], new ArrayList<String>());
                }

                aliases.get(info[0]).add(info[1]);

                if (info[1].equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    extDevice = info[0];
                }

                result.add(info[1]);
            }
            reader.close();

            if (extDevice != null) {
                result.removeAll(aliases.get(extDevice));

                for (String path : result) {
                    try {
                        list = new List();
                        list.title = "SD Card";
                        list.value = getRootSubtitle(path);
                        list.icon = R.drawable.ic_sdcard;
                        list.file = new File(path);
                        items.add(list);
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}

        list = new List();
        list.title = "/";
        list.value = "System Root";
        list.icon = R.drawable.ic_folder;
        list.file = new File("/");
        items.add(list);

        adapter.notifyDataSetChanged();
    }

    private String getRootSubtitle(String path) {
        StatFs stat = new StatFs(path);
        long total = stat.getBlockCountLong() * stat.getBlockSizeLong();
        long free = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        return "Free " + formatFileSize(free) + " of " + formatFileSize(total);
    }

    private boolean listFiles(File dir) {
        if (!dir.canRead()) {
            if (dir.getAbsolutePath().startsWith(Environment.getExternalStorageDirectory().toString()) || dir.getAbsolutePath().startsWith("/sdcard") || dir.getAbsolutePath().startsWith("/mnt/sdcard")) {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    currentDir = dir;
                    items.clear();
                    clearDrawableAnimation(listView);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
            //showMessage("Access Error");
            return false;
        }

        File[] files;

        try {
            files = dir.listFiles();
        } catch (Exception e) {
            //showMessage(e.getLocalizedMessage());
            return false;
        }

        if (files == null) {
            //showMessage("Unknown Error");
            return false;
        }

        currentDir = dir;
        items.clear();

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() != rhs.isDirectory()) {
                    return lhs.isDirectory() ? -1 : 1;
                }

                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });

        for (File file : files) {
            if (file.getName().startsWith(".")) {
                continue;
            }

            List list;

            if (file.isDirectory()) {
                list = new List();
                list.file = file;
                list.icon = R.drawable.ic_folder;
                list.title = file.getName();
                list.value = "Folder";
                items.add(list);
            } else if (file.isFile()) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".gif") || file.getName().endsWith(".jpeg")) {
                    list = new List();
                    list.file = file;
                    list.icon = R.drawable.ic_file;
                    list.title = file.getName();
                    list.value = formatFileSize(file.length());
                    items.add(list);
                } else {
                    list = new List();
                    list.file = file;
                    list.icon = R.drawable.ic_file;
                    list.title = file.getName();
                    list.value = formatFileSize(file.length());
                    items.add(list);
                }
            }
        }

        List item = new List();
        item.title = "..";

        if (entry.size() > 0) {
            Entry en = entry.get(entry.size() - 1);
            if (en.dir == null) {
                item.value = "Folder";
            } else {
                item.value = en.dir.toString();
            }
        } else {
            item.value = "Folder";
        }

        item.icon = R.drawable.ic_folder;
        item.file = null;
        items.add(0, item);

        clearDrawableAnimation(listView);
        adapter.notifyDataSetChanged();
        return true;
    }

    private static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }

    private static void clearDrawableAnimation(View view) {
        if (view == null) {
            return;
        }

        Drawable drawable;

        if (view instanceof ListView) {
            drawable = ((ListView) view).getSelector();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
            }
        } else {
            drawable = view.getBackground();

            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
                drawable.jumpToCurrentState();
            }
        }
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = new DoubleCell(mContext);
            }

            DoubleCell cell = (DoubleCell) view;
            List item = items.get(position);
            cell.addIcon(item.icon).addTitle(item.title).addValue(item.value);
            return view;
        }
    }
}