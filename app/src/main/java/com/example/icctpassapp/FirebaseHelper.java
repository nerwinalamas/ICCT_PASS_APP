package com.example.icctpassapp;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;

public class FirebaseHelper {

    DatabaseReference reference;
    Boolean saved = null;
    ArrayList<Classrooms> classrooms = new ArrayList<>();

    public FirebaseHelper(DatabaseReference reference) {
        this.reference = reference;
    }

    //SAVE
    public Boolean save(Classrooms classrooms) {
        if (classrooms == null) {
            saved = false;
        } else {
            try {
                reference.child("Classrooms").push().setValue(classrooms);
                saved = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }

    //READ
    public ArrayList<Classrooms> retrieve() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return classrooms;
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        classrooms.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            /*
            * String className = ds.getValue(Classrooms.class).getClassName();
            String subjectCode = ds.getValue(Classrooms.class).getSubjectCode();
            String section = ds.getValue(Classrooms.class).getSection();

            classrooms.add(className);
            classrooms.add(subjectCode);
            classrooms.add(section);
            * */

            Classrooms classroom = ds.getValue(Classrooms.class);
            classrooms.add(classroom);
        }
    }
}