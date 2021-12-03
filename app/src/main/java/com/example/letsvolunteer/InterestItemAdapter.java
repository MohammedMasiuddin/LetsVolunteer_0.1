package com.example.letsvolunteer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class InterestItemAdapter extends ArrayAdapter {
    private static final String TAG = "interitemadapter";
    int resource;
    ArrayList<String> arrayList = new ArrayList();

    public InterestItemAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
        arrayList = objects;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        MaterialTextView textView = view.findViewById(R.id.interestlistitem);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        textView.setText(arrayList.get(position));
        ImageButton imageView = view.findViewById(R.id.interestlistitemcross);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(" interest item cross ", "onClick: ");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Do you want to remove '" +  textView.getText().toString().toUpperCase() +"' from the interests ,Are you sure? ");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        db.collection("Volunteer").document(user.getUid())
                                .update("MyInterestCategories", FieldValue.arrayRemove(textView.getText().toString()));
                        arrayList.remove(position);
                        notifyDataSetChanged();
                        Log.d(TAG, "onClick: yes button");
                        dialog.dismiss();
                        Toast.makeText(getContext().getApplicationContext(), textView.getText().toString().toUpperCase() + " removed from the interest categories ",Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: on click no button");
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                }
        });

        return view;
    }
}
