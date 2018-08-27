package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.more;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.FragmentMoreBinding;
import com.example.cpu11398_local.etalk.presentation.view.profile.ProfileActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.content.MoreViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    @Inject
    @Named("MoreViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        WelcomeActivity.getAppComponent(getContext()).inject(this);
        FragmentMoreBinding fragmentMoreBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_more,
                container,
                false
        );
        fragmentMoreBinding.setViewModel((MoreViewModel)viewModel);
        return fragmentMoreBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.subscribeObserver(new MoreObserver());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        viewModel.endTask();
        super.onDestroy();
    }

    private class MoreObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.MORE_FRAGMENT_MY_PRORILE:
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
                    break;
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
