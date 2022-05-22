package com.mrq.paljobs.firebase;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mrq.paljobs.R;
import com.mrq.paljobs.helpers.NetworkHelper;

import java.util.ArrayList;
import java.util.Objects;

public class ApiRequest<T> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getData(
            Context context,
            String collection,
            Class<T> tClass,
            Results<ArrayList<T>> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            db.collection(collection)
                    .addSnapshotListener((query, error) -> {
                        if (Objects.requireNonNull(query).isEmpty()) {
                            result.onEmpty();
                        } else {
                            ArrayList<T> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : query) {
                                list.add(document.toObject(tClass));
                            }
                            result.onSuccess(list);
                        }
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void getData(
            Context context,
            String collection,
            String document,
            Class<T> tClass,
            Results<T> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            db.collection(collection)
                    .document(document)
                    .addSnapshotListener((query, error) -> {
                        if (Objects.requireNonNull(query).exists()) {
                            result.onEmpty();
                        } else {
                            result.onSuccess(query.toObject(tClass));
                        }
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void getData(
            Context context,
            String collection,
            String key,
            String value,
            Class<T> tClass,
            Results<ArrayList<T>> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            db.collection(collection)
                    .whereEqualTo(key, value)
                    .addSnapshotListener((query, error) -> {
                        if (Objects.requireNonNull(query).isEmpty()) {
                            result.onEmpty();
                        } else {
                            ArrayList<T> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : query) {
                                list.add(document.toObject(tClass));
                            }
                            result.onSuccess(list);
                        }
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void getData(
            Context context,
            String collection,
            String key1,
            String value1,
            String key2,
            String value2,
            Class<T> tClass,
            Results<ArrayList<T>> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            db.collection(collection)
                    .whereEqualTo(key1, value1)
                    .whereEqualTo(key2, value2)
                    .addSnapshotListener((query, error) -> {
                        if (Objects.requireNonNull(query).isEmpty()) {
                            result.onEmpty();
                        } else {
                            ArrayList<T> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : query) {
                                list.add(document.toObject(tClass));
                            }
                            result.onSuccess(list);
                        }
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void getDataOrderBy(
            Context context,
            String collection,
            String orderBy,
            Class<T> tClass,
            Results<ArrayList<T>> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            db.collection(collection)
                    .orderBy(orderBy)
                    .addSnapshotListener((query, error) -> {
                        if (Objects.requireNonNull(query).isEmpty()) {
                            result.onEmpty();
                        } else {
                            ArrayList<T> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : query) {
                                list.add(document.toObject(tClass));
                            }
                            result.onSuccess(list);
                        }
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }


}
