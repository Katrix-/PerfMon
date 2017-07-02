package net.katsstuff.perfmon.data

import java.lang.{Long => JLong}

class DataBytes(val bytes: Long) extends AnyVal with Ordered[DataBytes] {
  def +(other: DataBytes): DataBytes = DataBytes(bytes + other.bytes)
  def +(otherBytes: Long): DataBytes = DataBytes(bytes + otherBytes)

  def -(other: DataBytes): DataBytes = DataBytes(bytes - other.bytes)
  def -(otherBytes: Long): DataBytes = DataBytes(bytes - otherBytes)

  def *(other: DataBytes): DataBytes = DataBytes(bytes * other.bytes)
  def *(otherBytes: Long): DataBytes = DataBytes(bytes * otherBytes)
  def *(ratio: Double): DataBytes = DataBytes((bytes * ratio).toLong)

  def /(other: DataBytes): DataBytes = DataBytes(bytes / other.bytes)
  def /(otherBytes: Long): DataBytes = DataBytes(bytes / otherBytes)
  def /(ratio: Double): DataBytes = DataBytes((bytes / ratio).toLong)

  def kiloBytes: Long = bytes / 1024
  def megaBytes: Double = bytes / (1024D * 1024D)

  override def compare(that: DataBytes): Int = JLong.compare(bytes, that.bytes)
}
object DataBytes {
  def apply(bytes: Long): DataBytes = new DataBytes(bytes)
}