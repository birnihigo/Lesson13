package com.solomon.sqlitesample;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends Activity
{
    BookAdapter mBookAdapter;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();

    EditText searchText;

    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";

    private MyDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        searchText = new EditText(this);
        searchText.setWidth(200);

        Button searchButton = new Button(this);
        searchButton.setText("Search");
        searchButton.setOnClickListener(searchButtonListener);

        mainListView = new ListView(this);

        mDbHelper = new MyDBHelper(this);

        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                mNameList);

        mainListView.setAdapter(mArrayAdapter);

        mBookAdapter = new BookAdapter(this, getLayoutInflater());

        mainListView.setAdapter(mBookAdapter);

        layout.addView(searchText);
        layout.addView(searchButton);
        layout.addView(mainListView);

        setContentView(layout);


    }

    View.OnClickListener searchButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            doQuery(searchText.getText().toString());
        }
    };



    private void doQuery(String searchString)
    {

        String urlString = "";

        try
        {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();

        // AsyncHttpClient.get(String url, ResponseHandlerInterface responseHandler)
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        saveBooksToDb(jsonObject);
                        mBookAdapter.updateData(fetchBooks());

                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error)
                    {
                        Log.e("Query Failure", statusCode + " " + throwable.getMessage());
                    }
                });
    }
    private void saveBooksToDb(JSONObject jsonObject){
        JSONArray array = jsonObject.optJSONArray("docs");
        try {
            for(int i = 0; i < array.length(); ++i){
                JSONObject object = array.getJSONObject(i);
                mDbHelper.addBook(object.optString("cover_i"),
                        object.optString("title"),
                        object.optJSONArray("author_name").optString(0));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private ArrayList<Book> fetchBooks(){
        ArrayList<Book> list = new ArrayList<Book>();
        Cursor cursor = mDbHelper.getBooks();
        Book book;
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String author = cursor.getString(2);
            book = new Book(id, title, author);
            list.add(book);

            cursor.moveToNext();
        }
        //fetch books from DB and return it
        return list;
    }
}