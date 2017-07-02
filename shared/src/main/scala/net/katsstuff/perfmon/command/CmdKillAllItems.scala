package net.katsstuff.perfmon.command
import org.spongepowered.api.entity.{Entity, Item}

import net.katsstuff.perfmon.lib.LibPerm

class CmdKillAllItems extends CmdKillAll {
  override def permission: String = LibPerm.CmdKillAllItems
  override def name: String = "items"
  override def filter: (Entity) => Boolean = {
    case _: Item => true
    case _ => false
  }
}
