/*
 * Copyright 2017 LinkedIn Corp. Licensed under the BSD 2-Clause License (the "License").  See License in the project root for license information.
 */

package com.linkedin.kafka.clients.producer;

import com.linkedin.kafka.clients.annotations.InterfaceOrigin;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.common.annotation.InterfaceStability;


/**
 * The general producer interface that allows allows pluggable serializers and deserializers.
 * LiKafkaProducer has the same interface as open source {@link Producer}. We define the interface separately to allow
 * future extensions.
 * @see LiKafkaProducerImpl
 */
@InterfaceStability.Unstable
public interface LiKafkaProducer<K, V> extends Producer<K, V> {
  /**
   * Send the given record asynchronously and return a future which will eventually contain the response information.
   *
   * @param record The record to send
   * @return A future which will eventually contain the response information
   */
  @InterfaceOrigin.ApacheKafka
  Future<RecordMetadata> send(ProducerRecord<K, V> record);

  /**
   * Send a record and invoke the given callback when the record has been acknowledged by the server
   */
  @InterfaceOrigin.ApacheKafka
  Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback);

  /**
   * Flush any accumulated records from the producer. Blocks until all sends are complete.
   */
  @InterfaceOrigin.ApacheKafka
  void flush();

  /**
   * Get a list of partitions for the given topic for custom partition assignment. The partition metadata will change
   * over time so this list should not be cached.
   */
  @InterfaceOrigin.ApacheKafka
  List<PartitionInfo> partitionsFor(String topic);

  /**
   * Return a map of metrics maintained by the producer
   */
  @InterfaceOrigin.ApacheKafka
  Map<MetricName, ? extends Metric> metrics();

  /**
   * Close this producer
   */
  @InterfaceOrigin.ApacheKafka
  void close();

  /**
   * Tries to close the producer cleanly within the specified timeout. If the close does not complete within the
   * timeout, fail any pending send requests and force close the producer.
   *
   * Notice that if an auditor is used, depending on how the auditor is implemented, closing a producer with
   * timeout may cause inaccurate auditing result.
   */
  @InterfaceOrigin.ApacheKafka
  void close(long timeout, TimeUnit unit);
}
