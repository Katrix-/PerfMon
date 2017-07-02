package net.katsstuff.perfmon.command

import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandResult, CommandSource}
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._
import net.katsstuff.perfmon.PerfHandler
import net.katsstuff.perfmon.lib.LibPerm

class CmdLockDown(implicit perfHandler: PerfHandler) extends CommandBase(None) {
  override def execute(src: CommandSource, args: CommandContext): CommandResult = {
    perfHandler.manualLockDown = !perfHandler.manualLockDown
    if(perfHandler.manualLockDown) {
      src.sendMessage(t"${GREEN}Server now in lockdown")
    }
    else {
    src.sendMessage(t"${GREEN}Server no longer in lockdown")
  }

    CommandResult.success()
  }

  override def commandSpec: CommandSpec =
    CommandSpec.builder()
    .description(t"Enters the server into lockdown, or disables it if it is already in lockdown")
    .extendedDescription(t"When the server is in lockdown, only players with the required permission can join")
    .permission(LibPerm.CmdLockdown)
    .executor(this)
    .build()

  override def aliases: Seq[String] = Seq("lockdown")
}
