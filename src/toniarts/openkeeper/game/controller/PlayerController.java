/*
 * Copyright (C) 2014-2017 OpenKeeper
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
package toniarts.openkeeper.game.controller;

import java.util.Map;
import toniarts.openkeeper.game.controller.player.PlayerCreatureControl;
import toniarts.openkeeper.game.controller.player.PlayerGoldControl;
import toniarts.openkeeper.game.controller.player.PlayerManaControl;
import toniarts.openkeeper.game.controller.player.PlayerRoomControl;
import toniarts.openkeeper.game.controller.player.PlayerSpellControl;
import toniarts.openkeeper.game.data.Keeper;
import toniarts.openkeeper.game.listener.PlayerListener;
import toniarts.openkeeper.tools.convert.map.Player;
import toniarts.openkeeper.tools.convert.map.Variable;

/**
 * Player controller, hosts and provides player related methods
 *
 * @author Toni Helenius <helenius.toni@gmail.com>
 */
public class PlayerController implements IPlayerController {

    private final Keeper keeper;
    private final PlayerGoldControl goldControl;
    private final PlayerCreatureControl creatureControl;
    private final PlayerRoomControl roomControl;
    private final PlayerSpellControl spellControl;
    private final PlayerManaControl manaControl;

    public PlayerController(Keeper keeper, Map<Variable.MiscVariable.MiscType, Variable.MiscVariable> gameSettings) {
        this.keeper = keeper;

        // Create the actual controllers
        goldControl = new PlayerGoldControl(keeper);
        creatureControl = new PlayerCreatureControl(keeper);
        roomControl = new PlayerRoomControl(keeper);
        spellControl = new PlayerSpellControl(keeper);

        // Don't create mana control for neutral nor good player
        if (keeper.getId() != Player.GOOD_PLAYER_ID && keeper.getId() != Player.NEUTRAL_PLAYER_ID) {
            manaControl = new PlayerManaControl(keeper, gameSettings);
        } else {
            manaControl = null;
        }
    }

    @Override
    public Keeper getKeeper() {
        return keeper;
    }

    @Override
    public void addListener(PlayerListener listener) {
        goldControl.addListener(listener);
        if (manaControl != null) {
            manaControl.addListener(listener);
        }
    }

    @Override
    public void removeListener(PlayerListener listener) {
        goldControl.removeListener(listener);
        if (manaControl != null) {
            manaControl.removeListener(listener);
        }
    }

    @Override
    public PlayerGoldControl getGoldControl() {
        return goldControl;
    }

    @Override
    public PlayerManaControl getManaControl() {
        return manaControl;
    }

    @Override
    public PlayerSpellControl getSpellControl() {
        return spellControl;
    }

    @Override
    public PlayerCreatureControl getCreatureControl() {
        return creatureControl;
    }

    @Override
    public PlayerRoomControl getRoomControl() {
        return roomControl;
    }

}