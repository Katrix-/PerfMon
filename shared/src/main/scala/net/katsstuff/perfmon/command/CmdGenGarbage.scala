package net.katsstuff.perfmon.command

import scala.collection.JavaConverters._

import org.spongepowered.api.command.args.{CommandContext, GenericArguments}
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandException, CommandResult, CommandSource}
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._
import net.katsstuff.perfmon.lib.LibPerm

class CmdGenGarbage extends CommandBase(None) {
  val Garbage:  CommandKey[Long]      = cmdKey[Long](t"command")
  val DataType: CommandKey[DataSize] = cmdKey[DataSize](t"dataSize")

  var garbage = Array.empty[Byte]

  override def execute(src: CommandSource, args: CommandContext): CommandResult = {
    val data = args.one(Garbage).map { l =>
      val base = l / 8

      args.one(DataType).get match {
        case DataSize.Bytes => base -> "bytes"
        case DataSize.KiloBytes => (base * 1024) -> "kb"
        case DataSize.MegaBytes => (base * 1024 * 1024) -> "mb"
        case DataSize.GigaBytes => (base * 1024 * 1024 * 1024) -> "gb"
      }
    }

    data match {
      case Some((amount, _)) if amount > Int.MaxValue =>
        throw new CommandException(t"Too much garbage")
      case Some((amount, tpe)) =>

        garbage = Array.ofDim(amount.toInt)
        src.sendMessage(t"${GREEN}Set garbage to $amount $tpe")
        CommandResult.success()
      case None =>
        throw new CommandException(t"No garbage amount found")
    }
  }

  override def commandSpec: CommandSpec =
    CommandSpec
      .builder()
      .arguments(
        GenericArguments.longNum(Garbage),
        GenericArguments.optional(
          GenericArguments.choices(
            DataType,
            Map(
              "bytes"     -> DataSize.Bytes,
              "kilobytes" -> DataSize.KiloBytes,
              "kb"        -> DataSize.KiloBytes,
              "megabytes" -> DataSize.MegaBytes,
              "mb"        -> DataSize.MegaBytes,
              "gigabytes" -> DataSize.GigaBytes,
              "gb"        -> DataSize.GigaBytes
            ).asJava,
            true
          ),
          DataSize.MegaBytes
        )
      )
      .description(t"Set some specific garbage that the server won't clean up.")
      .extendedDescription(t"Use this to test your config")
      .permission(LibPerm.CmdGenGarbage)
      .executor(this)
      .build()

  override def aliases: Seq[String] = Seq("gengarbage")
}

sealed trait DataSize
object DataSize {
  case object Bytes     extends DataSize
  case object KiloBytes extends DataSize
  case object MegaBytes extends DataSize
  case object GigaBytes extends DataSize
}
