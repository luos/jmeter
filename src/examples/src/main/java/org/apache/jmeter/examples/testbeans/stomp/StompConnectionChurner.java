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

package org.apache.jmeter.examples.testbeans.stomp;

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

/**
 * This TestBean is just an example of the use of different TestBean types.
 */
public class StompConnectionChurner extends AbstractSampler implements TestBean {

    private static final long serialVersionUID = 240L;

    private String host1;
    private String host2;
    private String host3;
    private String host4;
    private String host5;



    private String username;
    private String password;


    private static byte[] CONNECTED = "CONNECTED".getBytes(StandardCharsets.UTF_8);

    private Random r = new Random();

    private String host() {
        switch (r.nextInt(5) + 1){
            case 1:
                return host1;
            case 2:
                return host2;
            case 3:
                return host3;
            case 4:
                return host4;
            case 5:
                return host5;
        };
        return host1;
    }

    @Override
    public SampleResult sample(Entry ignored) {
        SampleResult res = new SampleResult();
        res.setSampleLabel(getName());
        res.sampleStart();
        try {

            Socket clientSocket = new Socket(host(), 61613);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.write("CONNECT\nlogin: " + username + "\npasscode: " + password + "\n\n\0");
            out.flush();

            String x = in.readLine();
            boolean connected = Arrays.equals(x.getBytes(StandardCharsets.UTF_8), CONNECTED);
            if (connected) {
                res.setSuccessful(true);
            } else {
                System.out.println("Received: " + x);
                Boolean b = Arrays.equals("CONNECTED".getBytes(StandardCharsets.UTF_8), x.getBytes(StandardCharsets.UTF_8));
                System.out.println("Received: " + (b));
                res.setSuccessful(false);
            }
            out.write("DISCONNECT");
            out.flush();

            clientSocket.close();

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

    public String getHost2() {
        return host2;
    }

    public void setHost2(String host2) {
        this.host2 = host2;
    }

    public String getHost3() {
        return host3;
    }

    public void setHost3(String host3) {
        this.host3 = host3;
    }

    public String getHost4() {
        return host4;
    }

    public void setHost4(String host4) {
        this.host4 = host4;
    }

    public String getHost5() {
        return host5;
    }

    public void setHost5(String host5) {
        this.host5 = host5;
    }
}
