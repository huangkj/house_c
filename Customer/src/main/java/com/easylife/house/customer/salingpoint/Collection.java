package com.easylife.house.customer.salingpoint;

public class Collection {
    private String id;
    CollectionUser user = new CollectionUser();
    CollectionAction action = new CollectionAction();
    CollectionObject service = new CollectionObject();
    CollectionUser relatedUser = new CollectionUser();

    public CollectionUser getUser() {
        return user;
    }

    public void setUser(CollectionUser user) {
        this.user = user;
    }

    public CollectionAction getAction() {
        return action;
    }

    public void setAction(CollectionAction action) {
        this.action = action;
    }

    public CollectionObject getService() {
        return service;
    }

    public void setService(CollectionObject service) {
        this.service = service;
    }

    public CollectionUser getRelatedUser() {
        return relatedUser;
    }

    public void setRelatedUser(CollectionUser relatedUser) {
        this.relatedUser = relatedUser;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", action=" + action +
                ", service=" + service +
                ", relatedUser=" + relatedUser +
                '}';
    }
}
