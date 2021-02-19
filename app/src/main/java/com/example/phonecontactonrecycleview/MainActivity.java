package com.example.phonecontactonrecycleview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    Toolbar tb;
    RecyclerView rv;
    ArrayList<Mybean> contact=new ArrayList<>();
    MyrecyclerviewAdapter ardp;

    String name,number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb=findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);
        rv=findViewById(R.id.main_recyclerview);

        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(),0));


        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getphone();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "permission not granted", Toast.LENGTH_LONG).show();
            }
        };
        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
             switch (direction)
             {
                 case ItemTouchHelper.LEFT :
                     Toast.makeText(getApplicationContext(),"you clickon call", Toast.LENGTH_LONG).show();
                     if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                     {
                         ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                     }
                     else  startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+contact.get(position).getNumber())));
                    ardp.notifyDataSetChanged();
                     break;

                 case ItemTouchHelper.RIGHT :
                     if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                     {
                         ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                     }
                     else
                     {
                         Toast.makeText(getApplicationContext(),"message sent", Toast.LENGTH_LONG).show();
                         SmsManager smsManager= SmsManager.getDefault();
                         ArrayList<String> mess=smsManager.divideMessage(contact.get(position).getName());//contact name set as message
                         smsManager.sendMultipartTextMessage(contact.get(position).getNumber(),null,mess,null,null);

                     }
                     ardp.notifyDataSetChanged();
                     break;
             }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(MainActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.ic_call_black_24dp)
                        .addSwipeLeftLabel("Call")
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                        .setSwipeLeftLabelColor(Color.parseColor("#ffffff"))
                        .setSwipeLeftActionIconTint(Color.parseColor("#000000"))

                        .addSwipeRightActionIcon(R.drawable.ic_message_black_24dp)
                        .addSwipeRightLabel("Message")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark))
                        .setSwipeRightLabelColor(Color.parseColor("#ffffff"))

                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(rv);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);
        MenuItem menuItem =  menu.findItem(R.id.search);

        SearchView searchView = (SearchView)menuItem.getActionView();
        SearchManager mngr=(SearchManager)getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(mngr.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(ardp!=null){
                    ardp.filter(s);
                }
                else {
                    ardp.filter(s);
                }
                return true;
            }
        });

        return  true;
    }

    public void getphone() {

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        HashSet<String> data=new HashSet<String>();

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (!data.contains(name)) {

                contact.add(new Mybean(name, number));
                data.add(name);

                Toast.makeText(getApplicationContext(), Integer.toString(contact.size()), Toast.LENGTH_LONG).show();
            }
        }
        cursor.close();
        ardp = new MyrecyclerviewAdapter(this,contact);
        rv.setAdapter(ardp);
        GridLayoutManager gd=new GridLayoutManager(this,1);
        rv.setLayoutManager(gd);

    }
}
