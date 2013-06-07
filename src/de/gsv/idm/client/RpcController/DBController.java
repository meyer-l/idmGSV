package de.gsv.idm.client.RpcController;

import java.util.logging.Logger;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.logging.client.ConsoleLogHandler;

import de.gsv.idm.client.RpcController.datamanager.AssettypeMeasureLinkRpcController;
import de.gsv.idm.client.RpcController.datamanager.AssettypeModuleLinkRpcController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.RpcController.service.AssetLinkRpcController;
import de.gsv.idm.client.RpcController.service.AssetRpcController;
import de.gsv.idm.client.RpcController.service.AssettypeCategoryRpcController;
import de.gsv.idm.client.RpcController.service.AssettypeRpcController;
import de.gsv.idm.client.RpcController.service.ChangeEventRpcController;
import de.gsv.idm.client.RpcController.service.ChangeItemRpcController;
import de.gsv.idm.client.RpcController.service.DomainRpcController;
import de.gsv.idm.client.RpcController.service.EmployeeRpcController;
import de.gsv.idm.client.RpcController.service.InformationRpcController;
import de.gsv.idm.client.RpcController.service.MeasureRpcController;
import de.gsv.idm.client.RpcController.service.ModuleRpcController;
import de.gsv.idm.client.RpcController.service.OccupationRpcController;
import de.gsv.idm.client.RpcController.service.SecurityLevelChangeRpcController;
import de.gsv.idm.client.RpcController.service.SecurityzoneRpcController;
import de.gsv.idm.client.RpcController.service.ThreatRpcController;
import de.gsv.idm.client.RpcController.service.UserRpcController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.view.domain.ViewDomainViewEvent;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.dto.ChangeItemDTO;
import de.gsv.idm.shared.dto.DomainDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;
import de.gsv.idm.shared.dto.ThreatDTO;
import de.gsv.idm.shared.dto.UserDTO;

public class DBController {

	private static DBController instance;
	private static Logger logger;
	
	private UserDTO user;

	private final HandlerManager eventBus;

	private final DomainRpcController domainController;
	private final MeasureRpcController measureController;
	private final ModuleRpcController moduleRpcController;
	private final ThreatRpcController threatController;
	private final AssettypeCategoryRpcController assettypeCategoryController;
	private final SecurityzoneRpcController securityzoneController;

	private final AssettypeRpcController assettypeController;
	private final AssettypeModuleLinkRpcController assettypeModuleLinkRpcController;
	private final AssettypeMeasureLinkRpcController assettypeMeasureLinkRpcController;
	private final AssetRpcController assetController;
	private final AssetLinkRpcController assetLinkController;
	private final EmployeeRpcController employeeController;
	private final InformationRpcController informationController;
	private final OccupationRpcController occupationController;
	
	private final ChangeEventRpcController changeEventController;
	private final ChangeItemRpcController changeItemController;
	private final SecurityLevelChangeRpcController securityLevelChangeController;
	
	private final UserRpcController userController;

	public DBController() {
		eventBus = new HandlerManager(null);

		domainController = new DomainRpcController(this);
		measureController = new MeasureRpcController(this);
		moduleRpcController = new ModuleRpcController(this);
		threatController = new ThreatRpcController(this);
		assettypeCategoryController = new AssettypeCategoryRpcController(this);
		securityzoneController = new SecurityzoneRpcController(this);

		assettypeController = new AssettypeRpcController(this);
		assettypeModuleLinkRpcController = new AssettypeModuleLinkRpcController(this);
		assettypeMeasureLinkRpcController = new AssettypeMeasureLinkRpcController(this);
		assetController = new AssetRpcController(this);
		assetLinkController = new AssetLinkRpcController(this);
		employeeController = new EmployeeRpcController(this);
		informationController = new InformationRpcController(this);
		occupationController = new OccupationRpcController(this);

		changeEventController = new ChangeEventRpcController(this);
		changeItemController = new ChangeItemRpcController(this);
		securityLevelChangeController = new SecurityLevelChangeRpcController(this);
		
		userController = new UserRpcController();
		bindEventBus();
	}

	static public DBController getInstance() {
		if (instance == null) {
			instance = new DBController();
		}
		return instance;
	}

	public void bindEventBus() {
		eventBus.addHandler(ViewDomainViewEvent.TYPE,
		        new GeneralEventHandler<ViewDomainViewEvent>() {
			        public void onEvent(ViewDomainViewEvent event) {
				        assettypeController.clearMap();
				        assettypeModuleLinkRpcController.clearMap();
				        assettypeMeasureLinkRpcController.clearMap();
				        assetController.clearMap();
				        assetLinkController.clearMap();
				        employeeController.clearMap();
				        occupationController.clearMap();
				        informationController.clearMap();
				        changeEventController.clearMap();
			        }
		        });
	}

	public HandlerManager getEventBus() {
		return eventBus;
	}

	public DomainRpcController getDomainController() {
		return domainController;
	}

	public AssettypeRpcController getAssettypeController() {
		return assettypeController;
	}

	public AssettypeModuleLinkRpcController getAssettypeModuleLinkController() {
		return assettypeModuleLinkRpcController;
	}

	public AssettypeMeasureLinkRpcController getAssettypeMeasureLinkController() {
		return assettypeMeasureLinkRpcController;
	}

	public AssettypeCategoryRpcController getAssettypeCategoryController() {
		return assettypeCategoryController;
	}

	public AssetRpcController getAssetController() {
		return assetController;
	}
	
	public AssetLinkRpcController getAssetLinkController() {
		return assetLinkController;
	}
	
	public ChangeEventRpcController getChangeEventController() {
		return changeEventController;
	}
	
	public ChangeItemRpcController getChangeItemController() {
		return changeItemController;
	}

	public EmployeeRpcController getEmployeeController() {
		return employeeController;
	}

	public InformationRpcController getInformationController() {
		return informationController;
	}

	public MeasureRpcController getMeasureController() {
		return measureController;
	}

	public ModuleRpcController getModuleController() {
		return moduleRpcController;
	}

	public OccupationRpcController getOccupationController() {
		return occupationController;
	}

	public ThreatRpcController getThreatController() {
		return threatController;
	}

	public SecurityzoneRpcController getSecurityzoneController() {
		return securityzoneController;
	}
	
	public SecurityLevelChangeRpcController getSecurityLevelChangeController(){
		return securityLevelChangeController;
	}
	
	public UserRpcController getUserController(){
		return userController;
	}

	public static Logger getLogger() {
		if(logger == null){
			logger = Logger.getLogger("");
			logger.addHandler(new ConsoleLogHandler());
		}
	    return logger;
    }
	
	public UserDTO getUser(){
		return user;
	}
	
	public String getSessionId(){
		if(user != null){
			return user.getSessionId();
		} else {
			return "";
		}
	}
	
	public void setUser(UserDTO user){
		this.user = user;
	}

	public GeneralRpcController<?> getController(GeneralDTO<?> object) {
		GeneralRpcController<?> controller = null;
		if(object instanceof DomainDTO){
			controller = domainController;
		} else if(object instanceof MeasureDTO){
			controller = measureController;
		} else if(object instanceof ModuleDTO){
			controller = moduleRpcController;
		} else if(object instanceof ThreatDTO){
			controller = threatController;
		} else if(object instanceof AssettypeCategoryDTO){
			controller = assettypeCategoryController;
		} else if(object instanceof SecurityzoneDTO){
			controller = securityzoneController;
		} else if(object instanceof AssettypeDTO){
			controller = assettypeController;
		} else if(object instanceof AssettypeModuleLinkDTO) {
			controller = assettypeModuleLinkRpcController;
		} else if(object instanceof AssettypeMeasureLinkDTO) {
			controller = assettypeMeasureLinkRpcController;
		}  else if(object instanceof AssetDTO){
			controller = assetController;
		} else if(object instanceof AssetLinkDTO){
			controller = assetLinkController;
		} else if(object instanceof EmployeeDTO){
			controller = employeeController;
		} else if(object instanceof InformationDTO){
			controller = informationController;
		} else if(object instanceof OccupationDTO){
			controller = occupationController;
		} else if(object instanceof ChangeEventDTO){
			controller = changeEventController;
		} else if(object instanceof ChangeItemDTO){
			controller = changeItemController;
		} else if(object instanceof SecurityLevelChangeDTO){
			controller = securityLevelChangeController;
		}
		
		return controller;
    }
	
}
