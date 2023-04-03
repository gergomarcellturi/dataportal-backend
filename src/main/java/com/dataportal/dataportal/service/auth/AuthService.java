package com.dataportal.dataportal.service.auth;

import com.dataportal.dataportal.exception.ApplicationException;
import com.dataportal.dataportal.model.common.Response;
import com.dataportal.dataportal.model.user.FirestoreUser;
import com.dataportal.dataportal.repository.user.UserRepository;
import com.dataportal.dataportal.service.base.BaseService;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService extends BaseService {

//    CollectionReference collection = db.collection("users");

    @Autowired
    private UserRepository userRepository;

    public Response<String> login() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collection = firestore.collection("users");
        DocumentSnapshot document = collection.document(getAuthToken().getUid()).get().get();

        ApiFuture<DocumentSnapshot> future = collection.document(getAuthToken().getUid()).get();
        HashMap<String, Object> data = new HashMap<>();
        data.put("lastOnline", Timestamp.now());
        data.put("loggedInStatus", FirestoreUser.ONLINE);
        DocumentSnapshot snap = future.get();
        if (snap.exists()) {
            snap.getReference().update(data);
        } else {
            snap.getReference().create(data);
        }

        return Response.ok();
    }

    public Response<String> logout() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference collection = firestore.collection("users");
        DocumentSnapshot document = collection.document(getAuthToken().getUid()).get().get();
        ApiFuture<DocumentSnapshot> future = collection.document(getAuthToken().getUid()).get();
        HashMap<String, Object> data = new HashMap<>();
        data.put("lastOnline", Timestamp.now());
        data.put("loggedInStatus", FirestoreUser.OFFLINE);
        DocumentSnapshot snap = future.get();
        if (snap.exists()) {
            snap.getReference().update(data);
        } else {
            snap.getReference().create(data);
        }

        return Response.ok();
    }

}
