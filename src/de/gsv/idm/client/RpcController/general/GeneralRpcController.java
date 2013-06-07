package de.gsv.idm.client.RpcController.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.services.GeneralServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class GeneralRpcController<T extends GeneralDTO<T>> implements
        GeneralServiceAsync<T> {

	protected final DBController dbConnector;
	private final GeneralServiceAsync<T> generalService;
	protected final HashMap<Integer, T> map;

	public GeneralRpcController(GeneralServiceAsync<T> generalService,
	        DBController communicationController) {
		this.dbConnector = communicationController;
		this.generalService = generalService;
		map = new HashMap<Integer, T>();
	}

	public void getObject(final int id, final AsyncCallback<T> callback) {
		getObject(id, dbConnector.getSessionId(), callback);
	}

	public void getObject(final int id, String sessionId, final AsyncCallback<T> callback) {
		generalService.getObject(id, sessionId, new AsyncCallback<T>() {
			public void onSuccess(T result) {
				if (result != null) {
					result = putInMap(result);
					callback.onSuccess(result);
				} else {
					callback.onFailure(null);
				}

			}

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	public void clearMap() {
		map.clear();
	}

	public List<T> getMapValues() {
		return new ArrayList<T>(map.values());
	}

	public void getAssociatedObjects(final int id, final AsyncCallback<List<GeneralDTO<?>>> callback) {
		getAssociatedObjects(id, dbConnector.getSessionId(), callback);
	}

	public void getAssociatedObjects(final int id, String sessionId,
	        final AsyncCallback<List<GeneralDTO<?>>> callback) {
		generalService.getAssociatedObjects(id, sessionId,
		        new AsyncCallback<List<GeneralDTO<?>>>() {
			        public void onSuccess(List<GeneralDTO<?>> result) {

				        if (result != null) {
					        List<GeneralDTO<?>> mappedList = new ArrayList<GeneralDTO<?>>();
					        for (GeneralDTO<?> updatedItem : result) {
						        GeneralRpcController<?> controller = dbConnector
						                .getController(updatedItem);
						        if (controller != null) {
							        mappedList.add(controller.handleGeneralObject(updatedItem));
						        } else {
							        DBController.getLogger().log(
							                Level.SEVERE,
							                "No Controller to handle given object class: "
							                        + updatedItem.getClassName());
						        }

					        }
					        callback.onSuccess(mappedList);
				        } else {
					        callback.onFailure(null);
				        }

			        }

			        public void onFailure(Throwable caught) {
				        callback.onFailure(caught);
			        }
		        });
	}

	public void getAll(final AsyncCallback<ArrayList<T>> callback) {
		getAll(dbConnector.getSessionId(), callback);
	}

	public void getAll(final String sessionId, final AsyncCallback<ArrayList<T>> callback) {
		generalService.getAllCount(sessionId, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Integer result) {
				if (map.size() == result) {
					callback.onSuccess(new ArrayList<T>(map.values()));
				} else {
					generalService.getAll(sessionId, new AsyncCallback<ArrayList<T>>() {
						public void onSuccess(ArrayList<T> result) {
							ArrayList<T> mappedList = new ArrayList<T>();
							for (T object : result) {
								object = putInMap(object);
								mappedList.add(object);
							}
							callback.onSuccess(mappedList);
						}

						public void onFailure(Throwable caught) {
							callback.onFailure(caught);
						}

					});
				}
			}

		});

	}

	public void getAllCount(String sessionId, final AsyncCallback<Integer> callback) {
		generalService.getAllCount(sessionId, callback);
	}

	public void getAllCount(final AsyncCallback<Integer> callback) {
		getAllCount(dbConnector.getSessionId(), callback);
	}

	public void update(T update, final AsyncCallback<T> callback) {
		update(update, dbConnector.getSessionId(), callback);
	}

	@Override
	public void update(T update, String sessionId, final AsyncCallback<T> callback) {
		generalService.update(update, sessionId, new AsyncCallback<T>() {
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(T result) {
				callback.onSuccess(result);
			}
		});
	}

	public void create(T toCreate, final AsyncCallback<T> callback) {
		create(toCreate, dbConnector.getSessionId(), callback);
	}

	@Override
	public void create(T toCreate, String sessionId, final AsyncCallback<T> callback) {
		generalService.create(toCreate, sessionId, new AsyncCallback<T>() {
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(T result) {
				callback.onSuccess(result);

			}
		});
	}

	public void delete(final T toDelete, final AsyncCallback<T> callback) {
		delete(toDelete, dbConnector.getSessionId(), callback);
	}

	@Override
	public void delete(final T toDelete, String sessionId, final AsyncCallback<T> callback) {
		generalService.delete(toDelete, sessionId, new AsyncCallback<T>() {
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(T result) {
				callback.onSuccess(result);
			}
		});
	}

	public T putInMap(T toPut) {
		toPut = fixAssociations(toPut);
		if (map.containsKey(toPut.getId())) {
			if (toPut.isSlim() && !map.get(toPut.getId()).isSlim()) {
				toPut = map.get(toPut.getId());
			} else {
				toPut = map.get(toPut.getId()).update(toPut);
			}
		} else {
			map.put(toPut.getId(), toPut);
		}
		return toPut;
	}

	public T getFromMap(T toGet) {
		if (toGet == null) {
			return toGet;
		} else if (map.containsKey(toGet.getId())) {
			T itemInMap = map.get(toGet.getId());
			if (itemInMap.isSlim() && !toGet.isSlim()) {
				itemInMap.update(toGet);
			}
			return itemInMap;
		} else {
			fixAssociations(toGet);
			map.put(toGet.getId(), toGet);
			return toGet;
		}
	}

	public void removeFromMap(T toRemove) {
		if (toRemove != null && map.containsKey(toRemove.getId())) {
			map.remove(toRemove.getId());
		}
	}

	public abstract T fixAssociations(T toPut);

	protected abstract T handleGeneralObject(GeneralDTO<?> updatedItem);

}
