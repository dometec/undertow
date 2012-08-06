/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
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
 */

package io.undertow.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

import org.xnio.ChannelListener;
import org.xnio.ChannelListeners;
import org.xnio.Option;
import org.xnio.Pool;
import org.xnio.XnioWorker;
import org.xnio.channels.ConnectedChannel;
import org.xnio.channels.ConnectedStreamChannel;
import io.undertow.util.AbstractAttachable;

/**
 * A server-side HTTP connection.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class HttpServerConnection extends AbstractAttachable implements ConnectedChannel {
    private final ConnectedStreamChannel channel;
    private final ChannelListener.Setter<HttpServerConnection> closeSetter;
    private final Pool<ByteBuffer> bufferPool;
    private final HttpHandler rootHandler;

    HttpServerConnection(ConnectedStreamChannel channel, final Pool<ByteBuffer> bufferPool, final HttpHandler rootHandler) {
        this.channel = channel;
        this.bufferPool = bufferPool;
        this.rootHandler = rootHandler;
        closeSetter = ChannelListeners.getDelegatingSetter(channel.getCloseSetter(), this);
    }

    /**
     * Get the root HTTP handler for this connection.
     *
     * @return the root HTTP handler for this connection
     */
    public HttpHandler getRootHandler() {
        return rootHandler;
    }

    /**
     * Get the buffer pool for this connection.
     *
     * @return the buffer pool for this connection
     */
    public Pool<ByteBuffer> getBufferPool() {
        return bufferPool;
    }

    /**
     * Get the underlying channel.
     *
     * @return the underlying channel
     */
    public ConnectedStreamChannel getChannel() {
        return channel;
    }

    public ChannelListener.Setter<HttpServerConnection> getCloseSetter() {
        return closeSetter;
    }

    public XnioWorker getWorker() {
        return channel.getWorker();
    }

    public boolean isOpen() {
        return channel.isOpen();
    }

    public boolean supportsOption(final Option<?> option) {
        return channel.supportsOption(option);
    }

    public <T> T getOption(final Option<T> option) throws IOException {
        return channel.getOption(option);
    }

    public <T> T setOption(final Option<T> option, final T value) throws IllegalArgumentException, IOException {
        return channel.setOption(option, value);
    }

    public void close() throws IOException {
        channel.close();
    }

    public SocketAddress getPeerAddress() {
        return channel.getPeerAddress();
    }

    public <A extends SocketAddress> A getPeerAddress(final Class<A> type) {
        return channel.getPeerAddress(type);
    }

    public SocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    public <A extends SocketAddress> A getLocalAddress(final Class<A> type) {
        return channel.getLocalAddress(type);
    }
}