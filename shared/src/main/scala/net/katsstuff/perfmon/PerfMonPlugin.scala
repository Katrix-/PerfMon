package net.katsstuff.perfmon

import org.spongepowered.api.Sponge

import io.github.katrix.katlib.KatPlugin
import io.github.katrix.katlib.command.CommandBase
import net.katsstuff.perfmon.command._
import net.katsstuff.perfmon.persistent.PerfMonConfig

trait PerfMonPlugin extends KatPlugin {

  override def config: PerfMonConfig

  def registerCmds(implicit perfHandler: PerfHandler): Unit = {
    Seq(
      new CmdKillAllEntities,
      new CmdKillAllItems,
      new CmdKillAllLiving,
      new CmdKillAllMobs,
      new CmdLockDown,
      new CmdMemory,
      new CmdPing,
      new CmdTPS
    ).foreach(registerCmd)

    if(config.registerGarbageCmd.value) {
      registerCmd(new CmdGenGarbage)
    }
  }

  def registerCmd(cmd: CommandBase): Unit = Sponge.getCommandManager.register(this, cmd.commandSpec, cmd.aliases: _*)
}
