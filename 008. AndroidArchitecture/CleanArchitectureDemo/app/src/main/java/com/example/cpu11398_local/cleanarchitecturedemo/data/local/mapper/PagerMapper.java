package com.example.cpu11398_local.cleanarchitecturedemo.data.local.mapper;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.model.PaperModel;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import java.util.ArrayList;
import java.util.List;

public class PagerMapper {

    public PagerMapper() {
    }

    public User PagerModel2User(PaperModel data) {
        return new User(
                data.getKey(),
                data.getValue()
        );
    }

    public PaperModel User2PagerModel(User user) {
        return new PaperModel(
                user.getUsername(),
                user.getPassword()
        );
    }
}
