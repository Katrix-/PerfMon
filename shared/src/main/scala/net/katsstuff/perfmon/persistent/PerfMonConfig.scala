package net.katsstuff.perfmon.persistent

import org.spongepowered.api.text.TextTemplate

import io.github.katrix.katlib.persistant.{CommentedConfigValue, Config}

trait PerfMonConfig extends Config {

  val PercentUsedArg = "percentUsed"
  val TPSArg = "tps"

  val memoryWarningTemplate: CommentedConfigValue[TextTemplate]
  val memoryLockdownPercent: CommentedConfigValue[Double]
  val memoryWarningPercent : CommentedConfigValue[Double]

  val tpsWarningThreshold: CommentedConfigValue[Double]
  val tpsWarningTemplate: CommentedConfigValue[TextTemplate]

  val registerGarbageCmd: CommentedConfigValue[Boolean]

  override def seq: Seq[CommentedConfigValue[_]] = Seq(
    memoryWarningPercent,
    memoryLockdownPercent,
    memoryWarningPercent,

    tpsWarningThreshold,
    tpsWarningTemplate,

    registerGarbageCmd
  )
}
