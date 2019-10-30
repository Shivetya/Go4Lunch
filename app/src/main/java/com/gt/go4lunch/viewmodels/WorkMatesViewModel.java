package com.gt.go4lunch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.gt.go4lunch.models.User;
import com.gt.go4lunch.usecases.UsersFirestoreUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkMatesViewModel extends ViewModel {

    private UsersFirestoreUseCase mUsersUseCase;
    private List<User> mListUsers = new ArrayList<>();

    private MutableLiveData<List<User>> _mListUsersLiveData = new MutableLiveData<>();
    public LiveData<List<User>> mListUsersLiveData = _mListUsersLiveData;

    public WorkMatesViewModel(UsersFirestoreUseCase usersUseCase){
        mUsersUseCase = usersUseCase;
    }

    public void getListWorkMates(){

        List<DocumentSnapshot> listDocuments = Objects.requireNonNull(mUsersUseCase.getAllUsers().getResult()).getDocuments();

        for(DocumentSnapshot doc : listDocuments){
            mListUsers.add(doc.toObject(User.class));
        }

        _mListUsersLiveData.postValue(mListUsers);
    }
}
