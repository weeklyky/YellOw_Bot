import bwapi.Race;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitCommand;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

/// 상황을 판단하여, 정찰, 빌드, 공격, 방어 등을 수행하도록 총괄 지휘를 하는 class <br>
/// InformationManager 에 있는 정보들로부터 상황을 판단하고, <br>
/// BuildManager 의 buildQueue에 빌드 (건물 건설 / 유닛 훈련 / 테크 리서치 / 업그레이드) 명령을 입력합니다.<br>
/// 정찰, 빌드, 공격, 방어 등을 수행하는 코드가 들어가는 class
public class StrategyManager {

	private static StrategyManager instance = new StrategyManager();

	private CommandUtil commandUtil = new CommandUtil();

	private boolean isFullScaleAttackStarted;
	private boolean isInitialBuildOrderFinished;

	/// static singleton 객체를 리턴합니다
	public static StrategyManager Instance() {
		return instance;
	}

	public StrategyManager() {
		isFullScaleAttackStarted = false;
		isInitialBuildOrderFinished = false;
	}

	/// 경기가 시작될 때 일회적으로 전략 초기 세팅 관련 로직을 실행합니다
	public void onStart() {
		setInitialBuildOrder();		
	}

	///  경기가 종료될 때 일회적으로 전략 결과 정리 관련 로직을 실행합니다
	public void onEnd(boolean isWinner) {

	}

	/// 경기 진행 중 매 프레임마다 경기 전략 관련 로직을 실행합니다
	public void update() {
		if (BuildManager.Instance().buildQueue.isEmpty()) {
			isInitialBuildOrderFinished = true;
		}

		executeWorkerTraining();

		executeSupplyManagement();

		executeBasicCombatUnitTraining();

		executeCombat();
		
		
		executeExpandHatchery();
		
		executeManageOverlords();
	}

	private void executeManageOverlords() {
		if(MyBotModule.Broodwar.self().getRace() == Race.Zerg){
			OverlordManager.Instance().update();
		}
	}

	private void executeExpandHatchery() {
		if(MyBotModule.Broodwar.self().getRace() == Race.Zerg){
			
		}
		
	}

	public void setInitialBuildOrder() {
		if (MyBotModule.Broodwar.self().getRace() == Race.Zerg) {
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Spawning_Pool,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Zergling,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Zergling,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Zergling,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Overlord,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);

			/*
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hatchery,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hatchery,
					BuildOrderItem.SeedPositionStrategy.FirstExpansionLocation);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hatchery,
					BuildOrderItem.SeedPositionStrategy.FirstExpansionLocation);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(
					InformationManager.Instance().getBasicSupplyProviderUnitType(),
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);

			// 가스 익스트랙터
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Extractor,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation);

			// 성큰 콜로니
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Creep_Colony,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Sunken_Colony,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation);

			BuildManager.Instance().buildQueue
					.queueAsLowestPriority(InformationManager.Instance().getRefineryBuildingType());

			// 저글링 이동속도 업
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Metabolic_Boost);

			// 에볼루션 챔버
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Evolution_Chamber);
			// 에볼루션 챔버 . 지상유닛 업그레이드
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Zerg_Melee_Attacks, false);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Zerg_Missile_Attacks, false);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Zerg_Carapace, false);

			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Drone);
			
			// 스포어 코로니
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Creep_Colony,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Spore_Colony,
					BuildOrderItem.SeedPositionStrategy.MainBaseLocation);

			// 히드라
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hydralisk_Den);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hydralisk);

			// 레어
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Lair);

			// 오버로드 운반가능
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Ventral_Sacs);
			// 오버로드 시야 증가
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Antennae);
			// 오버로드 속도 증가
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Pneumatized_Carapace);

			// 히드라 이동속도 업
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Muscular_Augments, false);
			// 히드라 공격 사정거리 업
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Grooved_Spines, false);

			// 럴커
			BuildManager.Instance().buildQueue.queueAsLowestPriority(TechType.Lurker_Aspect);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hydralisk);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Lurker);
			
			// 스파이어
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Spire, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Mutalisk, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Scourge, true);

			// 스파이어 . 공중유닛 업그레이드
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Zerg_Flyer_Attacks, false);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Zerg_Flyer_Carapace, false);

			// 퀸
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Queens_Nest);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Queen);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(TechType.Spawn_Broodlings, false);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(TechType.Ensnare, false);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Gamete_Meiosis, false);

			// 하이브
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Hive);
			// 저글링 공격 속도 업
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Adrenal_Glands, false);

			// 스파이어 . 그레이트 스파이어
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Greater_Spire, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Mutalisk, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Guardian, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Mutalisk, true);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Devourer, true);

			// 울트라리스크
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Ultralisk_Cavern);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Ultralisk);
			// 울트라리스크 이동속도 업
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Anabolic_Synthesis, false);
			// 울트라리스크 방어력 업
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Chitinous_Plating, false);

			// 디파일러
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Defiler_Mound);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Defiler);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(TechType.Consume, false);
			BuildManager.Instance().buildQueue.queueAsLowestPriority(TechType.Plague, false);
			// 디파일러 에너지 업
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UpgradeType.Metasynaptic_Node, false);

			// 나이더스 캐널
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Nydus_Canal);

			// 참고로, Zerg_Nydus_Canal 건물로부터 Nydus Canal Exit를 만드는 방법은 다음과 같습니다
			//if (MyBotModule.Broodwar.self().completedUnitCount(UnitType.Zerg_Nydus_Canal) > 0) {
			//	for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
			//		if (unit.getType() == UnitType.Zerg_Nydus_Canal) {
			//			TilePosition targetTilePosition = new TilePosition(unit.getTilePosition().getX() + 6, unit.getTilePosition().getY()); // Creep 이 있는 곳이어야 한다
			//			unit.build(UnitType.Zerg_Nydus_Canal, targetTilePosition);
			//		}
			//	}
			//}

			// 퀸 - 인페스티드 테란 : 테란 Terran_Command_Center 건물의 HitPoint가 낮을 때, 퀸을 들여보내서 Zerg_Infested_Command_Center 로 바꾸면, 그 건물에서 실행 됨
			BuildManager.Instance().buildQueue.queueAsLowestPriority(UnitType.Zerg_Infested_Terran);
			*/
		}
	}

	// 일꾼 계속 추가 생산
	public void executeWorkerTraining() {

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		if (isInitialBuildOrderFinished == false) {
			return;
		}

		if (MyBotModule.Broodwar.self().minerals() >= 50) {
			// workerCount = 현재 일꾼 수 + 생산중인 일꾼 수
			int workerCount = MyBotModule.Broodwar.self().allUnitCount(UnitType.Zerg_Drone);

			if (MyBotModule.Broodwar.self().getRace() == Race.Zerg) {
				for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
					if (unit.getType() == UnitType.Zerg_Egg) {
						// Zerg_Egg 에게 morph 명령을 내리면 isMorphing = true,
						// isBeingConstructed = true, isConstructing = true 가 된다
						// Zerg_Egg 가 다른 유닛으로 바뀌면서 새로 만들어진 유닛은 잠시
						// isBeingConstructed = true, isConstructing = true 가
						// 되었다가,
						if (unit.isMorphing() && unit.getBuildType() == UnitType.Zerg_Drone) {
							workerCount++;
						}
					}
				}
			} else {
				for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
					if (unit.getType().isResourceDepot()) {
						if (unit.isTraining()) {
							workerCount += unit.getTrainingQueue().size();
						}
					}
				}
			}

			if (workerCount < 30) {
				for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
					if (unit.getType().isResourceDepot()) {
						if (unit.isTraining() == false || unit.getLarva().size() > 0) {
							// 빌드큐에 일꾼 생산이 1개는 있도록 한다
							if (BuildManager.Instance().buildQueue
									.getItemCount(UnitType.Zerg_Drone, null) == 0) {
								// std.cout << "worker enqueue" << std.endl;
								BuildManager.Instance().buildQueue.queueAsLowestPriority(
										new MetaType(UnitType.Zerg_Drone), false);
							}
						}
					}
				}
			}
		}
	}

	// Supply DeadLock 예방 및 SupplyProvider 가 부족해질 상황 에 대한 선제적 대응으로서<br>
	// SupplyProvider를 추가 건설/생산한다
	public void executeSupplyManagement() {

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		if (isInitialBuildOrderFinished == false) {
			return;
		}

		// 1초에 한번만 실행
		if (MyBotModule.Broodwar.getFrameCount() % 24 != 0) {
			return;
		}

		// 게임에서는 서플라이 값이 200까지 있지만, BWAPI 에서는 서플라이 값이 400까지 있다
		// 저글링 1마리가 게임에서는 서플라이를 0.5 차지하지만, BWAPI 에서는 서플라이를 1 차지한다
		if (MyBotModule.Broodwar.self().supplyTotal() <= 400) {

			// 서플라이가 다 꽉찼을때 새 서플라이를 지으면 지연이 많이 일어나므로, supplyMargin (게임에서의 서플라이 마진 값의 2배)만큼 부족해지면 새 서플라이를 짓도록 한다
			// 이렇게 값을 정해놓으면, 게임 초반부에는 서플라이를 너무 일찍 짓고, 게임 후반부에는 서플라이를 너무 늦게 짓게 된다
			int supplyMargin = 12;

			// currentSupplyShortage 를 계산한다
			int currentSupplyShortage = MyBotModule.Broodwar.self().supplyUsed() + supplyMargin - MyBotModule.Broodwar.self().supplyTotal();

			if (currentSupplyShortage > 0) {
				
				// 생산/건설 중인 Supply를 센다
				int onBuildingSupplyCount = 0;

				// 저그 종족인 경우, 생산중인 Zerg_Overlord (Zerg_Egg) 를 센다. Hatchery 등 건물은 세지 않는다
				if (MyBotModule.Broodwar.self().getRace() == Race.Zerg) {
					for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
						if (unit.getType() == UnitType.Zerg_Egg && unit.getBuildType() == UnitType.Zerg_Overlord) {
							onBuildingSupplyCount += UnitType.Zerg_Overlord.supplyProvided();
						}
						// 갓태어난 Overlord 는 아직 SupplyTotal 에 반영안되어서, 추가 카운트를 해줘야함
						if (unit.getType() == UnitType.Zerg_Overlord && unit.isConstructing()) {
							onBuildingSupplyCount += UnitType.Zerg_Overlord.supplyProvided();
						}
					}
				}
				
				if (currentSupplyShortage > onBuildingSupplyCount) {
					// BuildQueue 최상단에 SupplyProvider 가 있지 않으면 enqueue 한다
					boolean isToEnqueue = true;
					if (!BuildManager.Instance().buildQueue.isEmpty()) {
						BuildOrderItem currentItem = BuildManager.Instance().buildQueue.getHighestPriorityItem();
						if (currentItem.metaType.isUnit() 
							&& currentItem.metaType.getUnitType() == UnitType.Zerg_Overlord) 
						{
							isToEnqueue = false;
						}
					}
					if (isToEnqueue) {
						System.out.println("enqueue supply provider "+ UnitType.Zerg_Overlord);
						BuildManager.Instance().buildQueue.queueAsHighestPriority(
								new MetaType( UnitType.Zerg_Overlord), true);
					}
				}
			}
		}
	}

	public void executeBasicCombatUnitTraining() {

		// InitialBuildOrder 진행중에는 아무것도 하지 않습니다
		if (isInitialBuildOrderFinished == false) {
			return;
		}

		// 기본 병력 추가 훈련
		if (MyBotModule.Broodwar.self().minerals() >= 200 && MyBotModule.Broodwar.self().supplyUsed() < 390) {
			for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
				if (unit.getType() ==  UnitType.Zerg_Zergling) {
					if (unit.isTraining() == false || unit.getLarva().size() > 0) {
						if (BuildManager.Instance().buildQueue
								.getItemCount(UnitType.Zerg_Zergling, null) == 0) {
							BuildManager.Instance().buildQueue.queueAsLowestPriority(
									UnitType.Zerg_Zergling,
									BuildOrderItem.SeedPositionStrategy.MainBaseLocation, true);
						}
					}
				}
			}
		}
	}

	public void executeCombat() {

		// 공격 모드가 아닐 때에는 전투유닛들을 아군 진영 길목에 집결시켜서 방어
		if (isFullScaleAttackStarted == false) {
			Chokepoint firstChokePoint = BWTA.getNearestChokepoint(InformationManager.Instance().getMainBaseLocation(InformationManager.Instance().selfPlayer).getTilePosition());

			for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
				if (unit.getType() == UnitType.Zerg_Zergling && unit.isIdle()) {
					commandUtil.attackMove(unit, firstChokePoint.getCenter());
				}
			}

			// 전투 유닛이 2개 이상 생산되었고, 적군 위치가 파악되었으면 총공격 모드로 전환
			if (MyBotModule.Broodwar.self().completedUnitCount(UnitType.Zerg_Zergling) > 2) {
				if (InformationManager.Instance().enemyPlayer != null
					&& InformationManager.Instance().enemyRace != Race.Unknown  
					&& InformationManager.Instance().getOccupiedBaseLocations(InformationManager.Instance().enemyPlayer).size() > 0) {				
					isFullScaleAttackStarted = true;
				}
			}
		}
		// 공격 모드가 되면, 모든 전투유닛들을 적군 Main BaseLocation 로 공격 가도록 합니다
		else {
			//std.cout << "enemy OccupiedBaseLocations : " << InformationManager.Instance().getOccupiedBaseLocations(InformationManager.Instance()._enemy).size() << std.endl;
			
			if (InformationManager.Instance().enemyPlayer != null
					&& InformationManager.Instance().enemyRace != Race.Unknown 
					&& InformationManager.Instance().getOccupiedBaseLocations(InformationManager.Instance().enemyPlayer).size() > 0) 
			{					
				// 공격 대상 지역 결정
				BaseLocation targetBaseLocation = null;
				double closestDistance = 100000000;

				for (BaseLocation baseLocation : InformationManager.Instance().getOccupiedBaseLocations(InformationManager.Instance().enemyPlayer)) {
					double distance = BWTA.getGroundDistance(
						InformationManager.Instance().getMainBaseLocation(InformationManager.Instance().selfPlayer).getTilePosition(), 
						baseLocation.getTilePosition());

					if (distance < closestDistance) {
						closestDistance = distance;
						targetBaseLocation = baseLocation;
					}
				}

				if (targetBaseLocation != null) {
					for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
						// 건물은 제외
						if (unit.getType().isBuilding()) {
							continue;
						}
						// 모든 일꾼은 제외
						if (unit.getType().isWorker()) {
							continue;
						}
											
						// canAttack 유닛은 attackMove Command 로 공격을 보냅니다
						if (unit.canAttack()) {
							
							if (unit.isIdle()) {
								commandUtil.attackMove(unit, targetBaseLocation.getPosition());
							}
						} 
					}
				}
			}
		}
	}
}