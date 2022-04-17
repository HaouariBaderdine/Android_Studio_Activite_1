package com.e.activite_1;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.e.activite_1.contentprovider.MyClientContentProvider;
import com.e.activite_1.database.ClientTable;

public class ClientDetailActivity extends Activity {

    private Spinner mSexe;
    private EditText mNomText;
    private EditText mAdresseText;
    private EditText mEmailText;

    private Uri clientUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.client_edit);

        mSexe = (Spinner) findViewById(R.id.sexe);
        mNomText = (EditText) findViewById(R.id.client_edit_nom);
        mAdresseText = (EditText) findViewById(R.id.client_edit_adresse);
        mEmailText = (EditText) findViewById(R.id.client_edit_email);
        Button confirmButton = (Button) findViewById(R.id.client_edit_button);

        Bundle extras = getIntent().getExtras();

        // Check from the saved Instance
        clientUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyClientContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            clientUri = extras
                    .getParcelable(MyClientContentProvider.CONTENT_ITEM_TYPE);

            fillData(clientUri);
        }

        confirmButton.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mNomText.getText().toString())) {
                makeToast();
            } else {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void fillData(Uri uri) {
        String[] projection = { ClientTable.COLUMN_SEXE,ClientTable.COLUMN_NOM,
                ClientTable.COLUMN_ADRESSE, ClientTable.COLUMN_EMAIL };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String sexe = cursor.getString(cursor
                    .getColumnIndexOrThrow(ClientTable.COLUMN_SEXE));

            for (int i = 0; i < mSexe.getCount(); i++) {

                String s = (String) mSexe.getItemAtPosition(i);
                if (s.equalsIgnoreCase(sexe)) {
                    mSexe.setSelection(i);
                }
            }

            mNomText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ClientTable.COLUMN_NOM)));
            mAdresseText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ClientTable.COLUMN_ADRESSE)));
            mEmailText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(ClientTable.COLUMN_EMAIL)));

            // Always close the cursor
            cursor.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyClientContentProvider.CONTENT_ITEM_TYPE, clientUri);
    }

    private void saveState() {
        String sexe = (String) mSexe.getSelectedItem();
        String nom = mNomText.getText().toString();
        String adresse = mAdresseText.getText().toString();
        String email = mEmailText.getText().toString();

        // Only save if either marque or model
        // is available

        if (nom.length() == 0 && adresse.length() == 0 && email.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ClientTable.COLUMN_SEXE, sexe);
        values.put(ClientTable.COLUMN_NOM, nom);
        values.put(ClientTable.COLUMN_ADRESSE, adresse);
        values.put(ClientTable.COLUMN_EMAIL, email);

        if (clientUri == null) {
            // New Client
            clientUri = getContentResolver().insert(MyClientContentProvider.CONTENT_URI, values);
        } else {
            // Update Client
            getContentResolver().update(clientUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(ClientDetailActivity.this, "Indiquer le nom de client SVP",
                Toast.LENGTH_LONG).show();
    }

}
