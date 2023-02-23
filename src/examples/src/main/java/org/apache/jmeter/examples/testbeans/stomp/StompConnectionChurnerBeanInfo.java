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

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;

public class StompConnectionChurnerBeanInfo extends BeanInfoSupport {

    private PropertyDescriptor getprop(String name) {
        final PropertyDescriptor property = property(name);
        property.setValue(NOT_UNDEFINED, Boolean.FALSE); // Ensure it is not flagged as 'unconfigured'
        return property;
    }

    private PropertyDescriptor getprop(String name, Object deflt) {
        PropertyDescriptor p = property(name);
        p.setValue(DEFAULT, deflt);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        return p;
    }

    public StompConnectionChurnerBeanInfo() {
        super(StompConnectionChurner.class);
        getprop("host1", "127.0.0.1");
        getprop("host2", "127.0.0.1");
        getprop("host3", "127.0.0.1");
        getprop("host4", "127.0.0.1");
        getprop("host5", "127.0.0.1");
        getprop("username", "guest");
        getprop("password", "guest");
        getprop("send", false);

    }
}
