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

package me.cephetir.skyskipped.features.impl.dugeons

import me.cephetir.bladecore.utils.TextUtils.stripColor
import me.cephetir.bladecore.utils.threading.BackgroundJob
import me.cephetir.bladecore.utils.threading.BackgroundScope
import me.cephetir.skyskipped.config.Cache
import me.cephetir.skyskipped.config.Config
import me.cephetir.skyskipped.features.Feature
import me.cephetir.skyskipped.mixins.accessors.IMixinRenderManager
import me.cephetir.skyskipped.utils.render.RenderUtils
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.tileentity.TileEntityCommandBlock
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.util.*
import kotlin.math.abs

class TerminalEsp : Feature() {
    private var job: BackgroundJob? = null
    private var renderGreen = LinkedList<TileEntityCommandBlock>()
    private var renderRed = LinkedList<TileEntityCommandBlock>()

    private val greenColor = Color(0, 255, 20).rgb
    private val redColor = Color(255, 20, 0).rgb

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Unload) {
        renderGreen.clear()
        renderRed.clear()
        stopScan()
    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChat(event: ClientChatReceivedEvent) {
        if (!Cache.inDungeon || !Config.terminalEsp.value) return

        val msg = event.message.unformattedText.stripColor()
        if (msg.contains("[BOSS] Goldor: Little ants, plotting and scheming, thinking they are invincible…"))
            startScan()
        else if (msg.contains("[BOSS] Necron: Goodbye."))
            stopScan()
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (!Cache.inDungeon || !Config.terminalEsp.value || job == null) return

        val rm = mc.renderManager as IMixinRenderManager
        val renderPosX = rm.renderPosX
        val renderPosY = rm.renderPosY
        val renderPosZ = rm.renderPosZ

        for (block in renderGreen) {
            val x = block.pos.x - renderPosX
            val y = block.pos.y - renderPosY
            val z = block.pos.z - renderPosZ
            val bbox = block.renderBoundingBox
            val aabb = AxisAlignedBB(
                bbox.minX - block.pos.x + x,
                bbox.minY - block.pos.y + y,
                bbox.minZ - block.pos.z + z,
                bbox.maxX - block.pos.x + x,
                bbox.maxY - block.pos.y + y,
                bbox.maxZ - block.pos.z + z
            )
            RenderUtils.drawFilledBoundingBox(aabb, greenColor)
        }
        for (block in renderRed) {
            val x = block.pos.x - renderPosX
            val y = block.pos.y - renderPosY
            val z = block.pos.z - renderPosZ
            val bbox = block.renderBoundingBox
            val aabb = AxisAlignedBB(
                bbox.minX - block.pos.x + x,
                bbox.minY - block.pos.y + y,
                bbox.minZ - block.pos.z + z,
                bbox.maxX - block.pos.x + x,
                bbox.maxY - block.pos.y + y,
                bbox.maxZ - block.pos.z + z
            )
            RenderUtils.drawFilledBoundingBox(aabb, redColor)
        }
    }

    private fun startScan() {
        job = BackgroundScope.launchLooping("Termainl Esp Scan", 250L) {
            printdev("Running scan")
            val renderGreen1 = LinkedList<TileEntityCommandBlock>()
            val renderRed1 = LinkedList<TileEntityCommandBlock>()
            val listEntitiesData1 = mc.theWorld.loadedEntityList.filterIsInstance<EntityArmorStand>()
            val listTileEntitiesData1 = mc.theWorld.loadedTileEntityList.filterIsInstance<TileEntityCommandBlock>()

            listTileEntitiesData1.forEach { i ->
                listEntitiesData1.forEach { i2 ->
                    printdev("Checking terminal")
                    if (abs(i2.posX - i.pos.x) < 3 && abs(i2.posY - i.pos.y) < 3 && abs(i2.posZ - i.pos.z) < 3)
                        when {
                            i2.name.stripColor().contains("Inactive Terminal") -> {
                                printdev("Detected inactive")
                                renderRed1.push(i)
                            }

                            i2.name.stripColor().contains("Terminal Active") -> {
                                printdev("Detected active")
                                renderGreen1.push(i)
                            }
                        }
                }
            }

            renderGreen = renderGreen1
            renderRed = renderRed1
        }
    }

    private fun stopScan() {
        if (job != null) {
            BackgroundScope.cancel(job!!)
            job = null
        }
    }
}