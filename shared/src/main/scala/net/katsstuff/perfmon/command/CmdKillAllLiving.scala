package net.katsstuff.perfmon.command
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.living.Living

import net.katsstuff.perfmon.lib.LibPerm

class CmdKillAllLiving extends CmdKillAll {
  override def permission: String = LibPerm.CmdKillAllLiving
  override def name: String = "living"
  override def filter: (Entity) => Boolean = {
    case _: Living => true
    case _ => false
  }
}
