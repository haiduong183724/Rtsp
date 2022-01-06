package net.majorkernelpanic.streaming;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class ClientInfo {
    public InetAddress getmDestination() {
        return mDestination;
    }

    public int getRtpPort() {
        return rtpPort;
    }

    public int getRtcpPort() {
        return rtcpPort;
    }

    private InetAddress mDestination;
    private int rtpPort, rtcpPort;
    public ClientInfo(InetAddress des, int p1, int p2) throws UnknownHostException {
        mDestination =  des;
        rtcpPort = p2;
        rtpPort = p1;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "mDestination=" + mDestination +
                ", rtpPort=" + rtpPort +
                ", rtcpPort=" + rtcpPort +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientInfo that = (ClientInfo) o;
        return rtpPort == that.rtpPort && rtcpPort == that.rtcpPort && mDestination.getHostAddress().equals(that.mDestination.getHostAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mDestination, rtpPort, rtcpPort);
    }
}
