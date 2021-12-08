package com.example.letsvolunteer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letsvolunteer.notifications.APIService;
import com.example.letsvolunteer.notifications.Client;
import com.example.letsvolunteer.notifications.Data;
import com.example.letsvolunteer.notifications.Response;
import com.example.letsvolunteer.notifications.Sender;
import com.example.letsvolunteer.notifications.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;

    String KEY_ON = "organizationName";
    String KEY_photoUri = "photoUri";

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;

    // for checking if meesage was seen
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;

    APIService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        FirebaseUser user = firebaseAuth.getCurrentUser();

        //init views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.chat_recyclerView);
        profileIv = findViewById(R.id.chatProfileIv);
        nameTv = findViewById(R.id.chatNameTv);
        userStatusTv = findViewById(R.id.userStatusTv);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);



        //Layout for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        //api
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Users");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        myUid = firebaseAuth.getUid();
        DocumentReference documentReference = db.collection("Organizers").document(hisUid);

        Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUid);
        //get picture and name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("photoUri").getValue();
                    String typingStatus = "" + ds.child("typingTo").getValue();

                    if(typingStatus.equals(myUid)) {
                        userStatusTv.setText("typing...");
                    } else {
                        String onlineStatus = "" + ds.child("onlineStatus").getValue();

                        if (onlineStatus.equals("online")) {
                            userStatusTv.setText(onlineStatus);
                        } else {
                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                            calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                            String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                            userStatusTv.setText("Last seen at: "+ dateTime);
                        }
                    }



                    nameTv.setText(name);
                    try {
                        Picasso.get().load(hisImage).into(profileIv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_img).into(profileIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
//                                @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.d(TAG, "onEvent: Something went wrong");
//                }
//
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    String organizationName = documentSnapshot.getString(KEY_ON);
//                    hisImage = documentSnapshot.getString(KEY_photoUri);
//                    String typingStatus = documentSnapshot.getString("typingTo");
//
//                    assert typingStatus != null;
//                    if(typingStatus.equals(myUid)) {
//                        userStatusTv.setText("typing...");
//                    }
//                    else {
//                        String onlineStatus = documentSnapshot.getString("onlineStatus");
//                        assert onlineStatus != null;
//                        if(onlineStatus.equals("online")) {
//                            userStatusTv.setText(onlineStatus);
//                        }
//                        else {
//                            // convert timestamp to format
//                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
//                            calendar.setTimeInMillis(Long.parseLong(onlineStatus));
//                            String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
//                            userStatusTv.setText("Last seen at: "+ dateTime);
//                        }
//                    }
//
//
//
//                    nameTv.setText(organizationName);
//
//                    try {
//                        Picasso.get().load(hisImage).into(profileIv);
//                    } catch (Exception e) {
//                        Picasso.get().load(R.drawable.ic_default_img).into(profileIv);
//                    }
//                }
//
//            }
//        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify = true;

                // get the editText box message
                String message = messageEt.getText().toString().trim();
                // check for empty ET
                if(TextUtils.isEmpty(message)) {
                    //empty text
                    Toast.makeText(ChatActivity.this, "Cannot send empty message...", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }
                messageEt.setText("");
            }

        });

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0) {
                    checkTypingStatus("noOne");
                }
                else {
                    checkTypingStatus(hisUid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readMessages();
        
        seenMessages();
    }

    private void seenMessages() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)) {
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String KEY_sender = "sender";
    private String KEY_timestamp = "timestamp";

    EventListener<QuerySnapshot> eventListener = (value, error) -> {
      if(error != null) {
          return;
      }
      if(value != null) {
          int count = chatList.size();
          for(DocumentChange documentChange : value.getDocumentChanges()) {
              if(documentChange.getType() == DocumentChange.Type.ADDED) {
                  ModelChat modelChat = new ModelChat();
                  modelChat.sender = documentChange.getDocument().getString(KEY_sender);
                  modelChat.receiver = documentChange.getDocument().getString("receiver");
                  modelChat.message = documentChange.getDocument().getString("message");
                  modelChat.timestamp = documentChange.getDocument().getString(KEY_timestamp);
                  modelChat.isSeen = documentChange.getDocument().getBoolean("isSeen");
                  chatList.add(modelChat);
              }

          }
          Collections.sort(chatList, (obj1, obj2) -> obj1.timestamp.compareTo(obj2.timestamp));
          if (count == 0) {
              adapterChat = new AdapterChat(ChatActivity.this, chatList, hisImage);
              adapterChat.notifyDataSetChanged();
              recyclerView.setAdapter(adapterChat);
          } else {
              adapterChat.notifyItemRangeInserted(chatList.size(), chatList.size());
              recyclerView.smoothScrollToPosition(chatList.size() - 1);
          }
          recyclerView.setVisibility(View.VISIBLE);
      }
    };

    private void readMessages() {
        chatList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        myUid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds:  snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                        chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList, hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        ListenerRegistration collectionReference = db.collection("Chats")
//                .whereEqualTo("sender", myUid)
//                .whereEqualTo("receiver", hisUid)
//                .addSnapshotListener(eventListener);
//        ListenerRegistration collectionReference1 = db.collection("Chats")
//                .whereEqualTo("sender", hisUid)
//                .whereEqualTo("receiver", myUid)
//                .addSnapshotListener(eventListener);


//        DocumentReference documentReference = db.collection("Chats").document(myUid + " " + hisUid);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
//                                @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.d(TAG, "onEvent: Something went wrong");
//                }
//
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    ModelChat chat = documentSnapshot.get();
//                }
//
//            }
//        });


    }

    private void sendMessage(String message) {
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Chats");
        myUid = user.getUid();
        DocumentReference documentReference = db.collection("Volunteer").document(myUid);

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> chatData = new HashMap<>();
        chatData.put("sender", myUid);
        chatData.put("receiver", hisUid);
        chatData.put("message", message);
        chatData.put("timestamp", timestamp);
        chatData.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(chatData);



        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: Something went wrong");
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String age = documentSnapshot.getString("age");
                    String image = documentSnapshot.getString("photoUri");

                    ModelVolunteer modelVolunteer = new ModelVolunteer();
                    if (notify) {
                        sendNotification(hisUid, modelVolunteer.firstName, message);
                    }
                    notify = false;
                }
            }
        });


//        collectionReference.document().set(chatData).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d(TAG, "onSuccess chat added");
//                messageEt.setText("");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure " + e.getMessage());
//            }
//        });

        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(myUid)
                .child(hisUid);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    chatRef1.child("id").setValue(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(hisUid)
                .child(myUid);
        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    chatRef2.child("id").setValue(myUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(String hisUid, String firstName, String message) {

        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid, firstName + "" + message, "New Message", hisUid, R.drawable.ic_default_img );
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ChatActivity.this, ""+ response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            myUid = user.getUid();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void checkOnlineStatus(String status) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);

        dbref.updateChildren(hashMap);

//        DocumentReference documentReference = db.collection("Volunteer").document(user.getUid());
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
//                                @Nullable FirebaseFirestoreException error) {
//
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    HashMap<String, Object> data = new HashMap<>();
//                    data.put("onlineStatus", status);
//                    // update the online status value
//
//                    documentReference.update(data);
//                }
//                else {
//                    DocumentReference  odocumentReference = db.collection("Organizers").document(user.getUid());
//
//                    HashMap<String, Object> data = new HashMap<>();
//                    data.put("onlineStatus", status);
//                    // update the online status value
//
//                    odocumentReference.update(data);
//                }
//            }
//        });
    }

    private void checkTypingStatus(String typing) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typingTo", typing);

        dbref.updateChildren(hashMap);

//        DocumentReference documentReference = db.collection("Volunteer").document(user.getUid());
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
//                                @Nullable FirebaseFirestoreException error) {
//
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    HashMap<String, Object> data = new HashMap<>();
//                    data.put("typingTo", typing);
//                    // update the online status value
//
//                    documentReference.update(data);
//                }
//                else {
//                    DocumentReference  odocumentReference = db.collection("Organizers").document(user.getUid());
//
//                    HashMap<String, Object> data = new HashMap<>();
//                    data.put("typingTo", typing);
//                    // update the online status value
//
//                    odocumentReference.update(data);
//                }
//            }
//        });
    }


    @Override
    protected void onStart() {
        checkUserStatus();

        // set online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        userRefForSeen.removeEventListener(seenListener) ;

        String timestamp = String.valueOf(System.currentTimeMillis());
        // set to last seen
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
    }

    @Override
    protected void onResume() {
        // set online
        checkOnlineStatus("online");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }
}