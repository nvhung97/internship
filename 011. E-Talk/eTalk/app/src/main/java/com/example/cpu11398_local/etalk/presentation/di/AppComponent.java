package com.example.cpu11398_local.etalk.presentation.di;

import com.example.cpu11398_local.etalk.presentation.view.chat.person.ChatPersonActivity;
import com.example.cpu11398_local.etalk.presentation.view.content.ContentActivity;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts.ContactsFragment;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.groups.GroupsFragment;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages.MessagesFragment;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.more.MoreFragment;
import com.example.cpu11398_local.etalk.presentation.view.friend.AddFriendActivity;
import com.example.cpu11398_local.etalk.presentation.view.group.CreateGroupActivity;
import com.example.cpu11398_local.etalk.presentation.view.login.LoginActivity;
import com.example.cpu11398_local.etalk.presentation.view.profile.ProfileActivity;
import com.example.cpu11398_local.etalk.presentation.view.register.RegisterActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(WelcomeActivity welcomeActivity);

    void inject(RegisterActivity registerActivity);

    void inject(LoginActivity loginActivity);

    void inject(ContentActivity contentActivity);
    void inject(MessagesFragment messagesFragment);
    void inject(ContactsFragment contactsFragment);
    void inject(GroupsFragment groupsFragment);
    void inject(MoreFragment moreFragment);

    void inject(ProfileActivity profileActivity);

    void inject(ChatPersonActivity chatPersonActivity);

    void inject(AddFriendActivity addFriendActivity);

    void inject(CreateGroupActivity createGroupActivity);
}
