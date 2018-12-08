/*
 * Copyright (C) 2014-2018 OpenKeeper
 *
 * OpenKeeper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenKeeper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenKeeper.  If not, see <http://www.gnu.org/licenses/>.
 */
package toniarts.openkeeper.view.text;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import toniarts.openkeeper.game.component.CreatureAi;
import toniarts.openkeeper.game.component.CreatureComponent;
import toniarts.openkeeper.game.component.CreatureEfficiency;
import toniarts.openkeeper.game.component.CreatureMood;
import toniarts.openkeeper.game.component.TaskComponent;
import toniarts.openkeeper.game.map.IMapInformation;
import toniarts.openkeeper.game.map.MapTile;
import toniarts.openkeeper.tools.convert.map.Creature;
import toniarts.openkeeper.utils.Utils;

/**
 * Parses text where the entity is based on a Creature data object
 *
 * @author Toni Helenius <helenius.toni@gmail.com>
 */
public class CreatureTextParser extends EntityTextParser<Creature> {

    private final IMapInformation mapInformation;

    public CreatureTextParser(EntityData entityData, IMapInformation mapInformation) {
        super(entityData);

        this.mapInformation = mapInformation;
    }

    @Override
    protected String getReplacement(int index, EntityId entityId, Creature creature) {
        switch (index) {
            case 29:
                CreatureComponent creatureComponent = entityData.getComponent(entityId, CreatureComponent.class);
                if (creatureComponent != null) {
                    return creatureComponent.name;
                }
                return "";
            case 30:
                return Utils.getMainTextResourceBundle().getString(Integer.toString(creature.getNameStringId()));
            case 31:
                CreatureAi creatureAi = entityData.getComponent(entityId, CreatureAi.class);
                if (creatureAi != null) {
                    return getStatusText(entityData, entityId, creatureAi, mapInformation);
                }
                return "";
            case 32:
                // FIXME
                return "";
            case 33:
                CreatureMood creatureMood = entityData.getComponent(entityId, CreatureMood.class);
                if (creatureMood != null) {
                    return Integer.toString(creatureMood.moodValue);
                }
                return "";
            case 74:
                CreatureEfficiency creatureEfficiency = entityData.getComponent(entityId, CreatureEfficiency.class);
                if (creatureEfficiency != null) {
                    return Integer.toString(creatureEfficiency.efficiencyPercentage);
                }
                return "";
        }

        return super.getReplacement(index, entityId, creature);
    }

    private static String getStatusText(EntityData entityData, EntityId entityId, CreatureAi creatureAi,
            IMapInformation mapInformation) {
        switch (creatureAi.getCreatureState()) {
            case IDLE: {
                return Utils.getMainTextResourceBundle().getString("2599");
            }
            case WORK: {
                TaskComponent taskComponent = entityData.getComponent(entityId, TaskComponent.class);
                if (taskComponent != null) {
                    return getTaskTooltip(taskComponent, mapInformation);
                }
            }
            case WANDER: {
                return Utils.getMainTextResourceBundle().getString("2628");
            }
            case DEAD: {
                return Utils.getMainTextResourceBundle().getString("2598");
            }
            case FLEE: {
                return Utils.getMainTextResourceBundle().getString("2658");
            }
            case FIGHT: {
                return Utils.getMainTextResourceBundle().getString("2651");
            }
            case DRAGGED:
            case UNCONSCIOUS: {
                return Utils.getMainTextResourceBundle().getString("2655");
            }
            case STUNNED: {
                return Utils.getMainTextResourceBundle().getString("2597");
            }
            case FOLLOW: {
                return Utils.getMainTextResourceBundle().getString("2675");
            }
            case IMPRISONED: {
                return Utils.getMainTextResourceBundle().getString("2674");
            }
            case TORTURED: {
                return Utils.getMainTextResourceBundle().getString("2635");
            }
            case SLEEPING: {
                return Utils.getMainTextResourceBundle().getString("2672");
            }
            case RECUPERATING: {
                return Utils.getMainTextResourceBundle().getString("2667");
            }
        }

        return "";
    }

    private static String getTaskTooltip(TaskComponent taskComponent, IMapInformation mapInformation) {
        switch (taskComponent.taskType) {
            case CLAIM_LAIR:
                return Utils.getMainTextResourceBundle().getString("2627");
            case CAPTURE_ENEMY_CREATURE:
                return Utils.getMainTextResourceBundle().getString("2621");
            case CARRY_CREATURE_TO_JAIL:
                return Utils.getMainTextResourceBundle().getString("2619");
            case CARRY_CREATURE_TO_LAIR:
                return Utils.getMainTextResourceBundle().getString("2619");
            case CARRY_GOLD_TO_TREASURY:
                return Utils.getMainTextResourceBundle().getString("2786");
            case CLAIM_ROOM:
                return Utils.getMainTextResourceBundle().getString("2602");
            case CLAIM_TILE:
                return Utils.getMainTextResourceBundle().getString("2601");
            case CLAIM_WALL:
                return Utils.getMainTextResourceBundle().getString("2603");
            case DIG_TILE:
                MapTile tile = mapInformation.getMapData().getTile(taskComponent.targetLocation);
                return Utils.getMainTextResourceBundle().getString(tile.getGold() > 0 ? "2605" : "2600");
            case FETCH_OBJECT:
                return Utils.getMainTextResourceBundle().getString("546");
            case GO_TO_LOCATION:
                return Utils.getMainTextResourceBundle().getString("2670");
            case GO_TO_SLEEP:
                return Utils.getMainTextResourceBundle().getString("2671");
            case KILL_PLAYER:
                return Utils.getMainTextResourceBundle().getString("2645");
            case REPAIR_WALL:
                return Utils.getMainTextResourceBundle().getString("2604");
            case RESCUE_CREATURE:
                return Utils.getMainTextResourceBundle().getString("2617");
            case RESEARCH_SPELL:
                return Utils.getMainTextResourceBundle().getString("2625");
        }

        return "";
    }

}
