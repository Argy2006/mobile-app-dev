package com.example.myapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class For_context extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<Contact> adapter;
    ArrayList<Contact> contactList;

    public static class Contact {
        private final String name;
        private final String email;
        private final String phone;

        public Contact(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.context_menu);

        listView = findViewById(R.id.listView);
        contactList = new ArrayList<>();
        contactList.add(new Contact("Aryan", "aryan@gmail.com", "6367978267"));
        contactList.add(new Contact("Krish", "krish@gmail.com", "9876543210"));
        contactList.add(new Contact("Abhay", "abhay@gmail.com", "9676736862"));
        contactList.add(new Contact("Tarun", "tarun@gmail.com", "7427746192"));
        contactList.add(new Contact("Yuvraj", "yuvraj@gmail.com", "3864276284"));

        // Use simple_list_item_2 layout to show Name on line 1 and Email on line 2
        adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_2, android.R.id.text1, contactList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Contact contact = getItem(position);
                if (contact != null) {
                    text1.setText(contact.getName());
                    text1.setTextSize(18);
                    text1.setTypeface(null, Typeface.BOLD);
                    text2.setText(contact.getEmail());
                    text2.setTextSize(14);
                }
                return view;
            }
        };

        listView.setAdapter(adapter);

        // Register the ListView for Context Menu on long press
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contact_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if (info != null && info.position < contactList.size()) {
            menu.setHeaderTitle(contactList.get(info.position).getName());
        } else {
            menu.setHeaderTitle("Select Action");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null || info.position >= contactList.size()) {
            return super.onContextItemSelected(item);
        }

        Contact selectedContact = contactList.get(info.position);
        int itemId = item.getItemId();

        if (itemId == R.id.action_call) {
            Toast.makeText(this, "Calling " + selectedContact.getName() + " (" + selectedContact.getPhone() + ")", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_email) {
            Toast.makeText(this, "Emailing " + selectedContact.getName() + " (" + selectedContact.getEmail() + ")", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_edit) {
            Toast.makeText(this, "Editing " + selectedContact.getName(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_delete) {
            Toast.makeText(this, "Deleted " + selectedContact.getName(), Toast.LENGTH_SHORT).show();
            contactList.remove(info.position);
            adapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}
