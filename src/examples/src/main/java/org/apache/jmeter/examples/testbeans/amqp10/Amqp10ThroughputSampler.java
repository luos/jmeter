/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.examples.testbeans.amqp10;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;

import io.vertx.core.Future;
import io.vertx.amqp.*;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.future.PromiseInternal;
import io.vertx.core.json.JsonObject;


/**
 * This TestBean is just an example of the use of different TestBean types.
 */
public class Amqp10ThroughputSampler extends AbstractSampler implements TestBean {

    private static final long serialVersionUID = 240L;

    private String host1;

    private int count = 0;

    private Boolean send;

    private String username;
    private String password;


    private static byte[] CONNECTED = "CONNECTED".getBytes(StandardCharsets.UTF_8);

    private Random r = new Random();

    private String host() {
        return host1;
    }

    @Override
    public SampleResult sample(Entry ignored) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.sampleStart();
        try {
            System.out.println("Trying to connect");
            io.vertx.amqp.AmqpClientOptions options = new AmqpClientOptions()
                    .setHost(host())
                    .setPort(5672)
                    .setUsername(username)
                    .setPassword(password);
// Create a client using its own internal Vert.x instance.
            Vertx vertx = Vertx.vertx();
            ContextInternal ctx = (ContextInternal) vertx.getOrCreateContext();
            PromiseInternal<String> testEnd = ctx.promise();
            AmqpClient client1 = AmqpClient.create(options);

            System.out.println("Create client");
            int messageCount = 10000;

            client1.createSender("my-queue", done -> {
                if (done.failed()) {
                    System.out.println("Unable to create a sender");
                } else {
                    CountDownLatch publishAcked = new CountDownLatch(messageCount);

                    AmqpSender sender = done.result();
                    AmqpMessageBuilder builder = AmqpMessage.create();

                    AmqpMessage m3 = builder
                            .withJsonObjectAsBody(new JsonObject().put("message", "hello"))
                            .subject("subject")
                            .applicationProperties(new JsonObject().put("prop1", "value1"))
                            .build();
                    for (int i = 0; i < 1000; i++) {
                        sender.sendWithAck(m3, acked -> {
                            if (acked.succeeded()) {
                                System.out.println("Message accepted");
                            } else {
                                System.out.println("Message not accepted");
                            }
                            publishAcked.countDown();
                        });
                    }
                    System.out.println("Closing client");
                    System.out.println("Waiting for publishes to be acked");
                    try {
                        Thread.sleep(1000);
                        publishAcked.await();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    testEnd.complete("Hello, world!");

                }
            });

            String result = null;
            CountDownLatch latch = new CountDownLatch(1);
            testEnd.future().onComplete(ar -> {
                System.out.println("End ed: " + ar);
                latch.countDown();
            });
            latch.await();

            System.out.println("Endededededed");



        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            res.setSuccessful(false);
        } finally {
        }
        res.sampleEnd();
        return res;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



    public String getHost1() {
        return host1;
    }

    public void setHost1(String host1) {
        this.host1 = host1;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
