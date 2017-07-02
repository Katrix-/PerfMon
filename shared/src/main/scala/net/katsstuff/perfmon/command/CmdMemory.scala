package net.katsstuff.perfmon.command

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandResult, CommandSource}
import org.spongepowered.api.service.pagination.PaginationService
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._
import net.katsstuff.perfmon.PerfHandler
import net.katsstuff.perfmon.lib.LibPerm

class CmdMemory(implicit perfHandler: PerfHandler) extends CommandBase(None) {
  override def execute(src: CommandSource, args: CommandContext): CommandResult = {
    val maxMemory = perfHandler.maxMemory
    val freeMemory = perfHandler.freeMemory
    val allocatedMemory = perfHandler.allocatedMemory

    val builder = Sponge.getServiceManager.provideUnchecked(classOf[PaginationService]).builder()
    builder.header(t"${YELLOW}Memory usage")
    builder.contents(
      t"$YELLOW${allocatedMemory.megaBytes}MB/${maxMemory.megaBytes}MB allocated",
      t"$YELLOW${freeMemory.megaBytes}MB free"
    ).sendTo(src)

    CommandResult.success()
  }

  override def commandSpec: CommandSpec =
    CommandSpec.builder()
    .description(t"Shows current memory usage")
    .permission(LibPerm.CmdMemory)
    .executor(this)
    .build()

  override def aliases: Seq[String] = Seq("mem", "memory")
}
