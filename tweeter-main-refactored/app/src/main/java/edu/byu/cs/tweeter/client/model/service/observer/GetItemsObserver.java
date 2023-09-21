package edu.byu.cs.tweeter.client.model.service.observer;

public interface GetItemsObserver extends ServiceObserver{

    void handleSuccess(int count);
}
