/*
 * Copyright (c) 2018, Andrew EP | ElPinche256 <https://github.com/ElPinche256>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.reisub.unethicalite.autodropper;

import net.runelite.client.config.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup("chaosautodropper")

public interface Config extends net.runelite.client.config.Config {
    @ConfigItem(
            keyName = "itemNames",
            name = "Item names",
            description = "List of item names, separated with a semicolon.",
            position = 0
    )
    default String itemNames() { return "Empty plant pot"; }

    @ConfigItem(
            keyName = "itemIds",
            name = "Item IDs",
            description = "List of item IDs, separated with a semicolon. Useful if you want to differentiate between 2 items with the same name.",
            position = 1
    )
    default String itemIds() { return ""; }

    @ConfigItem(
            keyName = "dropMethod",
            name = "Auto drop method",
            description = "Choose the automatic drop method.",
            position = 3
    )
    default DropMethod dropMethod() { return DropMethod.NONE; }

    @ConfigItem(
            keyName = "dropEnableHotkey",
            name = "Enable hotkey",
            description = "Enable dropping on hotkey.",
            position = 4
    )
    default boolean dropEnableHotkey() { return false; }

    @ConfigItem(
            keyName = "dropHotkey",
            name = "Hotkey",
            description = "Choose the hotkey.",
            hidden = true,
            unhide = "dropEnableHotkey",
            position = 5
    )
    default Keybind dropHotkey() { return new Keybind(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK); }

    @ConfigSection(
            keyName = "seedsConfig",
            name = "Seeds configuration",
            description = "Seeds configuration",
            position = 70
    )
    String seedsConfig = "seedsConfig";

    @ConfigItem(
            keyName = "seedNames",
            name = "Seed names",
            description = "List of seeds to drop. This works the same as regular items but is an extra field to not clog the regular one.",
            position = 71
    )
    default String seedNames() { return "Potato seed; Onion seed; Cabbage seed; Tomato seed; Sweetcorn seed; Marigold seed; Rosemary seed; Nasturtium seed; Woad seed; Redberry seed; Cadavaberry seed; Dwellberry seed; Acorn; Apple tree seed; Banana tree seed"; }
}