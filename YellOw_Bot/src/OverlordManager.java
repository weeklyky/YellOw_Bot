import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;

public class OverlordManager {
	private static OverlordManager instance = new OverlordManager();

	private List<Unit> overlords = new ArrayList<Unit>();

	private List<BaseLocation> locationNeedScout = new ArrayList<BaseLocation>();
	private HashMap<Integer, TilePosition> overlordsTarget = new HashMap<Integer, TilePosition>();
	private HashMap<Integer, Character> overlordsStatus = new HashMap<Integer, Character>();

	private List<BaseLocation> locations;
	
	
	public static OverlordManager Instance() {
		return instance;
	}
	
	public void update(){
		
		//AM I going RIGHT?
		
		for (Unit overlord : overlords) {
			if(overlordsStatus.containsKey(overlord.getID()) && overlordsStatus.get(overlord.getID()) == 'M'){
				TilePosition target = overlordsTarget.get(overlord.getID());
				
				if(MyBotModule.Broodwar.isExplored(target)){
					
				}
				if(!overlord.isMoving()){
					overlord.move(target.toPosition());
				}
			}
		}
	}
	
	
	public void move(Unit overlord, BaseLocation location){
		int id = overlord.getID();
		
	}
	
	public void onUnitMorph(Unit unit){
		if (unit.getType() == UnitType.Zerg_Overlord && unit.getPlayer() == MyBotModule.Broodwar.self() && unit.getHitPoints() >= 0){
			System.out.println("OVERLORD SPAWN");
			overlords.add(unit);
			if(InformationManager.Instance().getMainBaseLocation(InformationManager.Instance().enemyPlayer) == null){				
					overlordsStatus.put(unit.getID(), 'S');
					locations = InformationManager.Instance().getUnknownBaseLocations();

				System.out.println(locations.size());
				locations.removeAll(locationNeedScout);
				
				System.out.println(locations.size());
				if(locations.isEmpty()) return;
				
				

				final BaseLocation myBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self());

				locations.sort(new Comparator<BaseLocation>() {
					@Override
					public int compare(BaseLocation o1, BaseLocation o2) {
						return (int)o2.getAirDistance(myBaseLocation) - (int)o1.getAirDistance(myBaseLocation);
					}
				});
				
				
				
				unit.move(locations.get(0).getPosition());
				locationNeedScout.add(locations.get(0));
			}
		} 
	}

	public void onUnitShow(Unit unit) {
		if (unit.getType() == UnitType.Zerg_Overlord && unit.getPlayer() == MyBotModule.Broodwar.self() && unit.getHitPoints() >= 0){
			System.out.println("Initial OVERLORD");
			overlords.add(unit);
			overlordsStatus.put(unit.getID(), 'S');
			locations = InformationManager.Instance().getUnknownBaseLocations();
			
			
			
			System.out.println(locations.size());
			locations.removeAll(locationNeedScout);
			
			System.out.println(locations.size());
			if(locations.isEmpty()) return;
			
			
			
			

			final BaseLocation myBaseLocation = InformationManager.Instance().getMainBaseLocation(MyBotModule.Broodwar.self());

			locations.sort(new Comparator<BaseLocation>() {
				@Override
				public int compare(BaseLocation o1, BaseLocation o2) {
					return (int)o2.getAirDistance(myBaseLocation) - (int)o1.getAirDistance(myBaseLocation);
				}
			});
			
			
			unit.move(locations.get(0).getPosition());
			locationNeedScout.add(locations.get(0));
			
		} else if(unit.getPlayer() == MyBotModule.Broodwar.enemy()){
			for (Unit overlord : overlords) {
				if(overlord.isInWeaponRange(unit)){ //NOT WORKING..
					System.out.println(unit.toString()+" can attack this overlord :(");
					int x=  unit.getPosition().getX()  - overlord.getPosition().getX();
					int y = unit.getPosition().getY()  - overlord.getPosition().getY();
					Position newPos = new Position(overlord.getPosition().getX() - x, overlord.getPosition().getY() - y);
					overlord.move(newPos);
				}
			}
		}
	}

	public void onUnitDestroy(Unit unit) {
		if (unit.getType() == UnitType.Zerg_Overlord && unit.getPlayer() == MyBotModule.Broodwar.self()){
			System.out.println("OVERLORD DIED");
			overlords.remove(unit);
		}
	}
	
	public void comebackHome() {
		for (Unit unit : overlords) {
			unit.move(InformationManager.Instance().getMainBaseLocation(InformationManager.Instance().selfPlayer).getPosition());
		}
	}
}
