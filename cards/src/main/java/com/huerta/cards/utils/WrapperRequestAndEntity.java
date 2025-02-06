package com.huerta.cards.utils;

public class WrapperRequestAndEntity<R, E> {
  private R request;
  private E entity;

  public WrapperRequestAndEntity(R request, E entity) {
    this.request = request;
    this.entity = entity;
  }

  public R getRequest() {
    return request;
  }

  public E getEntity() {
    return entity;
  }
}
