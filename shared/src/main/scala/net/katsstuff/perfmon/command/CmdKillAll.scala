package net.katsstuff.perfmon.command

import scala.collection.JavaConverters._

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.{CommandResult, CommandSource}
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.Entity

import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._

abstract class CmdKillAll extends CommandBase(None) {
  override def execute(src: CommandSource, args: CommandContext): CommandResult = {

    Sponge.getServer.getWorlds.asScala.flatMap(_.getEntities.asScala).filter(filter).foreach(_.remove())

    src.sendMessage(t"Killed all $name")
    CommandResult.success()
  }

  override def commandSpec: CommandSpec =
    CommandSpec
      .builder()
      .description(t"Kills all $name. Only use if needed")
      .permission(permission)
      .executor(this)
      .build()

  override def aliases: Seq[String] = Seq(s"killall$name")

  def permission: String
  def name:       String
  def filter:     Entity => Boolean
}
