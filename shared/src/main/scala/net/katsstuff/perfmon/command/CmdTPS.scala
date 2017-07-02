package net.katsstuff.perfmon.command

import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandResult, CommandSource}
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._
import net.katsstuff.perfmon.PerfHandler
import net.katsstuff.perfmon.lib.LibPerm

class CmdTPS(implicit perfHandler: PerfHandler) extends CommandBase(None) {
  override def execute(src: CommandSource, args: CommandContext): CommandResult = {
    src.sendMessage(t"${YELLOW}TPS: ${perfHandler.tps}")
    CommandResult.success()
  }

  override def commandSpec: CommandSpec =
    CommandSpec
      .builder()
      .description(t"Check the tps of the server")
      .permission(LibPerm.CmdTPS)
      .executor(this)
      .build()

  override def aliases: Seq[String] = Seq("tps")
}
