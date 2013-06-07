package de.gsv.idm.client.view.properties;

import java.util.ArrayList;
import java.util.List;

public class ObjectExchange<T> {
    private List<T> objects = new ArrayList<T>();
 
    public List<T> getObjects() {
      return objects;
    }
 
    public void setObjects(List<T> list) {
      this.objects = list;
    }
  }
