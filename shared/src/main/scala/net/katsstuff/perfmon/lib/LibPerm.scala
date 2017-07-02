package net.katsstuff.perfmon.lib

//noinspection SpellCheckingInspection
object LibPerm {
  val PerfMon = "perfmon"

  val Cmd     = s"$PerfMon.cmd"
  val CmdUser = s"$Cmd.user"

  val PingSelf      = s"$CmdUser.pingself"
  val PingOther     = s"$Cmd.pingother"
  val CmdTPS        = s"$CmdUser.tps"
  val CmdGenGarbage = s"$Cmd.gengarbage"
  val CmdMemory     = s"$Cmd.memory"
  val CmdLockdown   = s"$Cmd.lockdown"

  val CmdKillAll         = s"$Cmd.killall"
  val CmdKillAllEntities = s"$CmdKillAll.entities"
  val CmdKillAllItems    = s"$CmdKillAll.items"
  val CmdKillAllMobs     = s"$CmdKillAll.mobs"
  val CmdKillAllLiving   = s"$CmdKillAll.living"

  val BypassLockdown = s"$PerfMon.bypasslockdown"
}
