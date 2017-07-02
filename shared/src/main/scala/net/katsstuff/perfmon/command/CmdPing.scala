package net.katsstuff.perfmon.command

import org.spongepowered.api.command.args.{CommandContext, GenericArguments}
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandException, CommandResult, CommandSource}
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._
import io.github.katrix.katlib.lib.LibCommonTCommandKey
import net.katsstuff.perfmon.PerfHandler
import net.katsstuff.perfmon.lib.LibPerm

class CmdPing(implicit perfHandler: PerfHandler) extends CommandBase(None) {
  override def execute(src: CommandSource, args: CommandContext): CommandResult = {
    args.one(LibCommonTCommandKey.Player).fold[CommandResult](throw new CommandException(t"Can't get a ping for you")) { p =>
      p.sendMessage(t"${YELLOW}Ping: ${perfHandler.ping(p)}")
      CommandResult.success()
    }
  }

  override def commandSpec: CommandSpec =
    CommandSpec
      .builder()
      .arguments(GenericArguments.requiringPermission(GenericArguments.playerOrSource(LibCommonTCommandKey.Player), LibPerm.PingOther))
      .description(t"Gets your latency to the server")
      .permission(LibPerm.PingSelf)
      .executor(this)
      .build()

  override def aliases: Seq[String] = Seq("ping")
}
