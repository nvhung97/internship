package com.example.cpu11398_local.cleanarchitecturedemo.data.local.paper.mapper;

import com.example.cpu11398_local.cleanarchitecturedemo.data.local.paper.model.PaperUser;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;

public class PaperMapper {

    public static User PagerUser2User(PaperUser paperUser) {
        return new User(
                paperUser.getUsername(),
                paperUser.getPassword()
        );
    }

    public static PaperUser User2PagerUser(User user) {
        return new PaperUser(
                user.getUsername(),
                user.getPassword()
        );
    }
}
