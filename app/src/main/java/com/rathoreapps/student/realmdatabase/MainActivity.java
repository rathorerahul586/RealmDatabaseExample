package com.rathoreapps.student.realmdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.exceptions.RealmError;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private Realm mRealm;
    TextView name, rollno;
    EditText editName, editRollno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(getApplicationContext());
        name = findViewById(R.id.name);
        rollno = findViewById(R.id.rollno);
        editName = findViewById(R.id.name_et);
        editRollno = findViewById(R.id.rollno_et);
        mRealm = Realm.getDefaultInstance();
        readRealmData();

    }

    public void addItems(View view){
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        RealmListModel realmListModel = new RealmListModel();

                        if (!editName.getText().toString().isEmpty() && !editRollno.getText().toString().isEmpty()){
                            realmListModel.setName(editName.getText().toString());
                            realmListModel.setRollNo(editRollno.getText().toString());
                            realm.copyToRealm(realmListModel);
                            editName.setText("");
                            editRollno.setText("");
                        }else Toast.makeText(MainActivity.this, "Both Field are Mandatory", Toast.LENGTH_SHORT).show();

                    }catch (RealmError error){
                        Log.d(TAG, "execute: error: "+error.getMessage());
                    }

                }
            });
        }
        finally {
            if (realm!=null){
                realm.close();
            }
            readRealmData();
        }
    }

    private void readRealmData(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmListModel> realmResults = realm.where(RealmListModel.class).findAll();
                name.setText("");
                rollno.setText("");
                for (RealmListModel realmListModel : realmResults) {
                    name.append(realmListModel.name);
                    rollno.append(realmListModel.rollNo);
                }
                Log.d(TAG, "execute:List Size "+realmResults.size());
            }
        });
    }

    public void updateList(View view){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (editName.getText().toString().isEmpty() && editRollno.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Both fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else {
                    RealmListModel realmListModel = realm.where(RealmListModel.class).equalTo(RealmListModel.PROPERTY_NAME, editName.getText().toString()).findFirst();

                    if (realmListModel == null){
                        Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        realmListModel.name = editName.getText().toString();
                        realmListModel.rollNo = editRollno.getText().toString();
                        editName.setText("");
                        editRollno.setText("");
                    }
                }
            }
        });
        readRealmData();
    }

    public void deleteItem(View view){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmListModel employee = realm.where(RealmListModel.class).equalTo(RealmListModel.PROPERTY_NAME, editName.getText().toString()).findFirst();
                if (employee != null) {
                    employee.deleteFromRealm();
                    editName.setText("");
                    editRollno.setText("");
                }else Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
            }
        });
        readRealmData();
    }
}