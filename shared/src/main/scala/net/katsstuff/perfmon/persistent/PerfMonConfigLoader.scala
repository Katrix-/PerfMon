package net.katsstuff.perfmon.persistent

import java.nio.file.Path

import org.spongepowered.api.text.TextTemplate
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.katlib.KatPlugin
import io.github.katrix.katlib.helper.Implicits._
import io.github.katrix.katlib.helper.LogHelper
import io.github.katrix.katlib.persistant.{CommentedConfigValue, ConfigLoader, ConfigValue}

class PerfMonConfigLoader(dir: Path)(implicit plugin: KatPlugin) extends ConfigLoader[PerfMonConfig](dir, identity) {

  def reload(): Unit =
    cfgRoot = cfgLoader.load()

  override def loadData: PerfMonConfig = {
    val loaded = cfgRoot.getNode("version").getString("1") match {
      case "1" => new PerfMonConfigV1(cfgRoot, default)
      case unknown =>
        LogHelper.error(s"Unknown config version $unknown, using default instead")
        default
    }
    saveData(loaded)
    loaded
  }

  val default = new PerfMonConfig {
    override val memoryWarningPercent: CommentedConfigValue[Double] =
      ConfigValue(0.90, "The percent of memory to use before warnings are issues. 0 to disable.", Seq("memory", "percent", "warning"))
    override val memoryWarningTemplate: CommentedConfigValue[TextTemplate] =
      ConfigValue(tt"${RED}Remaining server memory at $PercentUsedArg%", "The warning template", Seq("memory", "text", "warning"))
    override val memoryLockdownPercent: CommentedConfigValue[Double] =
      ConfigValue(0.95, "The percent of memory to use before server enters lockdown. 0 to disable.", Seq("memory", "percent", "lockdown"))

    override val tpsWarningThreshold: CommentedConfigValue[Double] =
      ConfigValue(10D, "Start issuing warnings if TPS goes under this", Seq("tps", "warning", "text"))
    override val tpsWarningTemplate: CommentedConfigValue[TextTemplate] =
      ConfigValue(tt"${YELLOW}Server TPS currently at $TPSArg", "The tps warning to issue", Seq("tps", "warning", "threshold"))

    override val registerGarbageCmd: CommentedConfigValue[Boolean] =
      ConfigValue(false, "If a command that generates memory garbage should be registered for testing", Seq("testing", "gengarbage"))
  }

}
