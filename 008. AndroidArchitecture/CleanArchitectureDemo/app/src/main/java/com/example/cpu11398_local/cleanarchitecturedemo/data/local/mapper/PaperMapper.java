package com.example.cpu11398_local.cleanarchitecturedemo.data.local.mapper;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.model.PaperModel;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;

public class PaperMapper {

    public PaperMapper() {
    }

    public User PagerModel2User(PaperModel paperModel) {
        return new User(
                paperModel.getKey(),
                paperModel.getValue()
        );
    }

    public PaperModel User2PagerModel(User user) {
        return new PaperModel(
                user.getUsername(),
                user.getPassword()
        );
    }
}
