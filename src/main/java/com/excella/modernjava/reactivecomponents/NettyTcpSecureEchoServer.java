/*
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.excella.modernjava.reactivecomponents;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.examples.NettyExamplesEnvironment;
import io.reactivex.netty.examples.tcp.echo.EchoServer;
import io.reactivex.netty.protocol.tcp.server.TcpServer;

import java.nio.charset.Charset;

/**
 * A secure TCP echo server that echoes all input it receives on any connection, after prepending the input with a fixed
 * string. The only difference between this example and {@link EchoServer} is that this adds TLS.
 *
 * This example just aims to demonstrate how to write the simplest TCP server, it is however, not of much use in general
 * primarily because it reads unstructured data i.e. there are no boundaries that define what constitutes "a message".
 * In order to define such boundaries, one would typically add a {@link ChannelHandler} that converts the read raw
 * {@code ByteBuffer} to a structured message.
 */
public final class NettyTcpSecureEchoServer {

    public static void main(final String[] args) {

        NettyExamplesEnvironment env = NettyExamplesEnvironment.newEnvironment(SecureEchoServer.class);

        /*Starts a new TCP server on an ephemeral port.*/
        TcpServer<ByteBuf, ByteBuf> server = TcpServer.newServer()
                .enableWireLogging("echo-server", LogLevel.DEBUG)
                .unsafeSecure()
                .start(connection ->
                        connection.writeStringAndFlushOnEach(
                                connection.getInput()
                                        .map(bb -> bb.toString(
                                                Charset.defaultCharset()))
                                        .map(msg -> "echo => " + msg)));

        /*Wait for shutdown if not called from the client (passed an arg)*/
        if (env.shouldWaitForShutdown(args)) {
            server.awaitShutdown();
        }

        /*If not waiting for shutdown, assign the ephemeral port used to a field so that it can be read and used by
        the caller, if any.*/
        env.registerServerAddress(server.getServerAddress());
    }
}