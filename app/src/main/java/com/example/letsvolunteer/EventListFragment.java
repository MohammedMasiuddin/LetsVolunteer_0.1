package com.example.letsvolunteer;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CompletionInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Events";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<EventsPost> eventsResults = new ArrayList<EventsPost>();
    ArrayList<EventsPost> filtereventsResults = new ArrayList<EventsPost>();
    ArrayList<String> categoriesinterest  = new ArrayList<String>();

    SearchView searchView;
    boolean tempflagfliterevents;
    DocumentSnapshot lastDocument;
    EventListsAdapter tempEventListsAdapter;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance(String param1, String param2) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);


        MaterialButton btn = view.findViewById(R.id.addEventsbtn);
        btn.setVisibility(View.INVISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.EventListrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.recycleviewrefresh);
        searchView = view.findViewById(R.id.search_bar_icon_event);
        TextInputLayout filterautotextlayout = view.findViewById(R.id.filterautotextlayout);
        RelativeLayout autotextcontnr = view.findViewById(R.id.autotextcontnr);

        LinearLayout filtereventslist = view.findViewById(R.id.filtereventslist);

//        filterView = view.findViewById(R.id.filtereventslist);
        ImageButton filtericon = view.findViewById(R.id.filterlisticon);
        AutoCompleteTextView filterAutoText = view.findViewById(R.id.filterlistautotext);
        ImageButton crossbuttonforautotext = view.findViewById(R.id.crossbuttonforautotext);

        TextView eventListTxt = view.findViewById(R.id.eventListTxt);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.loadingspinner);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query dbref = db.collection("Events").orderBy("eventName").limit(7);
        dbref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                eventsResults.clear();
                queryDocumentSnapshots.getDocuments().forEach(
                     e -> {
                         eventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(),e.getId()));
                         lastDocument = e;
                     }
                );

            EventListsAdapter eventListsAdapter = new EventListsAdapter(eventsResults) {
                @Override
                public void navigatetodetails(EventsPost eventsPost) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view_tag, EventDetailsFragment.newInstance(eventsPost.eventid))
                            .addToBackStack(null)
                            .commit();
                }

                @Override
                public void loadNewEventsfnc() {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Query dbref = db.collection("Events").orderBy("eventName").startAfter(lastDocument).limit(7);
                    dbref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.d(TAG, "onSuccess: " + queryDocumentSnapshots.getDocuments());
                            queryDocumentSnapshots.getDocuments().forEach(
                                    e -> {
                                        eventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(), e.getId()));
                                        lastDocument = e;
                                    }
                            );
                            notifyDataSetChanged();
                            if(queryDocumentSnapshots.getDocuments().size() == 0){
                                lastElement = true;
                            }
                        }
                    });
                }

            };
            recyclerView.setAdapter(eventListsAdapter);
            progressDialog.dismiss();
            Log.d(TAG, "onSuccess: "+ eventsResults);



            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("Organizers").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getData() != null){
                Log.d(TAG, "onCreateView: "+ documentSnapshot.getData());
                btn.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnSearchClickListener(v -> {
            Log.d(TAG, "onCreateView: in search icon clicked");
            eventListTxt.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            filtereventslist.setVisibility(View.GONE);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query dbref = db.collection("Events").orderBy("eventName").whereGreaterThanOrEqualTo("eventName", query).whereLessThanOrEqualTo("eventName", query+"\uf8ff");
                dbref.get().addOnCompleteListener(task -> {

                    Log.d(TAG, "onQueryTextSubmit: arraysize " + filtereventsResults.size() );
                    filtereventsResults = new ArrayList<EventsPost>();
                    Log.d(TAG, "onQueryTextSubmit: " + task.getResult().getDocuments().size());
                    task.getResult().getDocuments().forEach(
                            e -> {
                                filtereventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(),e.getId()));
                            });

                    tempflagfliterevents = ((EventListsAdapter) recyclerView.getAdapter()).lastElement;
                    Log.d(TAG, "onQueryTextSubmit: mainarray " + eventsResults.size() );
                    ((EventListsAdapter) recyclerView.getAdapter()).lastElement = true;
                    ((EventListsAdapter) recyclerView.getAdapter()).setEvents(filtereventsResults);
                    recyclerView.getAdapter().notifyDataSetChanged();

                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "onClose: ");
                btn.setVisibility(View.VISIBLE);
                eventListTxt.setVisibility(View.VISIBLE);
                filtereventslist.setVisibility(View.VISIBLE);
                ((EventListsAdapter) recyclerView.getAdapter()).lastElement = tempflagfliterevents;
                Log.d(TAG, "onClose: "+ eventsResults.size());
                ((EventListsAdapter) recyclerView.getAdapter()).setEvents(eventsResults);
                recyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });

        DocumentReference dbref1 = db.collection("EventCatorgy").document("CategoriesDoc");
        dbref1.get().addOnSuccessListener(documentSnapshot -> {
            categoriesinterest = (ArrayList<String>) documentSnapshot.getData().get("Categories");
            Log.d(TAG, "onCreateView:  fetch categories" + categoriesinterest);
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,categoriesinterest);
            filterAutoText.setAdapter(arrayAdapter);
        });

        filtericon.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: in filter icon clicked");
            eventListTxt.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
            filterautotextlayout.setVisibility(View.VISIBLE);
            autotextcontnr.setVisibility(View.VISIBLE);
            filterAutoText.showDropDown();
        });

        filterAutoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.toString().length() > 0)){
                    crossbuttonforautotext.setVisibility(View.VISIBLE);
                }else{
                    crossbuttonforautotext.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        crossbuttonforautotext.setOnClickListener(v -> {
            btn.setVisibility(View.VISIBLE);
            eventListTxt.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            filterautotextlayout.setVisibility(View.GONE);
            autotextcontnr.setVisibility(View.GONE);
            ((EventListsAdapter) recyclerView.getAdapter()).lastElement = tempflagfliterevents;
            Log.d(TAG, "onClose: "+ eventsResults.size() + " :: " + tempflagfliterevents);
            ((EventListsAdapter) recyclerView.getAdapter()).setEvents(eventsResults);
            recyclerView.getAdapter().notifyDataSetChanged();


        });

       filterAutoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Query dbref = db.collection("Events").orderBy("eventName").whereEqualTo("categoryinterest",filterAutoText.getText().toString());
               dbref.get().addOnCompleteListener(task -> {

                   Log.d(TAG, "onQueryTextSubmit: arraysize " + filtereventsResults.size() );
                   filtereventsResults = new ArrayList<EventsPost>();
                   Log.d(TAG, "onQueryTextSubmit: " + task.getResult().getDocuments().size());
                   task.getResult().getDocuments().forEach(
                           e -> {
                               filtereventsResults.add(new EventsPost((HashMap<String, Object>) e.getData(),e.getId()));
                           });

                   tempflagfliterevents = ((EventListsAdapter) recyclerView.getAdapter()).lastElement;
                   Log.d(TAG, "onQueryTextSubmit: mainarray " + eventsResults.size()+ " :: " + tempflagfliterevents  );
                   Log.d(TAG, "onItemClick: "+filtereventsResults.size());
                   ((EventListsAdapter) recyclerView.getAdapter()).lastElement = true;
                   ((EventListsAdapter) recyclerView.getAdapter()).setEvents(filtereventsResults);
                   recyclerView.getAdapter().notifyDataSetChanged();

               });
           }
       });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetActionBarTitle mainActivity = (SetActionBarTitle) getActivity();
                mainActivity.ChangeActionBarTitle("Create Event");
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
                        new BlankFragment()).addToBackStack(null).commit();            }
        });


        return view;

    }



}