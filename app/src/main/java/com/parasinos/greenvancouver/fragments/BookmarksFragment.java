package com.parasinos.greenvancouver.fragments;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parasinos.greenvancouver.Bookmark;
import com.parasinos.greenvancouver.R;
import com.parasinos.greenvancouver.adapters.BookmarksAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment {
    private ListView lvBookmarks;
    private List<Bookmark> bookmarkList;
    private ArrayList<Bookmark> toDelete;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseBookmarks = FirebaseDatabase.getInstance().getReference("users");
    private FirebaseUser user = mAuth.getCurrentUser();
    private BookmarksAdapter adapter;
    private ActionMode actionMode = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        final TextView tvWarning = view.findViewById(R.id.bookmarks_warning);
        bookmarkList = new ArrayList<Bookmark>();
        lvBookmarks = view.findViewById(R.id.bookmarks_lv);
        lvBookmarks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvBookmarks.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                if (checked) {
                    toDelete.add(adapter.getItem(position));

                } else {
                    toDelete.remove(adapter.getItem(position));
                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.toolbar_cab, menu);
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        for (Bookmark i : toDelete) {
                            adapter.remove(i);
                            databaseBookmarks.child(user.getUid()).child("bookmarks").child(i.getMapid()).removeValue();
                        }
                        adapter.notifyDataSetChanged();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
            }
        });

        if (user != null) {
            tvWarning.setVisibility(View.GONE);

        } else {
            tvWarning.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        toDelete = new ArrayList<>();
        if (user != null) {

            databaseBookmarks.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bookmarkList.clear();
                    DataSnapshot userBookmark = dataSnapshot.child(user.getUid()).child("bookmarks");

                    for (DataSnapshot bookmarksSnapshot : userBookmark.getChildren()) {
                        if (!bookmarksSnapshot.getKey().equals("filler")) {
                            String name = bookmarksSnapshot.child("name").getValue().toString();
                            String address = bookmarksSnapshot.child("address").getValue().toString();
                            String mapid = bookmarksSnapshot.child("mapid").getValue().toString();
                            Bookmark bookmark = new Bookmark(address, name, mapid);
                            bookmarkList.add(bookmark);
                        }
                    }

                    if (!bookmarkList.isEmpty()) {
                        adapter = new BookmarksAdapter(getActivity(), bookmarkList);
                        lvBookmarks.setAdapter(adapter);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        //Destroy action mode
        if(actionMode != null)
            actionMode.finish();
    }
}