package net.katsstuff.perfmon.persistent
import scala.util.Try

import org.spongepowered.api.text.TextTemplate

import io.github.katrix.katlib.KatPlugin
import io.github.katrix.katlib.helper.LogHelper
import io.github.katrix.katlib.persistant.CommentedConfigValue
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.objectmapping.ObjectMappingException

class PerfMonConfigV1(cfgRoot: CommentedConfigurationNode, default: PerfMonConfig)(implicit plugin: KatPlugin) extends PerfMonConfig {
  override val memoryWarningTemplate: CommentedConfigValue[TextTemplate] = configValue(default.memoryWarningTemplate)
  override val memoryLockdownPercent: CommentedConfigValue[Double]       = configValue(default.memoryLockdownPercent)
  override val memoryWarningPercent : CommentedConfigValue[Double]       = configValue(default.memoryWarningPercent)

  override val tpsWarningThreshold: CommentedConfigValue[Double]       = configValue(default.tpsWarningThreshold)
  override val tpsWarningTemplate : CommentedConfigValue[TextTemplate] = configValue(default.tpsWarningTemplate)

  override val registerGarbageCmd: CommentedConfigValue[Boolean] = configValue(default.registerGarbageCmd)

  def configValue[A](existing: CommentedConfigValue[A])(implicit plugin: KatPlugin): CommentedConfigValue[A] =
    Try(Option(cfgRoot.getNode(existing.path: _*).getValue(existing.typeToken)).get)
      .map(found => existing.value_=(found)) //Doesn't want to work with CommentedConfigValue
      .recover {
      case _: ObjectMappingException =>
        LogHelper.error(s"Failed to deserialize value of ${existing.path.mkString(", ")}, using the default instead")
        existing
      case _: NoSuchElementException =>
        LogHelper.warn(s"No value found for ${existing.path.mkString(", ")}, using default instead")
        existing
    }
      .get
}
