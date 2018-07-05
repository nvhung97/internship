package com.example.cpu11398_local.cleanarchitecturedemo.data.local.mapper;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.model.PaperModel;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import java.util.ArrayList;
import java.util.List;

public class PagerMapper {

    public PagerMapper() {
    }

    public List<User> PagerModel2User(List<PaperModel> listData) {
        List<User> listUser = new ArrayList<>();
        for (PaperModel paperModel:listData) {
            listUser.add(
                    new User(
                            paperModel.getKey(),
                            paperModel.getValue()
                    )
            );
        }
        return listUser;
    }

    public PaperModel User2PagerModel(User user) {
        return new PaperModel(
                user.getUsername(),
                user.getPassword()
        );
    }
}
