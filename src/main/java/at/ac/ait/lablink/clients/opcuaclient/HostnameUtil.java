//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import static com.google.common.collect.Sets.newHashSet;

import org.apache.logging.log4j.LogManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

/**
 * This class provides a collection of utility methods to retrieve and / or resolve the hostname.
 */
public class HostnameUtil {

  /**
   * Retrieve the local hostname, if possible. Failure results in "localhost".
   *
   * @return the local hostname
   */
  public static String getHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
      return "localhost";
    }
  }

  /**
   * Given an address resolve it to as many unique addresses or hostnames as can be found.
   *
   * @param address the address to resolve.
   * @return the addresses and hostnames that were resolved from {@code address}.
   */
  public static Set<String> getHostnames(String address) {
    return getHostnames(address, true);
  }

  /**
   * Given an address resolve it to as many unique addresses or hostnames as can be found.
   *
   * @param address the address to resolve.
   * @param includeLoopback if {@code true} loopback addresses will be included in the returned set.
   * @return the addresses and hostnames that were resolved from {@code address}.
   */
  public static Set<String> getHostnames(String address, boolean includeLoopback) {
    Set<String> hostnames = newHashSet();

    try {
      InetAddress inetAddress = InetAddress.getByName(address);

      if (inetAddress.isAnyLocalAddress()) {
        try {
          Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

          for (NetworkInterface ni : Collections.list(nis)) {
            Collections.list(ni.getInetAddresses()).forEach(ia -> {
              if (ia instanceof Inet4Address) {
                if (includeLoopback || !ia.isLoopbackAddress()) {
                  hostnames.add(ia.getHostName());
                  hostnames.add(ia.getHostAddress());
                  hostnames.add(ia.getCanonicalHostName());
                }
              }
            });
          }
        } catch (SocketException ex) {
          LogManager.getLogger(HostnameUtil.class)
              .warn("Failed to NetworkInterfaces for bind address: {}", address, ex);
        }
      } else {
        if (includeLoopback || !inetAddress.isLoopbackAddress()) {
          hostnames.add(inetAddress.getHostName());
          hostnames.add(inetAddress.getHostAddress());
          hostnames.add(inetAddress.getCanonicalHostName());
        }
      }
    } catch (UnknownHostException ex) {
      LogManager.getLogger(HostnameUtil.class)
          .warn("Failed to get InetAddress for bind address: {}", address, ex);
    }

    return hostnames;
  }

}
