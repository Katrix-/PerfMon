package net.katsstuff.perfmon.command
import org.spongepowered.api.entity.Entity

import net.katsstuff.perfmon.lib.LibPerm

class CmdKillAllEntities extends CmdKillAll {
  override def permission: String = LibPerm.CmdKillAllEntities
  override def name: String = "entities"
  override def filter: (Entity) => Boolean = _ => true
}
