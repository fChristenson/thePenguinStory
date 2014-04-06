package se.fidde.thepenguinstory.activity;

import se.fidde.thepenguinstory.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectContactActivity extends Activity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver
                .query(android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null, null, null, null);

        setAdapter();
        addResponseDataToAdapter(cursor);
        setOnItemClickListener();
    }

    private void addResponseDataToAdapter(Cursor cursor) {
        while (cursor.moveToNext()) {
            int id = cursor
                    .getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Email.DATA);
            String emailString = cursor.getString(id);

            if (emailString != null)
                arrayAdapter.add(emailString);
        }
    }

    private void setAdapter() {
        listView = (ListView) findViewById(R.id.contactList);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        listView.setAdapter(arrayAdapter);
    }

    private void setOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                String itemString = (String) parent.getItemAtPosition(position);
                Log.d("contactList", itemString + " was clicked!");

                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { itemString });

                intent.putExtra(Intent.EXTRA_SUBJECT,
                        "Hey have you seen this new app?");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Checkout The Penguin Story!");

                startActivity(Intent.createChooser(intent, "Send email..."));
                finish();
            }
        });
    }
}