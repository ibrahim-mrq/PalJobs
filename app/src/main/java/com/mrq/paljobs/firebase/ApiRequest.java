package com.mrq.paljobs.firebase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mrq.paljobs.R;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.helpers.NetworkHelper;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.mrq.paljobs.models.Submit;
import com.mrq.paljobs.models.User;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ApiRequest<T> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public void login(
            Activity context,
            String email,
            String password,
            Results<User> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            db.collection("User")
                                    .document(Objects.requireNonNull(auth.getUid()))
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        User user = snapshot.toObject(User.class);
                                        result.onSuccess(user);
                                        Hawk.put(Constants.IS_LOGIN, true);
                                        Hawk.put(Constants.USER, user);
                                        Hawk.put(Constants.USER_TOKEN, auth.getUid());
                                        Hawk.put(Constants.USER_TYPE, Objects.requireNonNull(user).getUserType());
                                        result.onLoading(false);
                                    })
                                    .addOnFailureListener(e -> {
                                        result.onException(context.getString(R.string.error));
                                        result.onLoading(false);
                                    });
                        } else {
                            result.onLoading(false);
                            result.onException(context.getString(R.string.con_not_login));
                        }
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void register(
            Activity context,
            User user,
            Results<String> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.setId(auth.getUid());
                            db.collection("User")
                                    .document(Objects.requireNonNull(auth.getUid()))
                                    .set(user)
                                    .addOnSuccessListener(unused -> {
                                        result.onSuccess(context.getString(R.string.account_successfully));
                                        Hawk.put(Constants.IS_LOGIN, true);
                                        Hawk.put(Constants.USER, user);
                                        Hawk.put(Constants.USER_TOKEN, auth.getUid());
                                        Hawk.put(Constants.USER_TYPE, user.getUserType());
                                        result.onLoading(false);
                                    })
                                    .addOnFailureListener(e -> {
                                        result.onLoading(false);
                                        result.onException(context.getString(R.string.error));
                                    });
                        } else {
                            result.onLoading(false);
                            result.onException(context.getString(R.string.con_not_create_account));
                        }
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void uploadImage(
            Context context,
            Uri imagePath,
            String fileName,
            String photoType,
            Results<String> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            StorageReference reference = FirebaseStorage.getInstance().getReference()
                    .child("images/" + UUID.randomUUID().toString() + fileName);
            reference.putFile(imagePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        result.onLoading(false);
                        reference.getDownloadUrl().addOnSuccessListener(uri -> {
                            result.onSuccess(uri.toString());
                            DocumentReference docRef = db.collection("User")
                                    .document(Hawk.get(Constants.USER_TOKEN));
                            docRef.update(photoType, uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                        result.onLoading(false);
                        result.onException(context.getString(R.string.error_upload_image));
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void uploadFile(
            Context context,
            Uri filePath,
            String fileName,
            String type,
            Results<String> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            StorageReference reference = FirebaseStorage.getInstance().getReference()
                    .child("files/" + UUID.randomUUID().toString() + fileName);
            reference.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        result.onLoading(false);
                        reference.getDownloadUrl().addOnSuccessListener(uri -> {
                            result.onSuccess(uri.toString());
                            if (type.equals("update")) {
                                DocumentReference docRef = db.collection("User")
                                        .document(Hawk.get(Constants.USER_TOKEN));
                                docRef.update("cv", uri.toString());
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        result.onLoading(false);
                        result.onException(context.getString(R.string.error_upload_file));
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void getData(
            Context context,
            String collection,
            Class<T> tClass,
            Results<ArrayList<T>> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
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
                        result.onLoading(false);
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
            result.onLoading(true);
            DocumentReference docRef = db.collection(collection).document(document);
            docRef.addSnapshotListener((query, error) -> {
                if (!Objects.requireNonNull(query).exists()) {
                    result.onEmpty();
                } else {
                    result.onSuccess(query.toObject(tClass));
                }
                result.onLoading(false);
            });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void getData(
            Activity context,
            String collection,
            String key,
            String value,
            Class<T> tClass,
            Results<ArrayList<T>> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
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
                        result.onLoading(false);
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
            result.onLoading(true);
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
                        result.onLoading(false);
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void getDataOrderBy(
            Context context,
            String collection,
            String orderBy,
            Query.Direction direction,
            Class<T> tClass,
            Results<ArrayList<T>> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            db.collection(collection)
                    .orderBy(orderBy, direction)
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
                        result.onLoading(false);
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void addFavorite(
            Context context,
            Favorite favorite,
            Results<String> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            db.collection("Favorite")
                    .add(favorite)
                    .addOnSuccessListener(document -> {
                        document.update("id", document.getId());
                        result.onSuccess(context.getString(R.string.add_favorite_success));
                        result.onLoading(false);
                    })
                    .addOnFailureListener(error -> {
                        result.onLoading(false);
                        result.onException(context.getString(R.string.error));
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }

    public void removeFavorite(
            Context context,
            String id,
            Results<String> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            db.collection("Favorite").document(id).delete().addOnSuccessListener(runnable -> {
                result.onLoading(false);
                result.onSuccess(context.getString(R.string.remove_favorite_success));
            }).addOnFailureListener(error -> {
                result.onLoading(false);
                result.onException(context.getString(R.string.error));
            });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }


    public void addToSubmit(
            Context context,
            Submit submit,
            Results<String> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            db.collection("Submit")
                    .add(submit)
                    .addOnSuccessListener(document -> {
                        document.update("id", document.getId());
                        result.onSuccess(context.getString(R.string.add_favorite_success));
                        result.onLoading(false);
                    })
                    .addOnFailureListener(error -> {
                        result.onLoading(false);
                        result.onException(context.getString(R.string.error));
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }


    public void addToProposal(
            Context context,
            Proposal proposal,
            Results<String> result
    ) {
        if (NetworkHelper.INSTANCE.isNetworkOnline(context)) {
            result.onLoading(true);
            db.collection("Proposal")
                    .add(proposal)
                    .addOnSuccessListener(document -> {
                        document.update("id", document.getId());
                        result.onSuccess(context.getString(R.string.add_favorite_success));
                        result.onLoading(false);
                    })
                    .addOnFailureListener(error -> {
                        result.onLoading(false);
                        result.onException(context.getString(R.string.error));
                    });
        } else {
            result.onFailureInternet(context.getString(R.string.no_internet));
        }
    }
}
