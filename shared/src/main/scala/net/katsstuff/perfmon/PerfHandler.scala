package net.katsstuff.perfmon

import scala.collection.JavaConverters._

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.service.permission.PermissionService
import org.spongepowered.api.text.TextTemplate
import org.spongepowered.api.world.{ChunkTicketManager, World}
import org.spongepowered.api.{Server, Sponge}

import io.github.katrix.katlib.helper.Implicits._
import net.katsstuff.perfmon.data.DataBytes
import net.katsstuff.perfmon.lib.LibPerm

class PerfHandler(implicit plugin: PerfMonPlugin) extends Runnable {

  private def memoryWarningTemplate: TextTemplate = plugin.config.memoryWarningTemplate.value
  private def memoryLockdownPercent: Double       = plugin.config.memoryLockdownPercent.value
  private def memoryWarningPercent:  Double       = plugin.config.memoryWarningPercent.value

  private def tpsWarningThreshold: Double       = plugin.config.tpsWarningThreshold.value
  private def tpsWarningTemplate:  TextTemplate = plugin.config.tpsWarningTemplate.value

  def percentMaxMemory(percent: Double): DataBytes = maxMemory - (maxMemory * percent)

  def lockdownMemory: DataBytes = percentMaxMemory(memoryLockdownPercent)
  def warningMemory:  DataBytes = percentMaxMemory(memoryWarningPercent)

  private var _autoLockDown   = false
  private var _manualLockDown = false

  def autoLockDown:                 Boolean = _autoLockDown
  def manualLockDown:               Boolean = _manualLockDown
  def manualLockDown_=(b: Boolean): Unit    = {
    _manualLockDown = b
    if(b) {
      run()
    }
  }

  def isLockDown: Boolean = _autoLockDown || _manualLockDown

  private def server:  Server  = Sponge.getServer
  private def runtime: Runtime = Runtime.getRuntime

  def forcedChunks(world: World): Set[ChunkTicketManager.LoadingTicket] =
    server.getChunkTicketManager.getForcedChunks(world).asMap.asScala.toSet.flatMap(t => t._2.asScala.toSet)
  def allForcedChunks: Map[World, Set[ChunkTicketManager.LoadingTicket]] = server.getWorlds.asScala.map(w => w -> forcedChunks(w)).toMap

  def tps:         Double = server.getTicksPerSecond
  def uptimeTicks: Int    = server.getRunningTimeTicks

  def totalMemory:     DataBytes = DataBytes(runtime.totalMemory)
  def maxMemory:       DataBytes = DataBytes(runtime.maxMemory)
  def allocatedMemory: DataBytes = totalMemory - runtime.freeMemory
  def freeMemory:      DataBytes = maxMemory - allocatedMemory

  def percentUsed: Double = maxMemory.bytes.toDouble / allocatedMemory.bytes.toDouble

  def ping(player: Player): Int = player.getConnection.getLatency

  @Listener
  def onConnect(event: ClientConnectionEvent.Login): Unit = {
    val permService = Sponge.getServiceManager.provideUnchecked(classOf[PermissionService])
    val subject     = permService.getSubjects(PermissionService.SUBJECTS_USER).get(event.getProfile.getUniqueId.toString)

    if (isLockDown && !subject.hasPermission(LibPerm.BypassLockdown)) {
      event.setMessage(t"Server currently in lockdown")
      event.setMessageCancelled(false)
      event.setCancelled(true)
    }
  }

  override def run(): Unit = {
    val free = freeMemory

    if (memoryLockdownPercent != 0D && free < lockdownMemory) {
      _autoLockDown = true
      server.getOnlinePlayers.asScala.filter(!_.hasPermission(LibPerm.BypassLockdown)).foreach(_.kick(t"Server currently in lockdown"))
    } else if (memoryWarningPercent != 0D && free < warningMemory) {
      server.getBroadcastChannel.send(memoryWarningTemplate.apply(Map(plugin.config.PercentUsedArg -> percentUsed * 100).asJava).build())
    }

    if (tps < tpsWarningThreshold) {
      server.getBroadcastChannel.send(tpsWarningTemplate.apply(Map(plugin.config.TPSArg -> tps).asJava).build())
    }
  }
}
