package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetStoryPresenter extends PagedPresenter<Status> {

    public GetStoryPresenter(ItemView<Status> view) {
        super(view, "story");
    }
    @Override
    public void loadItems(User user) {
        followService.loadMoreItems(user, PAGE_SIZE, last, "story", new GetListObserver());
    }


}
