package com.example.cpu11398_local.cleanarchitecturedemo.data.local.paper;

import android.content.Context;
import com.example.cpu11398_local.cleanarchitecturedemo.data.helper.Optional;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.paper.mapper.PaperMapper;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.paper.model.PaperUser;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import javax.inject.Inject;
import io.paperdb.Paper;
import io.reactivex.Completable;
import io.reactivex.Single;

public class PaperDB implements LocalSource {

    @Inject
    public PaperDB(Context context) {
        Paper.init(context);
    }

    @Override
    public Single<Optional<User>> getUser(String username) {
        if (Paper.book().contains(username)) {
            return Single.just(Optional.of(
                    PaperMapper.PagerUser2User(
                            new PaperUser(
                                    username,
                                    Paper.book().read(username)
                            )
                    )
            ));
        }
        return Single.just(Optional.empty());
    }

    @Override
    public Completable putUser(User user) {
        PaperUser paperUser = PaperMapper.User2PagerUser(user);
        if (!Paper.book().contains(paperUser.getUsername())) {
            Paper.book().write(
                    paperUser.getUsername(),
                    paperUser.getPassword()
            );
            return Completable.complete();
        }
        return Completable.error(new Throwable("Username already existed!"));
    }
}
