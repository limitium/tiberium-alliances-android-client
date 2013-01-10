package com.example.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cnc.api.Authorizator;
import com.cnc.game.GameServer;

public class MainActivity extends Activity implements
        LoaderManager.LoaderCallbacks<List<String>> {

    public final String TAG = "Sandbox";

    // This is the Adapter being used to display the list's data.
    AppListAdapter mFirstAdapter;
    AppListAdapter mSecondAdapter;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;

    ListView mFirstListView;
    ListView mSecondListView;

    public static final String LOGIN_KEY = "LOGIN";
    public static final String PASSWORD_KEY = "PASSWORD";
    public static final String SHARED_PREFERENCES_KEY = "AUTH";
    public static final String LAST_HASH_KEY = "LAST_HASH";
    public static final String LAST_SERVER_ID_KEY = "SERVER_ID";

    public GameServer gs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Authentication
        SharedPreferences sharedPref = getSharedPreferences(
                SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

//        String lastHash = sharedPref.getString(LAST_HASH_KEY, new String());
//        int lastServerId = sharedPref.getInt(LAST_HASH_KEY, 0);

//        if (login.isEmpty() || password.isEmpty()) {
//            Log.v(TAG, "Asking for login and password");
        showAuthDialog();
//        } else {
//            Log.v(TAG, "Login: " + login);
//            Log.v(TAG, "Password: " + password);
//        }

//        // Server pick
//        showServerDialog();
//
//        // Create an empty adapter we will use to display the loaded data.
//        mFirstAdapter = new AppListAdapter(this);
//        mFirstListView = (ListView) findViewById(R.id.first_list);
//        mFirstListView.setAdapter(mFirstAdapter);
//
//        mSecondAdapter = new AppListAdapter(this);
//        mSecondListView = (ListView) findViewById(R.id.second_list);
//        mSecondListView.setAdapter(mSecondAdapter);
//
//        TextView textView = (TextView) findViewById(R.id.some_big_bad_text_view);
//        textView.setText("Big text view");
//
//        // Prepare the loader. Either re-connect with an existing one,
//        // or start a new one.
//        getLoaderManager().initLoader(0, null, this);
//        getLoaderManager().initLoader(1, null, this);
    }

    private void showAuthDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.auth_dialog, null);
        builder.setView(textEntryView);
        builder.setTitle("Enter login and password");
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        final EditText loginInput = (EditText) textEntryView.findViewById(R.id.login);
        final EditText passwordInput = (EditText) textEntryView.findViewById(R.id.password);

        final SharedPreferences sharedPref = getSharedPreferences(
                MainActivity.SHARED_PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        loginInput.setText(sharedPref.getString(LOGIN_KEY, ""));
        passwordInput.setText(sharedPref.getString(PASSWORD_KEY, ""));

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String login = loginInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                Toast.makeText(getApplicationContext(),
                        "Login: " + login + "\nPassword: " + password,
                        Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(MainActivity.LOGIN_KEY, login);
                editor.putString(MainActivity.PASSWORD_KEY, password);
                editor.commit();
                MainActivity.this.authorize();
            }
        });
        builder.show();
    }

    private void authorize() {
        final SharedPreferences sharedPref = getSharedPreferences(
                MainActivity.SHARED_PREFERENCES_KEY,
                Context.MODE_PRIVATE);

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Log.d(TAG, params[0] + " " + params[1]);
                return Authorizator.authorize(params[0], params[1]);
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d(TAG, "Result " + result);
                TextView tv = (TextView) findViewById(R.id.textView);
                tv.setText(result);
            }
        }.execute(sharedPref.getString(LOGIN_KEY, ""), sharedPref.getString(PASSWORD_KEY, ""));
    }

    private void showServerDialog() {
        // List items
//        cnc.updateHash("lworld10@mailinator.com", "qweqwe123");
//        cnc.openSession();
        ArrayList<String> serversList = new ArrayList<String>();
        serversList.add("America");
        serversList.add("Asia");
        serversList.add("Europe");
        serversList.add("Russia");
//        for(Server server:cnc.getServers()){
//            serversList.add(server.getName());
//        }

        final CharSequence[] names = serversList
                .toArray(new CharSequence[serversList.size()]);

        // Prepare the list dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pick server");

        // Set the list items and assign with the click listener
        builder.setItems(names, new DialogInterface.OnClickListener() {
            // Click listener
            public void onClick(DialogInterface dialog, int item) {
                String newServerName = names[item].toString();
                Log.v(TAG, "Setting server: " + newServerName);
                SharedPreferences sharedPref = getSharedPreferences(
                        SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(LAST_SERVER_ID_KEY, 1);
                editor.commit();
            }
        });
        AlertDialog alert = builder.create();
        // display dialog box
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created. This
        // sample only has one Loader with no arguments, so it is simple.
        return new AppListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        Log.v(TAG,
                "On load finished, in loader: "
                        + String.valueOf(loader.getId()));
        int id = loader.getId();

        if (id == 0) {
            mFirstAdapter.setData(data);
        } else if (id == 1) {
            List<String> modifiedList = new ArrayList<String>();
            for (String item : data) {
                modifiedList.add("Second: " + item);
            }
            mSecondAdapter.setData(modifiedList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        // Clear the data in the adapter.
        mFirstAdapter.setData(null);
        mSecondAdapter.setData(null);
    }

    public static class MyTestContentProvider implements Runnable {
        Thread myThread;
        AppListLoader mLoader;

        public final static String TAG = "MyTestContentProvider";
        private boolean stopped = false;

        public MyTestContentProvider(AppListLoader loader) {
            myThread = new Thread(this);
            myThread.start();
            mLoader = loader;
        }

        public void stopThread() {
            stopped = true;
        }

        public void run() {
            Random randomGenerator = new Random();
            int counter = 0;
            while (!stopped) {
                int randomInt = randomGenerator.nextInt(20);
                // Log.v(TAG, "Running, generated: " +
                // String.valueOf(randomInt));
                if (randomInt > 15) {
                    Log.v(TAG, "Content changed");
                    counter += 1;
                    if (counter >= 3) {
                        counter = 0;
                    }
                    mLoader.onContentChanged();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Log.e(TAG, ie.toString());
                    }
                }
            }
        }
    }

    /**
     * A custom Loader that loads all of the installed applications.
     */
    public static class AppListLoader extends AsyncTaskLoader<List<String>> {
        public static final String TAG = "AppListLoader";

        List<String> mStrings;
        List<String> changingStrings;

        private MyTestContentProvider mObserver;
        private static int counter = 0;

        public AppListLoader(Context context) {
            super(context);
        }

        /**
         * This is where the bulk of our work is done. This function is called
         * in a background thread and should generate a new set of data to be
         * published by the loader.
         */
        @Override
        public List<String> loadInBackground() {

            Log.v(TAG, "Load in back ground");

            // Create corresponding array of entries and load their labels.
            List<String> entries = new ArrayList<String>();
            for (int i = 0; i < counter; i++) {
                entries.add(String.valueOf(i));
            }
            counter += 1;
            // for (String item: entries) {
            // Log.v(TAG, "Item " + item);
            // }

            // Done!
            return entries;
        }

        /**
         * Called when there is new data to deliver to the client. The super
         * class will take care of delivering it; the implementation here just
         * adds a little more logic.
         */
        @Override
        public void deliverResult(List<String> apps) {
            if (isReset()) {
                // An async query came in while the loader is stopped. We
                // don't need the result.
                if (apps != null) {
                    onReleaseResources(apps);
                }
            }
            Log.v(TAG, "Delivering result");
            List<String> oldApps = apps;
            mStrings = apps;

            if (isStarted()) {
                // If the Loader is currently started, we can immediately
                // deliver its results.
                Log.v(TAG, "Started");
                super.deliverResult(apps);
            }

            // At this point we can release the resources associated with
            // 'oldApps' if needed; now that the new result is delivered we
            // know that it is no longer in use.
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override
        protected void onStartLoading() {
            if (mStrings != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(mStrings);
            }

            // Start watching for changes in the app data.
            if (mObserver == null) {
                mObserver = new MyTestContentProvider(this);
            }

            Log.v(TAG, "Has something changed?");

            if (takeContentChanged() || mStrings == null) {
                // If the data has changed since the last time it was loaded
                // or is not currently available, start a load.
                forceLoad();
            }
        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * Handles a request to cancel a load.
         */
        @Override
        public void onCanceled(List<String> apps) {
            super.onCanceled(apps);

            // At this point we can release the resources associated with 'apps'
            // if needed.
            onReleaseResources(apps);
        }

        /**
         * Handles a request to completely reset the Loader.
         */
        @Override
        protected void onReset() {
            super.onReset();

            // Ensure the loader is stopped
            onStopLoading();

            // At this point we can release the resources associated with 'apps'
            // if needed.
            if (mStrings != null) {
                onReleaseResources(mStrings);
                mStrings = null;
            }

            // Stop monitoring for changes.
            if (mObserver != null) {
                mObserver.stopThread();
            }
        }

        /**
         * Helper function to take care of releasing resources associated with
         * an actively loaded data set.
         */
        protected void onReleaseResources(List<String> apps) {
            // For a simple List<> there is nothing to do. For something
            // like a Cursor, we would close it here.
        }
    }

    public static class AppListAdapter extends ArrayAdapter<String> {
        private final LayoutInflater mInflater;

        public static final String TAG = "AppListAdapter";

        public AppListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<String> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        /**
         * Populate new items in the list.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_icon_text, parent,
                        false);
            } else {
                view = convertView;
            }

            String item = getItem(position);
            ((TextView) view.findViewById(R.id.text)).setText(item);
            return view;
        }
    }
}
