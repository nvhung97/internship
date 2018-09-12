package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.groups;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.FragmentGroupsBinding;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.content.GroupViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    @Inject
    @Named("GroupViewModel")
    public ViewModel viewModel;

    private Disposable disposable;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        WelcomeActivity.getAppComponent(getContext()).inject(this);
        FragmentGroupsBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_groups,
                container,
                false
        );
        binding.setViewModel((GroupViewModel) viewModel);
        binding.groupFragmentRvGroup.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.groupFragmentRvGroup.addItemDecoration(
                new GroupDivider(
                        getContext().getDrawable(R.drawable.divider),
                        (int)getContext().getResources().getDimension(R.dimen.divider_padding_left)
                )
        );
        binding.groupFragmentRvGroup.setHasFixedSize(true);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.subscribeObserver(new GroupObserver());
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

    private class GroupObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
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
