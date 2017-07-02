package net.katsstuff.perfmon.command
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.living.Hostile

import net.katsstuff.perfmon.lib.LibPerm

class CmdKillAllMobs extends CmdKillAll {
  override def permission: String = LibPerm.CmdKillAllMobs
  override def name: String = "mobs"
  override def filter: (Entity) => Boolean = {
    case _: Hostile => true
    case _ => false
  }
}
