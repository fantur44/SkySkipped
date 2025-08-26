/*
 * SkySkipped - Hypixel Skyblock QOL mod
 * Copyright (C) 2023  Cephetir
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.cephetir.skyskipped.mixins.accessors;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public interface IMixinEntityPlayerSP {
    @Accessor("lastReportedYaw")
    float getLastReportedYaw();

    @Accessor("lastReportedYaw")
    void setLastReportedYaw(float yaw);

    @Accessor("lastReportedPitch")
    float getLastReportedPitch();

    @Accessor("lastReportedPitch")
    void setLastReportedPitch(float pitch);

    @Accessor("lastReportedPosX")
    double getLastReportedPosX();

    @Accessor("lastReportedPosX")
    void setLastReportedPosX(double x);

    @Accessor("lastReportedPosY")
    double getLastReportedPosY();

    @Accessor("lastReportedPosY")
    void setLastReportedPosY(double y);

    @Accessor("lastReportedPosZ")
    double getLastReportedPosZ();

    @Accessor("lastReportedPosZ")
    void setLastReportedPosZ(double z);
}
