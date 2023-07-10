package me.rgn.asceciacurrencies.web;

import be.ceau.chart.LineChart;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;

import be.ceau.chart.color.Color;
import com.sun.net.httpserver.HttpServer;
import me.rgn.asceciacurrencies.files.CurrenciesConfig;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.logging.Logger;

public class ACWebServer extends WebSocketServer {
    private final Logger logger = Logger.getLogger(ACWebServer.class.getName());

    public ACWebServer(int port) {
        super(new InetSocketAddress("localhost", port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.info("Connection opened to " + conn.getRemoteSocketAddress().getHostName());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.info("Connection closed to " + conn.getRemoteSocketAddress().getHostName());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.info("Received message: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.info(ex.getMessage());
    }

    @Override
    public void onStart() {
        logger.info("Webserver started on port: " + getPort());
    }
}
